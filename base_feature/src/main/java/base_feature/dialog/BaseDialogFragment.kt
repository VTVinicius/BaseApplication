package base_feature.dialog

import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import base_feature.core.BaseFragment

open class BaseDialogFragment : DialogFragment(), LifecycleObserver {

    private var isLoading = false
    private var lifecycleOwner: LifecycleOwner? = null

    fun show(fragmentContainer: BaseFragment<*>){
        show(
            fragmentContainer.childFragmentManager,
            tag
        )
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isAdded || isLoading) {
            isLoading = true
            try {
                super.show(manager, tag)
            } catch (e: Exception) {
                return
            }
        }
    }

    override fun dismissAllowingStateLoss() {
        if (isLoading) {
            isLoading = false
            super.dismissAllowingStateLoss()
        }
    }

    fun updateWindowFeature(){
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.decorView?.setBackgroundResource(android.R.color.transparent)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}