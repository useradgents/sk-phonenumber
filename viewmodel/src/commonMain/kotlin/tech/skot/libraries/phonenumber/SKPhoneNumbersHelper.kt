package tech.skot.libraries.phonenumber

data class Indicatif(val code:Int, val region:String)
enum class PhoneType {
    MOBILE,
    FIXED
}

interface SKPhoneNumbersHelper {
    fun getIndicatifsList():List<Indicatif>
    fun parse(num:String):Pair<Indicatif,Long>?
    fun formatNationalNumber(code:Int, nationalNum:String):String
    fun formatE164(code:Int, nationalNum:String):String?
    fun isValid(code:Int?, nationalNum:String?, phoneType : PhoneType?):Boolean
}
