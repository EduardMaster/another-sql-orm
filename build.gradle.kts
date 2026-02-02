plugins {
    java
    kotlin("jvm") version "2.3.0"
    `maven-publish`
}

group = "br.com.eduard"
version = "1.0"

java.sourceCompatibility = JavaVersion.VERSION_HIGHER
tasks {
    compileJava{
        // options.encoding = "UTF-8"
    }
    // compileKotlin {    kotlinOptions.jvmTarget = "1.8"}
}

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

repositories {
    mavenCentral()
    mavenLocal()
    google();
}

dependencies {
    compileOnly(kotlin("stdlib"))
    implementation("com.google.code.gson:gson:2.9.1")
}
