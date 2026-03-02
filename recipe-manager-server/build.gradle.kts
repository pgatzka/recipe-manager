plugins {
    id("java")
    id("io.freefair.lombok") version "9.2.0"
    id("org.flywaydb.flyway") version "11.14.1";
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jooq.jooq-codegen-gradle") version "3.19.30"
}

val databaseUrl = providers.gradleProperty("database.url").get()
val databaseUsername = providers.gradleProperty("database.username").get()
val databasePassword = providers.gradleProperty("database.password").get()

tasks {
    compileJava {
        dependsOn(jooqCodegen)
    }
    jooqCodegen {
        dependsOn(flywayMigrate)
    }
    withType<Test> {
        useJUnitPlatform()
    }
}

flyway {
    url = databaseUrl
    user = databaseUsername
    password = databasePassword
}

jooq {
    configuration {
        jdbc {
            url = databaseUrl
            username = databaseUsername
            password = databasePassword
        }
        generator {
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                excludes = "flyway_schema_history"
                inputSchema = "public"
                schemaVersionProvider = "select max(version) from flyway_schema_history"
            }
            strategy {
                matchers {
                    tables {
                        table {
                            tableClass {
                                transform = org.jooq.meta.jaxb.MatcherTransformType.PASCAL
                                expression = "$0_TABLE"
                            }
                        }
                    }
                }
            }
            target {
                packageName = "io.github.pgatzka.jooq"
            }
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.flywaydb:flyway-database-postgresql")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
    testImplementation("org.springframework.boot:spring-boot-starter-jooq-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(platform("io.jsonwebtoken:jjwt-bom:0.13.0"))
    implementation("io.jsonwebtoken:jjwt-api")
    implementation("io.jsonwebtoken:jjwt-impl")
    implementation("io.jsonwebtoken:jjwt-jackson")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.7.10")
        classpath("org.flywaydb:flyway-database-postgresql:11.14.1")
    }
}