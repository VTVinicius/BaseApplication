package base_feature.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import base_feature.dialog.LoadingDialog
import org.koin.core.KoinComponent

abstract class BaseFragment <Binding : ViewBinding> : Fragment(), ViewStateListener, KoinComponent {


    private var _binding: Binding? = null
        get() {
            if (field == null)
                field = onCreateViewBinding(layoutInflater)
            return field
        }

    private var toolbar: Toolbar? = null

    private var loadingDialogFragment: LoadingDialog? = null
        get() {
            if (field == null)
                field = LoadingDialog()

            return field
        }

    private var loadingLottie: BottomSheetLottie? = null

    protected val binding: Binding get() = _binding!!

    private var keyboardEventListener: KeyboardEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        addObservers(viewLifecycleOwner)
    }

    override fun onResume() {
        super.onResume()
        if (keyboardEventListener == null) {
            keyboardEventListener = KeyboardEventListener(activity as AppCompatActivity) { isOpen ->
                onKeyboardChange(isOpen)
            }
        }
    }

    abstract fun onCreateViewBinding(inflater: LayoutInflater): Binding


    override fun onStop() {
        super.onStop()
        keyboardEventListener?.onLifecyclePause()
        hideKeyboard()
    }

    open fun setupToolbar(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener {
            toolbarBackButtonEvent()
            requireView().findNavController().navigateUp()
        }
    }

    open fun toolbarBackButtonEvent() = Unit
    open fun toolbarCloseButtonEvent() = Unit

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> try {
                findNavController().popBackStack()
            } catch (e: Exception) {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onStateError(error: Throwable) {
        hideLoading()
    }

    override fun onStateLoading() {
        childFragmentManager.let {
            it.executePendingTransactions()
            if (loadingDialogFragment?.isAdded?.not() == true) {
                loadingDialogFragment?.show(this)
            }
        }
    }

    override fun hideLoading() {
        loadingDialogFragment?.dismissAllowingStateLoss()
        loadingDialogFragment = null
    }

    override fun navigateToLogin() {
        baseNavigation.navigateToLogin()
    }

    override fun handleWithUnauthorized(error: UnauthorizedException) {
        error.message?.let {
            showErrorDialog(description = it, action = this::navigateToLogin)
        } ?: navigateToLogin()
    }

    override fun handlePresentationException(error: DataSourceException, action: (() -> Unit)?) {
        showErrorDialog(
            description = error.message,
            action = action,
            backButtonIsVisible = true,
            backButtonListener = {
                activity?.onBackPressed()
            }
        )
    }

    override fun handleNoNetworkConnectionException(action: (() -> Unit)?) {
        showErrorDialog(
            title = getString(UikitR.string.something_went_wrong),
            description = getString(UikitR.string.generic_network_error_description),
            buttonText = getString(UikitR.string.understood),
            action = {
                if (action != null) action.invoke()
                else activity?.onBackPressed()
            },
            backButtonIsVisible = true,
            backButtonListener = {
                activity?.onBackPressed()
            }
        )
    }

    fun showGenericErrorDialog() {
        showErrorDialog(
            title = getString(UikitR.string.generic_error_title),
            description = getString(UikitR.string.generic_error_description),
            buttonText = getString(UikitR.string.understood),
            action = {
                activity?.onBackPressed()
            },
            backButtonIsVisible = true,
            backButtonListener = {
                activity?.onBackPressed()
            }
        )
    }

    open fun showErrorDialog(
        @DrawableRes drawable: Int? = null,
        title: String? = null,
        description: String? = null,
        buttonText: String? = null,
        action: (() -> Unit)? = null,
        backButtonIsVisible: Boolean = false,
        backButtonListener: (() -> Unit)? = null
    ) {
        postPageView(
            BaseAppEventsConstants.GENERIC_ERROR_SCREEN,
            BaseAppEventsConstants.ERROR_FLOW
        )
        GenericErrorBottomSheet.newInstance(
            drawable = drawable,
            title = title ?: getString(UikitR.string.generic_error_title),
            description = description ?: getString(UikitR.string.generic_network_error_description),
            buttonText = buttonText ?: getString(UikitR.string.understood),
            onPressed = {
                action?.invoke()
            },
            backButtonIsVisible = backButtonIsVisible,
            backButtonListener = {
                backButtonListener?.invoke()
            }
        ).showBottomSheet(this@BaseFragment)
    }

    open fun showGenericDialog(
        @DrawableRes drawable: Int? = null,
        title: String = "",
        description: CharSequence? = null,
        buttonText: String? = null,
        action: (() -> Unit)? = null,
        closeButtonIsVisible: Boolean = true,
        textColor: Int? = null
    ) {
        GenericBottomSheet.newInstance(
            drawable = drawable,
            title = title,
            description = description ?: "",
            buttonText = buttonText ?: getString(UikitR.string.understood),
            onPressed = {
                action?.invoke()
            },
            closeButtonIsVisible = closeButtonIsVisible,
            textColor = textColor
        ).showBottomSheet(this@BaseFragment)
    }

    protected fun showLottie(lottieEnum: Int) {
        this.let { fragment ->
            lifecycleScope.launchWhenResumed {
                loadingLottie = BottomSheetLottie.newInstance(type = lottieEnum)
                loadingLottie?.showBottomSheet(fragment)
            }
        }
    }

    protected fun hideLottie() = lifecycleScope.launchWhenResumed {
        loadingLottie?.dismissAllowingStateLoss()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        loadingLottie = null
        keyboardEventListener = null
        loadingDialogFragment = null
    }

    open fun addObservers(owner: LifecycleOwner) = Unit

    open fun setupView() = Unit

    open fun onKeyboardChange(isOpen: Boolean) = Unit

    fun setToolbarButtonVisible(enabled: Boolean) {
        (requireActivity() as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(enabled)
        }
    }

    fun postPageView(screenName: String, flowName: String) {
        corebaseViewModel.postPageView(screenName, flowName)
    }

    fun postPageClick(screenName: String, flowName: String, buttonName: String) {
        corebaseViewModel.postPageClick(screenName, flowName, buttonName)
    }
}