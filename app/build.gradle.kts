plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id ("kotlin-kapt")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.dicoding.challange4"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dicoding.challange4"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Architectural Components
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // Room
    implementation ("androidx.room:room-runtime:2.6.0")
    kapt ("androidx.room:room-compiler:2.6.0")

    // Kotlin Extensions and Coroutines support for Room
    implementation ("androidx.room:room-ktx:2.6.0")

    // Navigation Components
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.4")
    implementation ("androidx.navigation:navigation-dynamic-features-fragment:2.7.4")

    // Espresso
    val androidx_test_espresso = "3.5.1"
    androidTestImplementation ("androidx.test.espresso:espresso-core:$androidx_test_espresso")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:$androidx_test_espresso")

    // Mockk.io
    val mockk_version = "1.9.3"
    androidTestImplementation ("io.mockk:mockk-android:$mockk_version")

    // androidx.test
    val androidx_test = "1.5.2"
    androidTestImplementation ("androidx.test:runner:$androidx_test")
    androidTestImplementation ("androidx.test:core:1.5.0")
    androidTestImplementation ("androidx.test.ext:junit-ktx:$androidx_test")

    // androidx.fragment
    debugImplementation ("androidx.fragment:fragment-testing:1.6.1")
    implementation ("androidx.fragment:fragment-ktx:1.6.1")
}