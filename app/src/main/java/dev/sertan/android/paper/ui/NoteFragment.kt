package dev.sertan.android.paper.ui

import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.FragmentNoteBinding

@AndroidEntryPoint
internal class NoteFragment : BaseFragment<FragmentNoteBinding>() {
    private val args by navArgs<NoteFragmentArgs>()
    private val note by lazy { args.note }

    override fun getLayoutRes(): Int = R.layout.fragment_note

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.note = note

        customizeFab {
            val icon = Icon.createWithResource(requireContext(), R.drawable.ic_edit)
            setImageIcon(icon)
            setOnClickListener {
                val direction = NoteFragmentDirections.actionNoteToEditNote(note)
                findNavController().navigate(direction)
            }
            show()
        }
    }

}
