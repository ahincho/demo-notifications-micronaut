plugins {
    id("io.micronaut.application") version "5.0.2"
    id("com.gradleup.shadow") version "9.4.1"
    id("checkstyle")
}

repositories {
    mavenLocal()
    mavenCentral()
    // GitHub Packages of the Micronaut module (cross-repo dependency).
    // Same rationale as demo-notifications-spring-boot/build.gradle.kts:9-30.
    maven {
        name = "GitHubPackages-NovaNotifications-Micronaut"
        url = uri("https://maven.pkg.github.com/ahincho/nova-java-notifications-micronaut-module")
        val token = System.getenv("NOVA_PACKAGES_READ_TOKEN")
            ?: System.getenv("NOVA_RELEASE_PAT")
            ?: System.getenv("GITHUB_TOKEN")
        if (!token.isNullOrBlank()) {
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: "x-access-token"
                password = token
            }
        }
    }
    maven {
        name = "GitHubPackages-NovaNotifications-Core"
        url = uri("https://maven.pkg.github.com/ahincho/nova-java-notifications")
        val token = System.getenv("NOVA_PACKAGES_READ_TOKEN")
            ?: System.getenv("NOVA_RELEASE_PAT")
            ?: System.getenv("GITHUB_TOKEN")
        if (!token.isNullOrBlank()) {
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: "x-access-token"
                password = token
            }
        }
    }
}

dependencies {
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut.validation:micronaut-validation")

    // The Nova Micronaut module (colloquial, locally published). Exposes
    // NotificationFacade as a @Bean.
    implementation("pe.edu.nova.java.starters:nova-notifications-micronaut-module:1.0.0")

    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.yaml:snakeyaml")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("io.micronaut:micronaut-http-client")
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

checkstyle {
    toolVersion = "10.20.1"
    configFile = file("config/checkstyle/checkstyle.xml")
}