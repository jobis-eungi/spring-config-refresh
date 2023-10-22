plugins {
    java
    id("org.springframework.boot") version "2.7.17"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

allprojects {
    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}


extra["springCloudVersion"] = "2021.0.8"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-config-server")
    implementation("org.springframework.cloud:spring-cloud-starter-config")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
