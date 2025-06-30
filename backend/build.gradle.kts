plugins {
    kotlin("jvm") version "1.8.10"
    application
}


dependencies {
    implementation("io.ktor:ktor-server-netty:2.1.3")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

application {
    mainClass.set("com.example.backend.ApplicationKt")
}
