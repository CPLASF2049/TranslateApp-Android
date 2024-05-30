pluginManagement {
    repositories {
        maven ("https://maven.aliyun.com/repository/public/" )
        maven ("https://maven.aliyun.com/repository/google/")
        maven ("https://maven.aliyun.com/repository/jcenter/")
        maven ("https://maven.aliyun.com/repository/central/")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven ("https://maven.aliyun.com/repository/public/")
        maven ("https://maven.aliyun.com/repository/google/")
        maven ("https://maven.aliyun.com/repository/jcenter/")
        maven ("https://maven.aliyun.com/repository/central/")
        google()
        mavenCentral()
    }
}

rootProject.name = "translationapp"
include(":app")
include(":tesseract4android")

 