package dev.sertan.android.paper.ui.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.FragmentRegisterBinding
import dev.sertan.android.paper.ui.BaseFragment

@AndroidEntryPoint
internal class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private val viewModel by viewModels<RegisterViewModel>()

    override fun getLayoutRes(): Int = R.layout.fragment_register

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
    }

}
