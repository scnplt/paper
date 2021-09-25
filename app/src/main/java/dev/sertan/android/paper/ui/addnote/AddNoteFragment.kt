package dev.sertan.android.paper.ui.addnote

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.FragmentAddNoteBinding
import dev.sertan.android.paper.ui.common.BaseFragment
import dev.sertan.android.paper.ui.main.MainActivity

@AndroidEntryPoint
internal class AddNoteFragment : BaseFragment<FragmentAddNoteBinding>() {
    private val viewModel by viewModels<AddNoteViewModel>()

    override fun getLayoutRes(): Int = R.layout.fragment_add_note

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        (requireActivity() as MainActivity).onFabClicked { viewModel.save(view) }
    }

}
