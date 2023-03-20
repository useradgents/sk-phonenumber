import tech.skot.tools.gradle.androidBaseConfig

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    id("kotlinx-serialization")
    signing
}

android {
    androidBaseConfig(project)
}

kotlin {

    jvm()

    //ios()

    android {
        publishLibraryVariants("release")
    }



    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation("${tech.skot.Versions.group}:viewmodel:${tech.skot.Versions.skot}")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("io.michaelrocks:libphonenumber-android:8.13.5")
            }
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation("androidx.test.ext:junit-ktx:1.1.5")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("com.googlecode.libphonenumber:libphonenumber:8.13.5")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("${tech.skot.Versions.group}:viewmodelTests:${tech.skot.Versions.skot}")
            }
        }
    }
}


tasks.dokkaHtmlPartial.configure {
    suppressInheritedMembers.set(true)
}

tasks.dokkaGfmPartial.configure {
    suppressInheritedMembers.set(true)
}

val dokkaOutputDir = "$buildDir/dokka"

tasks.getByName<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml") {
    outputDirectory.set(file(dokkaOutputDir))
}

val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") {
    delete(dokkaOutputDir)
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaOutputDir)
}


if (!localPublication) {
    val publication = getPublication(project)
    publishing {
        publications.withType<MavenPublication> {
            artifact(javadocJar.get())

            pom {
                name.set(project.name)
                description.set("${project.name} module for sk-phonenumber skot library")
                url.set("https://github.com/skot-framework/sk-phonenumber")
                licenses {
                    license {
                        name.set("Apache 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        id.set("sgueniot")
                        name.set("Sylvain Guéniot")
                        email.set("sylvain.gueniot@gmail.com")
                    }
                    developer {
                        id.set("MathieuScotet")
                        name.set("Mathieu Scotet")
                        email.set("mscotet.lmit@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:github.com/skot-framework/sk-phonenumber.git")
                    developerConnection.set("scm:git:ssh://github.com/skot-framework/sk-phonenumber.git")
                    url.set("https://github.com/skot-framework/sk-phonenumber/tree/master")
                }
            }
        }
    }

    signing {
        useInMemoryPgpKeys(
            publication.signingKeyId,
            publication.signingKey,
            publication.signingPassword
        )
        this.sign(publishing.publications)
    }
}