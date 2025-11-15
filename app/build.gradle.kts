import java.util.Properties

val properties = Properties();
val localPropertiesFile = project.rootProject.file("local.properties");
if (localPropertiesFile.exists()) {
    properties.load(localPropertiesFile.inputStream());
}

plugins {
//    alias(libs.plugins.android.application)

    id("com.android.application")
    // Add Google services Gradle plugin
    id("com.google.gms.google-services")
}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.4")
    }
}


android {
    namespace = "com.example.qlchitieu"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.qlchitieu"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String","GOOGLE_CLIENT_ID","\"${properties.getProperty("GOOGLE_CLIENT_ID") ?: ""}\"")

        // Lấy key từ local.properties và gán vào BuildConfig
        buildConfigField("String", "GEMINI_API_KEY", properties.getProperty("GEMINI_API_KEY") ?: "")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }

}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.impress)
    implementation(libs.generativeai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4") // Hoặc phiên bản mới hơn

    //BottomNavigation
    implementation("com.github.Foysalofficial:NafisBottomNav:5.0")
    //Login with google
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-firestore")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Thư viện Google AI (Gemini)
//    implementation("com.google.ai.client:generativeai:0.6.0")
    implementation("com.google.guava:guava:33.2.1-android")

}