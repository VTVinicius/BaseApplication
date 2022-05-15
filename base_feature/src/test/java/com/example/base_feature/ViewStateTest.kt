package com.example.base_feature

import base_feature.core.isError
import base_feature.core.isLoading
import base_feature.core.isSuccess
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations

class ViewStateTest {

    @After
    fun after() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `isSuccess MUST return true WHEN viewState status == SUCCESS`() {
        val subject = base_feature.core.ViewState<Boolean>(
            base_feature.core.ViewState.Status.SUCCESS
        )

        assertTrue(subject.isSuccess())
        assertFalse(subject.isError())
        assertFalse(subject.isLoading())
    }

    @Test
    fun `isError MUST return true WHEN viewState status == ERROR`() {
        val subject = base_feature.core.ViewState<Boolean>(
            base_feature.core.ViewState.Status.ERROR
        )

        assertTrue(subject.isError())
        assertFalse(subject.isSuccess())
        assertFalse(subject.isLoading())
    }

    @Test
    fun `isLoading MUST return true WHEN viewState status == LOADING`() {
        val subject = base_feature.core.ViewState<Boolean>(
           base_feature.core.ViewState.Status.LOADING
        )

        assertTrue(subject.isLoading())
        assertFalse(subject.isSuccess())
        assertFalse(subject.isError())
    }

}