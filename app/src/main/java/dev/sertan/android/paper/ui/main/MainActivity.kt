package dev.sertan.android.paper.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.sertan.android.paper.NavGraphDirections
import dev.sertan.android.paper.R
import dev.sertan.android.paper.data.model.Note
import dev.sertan.android.paper.databinding.ActivityMainBinding
import dev.sertan.android.paper.ui.note.NoteFragmentArgs
import dev.sertan.android.paper.ui.note.ScreenMode
import dev.sertan.android.paper.util.showToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener,
    Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModels<MainViewModel>()
    private var currentNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
            .apply { addOnDestinationChangedListener(this@MainActivity) }

        binding.bottomAppBar.setOnMenuItemClickListener(this)
        subscribeUi()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete -> currentNote?.let { viewModel.deleteNote(it) }
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
        when (destination.id) {
            R.id.homeFragment -> {
                currentNote = null
                setBottomAppBarForHome()
            }
            R.id.noteFragment -> {
                val args = NoteFragmentArgs.fromBundle(arguments!!)
                currentNote = args.note
                setBottomAppBarForNote(args.screenMode)
            }
            else -> {
                currentNote = null
                binding.fab.hide()
                hideBottomAppBar()
            }
        }
    }

    private fun setBottomAppBarForHome() {
        binding.run {
            bottomAppBar.setFabAlignmentModeAndReplaceMenu(FAB_ALIGNMENT_MODE_CENTER, R.menu.home)
            fab.setImageResource(R.drawable.ic_add)
            showBottomAppBar()
            fab.show()
        }
    }

    private fun setBottomAppBarForNote(screenMode: ScreenMode) {
        binding.run {
            bottomAppBar.setFabAlignmentModeAndReplaceMenu(FAB_ALIGNMENT_MODE_END, R.menu.note)
            if (screenMode == ScreenMode.GET) {
                fab.setImageResource(R.drawable.ic_edit)
                showBottomAppBar()
            } else {
                fab.setImageResource(R.drawable.ic_done)
                hideBottomAppBar()
            }
            fab.show()
        }
    }

    private fun showBottomAppBar() {
        binding.bottomAppBar.apply { visibility = View.VISIBLE }.performShow()
    }

    private fun hideBottomAppBar() {
        binding.bottomAppBar.apply {
            performHide()
            animate().setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    visibility = View.INVISIBLE
                }
            })
        }
    }

    fun onFabClicked(listener: View.OnClickListener) = binding.fab.setOnClickListener(listener)

    private fun subscribeUi() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.currentUser.collect {
                        when {
                            it.isSuccess && it.value != null -> navigateToHome()
                            it.isIdle -> Handler(Looper.getMainLooper()).postDelayed({
                                navigateToLogin()
                                viewModel.refreshUser()
                            }, resources.getInteger(R.integer.duration_animation_3).toLong())
                            else -> navigateToLogin()
                        }
                    }
                }

                viewModel.uiState.collect { uiState ->
                    if (uiState.noteDeleted.value == true) {
                        navController.popBackStack()
                        Snackbar.make(
                            window.decorView.rootView,
                            R.string.note_deleted,
                            Snackbar.LENGTH_LONG
                        ).apply {
                            setAction(R.string.undo) { viewModel.undoDelete() }
                            setAnchorView(R.id.fab)
                        }.show()
                    }

                    uiState.message.value?.let { showToast(it) }
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
