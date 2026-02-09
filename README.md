# Auto SQL ORM

### How to Install

#### Gradle

```kts
repositories {
   maven("https://jitpack.io")
}

dependencies {
    api("com.github.EduardMaster:auto-sql-orm:main-SNAPSHOT") // last-version
    api("com.github.EduardMaster:auto-sql-orm:1.0.0") // v1.0-version
}
```
#### Maven

```xml
<repositories>
    <repository>
        <id>Jitpack</id>
        <url>https://jitpack.com/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.EduardMaster</groupId>
        <artifactId>auto-sql-orm</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```