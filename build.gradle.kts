plugins {
    id("io.micronaut.application") version "5.0.2"
    id("com.gradleup.shadow") version "9.4.1"
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut.validation:micronaut-validation")

    // The Nova Micronaut module (colloquial, locally published). Exposes
    // NotificationFacade as a @Bean.
    implementation("pe.edu.nova.java.starters:nova-notifications-micronaut-module:1.0.0")

    runtimeOnly("ch.qos.logback:logback-classic")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "pe.edu.nova.demo.notifications.micronaut.DemoApplication"
}

group = "pe.edu.nova"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("pe.edu.nova.demo.*")
    }
}

tasks.named<JavaCompile>("compileJava") {
    options.compilerArgs.add("-parameters")
}

graalvmNative.toolchainDetection = false
