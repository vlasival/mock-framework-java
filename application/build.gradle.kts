plugins {
    id("application")
}

group = "org.example"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":mock-core")) // Подключаем основное ядро
    testImplementation(project(":mock-junit-extension")) // Подключаем JUnit-расширение

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.test {
    useJUnitPlatform()
}
