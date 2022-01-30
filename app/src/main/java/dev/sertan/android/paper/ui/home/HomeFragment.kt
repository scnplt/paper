package dev.sertan.android.paper.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.R
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.databinding.FragmentHomeBinding
import dev.sertan.android.paper.ui.main.MainActivity
import dev.sertan.android.paper.ui.note.ScreenMode
import dev.sertan.android.paper.util.showToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class HomeFragment : Fragment(), NoteAdapter.NoteListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = homeViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        subscribeUi()

        (requireActivity() as? MainActivity)?.onFabClicked {
            val direction = HomeFragmentDirections.actionHomeToNote(screenMode = ScreenMode.CREATE)
            findNavController().navigate(direction)
        }
    }

    private fun subscribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.uiState.collect { uiState ->
                    if (uiState.noteDeleted.value == true) {
                        Snackbar.make(requireView(), R.string.note_deleted, Snackbar.LENGTH_LONG)
                            .apply {
                                setAction(R.string.undo) { homeViewModel.undoDelete() }
                                setAnchorView(R.id.fab)
                            }.show()
                    }

                    uiState.message.value?.let { requireContext().showToast(it) }
                }
            }
        }
    }

    override fun onNoteClicked(note: Note) {
        val direction = HomeFragmentDirections.actionHomeToNote(note = note)
        findNavController().navigate(direction)
    }

    override fun onNoteSwipedToLeft(position: Int) = homeViewModel.deleteNote(position)

    private fun setUpRecyclerView() {
        val noteAdapter = NoteAdapter(this)
        val noteSwipeToDeleteCallback = NoteSwipeCallback(this)

        binding.recyclerViewNotes.apply {
            adapter = noteAdapter
            ItemTouchHelper(noteSwipeToDeleteCallback).attachToRecyclerView(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
