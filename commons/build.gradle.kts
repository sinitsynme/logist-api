plugins {
    id("java")
}

group = "ru.sinitsynme"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    val openApiVersion: String by project
    val lombokVersion: String by project
    val jjwtVersion: String by project
    val springSecurityVersion: String by project
    val jakartaServletApiVersion: String by project
    val springDataVersion: String by project

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${openApiVersion}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.springframework.security:spring-security-core:$springSecurityVersion")
    implementation("org.springframework.security:spring-security-web:$springSecurityVersion")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springDataVersion")

    compileOnly("jakarta.servlet:jakarta.servlet-api:$jakartaServletApiVersion")

    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
}

tasks.test {
    useJUnitPlatform()
}