package dev.sertan.android.paper.ui.login

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
import dev.sertan.android.paper.databinding.FragmentLoginBinding
import dev.sertan.android.paper.util.Validate
import dev.sertan.android.paper.util.showToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class LoginFragment : Fragment(), LoginCallback {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = loginViewModel
        binding.callback = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
    }

    private fun subscribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.uiState.collect { uiState ->
                    uiState.message.value?.let { requireContext().showToast(it) }
                    uiState.messageRes.value?.let {
                        val message = resources.getString(it)
                        requireContext().showToast(message)
                    }
                }
            }
        }
    }

    override fun onLoginClick() {
        if (checkEmailAddress() && checkPassword()) loginViewModel.logIn()
    }

    override fun onRecoverPasswordClick() {
        if (checkEmailAddress()) loginViewModel.recoverPassword()
    }

    override fun onRegisterClick() {
        val direction = LoginFragmentDirections.actionLoginToRegister()
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkEmailAddress(): Boolean {
        return Validate.email("${binding.editTextEmail.text}").also { isValid ->
            binding.textLayoutEmail.error =
                resources.getString(R.string.invalid_email).takeUnless { isValid }
        }
    }

    private fun checkPassword(): Boolean {
        return Validate.password("${binding.editTextPassword.text}").also { isValid ->
            binding.textLayoutPassword.error =
                resources.getString(R.string.invalid_pwd).takeUnless { isValid }
        }
    }
}
