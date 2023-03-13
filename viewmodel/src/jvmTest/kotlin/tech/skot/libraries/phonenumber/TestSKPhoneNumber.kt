package tech.skot.libraries.phonenumber

import org.junit.Test
import tech.skot.core.SKLog
import tech.skot.core.di.CoreViewInjector
import tech.skot.core.di.CoreViewInjectorMock
import tech.skot.core.di.module
import tech.skot.core.test.SKTestViewModel
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestSKPhoneNumber: SKTestViewModel(
    module {
        single<CoreViewInjector> { CoreViewInjectorMock() }
        single<SKPhoneNumbersHelper> { SKPhoneNumbersHelperJVM() }
    },
) {


    @Test
    fun `La liste des indicatifs est bien chargée`() {
        val skPhoneNumber = SKPhoneNumber()
        assertTrue {
            skPhoneNumber.comboIndicatif.view.choices.size > 200
        }
    }


    @Test
    fun `L'indicatif par défaut est bien pris en compte'`() {
        val skPhoneNumber = SKPhoneNumber()
        assertEquals(
            expected ="FR +33",
            actual = skPhoneNumber.comboIndicatif.view.selected?.text
        )
    }


    @Test
    fun `Set et get d'un numéro valide`() {
        val skPhoneNumber = SKPhoneNumber()
        skPhoneNumber.number = "0298566598"
        assertTrue {
            skPhoneNumber.isValid
        }
        assertEquals(
            expected = "+33298566598",
            actual = skPhoneNumber.number
        )
    }

    @Test
    fun `Set et get d'un numéro invalidevalide`() {
        val skPhoneNumber = SKPhoneNumber()
        skPhoneNumber.number = "0298566598123"
        assertFalse {
            skPhoneNumber.isValid
        }
        assertEquals(
            expected = null,
            actual = skPhoneNumber.number
        )
    }


    @Test
    fun `Un numéro valide est formaté`() {
        val skPhoneNumber = SKPhoneNumber()
        skPhoneNumber.number = "0298566598"
        assertEquals(
            expected = "02 98 56 65 98",
            actual = skPhoneNumber.inputNumber.value
        )
    }

    @Test
    fun `Numéro US`() {
        val skPhoneNumber = SKPhoneNumber()
        skPhoneNumber.number = "+17814853600"
        assertEquals(
            expected = "(781) 485-3600",
            actual = skPhoneNumber.inputNumber.value
        )
        assertEquals(
            expected = "+17814853600",
            actual = skPhoneNumber.number
        )
    }

}