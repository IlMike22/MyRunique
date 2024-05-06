plugins {
    alias(libs.plugins.myrunique.android.library)
    alias(libs.plugins.myrunique.jvm.ktor)
}

android {
    namespace = "com.example.run.network"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
}