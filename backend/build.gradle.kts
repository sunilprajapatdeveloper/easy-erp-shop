plugins {
    java
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
    kotlin("plugin.jpa") version "1.9.10"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.9.10"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "nextpos.app"
version = "0.0.1-SNAPSHOT"

noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    mavenCentral()
    maven("https://maven.google.com")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.data:spring-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.h2database:h2")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    // ZXing
    implementation("com.google.zxing:core:3.5.2")
    implementation("com.google.zxing:javase:3.5.2")

    // Redis & Kafka
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.kafka:spring-kafka")

    implementation("org.springframework.boot:spring-boot-starter-mail")

    // Razorpay
    implementation("com.razorpay:razorpay-java:1.4.5")
    implementation("org.json:json:20230618")

    implementation("commons-io:commons-io:2.15.1")
    implementation("org.apache.tika:tika-core:2.9.1")
    implementation("net.coobird:thumbnailator:0.4.20")

    // Cloud storages
    implementation("com.azure:azure-storage-blob:12.25.0")
    implementation(platform("com.google.cloud:libraries-bom:26.37.0"))
    implementation("com.google.cloud:google-cloud-storage")

    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // Caffeine cache
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // AWS SDK
    implementation("software.amazon.awssdk:s3:2.21.0")
    implementation("software.amazon.awssdk:auth:2.21.0")

    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("com.google.code.gson:gson:2.10.1")

    // Spring Boot Actuator for health checks
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // SpringDoc OpenAPI for Swagger/OpenAPI documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    
    // Jakarta Persistence API (required for Spring Boot 3+)
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    
    // Jackson for JSON serialization in Kafka
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Retry support
    implementation("org.springframework.retry:spring-retry:2.0.5")
    implementation("org.springframework:spring-aspects:6.1.5")

    // WebFlux for reactive WebClient (used by AI providers)
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Mustache for prompt templating
    implementation("com.github.spullara.mustache.java:compiler:0.9.10") // or implementation("com.github.mustachejava:mustache-java:0.9.10")

    // OpenTelemetry for distributed tracing (optional but recommended)
    implementation("io.opentelemetry:opentelemetry-api:1.38.0")
    implementation("io.opentelemetry:opentelemetry-sdk:1.38.0")

    // Import/Export System Dependencies
    // Spring Batch for processing large datasets
    implementation("org.springframework.boot:spring-boot-starter-batch")

    // Apache POI for Excel (XLSX) support
    implementation("org.apache.poi:poi-ooxml:5.2.5")

    // OpenCSV for CSV handling (optional, but recommended)
    implementation("com.opencsv:opencsv:5.8")

    // Apache PDFBox for PDF export
    implementation("org.apache.pdfbox:pdfbox:2.0.30")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "20"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}