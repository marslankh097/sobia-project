plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

//    google services
}
android {
    namespace = "com.demo.budgetly"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.demo.budgetly" //sir app ka name firebase par "com.demo.budgetly" rajkha ha //firbase crasylytics library lrakhni ha? yes
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose  = true
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    buildTypes {
        debug {
            resValue("string", "ad_app_id", "ca-app-pub-3940256099942544~3347511713")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            resValue("string", "ad_app_id", "")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    coreLibraryDesugaring (libs.desugar.jdk.libs)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.material3.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    //vico graph
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)
    implementation(libs.vico.views)

    //coil image
    implementation(libs.coil.compose)
//    implementation(libs.accompanist.glide)

    //pager
    implementation(libs.accompanist.pager)

    //lottie
    implementation(libs.lottie.compose)

    implementation (libs.accompanist.systemuicontroller)


    // Jetpack Compose
    implementation(libs.compose.ui)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.compiler)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    implementation(libs.sdp.compose)

    //ads
//    implementation(libs.ads.sdk)
    // Ads
//    api (libs.play.services.ads)
//    api (libs.user.messaging.platform)

    implementation(libs.ads.compose)
    implementation(libs.remote.config)
    implementation(libs.mon.consent)
    implementation(libs.app.update)

    //gif
    implementation(libs.coil.gif)

    //preference
    implementation(libs.androidx.datastore.preferences)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.room.compiler)


    /** Firebase **/
    implementation(libs.firebase.perf)
//    implementation(libs.firebase.config)
    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    implementation("androidx.compose.material:material-icons-extended")
}