plugins {
    id("org.springframework.boot") version("3.1.7")
    id("io.spring.dependency-management") version("1.0.11.RELEASE")
    id("java")
}

group = "ru.sinitsynme.logistapi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

configurations {
    runtimeOnly {
        exclude(group = "commons-logging", module = "commons-logging")
    }
}

dependencyManagement {
    val springCloudVersion: String by project

    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

dependencies {
    val mapstructVersion: String by project
    val lombokVersion: String by project
    val openApiVersion: String by project

    implementation(project(":commons"))

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${openApiVersion}")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.mapstruct:mapstruct:${mapstructVersion}")

    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    compileOnly("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.sinitsynme.logistapi.OrderApplication"
    }
}