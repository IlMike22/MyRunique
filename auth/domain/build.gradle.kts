plugins {
    alias(libs.plugins.myrunique.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
}