package tech.skot.libraries.phonenumber

import org.junit.Test
import kotlin.test.assertTrue

class TestPhoneNumberHelper {

    private fun helper() = SKPhoneNumbersHelperJVM()

    @Test
    fun liste_des_indicatifs() {
        val phoneNumbersHelper = helper()
        println(phoneNumbersHelper.getIndicatifsList())
    }

    @Test
    fun deconcatenate_fr_int() {
        val phoneNumbersHelper = helper()
        println(phoneNumbersHelper.parse("+33298566598"))
    }

    @Test
    fun deconcatenate_fr_not_int() {
        val phoneNumbersHelper = helper()
        println(phoneNumbersHelper.parse("0298566598"))
        println(phoneNumbersHelper.parse("+33298566598"))
        println(phoneNumbersHelper.parse("33298566598"))
        println(phoneNumbersHelper.parse("3329856659834534654"))
    }

    @Test
    fun formatNationalNumber() {
        val phoneNumbersHelper = helper()
        println(phoneNumbersHelper.formatNationalNumber(33,"298566598"))

        println(phoneNumbersHelper.formatNationalNumber(33,"0298566598"))

        println(phoneNumbersHelper.formatNationalNumber(33,"02985669"))

        println(phoneNumbersHelper.formatNationalNumber(33,"029856"))

        println(phoneNumbersHelper.formatNationalNumber(33,"029856659899"))
    }


    @Test
    fun formatE164() {
        val phoneNumbersHelper = helper()

        println(phoneNumbersHelper.formatE164(33,"298566598"))
        println(phoneNumbersHelper.formatE164(33,"2985665"))
        println(phoneNumbersHelper.formatE164(33,"0298566598"))
        println(phoneNumbersHelper.formatE164(33,"29856659899"))
    }

    @Test
    fun testValidity() {
        val phoneNumbersHelper = helper()
        assertTrue {
            phoneNumbersHelper.isValid(33,"298566598")
        }
        assertTrue {
            phoneNumbersHelper.isValid(33,"0298566598")
        }

        assertTrue {
            phoneNumbersHelper.isValid(33,"2985665")
        }

    }
}