package base_feature.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import base_feature.utils.extensions.observeLiveData
import com.example.domain.exception.DataSourceException
import java.net.ConnectException
import java.net.UnknownHostException

interface ViewStateListener {

        fun onStateError(error: Throwable)

        fun onStateLoading()

        fun hideLoading()

        fun handlePresentationException(error: DataSourceException, action: (() -> Unit)? = null)

        fun handleNoNetworkConnectionException(action: (() -> Unit)? = null)

        private fun <T> ViewState<T>.handle(
            onError: ((Throwable) -> Unit)? = null,
            onLoading: (() -> Unit)? = null,
            onComplete: (() -> Unit)? = null,
            onSuccess: ((T) -> Unit)? = null,
            errorBottomSheetAction: (() -> Unit)? = null,
            showDataSourceException: Boolean = true
        ) {

            stateHandler(
                onSuccess = {
                    hideLoading()
                    onSuccess?.invoke(it)
                    onComplete?.invoke()
                },
                onError = { error ->
                    hideLoading()
                    when (error) {
                        is DataSourceException -> {
                            if (showDataSourceException)
                                handlePresentationException(error, errorBottomSheetAction)
                        }
                        is UnknownHostException, is ConnectException -> {
                            handleNoNetworkConnectionException(errorBottomSheetAction)
                            return@stateHandler
                        }
                    }
                    onError?.invoke(error) ?: onStateError(error)
                    onComplete?.invoke()
                },
                loading = { onLoading?.invoke() ?: onStateLoading() }
            )
        }

        fun <T> LiveData<ViewState<T>>.onPostValue(
            lifecycleOwner: LifecycleOwner,
            onError: ((Throwable) -> Unit)? = null,
            onComplete: (() -> Unit)? = null,
            onLoading: (() -> Unit)? = null,
            onSuccess: ((T) -> Unit)? = null,
            errorBottomSheetAction: (() -> Unit)? = null,
            showDataSourceException: Boolean = true
        ) {
            observeLiveData(lifecycleOwner) {
                it.handle(
                    onError,
                    onLoading,
                    onComplete,
                    onSuccess,
                    errorBottomSheetAction,
                    showDataSourceException
                )
            }
        }

        fun <T> LiveData<ViewState<T>>.onFirstPostValue(
            lifecycleOwner: LifecycleOwner,
            onError: ((Throwable) -> Unit)? = null,
            onComplete: (() -> Unit)? = null,
            onLoading: (() -> Unit)? = null,
            onSuccess: (T) -> Unit,
            errorBottomSheetAction: (() -> Unit)? = null
        ) {
            observeLiveData(lifecycleOwner, true) {
                it.handle(onError, onLoading, onComplete, onSuccess, errorBottomSheetAction)
            }
        }

    }
