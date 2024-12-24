plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
    id("org.jetbrains.intellij") version "1.17.4"
}

dependencies {
    implementation("cn.hutool:hutool-all:5.8.21") {
        exclude(group = "org.slf4j")
    }
    implementation("org.dom4j:dom4j:2.1.4") {
        exclude(group = "org.slf4j")
    }
    
    testImplementation("cn.hutool:hutool-all:5.8.21")
    testImplementation("org.dom4j:dom4j:2.1.4")
}

group = "cn.wanda.restful.tool"
version = "1.0.0"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/nexus/content/repositories/central/")
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

intellij {
    updateSinceUntilBuild.set(true)
    sameSinceUntilBuild.set(false)
    version.set("2023.2.2")
    type.set("IC")
    pluginName.set("RestfulTool-wanda")
    plugins.set(listOf("java","properties","yaml","Kotlin"))
}
//intellij {
//    localPath.set("/Applications/IntelliJ IDEA.app/Contents")
//    //    version.set("2023.2.6")
//    //    type.set("IC") // Target IDE Platform
//
//    plugins.set(listOf(
//        "java",
//        "properties",
//        "yaml",
//        "org.jetbrains.kotlin"
//    ))
//}


tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.compilerArgs.addAll(listOf(
            // todo 暂时忽略
//            "-Xlint:deprecation",  // 显示过时API的使用
//            "-Xlint:unchecked"     // 显示未经检查的转换警告
        ))
    }

    patchPluginXml {
        // 最低版本
        sinceBuild.set("200")
        // 最高版本
        untilBuild.set("245.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    buildPlugin {
        dependsOn("jar")
    }

    jar {
        from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}