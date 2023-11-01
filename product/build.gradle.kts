plugins {
    id("org.springframework.boot") version("2.7.2")
    id("io.spring.dependency-management") version("1.0.11.RELEASE")
    id("java")
}

group = "ru.sinitsynme.logistapi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    val mapstructVersion = "1.3.1.Final"

    implementation("org.springdoc:springdoc-openapi-ui:1.6.9")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.mapstruct:mapstruct:${mapstructVersion}")

    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    compileOnly("org.mapstruct:mapstruct-processor:${mapstructVersion}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.sinitsynme.logistapi.ProductApplication"
    }
}