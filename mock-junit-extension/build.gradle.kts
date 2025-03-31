plugins {
    `java-library`
}

group = "org.mock"
version = "1.0.0"

dependencies {
    implementation(project(":mock-core"))
    implementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}