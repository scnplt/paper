package dev.sertan.android.paper.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.Fragment
import dev.sertan.android.paper.R

internal class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullscreenMode(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fullscreenMode(false)
    }

    private fun fullscreenMode(isActive: Boolean) {
        val window = requireActivity().window

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                .let { if (isActive) window.addFlags(it) else window.clearFlags(it) }
            return
        }

        window.insetsController?.apply {
            WindowInsets.Type.navigationBars().let { if (isActive) hide(it) else show(it) }
        }
    }

}
