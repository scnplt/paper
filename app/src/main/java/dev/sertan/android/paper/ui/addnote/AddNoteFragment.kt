package dev.sertan.android.paper.ui.addnote

import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.FragmentAddNoteBinding
import dev.sertan.android.paper.ui.BaseFragment

@AndroidEntryPoint
internal class AddNoteFragment : BaseFragment<FragmentAddNoteBinding>() {
    private val viewModel by viewModels<AddNoteViewModel>()

    override fun getLayoutRes(): Int = R.layout.fragment_add_note

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        customizeFab {
            val icon = Icon.createWithResource(requireContext(), R.drawable.ic_done)
            setImageIcon(icon)
            setOnClickListener { viewModel.save(view) }
            show()
        }
    }

}
