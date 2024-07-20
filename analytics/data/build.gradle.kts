plugins {
    alias(libs.plugins.myrunique.android.library)
    alias(libs.plugins.myrunique.android.room)
}

android {
    namespace = "com.example.analytics.data"
}

dependencies {

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)

    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.analytics.domain)
}