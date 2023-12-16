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

    implementation("org.springdoc:springdoc-openapi-ui:${openApiVersion}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")


    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}