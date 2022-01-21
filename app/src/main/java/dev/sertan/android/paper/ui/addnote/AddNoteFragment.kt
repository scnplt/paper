package dev.sertan.android.paper.ui.addnote

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
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.databinding.FragmentAddNoteBinding
import dev.sertan.android.paper.ui.main.MainActivity
import dev.sertan.android.paper.util.showToast
import kotlinx.coroutines.launch


@AndroidEntryPoint
internal class AddNoteFragment : Fragment() {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private val addNoteViewModel by viewModels<AddNoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = addNoteViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
        (requireActivity() as? MainActivity)?.onFabClicked { addNoteViewModel.save() }
    }

    private fun subscribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addNoteViewModel.uiState.collect { uiState ->
                    if (uiState.isSaved) findNavController().popBackStack()
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
