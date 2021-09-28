package dev.sertan.android.paper.ui.editnote

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.FragmentEditNoteBinding
import dev.sertan.android.paper.ui.common.BaseFragment
import dev.sertan.android.paper.ui.main.MainActivity

@AndroidEntryPoint
internal class EditNoteFragment : BaseFragment<FragmentEditNoteBinding>() {
    private val viewModel by viewModels<EditNoteViewModel>()
    private val args by navArgs<EditNoteFragmentArgs>()
    private val note by lazy { args.note }

    override fun getLayoutRes(): Int = R.layout.fragment_edit_note

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel.apply { note.postValue(this@EditNoteFragment.note) }

        (requireActivity() as? MainActivity)?.onFabClicked { viewModel.update(view) }
    }

}
