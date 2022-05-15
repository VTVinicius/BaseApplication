package com.example.base_feature.extensions

import base_feature.utils.extensions.*
import com.example.domain.utils.*
import junit.framework.Assert
import junit.framework.Assert.assertTrue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StringXtKtTest {

    @Test
    fun `data is valid with a valid date`() {
        assertTrue("28/08/1996".isDate())
        assertTrue("28/8/1996".isNotDate())
        assertTrue("08/08/1996".isDate())
        assertTrue("8/08/19".isNotDate())
    }

    @Test
    fun `email is valid with a valid email`() {
        assertTrue("a@a.com".isEmail())
        assertTrue("a@a.c".isEmail())
        assertTrue("teste@a.com.br".isEmail())
    }

    @Test
    fun `email is invalid with a invalid email`() {
        Assert.assertFalse("a@@a.com".isEmail())
        Assert.assertFalse("a@@a.com.".isEmail())
        Assert.assertFalse("a@".isEmail())
    }

    @Test
    fun `email is valid when does not contains special characters`(){
        val validEmail = "validEmailWithOutSpecialCharacters@email.com"
        assertTrue(validEmail.isEmail())
    }

    @Test
    fun `email is invalid when email address contains special characters`(){
        val specialCharList = "áÁàÀâÂäÄãÃåÅæÆçÇéÉèÈêÊëËíÍìÌîÎïÏñÑóÓòÒôÔöÖõÕøØœŒßúÚùÙûÛüÜ"
        val invalidEmail = "invalid${specialCharList}Email@email.com"
        Assert.assertFalse(invalidEmail.isEmail())
    }

    @Test
    fun `email is invalid when email sufix contains special characters`(){
        val specialCharList = "áàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ"
        val invalidEmail = "invalidEmail@$${specialCharList}.com"
        Assert.assertFalse(invalidEmail.isEmail())
    }

    @Test
    fun `ensure that isPassword() verify a valid password`() {
        assertTrue("123456".isPassword())
        assertTrue("12345".isNotPassword())
    }

    @Test
    fun `ensure that initials() returns the initials of words`() {
        Assertions.assertTrue("José Das Couves".initials(1) == "J")
        Assertions.assertTrue("José Das Couves".initials(2) == "JD")
        Assertions.assertTrue("José Das Couves".initials(3) == "JDC")
        Assert.assertFalse("José Das Couves".initials(1) == "j")
        Assert.assertFalse("José Das Couves".initials(2) == "jd")
        Assert.assertFalse("José Das Couves".initials(3) == "jdc")
        Assert.assertFalse("josé das couves".initials(3) == "jdc")
        Assertions.assertTrue("josé das couves".initials(3) == "JDC")
    }

    @Test
    fun `formatAsPhoneNumber test`() {
        Assertions.assertNotEquals("", "".formatAsPhoneNumber())
        Assertions.assertEquals("(79) ", "79".formatAsPhoneNumber())
        Assertions.assertEquals("(79) 9 ", "799".formatAsPhoneNumber())
        Assertions.assertEquals("(79) 9 9866-5078", "79998665078".formatAsPhoneNumber())
    }

    @Test
    fun `insertSafe WHEN has a offset invalid MUST return the same StringBuilder`() {
        val builder = StringBuilder()
        Assertions.assertEquals(builder, builder.insertSafe(-1, "123"))
    }

    @Test
    fun `insertSafe WHEN has a offset valid MUST the StringBuilder with the content string`() {
        val builder = StringBuilder()
        Assertions.assertEquals(builder.insert(0, "123"), builder.insertSafe(0, "123"))
    }

    @Test
    fun `puttingHiddenEmailMask WHEN a email was valid MUST return a mask with the three first digits of email address and the three first digits of email domain`() {
        val expectedEmail = "tes*****@io***.com.br"

        val result = "teste@ioasys.com.br".puttingHiddenEmailMask()

        Assertions.assertEquals(expectedEmail, result)
    }

    @Test
    fun `puttingHiddenEmailMask WHEN a email is invalid MUST return a empty String`() {
        val expectedEmail = ""

        val result = "teste".puttingHiddenEmailMask()

        Assertions.assertEquals(expectedEmail, result)
    }

    @Test
    fun `puttingHiddenPhoneMask WHEN a phone is valid MUST return a masked phone`() {
        val expectedPhone = "(81) 9 9*******57"

        val result = "(81) 9 9981-4757".puttingHiddenPhoneMask()

        Assertions.assertEquals(expectedPhone, result)
    }

    @Test
    fun `puttingHiddenPhoneMask WHEN a phone is valid MUST return a empty String`() {
        val expectedPhone = ""

        val result = "12345678".puttingHiddenPhoneMask()

        Assertions.assertEquals(expectedPhone, result)
    }

    @Test
    fun `isEmailHiddenMasked WHEN email hidden mask has a valid international MUST return true`() {
        val emailHiddenMask = "tes*****@io***.com"

        assertTrue(emailHiddenMask.isEmailHiddenMasked())
    }

    @Test
    fun `isEmailHiddenMasked WHEN email hidden mask has a valid local domain MUST return true`() {
        val emailHiddenMask = "tes*****@io***.com.br"

        assertTrue(emailHiddenMask.isEmailHiddenMasked())
    }

    @Test
    fun `isEmailHiddenMasked WHEN email hidden mask is invalid MUST return false`() {
        val emailHiddenMask = "tes********io********"

        Assert.assertFalse(emailHiddenMask.isEmailHiddenMasked())
    }

    @Test
    fun `isEmailHiddenMasked WHEN email has no mask MUST return false`() {
        val emailHiddenMask = "teste@ioasys.com"

        Assert.assertFalse(emailHiddenMask.isEmailHiddenMasked())
    }

    @Test
    fun `isEmailHiddenMasked WHEN email hidden mask has at LEAST one char in address prefix return true`() {
        val emailHiddenMask = "a*****@io***.com.br"
        assertTrue(emailHiddenMask.isEmailHiddenMasked())
    }

    @Test
    fun `isEmailHiddenMasked WHEN email hidden mask has at LEAST one char in domain prefix return true`() {
        val emailHiddenMask = "te*****@i***.com.br"
        assertTrue(emailHiddenMask.isEmailHiddenMasked())
    }

    @Test
    fun `isEmailHiddenMasked WHEN email has empty address prefix and address domain  return false`() {
        val emailHiddenMask = "*****@***.com.br"
        Assert.assertFalse(emailHiddenMask.isEmailHiddenMasked())
    }

    @Test
    fun `isPhoneHiddenMasked WHEN phone has valid hidden mask MUST return true`() {
        val phoneHiddenMask = "(81) 9 7*******90"

        assertTrue(phoneHiddenMask.isPhoneHiddenMasked())
    }

    @Test
    fun `isPhoneHiddenMasked WHEN phone has invalid hidden mask MUST return false`() {
        val phoneHiddenMask = "(81) 97*******90"

        Assert.assertFalse(phoneHiddenMask.isPhoneHiddenMasked())
    }

    @Test
    fun `isPhoneHiddenMasked WHEN phone has no hidden mask MUST return false`() {
        val phoneHiddenMask = "(81) 9 7965-3490"

        Assert.assertFalse(phoneHiddenMask.isPhoneHiddenMasked())
    }



}