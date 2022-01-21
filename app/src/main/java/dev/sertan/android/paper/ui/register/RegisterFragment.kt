package dev.sertan.android.paper.ui.register

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
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.FragmentRegisterBinding
import dev.sertan.android.paper.util.Validate
import dev.sertan.android.paper.util.showToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class RegisterFragment : Fragment(), RegisterCallback {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = registerViewModel
        binding.callback = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
    }

    private fun subscribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerViewModel.uiState.collect { uiState ->
                    uiState.message.value?.let { requireContext().showToast(it) }
                    uiState.messageRes.value?.let {
                        val message = resources.getString(it)
                        requireContext().showToast(message)
                    }

                    if (uiState.isRegistered) findNavController().popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRegisterClick(): Unit = with(binding) {
        when {
            !editTextEmail.text.toString().let(Validate::email) -> {
                editTextEmail.error = resources.getString(R.string.invalid_email)
                editTextEmail.requestFocus()
            }
            !editTextPassword.text.toString().let(Validate::password) -> {
                editTextPassword.error = resources.getString(R.string.invalid_pwd)
                editTextPassword.requestFocus()
            }
            editTextPassword.text.toString() != editTextPasswordConfirm.text.toString() -> {
                editTextPasswordConfirm.error = resources.getString(R.string.invalid_pwd_confirm)
                editTextPasswordConfirm.requestFocus()
            }
            else -> registerViewModel.register()
        }
    }
}
