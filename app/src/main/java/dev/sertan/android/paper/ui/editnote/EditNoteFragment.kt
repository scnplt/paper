package dev.sertan.android.paper.ui.editnote

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
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.databinding.FragmentEditNoteBinding
import dev.sertan.android.paper.ui.main.MainActivity
import dev.sertan.android.paper.util.showToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class EditNoteFragment : Fragment() {
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private val editNoteViewModel by viewModels<EditNoteViewModel>()

    private val args by navArgs<EditNoteFragmentArgs>()
    private val note by lazy { args.note }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = editNoteViewModel.apply { setNote(note) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
        (requireActivity() as? MainActivity)?.onFabClicked { editNoteViewModel.update() }
    }

    private fun subscribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                editNoteViewModel.uiState.collect { uiState ->
                    if (uiState.isUpdated) findNavController().popBackStack()
                    uiState.message.value?.let { requireContext().showToast(it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
