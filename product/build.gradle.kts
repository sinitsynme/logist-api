plugins {
    id("org.springframework.boot") version("3.2.1")
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
    val eurekaClientVersion: String by project
    val awsSdkVersion: String by project
    val jaxbApiVersion: String by project

    implementation(project(":commons"))

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${openApiVersion}")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:$eurekaClientVersion")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql:${postgresqlVersion}")
    implementation("org.flywaydb:flyway-core:${flywayVersion}")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:${flywayVersion}")

    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    implementation("org.mapstruct:mapstruct:${mapstructVersion}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    compileOnly("org.mapstruct:mapstruct-processor:${mapstructVersion}")
    implementation("org.projectlombok:lombok-mapstruct-binding:${lombokMapstructBindingVersion}")

    implementation("com.amazonaws:aws-java-sdk-s3:$awsSdkVersion")
    implementation("javax.xml.bind:jaxb-api:$jaxbApiVersion")

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