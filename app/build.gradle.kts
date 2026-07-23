plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mytaskflow"
    compileSdk=37

    defaultConfig {
        applicationId = "com.example.mytaskflow"
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.gson)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.4.0")

// CardView
    implementation("androidx.cardview:cardview:1.0.0")

// Material Components (keep only if not already present)
    implementation("com.google.android.material:material:1.13.0")

}