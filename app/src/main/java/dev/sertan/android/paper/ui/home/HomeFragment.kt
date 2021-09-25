package dev.sertan.android.paper.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.R
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.databinding.FragmentHomeBinding
import dev.sertan.android.paper.ui.common.BaseFragment
import dev.sertan.android.paper.ui.main.MainActivity

@AndroidEntryPoint
internal class HomeFragment : BaseFragment<FragmentHomeBinding>(), NoteAdapter.NoteListener {

    private val viewModel by viewModels<HomeViewModel>()

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        setUpRecyclerView()

        (requireActivity() as MainActivity).onFabClicked {
            val direction = HomeFragmentDirections.actionHomeToAddNote()
            findNavController().navigate(direction)
        }
    }

    override fun onNoteClicked(note: Note) {
        val direction = HomeFragmentDirections.actionHomeToNote(note)
        findNavController().navigate(direction)
    }

    override fun onNoteSwipedToLeft(position: Int) {
        viewModel.delete(requireView(), position)
    }

    private fun setUpRecyclerView() {
        val noteAdapter = NoteAdapter(this)
        val noteSwipeToDeleteCallback = NoteSwipeCallback(this)

        binding.recyclerViewNotes.apply {
            adapter = noteAdapter
            ItemTouchHelper(noteSwipeToDeleteCallback).attachToRecyclerView(this)
        }
    }

}
