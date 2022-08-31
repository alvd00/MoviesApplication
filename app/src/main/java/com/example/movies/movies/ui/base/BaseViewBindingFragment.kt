package com.alvd.movies.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.movies.movies.model.app_state.AppState
import com.example.movies.movies.model.app_state.IAppState


abstract class BaseViewBindingFragment<VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {
    private var binding: VB? = null
    protected val viewBinding: VB get() = binding!!

    abstract fun renderSuccess(result: AppState.Success<*>)
    abstract fun showLoading(isShow: Boolean)
    abstract fun showError(throwable: Throwable)

    abstract fun initListeners()
    abstract fun initObservers()
    protected fun renderData(result: IAppState) {
        when (result) {
            is AppState.Error -> {
                showLoading(false)
                showError(result.error)
            }
            is AppState.Loading -> showLoading(true)
            is AppState.Success<*> -> renderSuccess(result)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, container, false)
        initObservers()
        initListeners()
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}