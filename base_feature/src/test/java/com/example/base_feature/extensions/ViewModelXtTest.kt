package com.example.base_feature.extensions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import base_feature.utils.extensions.postSuccess
import base_feature.utils.extensions.viewState
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.jupiter.api.Test

class ViewModelXtTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `viewState extension MUST act like a MutableLiveData ViewState T`() {
        val a by viewState<Boolean>()
        val b = MutableLiveData<base_feature.core.ViewState<Boolean>>()
        a.postSuccess(true)
        b.postSuccess(true)
        assertEquals(
            a.value?.data,
            b.value?.data
        )
    }

}