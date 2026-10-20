plugins {
    id("tech.skot.library-viewmodeltests")
    signing
}

android {
    namespace = "tech.skot.libraries.phonenumber.viewmodeltests"
}

dependencies {
    implementation("com.googlecode.libphonenumber:libphonenumber:9.0.33")
    testImplementation("${tech.skot.Versions.group}:viewmodel:${tech.skot.Versions.skot}")
}
