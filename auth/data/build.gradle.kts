plugins {
    alias(libs.plugins.myrunique.android.library)
    alias(libs.plugins.myrunique.jvm.ktor)
}

android {
    namespace = "com.example.auth.data"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.auth.domain)
}