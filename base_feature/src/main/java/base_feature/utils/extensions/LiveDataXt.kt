package base_feature.utils.extensions

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import base_feature.core.ViewState
import base_feature.core.isError
import base_feature.core.isLoading
import base_feature.core.isSuccess
import base_feature.utils.EventLiveData
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> MutableLiveData<base_feature.core.ViewState<T>>.postNeutral() {
    value =base_feature.core.ViewState(ViewState.Status.NEUTRAL, null, null)
}

fun <T> MutableLiveData<base_feature.core.ViewState<T>>.setSuccess(data: T) {
    value = base_feature.core.ViewState(ViewState.Status.SUCCESS, data, null)
}

fun <T> MutableLiveData<base_feature.core.ViewState<T>>.setError(error: Throwable?) {
    value = base_feature.core.ViewState(ViewState.Status.ERROR, null, error)
}

fun <T> MutableLiveData<base_feature.core.ViewState<T>>.setError(message: String?) {
    value = base_feature.core.ViewState(ViewState.Status.ERROR, null, RuntimeException(message))
}

fun <T> MutableLiveData<base_feature.core.ViewState<T>>.postSuccess(data: T) {
    postValue(base_feature.core.ViewState(ViewState.Status.SUCCESS, data, null))
}

fun <T> MutableLiveData<base_feature.core.ViewState<T>>.postError(error: Throwable?) {
    postValue(base_feature.core.ViewState(ViewState.Status.ERROR, null, error))
}

fun <T> MutableLiveData<base_feature.core.ViewState<T>>.postError(message: String?) {
    postValue(base_feature.core.ViewState(ViewState.Status.ERROR, null, RuntimeException(message)))
}

fun <T> MutableLiveData<base_feature.core.ViewState<T>>.postLoading() {
    postValue(base_feature.core.ViewState(ViewState.Status.LOADING, null, null))
}

fun <T> MutableLiveData<base_feature.core.ViewState<T>>.setLoading() {
    value = base_feature.core.ViewState(ViewState.Status.LOADING, null, null)
}

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> = this

fun <T> LiveData<base_feature.core.ViewState<T>>.isLoading() = value.isLoading()

fun <T> LiveData<base_feature.core.ViewState<T>>.isSuccess() = value.isSuccess()

fun <T> LiveData<base_feature.core.ViewState<T>>.isError() = value.isError()

fun <T> LiveData<base_feature.core.ViewState<T>>.observeLiveData(
    lifecycleOwner: LifecycleOwner,
    isSingleEvent: Boolean = false,
    event: (base_feature.core.ViewState<T>) -> Unit
) {
    observe(lifecycleOwner, Observer {
        (this as? EventLiveData)?.apply {
            getContent(isSingleEvent)?.let { event(it) }
            return@Observer
        }

        value?.let { event(it) }
    })
}


@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> ViewState<T>.assert(isSuccessAssert: Boolean = true) {
    if(this.status == ViewState.Status.LOADING) return
    if (isSuccessAssert) {
        kotlin.assert(this.error == null)
        kotlin.assert(this.status == ViewState.Status.SUCCESS)
        kotlin.assert(this.data != null)
    }  else {
        kotlin.assert(this.error != null)
        kotlin.assert(this.status == ViewState.Status.ERROR)
        kotlin.assert(this.data == null)
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.NONE)

fun <T> LiveData<ViewState<T>>.assertNeutral() {
    assert(this.value?.status == ViewState.Status.NEUTRAL)
    assert(this.value?.error == null)
    assert(this.value?.data == null)
}

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValueAndAssert(
    isSuccessAssert: Boolean = true,
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
) {
    val value = this.getOrAwaitValue(time, timeUnit, afterObserve) as ViewState<T>
    value.assert(isSuccessAssert)
}