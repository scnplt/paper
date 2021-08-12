package dev.sertan.android.paper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.sertan.android.paper.ui.main.MainActivity

internal abstract class BaseFragment<VDB : ViewDataBinding> : Fragment() {
    lateinit var binding: VDB

    @LayoutRes
    abstract fun getLayoutRes(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, getLayoutRes(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    inline fun customizeFab(block: FloatingActionButton.() -> Unit) {
        val activity = requireActivity()
        if (activity !is MainActivity) return
        activity.customizeFab(block)
    }

}
