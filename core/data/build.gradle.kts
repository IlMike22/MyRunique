plugins {
    alias(libs.plugins.myrunique.android.library)
    alias(libs.plugins.myrunique.jvm.ktor)
}

android {
    namespace = "com.example.core.data"
}

dependencies {
    implementation(libs.timber)
    implementation(projects.core.domain)
    implementation(projects.core.database)
}