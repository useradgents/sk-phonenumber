package tech.skot.libraries.phonenumber

import tech.skot.core.SKLog
import tech.skot.core.components.inputs.SKCombo
import tech.skot.core.components.inputs.SKInput
import tech.skot.core.components.inputs.SKInputVC
import tech.skot.core.di.get

class SKPhoneNumber(
    hint: String? = null,
    defaultErrorMessage: String? = null,
    afterValidation: ((SKInput.Validity) -> Unit)? = null,
    private val defaultIndicatif: Indicatif? = Indicatif(33, "FR"),
) {

    private val phoneNumbersHelper: SKPhoneNumbersHelper = get()

    val comboIndicatif = SKCombo<Indicatif>(
        initialChoices = phoneNumbersHelper.getIndicatifsList(),
        label = {
            "${it.region} +${it.code}"
        }
    ).also {
        it.value = defaultIndicatif
    }

    val inputNumber = object : SKInput(
        hint = hint,
        nullable = false,
        defaultErrorMessage = defaultErrorMessage,
        afterValidation = afterValidation,
        viewType = SKInputVC.Type.Phone
    ) {
        override fun format(str: String?): String? {
            return comboIndicatif.value?.code?.let { code ->
                str?.let { nationalNumber ->
                    phoneNumbersHelper.formatNationalNumber(code, nationalNumber).also { SKLog.d("------ $str  --formatted--> $it") }
                }
            } ?: str
        }

        override fun validate(str: String?): Validity {
            return if (phoneNumbersHelper.isValid(comboIndicatif.value?.code, str?.trim())) {
                Validity.Valid
            } else {
                Validity.Error(defaultErrorMessage)
            }
        }
    }



    var number: String?
        get() {
            return comboIndicatif.value?.let { ind ->
                phoneNumbersHelper.formatE164(
                    code = ind.code,
                    nationalNum = inputNumber.value ?: ""
                )
            }
        }
        set(value) {
            value?.let {
                phoneNumbersHelper.parse(it)?.let { (indicatif, nationalNumber) ->
                    comboIndicatif.value = indicatif
                    inputNumber.value = nationalNumber.toString()
                }
            } ?: run {
                comboIndicatif.value = defaultIndicatif
                inputNumber.value = null
            }
        }

    val isValid: Boolean
        get() {
            return inputNumber.isValid
        }
}