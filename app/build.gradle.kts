plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    buildFeatures {
        buildConfig = true
    }

    namespace = "com.example.mind_care"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mind_care"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.ui.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.glide)
    implementation(libs.worker)
    implementation(libs.guava)
    implementation(libs.room.runtime)
    implementation(libs.generativeai)
    implementation(libs.reactive.streams)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    annotationProcessor(libs.room.compiler)
    implementation (libs.libphonenumber)
    implementation (libs.ccp)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.taptargetview)
    implementation(libs.showcaseview)
    implementation(libs.materiashowcaseview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

secrets {
    // To add your Maps API key to this project:
    // 1. If the secrets.properties file does not exist, create it in the same folder as the local.properties file.
    // 2. Add this line, where YOUR_API_KEY is your API key:
    //        MAPS_API_KEY=YOUR_API_KEY
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"

    // Configure which keys should be ignored by the plugin by providing regular expressions.
    // "sdk.dir" is ignored by default.
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
}
