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
}

tasks.test {
    useJUnitPlatform()
}