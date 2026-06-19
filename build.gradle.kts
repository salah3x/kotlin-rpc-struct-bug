plugins {
    kotlin("jvm") version "2.3.20"
    id("org.jetbrains.kotlinx.rpc.plugin") version "0.11.0-grpc-188"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://redirector.kotlinlang.org/maven/kxrpc-grpc")
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:kotlinx-rpc-grpc-core:0.11.0-grpc-188")
    implementation("org.jetbrains.kotlinx:kotlinx-rpc-protobuf-core:0.11.0-grpc-188")
    implementation("org.jetbrains.kotlinx:kotlinx-rpc-grpc-client:0.11.0-grpc-188")
    implementation("org.jetbrains.kotlinx:kotlinx-rpc-grpc-server:0.11.0-grpc-188")
    implementation("io.grpc:grpc-netty:1.81.0")
}

rpc {
    protoc {
        this@rpc.strict
    }
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}