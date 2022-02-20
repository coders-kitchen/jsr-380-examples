import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "com.coderskitchen"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.hibernate.validator:hibernate-validator:7.0.2.Final")
    implementation("org.glassfish:jakarta.el:4.0.1")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("com.coderskitchen.jsr380examples.ApplicationKt")
}