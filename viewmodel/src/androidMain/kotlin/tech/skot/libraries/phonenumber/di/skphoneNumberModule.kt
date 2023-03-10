package tech.skot.libraries.phonenumber.di

import tech.skot.core.di.BaseInjector
import tech.skot.core.di.module
import tech.skot.libraries.phonenumber.SKPhoneNumbersHelper
import tech.skot.libraries.phonenumber.SKPhoneNumbersHelperAndroid

val skphonenumberModule = module<BaseInjector> {
    single<SKPhoneNumbersHelper> { SKPhoneNumbersHelperAndroid(androidApplication) }
}