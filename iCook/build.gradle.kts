plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.0") // Use a versão apropriada
        classpath("com.google.gms:google-services:4.3.15") // Adicione o plugin Google Services
    }
}