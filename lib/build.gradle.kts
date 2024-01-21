import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
}

kotlin {
    val hostOs = System.getProperty("os.name")

    androidTarget {
        publishLibraryVariants("release")
    }

    jvm("desktop")

    if (hostOs == "Mac OS X") {
        macosArm64()
        macosX64()
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64(),
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = rootProject.name
                isStatic = true
            }
        }
    } else {
        linuxArm64()
        linuxX64()
        mingwX64()
    }

    targets.filterIsInstance<KotlinNativeTarget>().forEach {
        it.apply {
            binaries {
                sharedLib {
                    baseName = rootProject.name
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.kotlincrypto:secure-random:0.2.0")
                implementation("com.ionspin.kotlin:bignum:0.3.8")
                implementation(project.dependencies.platform("org.kotlincrypto.hash:bom:0.4.0"))
                implementation("org.kotlincrypto.hash:sha2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.myapplication.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

mavenPublishing {
//    publishToMavenCentral(SonatypeHost.DEFAULT)
    // or when publishing to https://s01.oss.sonatype.org
    publishToMavenCentral(SonatypeHost.S01, automaticRelease = true)
    signAllPublications()
    coordinates("com.dshatz.kmp", "ecdsa", "1.0.0")

    pom {
        name.set(project.name)
        description.set("A simple, lightweight, fast elliptical curve cryptography library in pure kotlin.")
        inceptionYear.set("2024")
        url.set("https://github.com/dshatz/ecdsa-kotlin-mpp/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("dshatz")
                name.set("Daniels Šatcs")
                url.set("https://github.com/dshatz/")
            }
        }
        scm {
            url.set("https://github.com/dshatz/ecdsa-kotlin-mpp/")
            connection.set("scm:git:git://github.com/dshatz/ecdsa-kotlin-mpp.git")
            developerConnection.set("scm:git:ssh://git@github.com/dshatz/ecdsa-kotlin-mpp.git")
        }
    }
}
