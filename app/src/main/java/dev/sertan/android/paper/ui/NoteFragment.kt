package dev.sertan.android.paper.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.FragmentNoteBinding
import dev.sertan.android.paper.ui.common.BaseFragment
import dev.sertan.android.paper.ui.main.MainActivity

@AndroidEntryPoint
internal class NoteFragment : BaseFragment<FragmentNoteBinding>() {
    private val args by navArgs<NoteFragmentArgs>()
    private val note by lazy { args.note }

    override fun getLayoutRes(): Int = R.layout.fragment_note

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.note = note

        (requireActivity() as? MainActivity)?.onFabClicked {
            val direction = NoteFragmentDirections.actionNoteToEditNote(note)
            findNavController().navigate(direction)
        }
    }

}
