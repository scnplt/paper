package dev.sertan.android.paper.util.extension

import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dev.sertan.android.paper.screen.main.MainActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal fun <VB : ViewBinding> Fragment.provideBinding(
    viewBindingProvider: (View) -> VB
): ReadOnlyProperty<Fragment, VB> {
    return object : ReadOnlyProperty<Fragment, VB> {
        private var binding: VB? = null

        private val lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                binding = null
            }
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
            return binding ?: viewBindingProvider(thisRef.requireView()).also {
                viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
                binding = it
            }
        }
    }
}

internal fun Fragment.setLoadingDialogVisibility(isVisible: Boolean) {
    (activity as? MainActivity)?.setLoadingDialogVisibility(isVisible)
}

internal fun Fragment.showMessage(@StringRes messageRes: Int?) {
    if (messageRes == null) return
    (activity as? MainActivity)?.showToastMessage(messageRes)
}

internal fun Fragment.navigateTo(direction: NavDirections) {
    findNavController().navigate(direction)
}
