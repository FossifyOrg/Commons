import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.android).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.parcelize).apply(false)
    alias(libs.plugins.library).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
    alias(libs.plugins.detekt).apply(false)
}

tasks.register<Exec>("setVersionToGitCommitHash") {
    val outputStream = ByteArrayOutputStream()
    standardOutput = outputStream
    workingDir = project.rootDir
    commandLine = listOf("git", "rev-parse", "--short=10", "HEAD")
    doLast {
        val commitHash = outputStream.toString().trim()
        project(":commons").version = commitHash
    }
}
tasks.register("publishToMavenLocalWithCommitHash") {
    group = "Publishing"
    description = "Publish project to local maven cache with git commit hash as version"
    dependsOn("setVersionToGitCommitHash")
    dependsOn("commons:publishToMavenLocal")
}
tasks.register<Delete>("clean") {
    delete {
        layout.buildDirectory.asFile
    }
}
