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
    val postgresqlVersion: String by project
    val lombokVersion: String by project
    val flywayVersion: String by project
    val jjwtVersion: String by project
    val openApiVersion: String by project
    val commonsCodecVersion: String by project

    implementation(project(":commons"))

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${openApiVersion}")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    implementation("org.postgresql:postgresql:${postgresqlVersion}")
    implementation("org.flywaydb:flyway-core:${flywayVersion}")

    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    implementation("commons-codec:commons-codec:$commonsCodecVersion")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}