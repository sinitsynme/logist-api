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
    val openApiVersion: String by project
    val mapstructVersion: String by project
    val postgresqlVersion: String by project
    val lombokVersion: String by project
    val flywayVersion: String by project
    val lombokMapstructBindingVersion: String by project

    implementation("org.springdoc:springdoc-openapi-ui:${openApiVersion}")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql:${postgresqlVersion}")
    implementation("org.flywaydb:flyway-core:${flywayVersion}")

    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    compileOnly("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    implementation("org.projectlombok:lombok-mapstruct-binding:${lombokMapstructBindingVersion}")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.sinitsynme.logistapi.WarehouseApplication"
    }
}