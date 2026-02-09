plugins {
    java
    kotlin("jvm") version "2.1.10"
    `maven-publish`
}

group = "br.com.eduard"
version = "1.0.0"

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "br.com.eduard"
            artifactId = "auto_sql"
            version = project.version as String
            from(components["java"])
        }
    }
}

kotlin { jvmToolchain(21)}

repositories {
    mavenCentral()
    mavenLocal()
    google();
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("com.mysql:mysql-connector-j:9.3.0")
    implementation("com.github.EduardMaster:java-utils:1.0.0")
    testImplementation(kotlin("test"))
}

tasks.test {
   useJUnitPlatform() // Garante que o Gradle saiba como rodar os testes
}
