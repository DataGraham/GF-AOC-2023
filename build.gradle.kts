plugins {
    kotlin("jvm") version "2.1.0"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    implementation("com.google.truth:truth:1.2.0")
    implementation("com.github.jonpeterson:kotlin-range-sets:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.4.0")
}