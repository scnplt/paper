package dev.sertan.android.paper.screen.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.NavMainDirections
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    private val currentUserUidFlowCollector = FlowCollector<String?> {
        val direction = when {
            it.isNullOrBlank() -> NavMainDirections.actionGlobalLoginFragment()
            else -> NavMainDirections.actionGlobalHomeFragment()
        }
        findNavController(R.id.fragmentContainerView).navigate(direction)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeUi()
    }

    private fun subscribeUi() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.currentUserUid.collect(currentUserUidFlowCollector)
            }
        }
    }

    fun setLoadingDialogVisibility(isVisible: Boolean) {
        binding.loadingDialogLayout.isVisible = isVisible
    }

    fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
