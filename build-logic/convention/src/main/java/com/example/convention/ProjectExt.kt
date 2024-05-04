package com.example.convention

import com.android.build.gradle.external.cmake.server.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType

val org.gradle.api.Project.libs
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")