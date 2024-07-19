plugins {
    alias(libs.plugins.myrunique.android.feature.ui)
}

android {
    namespace = "com.example.analytics.persentation"
}

dependencies {
    implementation(projects.analytics.domain)
}