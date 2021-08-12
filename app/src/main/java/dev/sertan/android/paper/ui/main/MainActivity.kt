package dev.sertan.android.paper.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.NavGraphDirections
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.ActivityMainBinding

@AndroidEntryPoint
internal class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navHostFragment.navController
            .apply { addOnDestinationChangedListener(this@MainActivity) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_main, null, false)
        setContentView(binding.root)

        listenObservables()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        binding.fab.hide()
    }

    inline fun customizeFab(block: (FloatingActionButton) -> Unit) {
        block(binding.fab)
    }

    private fun listenObservables() {
        viewModel.currentUser.observe(this) {
            if (it.isSuccess() && it.value != null) {
                val direction = NavGraphDirections.actionGlobalHome()
                navController.navigate(direction)
                return@observe
            }

            if (it.isFailure()) {
                val direction = NavGraphDirections.actionGlobalLogin()
                navController.navigate(direction)
                return@observe
            }

            if (it.isIdle()) {
                val duration = resources.getInteger(R.integer.duration_animation_3).toLong()
                val runnable = Runnable {
                    val direction = NavGraphDirections.actionGlobalLogin()
                    navController.navigate(direction)
                    viewModel.refreshUser()
                }
                Handler(Looper.getMainLooper()).postDelayed(runnable, duration)
            }
        }
    }

}
