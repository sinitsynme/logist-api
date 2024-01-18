plugins {
    id("org.springframework.boot") version("3.1.7")
    id("io.spring.dependency-management") version("1.0.11.RELEASE")
    id("java")
}

group = "ru.sinitsynme.logistapi"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencyManagement {
    val springCloudVersion: String by project

    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

dependencies {
    val gatewayVersion: String by project
    val eurekaClientVersion: String by project

    implementation(project(":commons"))

    implementation("org.springframework.boot:spring-boot-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}