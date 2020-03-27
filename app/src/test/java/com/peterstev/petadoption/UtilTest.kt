package com.peterstev.petadoption

import com.peterstev.petadoption.utils.isCapital
import com.peterstev.petadoption.utils.validateEmail
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test


class UtilTest {

    private val EMAIL_CORRECT = "peterstevOfficial@gmail.com"
    private val EMAIL_WRONG_1 = "peterstevOfficialgmail.com"
    private val EMAIL_WRONG_2 = "peterstevOfficial@gmailcom"
    private val EMAIL_WRONG_3 = "peterstevOfficial"

    @Test
    fun isEmailValid_correct_true() {
        assertTrue(validateEmail(EMAIL_CORRECT))
    }

    @Test
    fun isEmailValid_missingAt_false() {
        assertFalse(validateEmail(EMAIL_WRONG_1))
    }

    @Test
    fun isEmailValid_missingDot_false() {
        assertFalse(validateEmail(EMAIL_WRONG_2))
    }

    @Test
    fun isEmailValid_wrongText_false() {
        assertFalse(validateEmail(EMAIL_WRONG_3))
    }

    @Test
    fun isEmailValid_EmptySting_false() {
        assertFalse(validateEmail(""))
    }

    @Test
    fun isCapital_EmptySting_false() {
        assertFalse(isCapital(""))
    }

    @Test
    fun isCapital_Correct_true() {
        assertTrue(isCapital(EMAIL_CORRECT))
    }

}
