plugins {
    alias(libs.plugins.myrunique.android.library)
    alias(libs.plugins.myrunique.android.room)
}

android {
    namespace = "com.example.core.database"
}

dependencies {
    implementation(libs.org.mongodb.bson)
    implementation(projects.core.domain)
}