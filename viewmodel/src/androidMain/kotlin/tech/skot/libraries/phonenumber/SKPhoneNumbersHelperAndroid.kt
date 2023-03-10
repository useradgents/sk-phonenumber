package tech.skot.libraries.phonenumber

import android.content.Context
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber.PhoneNumber
import tech.skot.core.SKLog

class SKPhoneNumbersHelperAndroid(applicationContext: Context): SKPhoneNumbersHelper {
    private val util = PhoneNumberUtil.createInstance(applicationContext)

    override fun getIndicatifsList(): List<Indicatif> {
        return util.supportedRegions.sorted().map {
            Indicatif(util.getCountryCodeForRegion(it), it)
        }
    }

    override fun parse(num: String): Pair<Indicatif, Long>? {
        val phoneNumber = util.parse(num, "FR")
        return Pair(
            Indicatif(
                phoneNumber.countryCode,
                util.getRegionCodeForCountryCode(phoneNumber.countryCode)
            ), phoneNumber.nationalNumber
        )
    }

    private fun String.toLongNationalNumber(): Long? = try {
        filter { !it.isWhitespace() }.takeIf { it.isNotEmpty() }?.toLong()
    } catch (ex: Exception) {
        SKLog.e(ex, "numéro national invalide : $this")
        null
    }

    override fun formatNationalNumber(code: Int, nationalNum: String): String {
        return nationalNum.toLongNationalNumber()?.let { nationalNumLong ->
            val phoneNumber = PhoneNumber().apply {
                countryCode = code
                nationalNumber = nationalNumLong
            }
            if (util.isValidNumber(phoneNumber)) {
                util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
            } else {
                nationalNum
            }


        } ?: nationalNum

    }

    private fun numFrom(code: Int, nationalNum: String): PhoneNumber? {
        return nationalNum.toLongNationalNumber()?.let { nationalNumLong ->
            return PhoneNumber().apply {
                countryCode = code
                nationalNumber = nationalNumLong
            }
        }
    }

    override fun formatE164(code: Int, nationalNum: String): String? {
        return numFrom(code, nationalNum)?.let { phoneNumber ->
            if (util.isValidNumber(phoneNumber)) {
                util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
            } else {
                null
            }
        }

    }


    override fun isValid(code: Int?, nationalNum: String?): Boolean {
        return code != null && !nationalNum.isNullOrBlank() && numFrom(
            code,
            nationalNum
        )?.let { util.isValidNumber(it) } == true
    }
}