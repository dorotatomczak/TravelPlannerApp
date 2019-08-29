import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.1.6.RELEASE"
	id("io.spring.dependency-management") version "1.0.7.RELEASE"
	kotlin("jvm") version "1.2.71"
	kotlin("plugin.spring") version "1.2.71"
}

group = "com.github.travelplannerapp"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation ("com.google.code.gson:gson:2.8.0")
	implementation("org.locationtech.jts:jts-core:1.15.0")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation ("postgresql:postgresql:9.1-901-1.jdbc4")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.mockk:mockk:1.9.3.kotlin12")

	// authentication and security
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("org.apache.commons:commons-lang3:3.0")
	implementation("javax.xml.bind:jaxb-api:2.2.12")

	implementation("com.squareup.retrofit2:converter-gson:2.6.0")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	implementation ("org.tinylog:tinylog-api-kotlin:2.0.0")
	implementation ("org.tinylog:tinylog-impl:2.0.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

