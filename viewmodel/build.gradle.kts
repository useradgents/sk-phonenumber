import tech.skot.tools.gradle.androidBaseConfig

plugins {
    id("com.android.library")
    id("maven-publish")
    signing
}

android {
    namespace = "tech.skot.libraries.phonenumber.viewmodel"
    androidBaseConfig(project)

    sourceSets {
        getByName("main") {
            kotlin {
                directories.add("src/commonMain/kotlin")
                directories.add("src/androidMain/kotlin")
            }
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }

    publishing {
        singleVariant("release") { withSourcesJar() }
    }
}

dependencies {
    implementation("${tech.skot.Versions.group}:viewmodel:${tech.skot.Versions.skot}")
    implementation("io.michaelrocks:libphonenumber-android:9.0.32")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.3.0")
}

val dokkaOutputDir = layout.buildDirectory.dir("dokkaHtml")

dokka {
    moduleName.set("sk-phonenumber")
    dokkaPublications.html {
        outputDirectory.set(dokkaOutputDir)
    }
    dokkaSourceSets.configureEach {
        suppressInheritedMembers.set(true)
    }
}

val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") {
    delete(dokkaOutputDir)
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaOutputDir)
}

// Before 2.0.0, Kotlin Multiplatform created the publication: it is now declared here.
afterEvaluate {
    publishing {
        publications.create<MavenPublication>("release") {
            from(components["release"])
        }
    }
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