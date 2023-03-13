# sk-phonenumber

Usage:
ajouter la dépendance à votre viewmodel :

```
kotlin {
    sourceSets {

        val commonMain by getting {
            dependencies {
                api("tech.skot.libraries.sk-phone:viewmodel:0.0.0_1.2.7")
            }
        }

    }
}
```

SKPhoneNumber gère un SKCombo et un SKInput

```
private val phone: SKPhoneNumber = SKPhoneNumber(
        hint = strings.monCompte_Parametres_InfoPerso_NumeroTel,
        defaultErrorMessage = strings.monCompte_Parametres_InfoPerso_NumeroTelErreur,
        afterValidation = { checkRegisterValidity() },
    )

    override val phoneIndicatif: SKCombo = phone.comboIndicatif

    override val phoneNumber: SKInput = phone.inputNumber
```