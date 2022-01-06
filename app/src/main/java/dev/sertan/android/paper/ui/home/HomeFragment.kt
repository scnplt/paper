package dev.sertan.android.paper.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.databinding.FragmentHomeBinding
import dev.sertan.android.paper.ui.main.MainActivity

@AndroidEntryPoint
internal class HomeFragment : Fragment(), NoteAdapter.NoteListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: HomeViewModel by viewModels()
        binding.viewModel = viewModel
        setUpRecyclerView()

        (requireActivity() as? MainActivity)?.onFabClicked {
            val direction = HomeFragmentDirections.actionHomeToAddNote()
            findNavController().navigate(direction)
        }
    }

    override fun onNoteClicked(note: Note) {
        val direction = HomeFragmentDirections.actionHomeToNote(note)
        findNavController().navigate(direction)
    }

    override fun onNoteSwipedToLeft(position: Int) {
        binding.viewModel?.delete(requireView(), position)
    }

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
