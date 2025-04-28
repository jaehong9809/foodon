plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.swallaby.foodon"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.swallaby.foodon"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = "24a64adccf1bc7b63a4cd6c0ba58dc9f"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"${project.findProperty("BASE_URL")}\"")
            buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"24a64adccf1bc7b63a4cd6c0ba58dc9f\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "BASE_URL", "\"${project.findProperty("BASE_URL")}\"")
            buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"24a64adccf1bc7b63a4cd6c0ba58dc9f\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ---------- 의존성 추가 -----------
    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // okhttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // gson
    implementation(libs.gson)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // androidx library
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.androidx.foundation.layout.android)

    // javapoet
    //implementation("com.squareup:javapoet:1.13.0")

    // navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // coil
    implementation(libs.coil)
    implementation(libs.coil.compose)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    implementation(libs.threetenabp)

    // dataStore
    implementation(libs.datastore.preferences)

    // kakao
    implementation(libs.kakao.user)

}
