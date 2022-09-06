package dev.sertan.android.paper.util.extension

import android.view.View
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
    return object : ReadOnlyProperty<Fragment, VB>, DefaultLifecycleObserver {
        private var binding: VB? = null

        override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
            return binding ?: viewBindingProvider(thisRef.requireView()).also {
                binding = it
                thisRef.lifecycle.addObserver(this)
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            binding = null
        }
    }
}

internal fun Fragment.setLoadingDialogVisibility(isVisible: Boolean) {
    (activity as? MainActivity)?.setLoadingDialogVisibility(isVisible)
}

internal fun Fragment.showMessage(message: String?) {
    if (message == null) return
    (activity as? MainActivity)?.showToastMessage(message)
}

internal fun Fragment.navigateTo(direction: NavDirections) {
    findNavController().navigate(direction)
}
