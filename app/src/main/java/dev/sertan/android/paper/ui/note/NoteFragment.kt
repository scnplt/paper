package dev.sertan.android.paper.ui.note

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.databinding.FragmentNoteBinding
import dev.sertan.android.paper.ui.main.MainActivity
import kotlinx.coroutines.flow.update

@AndroidEntryPoint
internal class NoteFragment : Fragment() {
    private val args by navArgs<NoteFragmentArgs>()
    private val noteViewModel by viewModels<NoteViewModel>()

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = noteViewModel.also {
                args.note?.let { note -> it.note.update { note } }
                it.screenMode = args.screenMode
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFab()
        if (args.screenMode != ScreenMode.GET) {
            binding.editTextTitle.requestFocus()
            val manager = requireActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(binding.editTextTitle, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun setUpFab() = (requireActivity() as? MainActivity)?.onFabClicked {
        when (args.screenMode) {
            ScreenMode.GET -> {
                val direction = NoteFragmentDirections.actionNoteSelf(args.note, ScreenMode.UPDATE)
                findNavController().navigate(direction)
            }
            ScreenMode.UPDATE -> {
                noteViewModel.update()
                findNavController().popBackStack()
            }
            ScreenMode.CREATE -> {
                noteViewModel.create()
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
