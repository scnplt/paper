package dev.sertan.android.paper.ui.main

import android.graphics.drawable.Icon
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomappbar.BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
import com.google.android.material.bottomappbar.BottomAppBar.FAB_ALIGNMENT_MODE_END
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.NavGraphDirections
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.ActivityMainBinding
import dev.sertan.android.paper.util.showToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener,
    Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment)
            .navController.apply { addOnDestinationChangedListener(this@MainActivity) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.bottomAppBar.setOnMenuItemClickListener(this)
        subscribeUi()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        // TODO Not yet implemented
        when (item?.itemId) {
            R.id.delete -> showToast("delete clicked")
            R.id.archive -> showToast("archive clicked")
            R.id.favorite -> showToast("favorite clicked")
            R.id.search -> showToast("search clicked")
            R.id.logOut -> viewModel.logOut()
            else -> return false
        }
        return true
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        setUpBottomAppBar(destination.id)
        setUpFab(destination.id)
    }

    private fun setUpBottomAppBar(destinationId: Int) = binding.bottomAppBar.run {
        val (alignment, menu) = when (destinationId) {
            R.id.addNoteFragment -> FAB_ALIGNMENT_MODE_END to R.menu.note_bottom_appbar
            R.id.editNoteFragment -> FAB_ALIGNMENT_MODE_END to R.menu.note_bottom_appbar
            R.id.homeFragment -> FAB_ALIGNMENT_MODE_CENTER to R.menu.home_bottom_appbar
            R.id.noteFragment -> FAB_ALIGNMENT_MODE_END to R.menu.note_bottom_appbar
            else -> {
                performHide()
                visibility = View.INVISIBLE
                return@run
            }
        }
        setFabAlignmentModeAndReplaceMenu(alignment, menu)
        visibility = View.VISIBLE
        performShow()
    }

    private fun setUpFab(destinationId: Int) = binding.fab.run {
        val iconId = when (destinationId) {
            R.id.addNoteFragment -> R.drawable.ic_done
            R.id.editNoteFragment -> R.drawable.ic_done
            R.id.homeFragment -> R.drawable.ic_add
            R.id.noteFragment -> R.drawable.ic_edit
            else -> {
                hide()
                return@run
            }
        }
        setImageIcon(Icon.createWithResource(this@MainActivity, iconId))
        show()
    }

    fun onFabClicked(listener: View.OnClickListener) = binding.fab.setOnClickListener(listener)

    private fun subscribeUi() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUser.collect {
                    when {
                        it.isSuccess && it.value != null -> navigateToHome()
                        it.isFailure || it.value == null -> navigateToLogin()
                        it.isIdle -> Handler(Looper.getMainLooper()).postDelayed({
                            navigateToLogin()
                            viewModel.refreshUser()
                        }, resources.getInteger(R.integer.duration_animation_3).toLong())
                    }
                }
            }
        }
    }

    private fun navigateToHome() {
        val direction = NavGraphDirections.actionGlobalHome()
        navController.navigate(direction)
    }

    private fun navigateToLogin() {
        val direction = NavGraphDirections.actionGlobalLogin()
        navController.navigate(direction)
    }
}
