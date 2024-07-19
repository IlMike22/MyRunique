plugins {
    alias(libs.plugins.myrunique.android.library)
}

android {
    namespace = "com.example.analytics.data"
}

dependencies {

    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.analytics.domain)
}