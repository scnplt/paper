package dev.sertan.android.paper.ui.main

import android.graphics.drawable.Icon
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.NavGraphDirections
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

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

        val iconId = when (destination.id) {
            R.id.addNoteFragment -> R.drawable.ic_done
            R.id.editNoteFragment -> R.drawable.ic_done
            R.id.homeFragment -> R.drawable.ic_add
            R.id.noteFragment -> R.drawable.ic_edit
            else -> return
        }

        val icon = Icon.createWithResource(this, iconId)
        binding.fab.apply { setImageIcon(icon) }.show()
    }

    fun onFabClicked(listener: View.OnClickListener) = binding.fab.setOnClickListener(listener)

    private fun listenObservables() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUser.collect {
                    if (it.isSuccess && it.value != null) {
                        val direction = NavGraphDirections.actionGlobalHome()
                        navController.navigate(direction)
                        return@collect
                    }

                    if (it.isFailure) {
                        val direction = NavGraphDirections.actionGlobalLogin()
                        navController.navigate(direction)
                        return@collect
                    }

                    if (it.isIdle) {
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
    }

}
