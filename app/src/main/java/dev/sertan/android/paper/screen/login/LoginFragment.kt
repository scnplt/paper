package dev.sertan.android.paper.screen.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.FragmentLoginBinding
import dev.sertan.android.paper.util.extension.navigateTo
import dev.sertan.android.paper.util.extension.provideBinding
import dev.sertan.android.paper.util.extension.setLoadingDialogVisibility
import dev.sertan.android.paper.util.extension.showMessage
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by provideBinding(FragmentLoginBinding::bind)
    private val loginViewModel by viewModels<LoginViewModel>()

    private val uiStateFlowCollector = FlowCollector<LoginUiState> {
        setLoadingDialogVisibility(isVisible = it.isLoading)
        showMessage(messageRes = it.exceptionMessageRes.data)
        if (it.isLoggedIn) navigateTo(LoginFragmentDirections.actionGlobalHomeFragment())
        with(binding) {
            it.isEmailValid?.let { isValid -> emailInputLayout.errorEnabled = !isValid }
            it.isPasswordValid?.let { isValid -> passwordInputLayout.errorEnabled = !isValid }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewListeners()
        subscribeUi()
    }

    private fun initViewListeners() {
        with(binding) {
            emailInputLayout.doAfterTextChanged(loginViewModel::updateEmailAddress)
            passwordInputLayout.doAfterTextChanged(loginViewModel::updatePassword)
            // logInButton.setOnClickListener { loginViewModel.logIn() }
            registerButton.setOnClickListener {
                navigateTo(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
            sendPasswordResetEmailTextView.setOnClickListener {
                // loginViewModel.sendPasswordResetEmail()
            }
        }
    }

    private fun subscribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.loginUiState.collect(uiStateFlowCollector)
            }
        }
    }
}
