package com.example.base_feature.extensions

import base_feature.utils.extensions.insertOrUpdateCharPosition
import org.junit.Assert
import org.junit.jupiter.api.Test

class EditTextXtTest {

    @Test
    fun `insertOrUpdateCharPosition - if char DOES NOT EXISTS MUST insert on referenced position`() {
        val s = "123456"
        val result = s.insertOrUpdateCharPosition(s.length - 2, "-")

        Assert.assertEquals("12345-6", result)
    }

    @Test
    fun `insertOrUpdateCharPosition - if char EXISTS on String MUST update position of it on referenced position`() {
        val s = "12345-67"
        val result = s.insertOrUpdateCharPosition(s.length - 2, "-")

        Assert.assertEquals("123456-7", result)
    }

    @Test
    fun `insertOrUpdateCharPosition - if char EXISTS LOTS of selected char MUST delete them all and insert in referenced position`() {
        val s = "12-34-5-67"
        val result = s.insertOrUpdateCharPosition(s.length - 2, "-")

        Assert.assertEquals("123456-7", result)
    }

    @Test
    fun `insertOrUpdateCharPosition - if the String is EMPTY MUST return a EMPTY string`() {
        val s = ""
        val result = s.insertOrUpdateCharPosition(s.length - 2, "-")

        Assert.assertEquals("", result)
    }

    @Test
    fun `insertOrUpdateCharPosition - if the String is BLANK MUST return a EMPTY string`() {
        val s = "   "
        val result = s.insertOrUpdateCharPosition(s.length - 2, "-")

        Assert.assertEquals("", result)
    }

    @Test
    fun `insertOrUpdateCharPosition - if the char is EMPTY MUST return THE SAME string`() {
        val s = "1234567"
        val result = s.insertOrUpdateCharPosition(s.length - 2, "")

        Assert.assertEquals(s, result)
    }

    @Test
    fun `insertOrUpdateCharPosition - if position is HIGHER than string length MUST return the SAME string`() {
        val s = "1234567"
        val result = s.insertOrUpdateCharPosition(999, "-")

        Assert.assertEquals(s, result)
    }

    @Test
    fun `insertOrUpdateCharPosition - if position is SMALLER than ZERO MUST return the SAME string`() {
        val s = "1234567"
        val result = s.insertOrUpdateCharPosition(-2, "-")

        Assert.assertEquals(s, result)
    }

    @Test
    fun `insertOrUpdateCharPosition - if position is HIGHER than string length and char already exists on String MUST RETURN the SAME string`() {
        val s = "12345-67"
        val result = s.insertOrUpdateCharPosition(9, "-")

        Assert.assertEquals(s, result)
    }

    @Test
    fun `insertOrUpdateCharPosition - if position is SMALLER than ZERO and char already exists on String MUST RETURN the SAME string`() {
        val s = "12345-67"
        val result = s.insertOrUpdateCharPosition(-1, "-")

        Assert.assertEquals(s, result)
    }
}