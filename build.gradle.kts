plugins {
    java
    kotlin("jvm") version "1.9.22"
    `maven-publish`
}

group = "org.eduard.another"
version = "1.0"

java.sourceCompatibility = JavaVersion.VERSION_1_8
tasks {
    compileJava{
        options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "org.eduard.another"
            artifactId = "sql_orm"
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
