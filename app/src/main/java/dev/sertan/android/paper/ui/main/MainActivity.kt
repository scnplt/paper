package dev.sertan.android.paper.ui.main

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
internal class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener,
    Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment)
            .navController.apply { addOnDestinationChangedListener(this@MainActivity) }
    }

    private var currentNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.bottomAppBar.setOnMenuItemClickListener(this)
        subscribeUi()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete -> currentNote?.let { viewModel.deleteNote(it) }
            R.id.archive -> showToast("archive clicked - note: ${currentNote?.title}")
            R.id.favorite -> showToast("favorite clicked - note: ${currentNote?.title}")
            R.id.search -> showToast("search clicked - note: ${currentNote?.title}")
            R.id.logOut -> viewModel.logOut()
            else -> return false
        }
        return true
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) = binding.run {
        when (destination.id) {
            R.id.homeFragment -> {
                currentNote = null
                fab.setImageResource(R.drawable.ic_add)
                bottomAppBar
                    .setFabAlignmentModeAndReplaceMenu(FAB_ALIGNMENT_MODE_CENTER, R.menu.home)
                showBottomBar()
                fab.show()
            }
            R.id.noteFragment -> {
                val args = NoteFragmentArgs.fromBundle(arguments!!)
                currentNote = args.note
                if (args.screenMode == ScreenMode.GET) {
                    fab.setImageResource(R.drawable.ic_edit)
                    bottomAppBar
                        .setFabAlignmentModeAndReplaceMenu(FAB_ALIGNMENT_MODE_END, R.menu.note)
                    showBottomBar()
                } else {
                    bottomAppBar.fabAlignmentMode = FAB_ALIGNMENT_MODE_END
                    fab.setImageResource(R.drawable.ic_done)
                    hideBottomBar()
                }
                fab.show()
            }
            else -> {
                currentNote = null
                fab.hide()
                hideBottomBar()
            }
        }
    }

    private fun hideBottomBar() {
        binding.bottomAppBar.performHide()
        binding.bottomAppBar.visibility = View.INVISIBLE
    }

    private fun showBottomBar() {
        binding.bottomAppBar.visibility = View.VISIBLE
        binding.bottomAppBar.performShow()
    }

    fun onFabClicked(listener: View.OnClickListener) = binding.fab.setOnClickListener(listener)

    private fun subscribeUi() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.currentUser.collect {
                        when {
                            it.isSuccess && it.value != null -> navigateToHome()
                            it.isFailure -> navigateToLogin()
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
