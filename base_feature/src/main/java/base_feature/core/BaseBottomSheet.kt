package base_feature.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import base_feature.utils.extensions.adjustResize
import base_feature.utils.extensions.setStateExpanded
import com.example.base_feature.R
import com.example.domain.exception.DataSourceException
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<Binding : ViewBinding> : BottomSheetDialogFragment(),
    ViewStateListener {

    private var mView: View? = null

    private var _binding: Binding? = null
        get() {
            if (field == null)
                field = onCreateViewBinding(layoutInflater)
            return field
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mView == null) {
            mView = binding.root
        } else {
            dismiss()
        }
        return mView
    }

    protected val binding: Binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers(viewLifecycleOwner)
        setupView()
    }

    abstract fun onCreateViewBinding(inflater: LayoutInflater): Binding

    open fun setupView() = Unit

    open fun addObservers(owner: LifecycleOwner) = Unit

    override fun getTheme() = R.style.BottomSheetDialog

    override fun onStart() {
        super.onStart()

        adjustResize()
        setStateExpanded()
    }

    override fun dismiss() {
        super.dismiss()
        mView = null
    }

    override fun onStateError(error: Throwable) {
        return
    }

    override fun onStateLoading() {
        return
    }

    override fun hideLoading() {
        return
    }


    override fun handlePresentationException(error: DataSourceException, action: (() -> Unit)?) {
        return
    }

    override fun handleNoNetworkConnectionException(action: (() -> Unit)?) {
        return
    }
}