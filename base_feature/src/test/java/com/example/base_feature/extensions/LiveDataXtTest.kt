package com.example.base_feature.extensions

import androidx.lifecycle.MutableLiveData
import base_feature.core.ViewState
import base_feature.utils.extensions.*
import com.example.base_feature.utils.InstantExecutorExtension
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
class LiveDataXtTest {

    @Test
    fun `postNeutral() MUST set status to NEUTRAL WHEN it called`() {
        val mutableLiveData = MutableLiveData<ViewState<Boolean>>()
        mutableLiveData.postNeutral()
        assertEquals(
            mutableLiveData.value?.status,
            ViewState.Status.NEUTRAL
        )
        assertEquals(mutableLiveData.value?.data, null)
        assertEquals(mutableLiveData.value?.error, null)
    }

    @Test
    fun `setSuccess() MUST set status to SUCCESS and set data WHEN it called`() {
        val mutableLiveData = MutableLiveData<ViewState<Boolean>>()
        mutableLiveData.setSuccess(true)
        assertEquals(
            mutableLiveData.value?.status,
            ViewState.Status.SUCCESS
        )
        Assertions.assertEquals(mutableLiveData.value?.data, true)
        assertEquals(mutableLiveData.value?.error, null)

        mutableLiveData.setSuccess(false)
        assertEquals(
            mutableLiveData.value?.status,
            ViewState.Status.SUCCESS
        )
        Assertions.assertEquals(mutableLiveData.value?.data, false)
        assertEquals(mutableLiveData.value?.error, null)
    }

    @Test
    fun `postSuccess() MUST set status to SUCCESS and set data to received data WHEN it called`() {
        val mutableLiveData = MutableLiveData<ViewState<Boolean>>()
        mutableLiveData.postSuccess(true)
        assertEquals(
            mutableLiveData.value?.status,
            ViewState.Status.SUCCESS
        )
        Assertions.assertEquals(mutableLiveData.value?.data, true)
        assertEquals(mutableLiveData.value?.error, null)

        mutableLiveData.postSuccess(false)
        assertEquals(
            mutableLiveData.value?.status,
            ViewState.Status.SUCCESS
        )
        Assertions.assertEquals(mutableLiveData.value?.data, false)
        assertEquals(mutableLiveData.value?.error, null)
    }

    @Test
    fun `setError() MUST set status to ERROR and set data to null WHEN receives a throwable`() {
        val mutableLiveData = MutableLiveData<ViewState<Boolean>>()
        mutableLiveData.setError(Throwable("Error!"))
        assertEquals(
            mutableLiveData.value?.status,
            ViewState.Status.ERROR
        )
        assertEquals(mutableLiveData.value?.data, null)
        Assertions.assertEquals(mutableLiveData.value?.error?.message, "Error!")
        Assertions.assertTrue(mutableLiveData.value?.error is Throwable)
    }

    @Test
    fun `setError() MUST set status to ERROR and set data to null WHEN receives a message`() {
        val mutableLiveData = MutableLiveData<ViewState<Boolean>>()
        mutableLiveData.setError("Error!")
        assertEquals(
            mutableLiveData.value?.status,
            ViewState.Status.ERROR
        )
        assertEquals(mutableLiveData.value?.data, null)
        Assertions.assertEquals(mutableLiveData.value?.error?.message, "Error!")
        Assertions.assertTrue(mutableLiveData.value?.error is RuntimeException)
    }

    @Test
    fun `postError() MUST set status to ERROR and set data null WHEN it called`() {
        val mutableLiveData = MutableLiveData<ViewState<Boolean>>()
        mutableLiveData.postError(Throwable("Error!"))
        assertEquals(
            mutableLiveData.value?.status,
            ViewState.Status.ERROR
        )
        assertEquals(mutableLiveData.value?.data, null)
        Assertions.assertTrue(mutableLiveData.value?.error is Throwable)
    }

    @Test
    fun `postError() MUST set status to ERROR and set data to null WHEN receives a message`() {
        val mutableLiveData = MutableLiveData<ViewState<Boolean>>()
        mutableLiveData.postError("Error!")
        assertEquals(
            mutableLiveData.value?.status,
            ViewState.Status.ERROR
        )
        assertEquals(mutableLiveData.value?.data, null)
        Assertions.assertEquals(mutableLiveData.value?.error?.message, "Error!")
        Assertions.assertTrue(mutableLiveData.value?.error is RuntimeException)
    }

    @Test
    fun `postLoading() MUST set status to LOADING and set data to null WHEN receives a message`() {
        val mutableLiveData = MutableLiveData<ViewState<Boolean>>()
        mutableLiveData.postLoading()
        assertEquals(
            mutableLiveData.value?.status,
            ViewState.Status.LOADING
        )
        assertEquals(mutableLiveData.value?.data, null)
        assertEquals(mutableLiveData.value?.error, null)
    }

    @Test
    fun `setLoading() MUST set status to LOADING and set data to null WHEN receives a message`() {
        val mutableLiveData = MutableLiveData<ViewState<Boolean>>()
        mutableLiveData.setLoading()
        assertEquals(
            mutableLiveData.value?.status,
            ViewState.Status.LOADING
        )
        assertEquals(mutableLiveData.value?.data, null)
        assertEquals(mutableLiveData.value?.error, null)
    }

    @Test
    fun `ensure that asLiveData() returns an equivalent value`() {
        val a: MutableLiveData<Boolean> = MutableLiveData(true)
        Assertions.assertTrue(a.asLiveData().value == true)
    }

}