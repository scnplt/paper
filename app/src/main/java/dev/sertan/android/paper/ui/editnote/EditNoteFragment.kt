package dev.sertan.android.paper.ui.editnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.databinding.FragmentEditNoteBinding
import dev.sertan.android.paper.ui.main.MainActivity

@AndroidEntryPoint
internal class EditNoteFragment : Fragment() {
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EditNoteViewModel>()
    private val args by navArgs<EditNoteFragmentArgs>()
    private val note by lazy { args.note }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel.apply { note.value = this@EditNoteFragment.note }
        (requireActivity() as? MainActivity)?.onFabClicked { viewModel.update(view) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
