package dev.sertan.android.paper.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

internal abstract class BaseFragment<VDB : ViewDataBinding> : Fragment() {
    private var _binding: VDB? = null
    val binding get() = _binding!!

    @LayoutRes
    abstract fun getLayoutRes(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(layoutInflater, getLayoutRes(), container, false)
        return binding.apply { lifecycleOwner = viewLifecycleOwner }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
