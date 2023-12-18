plugins {
    id("org.springframework.boot") version("2.7.2")
    id("io.spring.dependency-management") version("1.0.11.RELEASE")
    id("java")
}

group = "ru.sinitsynme.logistapi"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    val gatewayVersion: String by project
    val eurekaClientVersion: String by project

    implementation("org.springframework.boot:spring-boot-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:$eurekaClientVersion")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway:$gatewayVersion")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}