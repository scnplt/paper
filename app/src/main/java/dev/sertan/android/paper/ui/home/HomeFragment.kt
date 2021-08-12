package dev.sertan.android.paper.ui.home

import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.R
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.databinding.FragmentHomeBinding
import dev.sertan.android.paper.ui.BaseFragment

@AndroidEntryPoint
internal class HomeFragment : BaseFragment<FragmentHomeBinding>(), NoteAdapter.Listener {
    private val viewModel by viewModels<HomeViewModel>()

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.recyclerViewNotes.adapter = NoteAdapter(this)

        val touchHelperCallback =
            NoteTouchHelperCallback { viewModel.deleteWithPosition(view, it) }
        ItemTouchHelper(touchHelperCallback).attachToRecyclerView(binding.recyclerViewNotes)

        customizeFab {
            val icon = Icon.createWithResource(requireContext(), R.drawable.ic_add)
            setImageIcon(icon)
            setOnClickListener {
                val direction = HomeFragmentDirections.actionHomeToAddNote()
                findNavController().navigate(direction)
            }
            show()
        }
    }

    override fun oNoteClicked(note: Note) {
        val direction = HomeFragmentDirections.actionHomeToNote(note)
        findNavController().navigate(direction)
    }

}
