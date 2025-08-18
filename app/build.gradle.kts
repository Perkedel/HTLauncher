import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    id("kotlin-parcelize")
}

//allprojects{
//    repositories{
////        mavenCentral()
////        maven("https://jitpack.io")
//    }
//}

// https://youtu.be/vvP5vnmzY84 Philip Lackner Compose Multiplatform
// https://www.youtube.com/playlist?list=PLQkwcJG4YTCS55alEYv3J8CD4BXhqLUuk

android {
    namespace = "com.perkedel.htlauncher"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.perkedel.htlauncher"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "2025.8.0"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    flavorDimensions += listOf("version") // https://developer.android.com/build/build-variants#kts
    buildTypes {
        debug {

        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
    lint {
        baseline = file("lint-baseline.xml")
    }
}

dependencies {
//    implementation(libs.androidx.foundation.desktop)
    //    implementation(libs.androidx.ui.text.google.fonts) // NON-FREE?
    val nav_version = "2.8.3"
    val viewModel_version = "2.6.1"
    val retrofit_version = "2.11.0" // https://github.com/square/retrofit
    val happyFresh_version = "1.5.5"

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
    implementation(libs.androidx.material.icons.extended) // https://youtu.be/Vj9k0cmBqZE https://stackoverflow.com/a/65665564/9079640
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
    implementation(libs.accompanist.systemuicontroller)

    // https://github.com/coil-kt/coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.svg)

    // https://jitpack.io
    implementation(libs.kryptoprefs) // https://github.com/rumboalla/KryptoPrefs
//    implementation(libs.kryptoprefs.gson) // https://github.com/rumboalla/KryptoPrefs

    implementation(libs.kotlinx.serialization.json) // https://github.com/Kotlin/kotlinx.serialization
    //    implementation(libs.androidx.compose.material3)

    api(libs.androidx.datastore)
    api(libs.androidx.datastore.preferences)
    api(libs.androidx.datastore.preferences.core)
    api(libs.ui.tooling.preview) // https://developer.android.com/develop/ui/compose/tooling/previews#multipreview-templates

    implementation(libs.gson) // https://stackoverflow.com/a/77173046/9079640

    implementation(libs.jsontree) // https://github.com/snappdevelopment/JsonTree

    api(libs.guava) //api("com.google.guava:guava:33.3.1-android")

    implementation(libs.androidx.documentfile) // https://stackoverflow.com/a/67630165/9079640

    // https://github.com/philipplackner/ListPaneScaffoldGuide/blob/master/app/build.gradle.kts
    // https://youtu.be/W3R_ETKMj0E Philip Lackner List detail
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.compose.material3.adaptive.navigation)

    // HappyFresh. https://github.com/happyfresh/HappySupportAndroid Yes, that fruit store!
    // StringHelper.getString(context, R.string.welcome)
//    implementation(libs.happysupport)
//    implementation(libs.happysupport.kotlinextentions)
//    implementation(libs.happytracker)
//    implementation(libs.happyrouter)
//    implementation(libs.happyrouter.processor)

    // https://composables.com/tv-foundation/tvlazyverticalgrid
    // https://joebirch.co/android/lazy-grids-for-android-tv-using-jetpack-compose/
    implementation(libs.androidx.tv.foundation)

    // https://github.com/saket/cascade
//    implementation(libs.cascade)
//    implementation(libs.cascade.compose) // INCOMPATIBLE with API < 23

    // https://insert-koin.io/docs/setup/koin
    implementation(libs.koin.androidx.compose)

    // https://github.com/GIGAMOLE/ComposeScrollbars?tab=readme-ov-file#sample-app
//    implementation(libs.scrollbar)
//    implementation(libs.composescrollbars)

    // https://github.com/nanihadesuka/LazyColumnScrollbar
    implementation(libs.lazycolumnscrollbar)

    // https://github.com/BILLyTheLiTTle/LazyColumns
//    implementation(libs.lazycolumns)

    // https://github.com/Calvin-LL/Reorderable
    implementation(libs.reorderable)

    // https://github.com/lvabarajithan/BatteryStatsLibrary
//    implementation(libs.batterystatslibrary)

    // https://developer.android.com/jetpack/androidx/releases/xr-compose
    implementation(libs.androidx.compose)
    testImplementation(libs.androidx.compose.testing)
}

//composeCompiler {
//    reportsDestination = layout.buildDirectory.dir("compose_compiler")
//    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
//}