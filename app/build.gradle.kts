plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.kotlin.serialization)
}

//allprojects{
//    repositories{
////        mavenCentral()
////        maven("https://jitpack.io")
//    }
//}

android {
    namespace = "com.perkedel.htlauncher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.perkedel.htlauncher"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "2025.1.0"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true // https://stackoverflow.com/a/21119027

    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val nav_version = "2.8.3"
    val viewModel_version = "2.6.1"
    val retrofit_version = "2.11.0" // https://github.com/square/retrofit

    implementation(libs.androidx.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.leanback)
    implementation(libs.glide)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.library)
    implementation("me.zhanghai.compose.preference:library:1.1.1")
    implementation(libs.androidx.material.icons.extended) // https://youtu.be/Vj9k0cmBqZE
//    implementation(libs.xicon.pack.z) // https://github.com/DevSrSouza/compose-icons
//    implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.1") // https://github.com/DevSrSouza/compose-icons
//    implementation("br.com.devsrsouza.compose.icons:simple-icons:1.1.1") // https://github.com/DevSrSouza/compose-icons
//    implementation("br.com.devsrsouza.compose.icons:feather:1.1.1") // https://github.com/DevSrSouza/compose-icons
//    implementation("br.com.devsrsouza.compose.icons:tabler-icons:1.1.1") // https://github.com/DevSrSouza/compose-icons
//    implementation("br.com.devsrsouza.compose.icons:tabler-icons:1.1.1") // https://github.com/DevSrSouza/compose-icons
//    implementation("br.com.devsrsouza.compose.icons:eva-icons:1.1.1") // https://github.com/DevSrSouza/compose-icons
//    implementation("br.com.devsrsouza.compose.icons:octicons:1.1.1") // https://github.com/DevSrSouza/compose-icons
//    implementation("br.com.devsrsouza.compose.icons:linea:1.1.1") // https://github.com/DevSrSouza/compose-icons
//    implementation("br.com.devsrsouza.compose.icons:line-awesome:1.1.1") // https://github.com/DevSrSouza/compose-icons
//    implementation("br.com.devsrsouza.compose.icons:erikflowers-weather-icons:1.1.1") // https://github.com/DevSrSouza/compose-icons
//    implementation("br.com.devsrsouza.compose.icons:css-gg:1.1.1") // https://github.com/DevSrSouza/compose-icons
//    implementation(libs.navigation.compose)
//    implementation(libs.kotlinx.serialization.json)

    // https://github.com/google/accompanist
//    implementation(libs.accompanist.drawablepainter)
//    implementation(libs.accompanist.permissions)
//    implementation(libs.accompanist.adaptive)
//    implementation("com.google.accompanist:accompanist-drawablepainter")
//    implementation("com.google.accompanist:accompanist-permissions")
//    implementation("com.google.accompanist:accompanist-adaptive")

    // https://github.com/coil-kt/coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // https://jitpack.io
    implementation(libs.kryptoprefs) // https://github.com/rumboalla/KryptoPrefs
//    implementation(libs.kryptoprefs.gson) // https://github.com/rumboalla/KryptoPrefs

    implementation(libs.kotlinx.serialization.json) // https://github.com/Kotlin/kotlinx.serialization
    //    implementation(libs.androidx.compose.material3)
}