plugins {
    alias(libs.plugins.myrunique.android.library)
}

android {
    namespace = "com.example.run.location"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.android.gms.play.services.location)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.run.domain)

}