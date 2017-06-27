import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar

plugins {
    java
    eclipse
    maven
}

group = "ch-obermuhlner"
version = "1.0-beta1"
//archivesBaseName = 'big-math'

repositories {
	mavenLocal()
	jcenter()
}

dependencies {
	testCompile("junit:junit:4.12")
}

val sourcesJar = task<Jar>("sourcesJar") {
    val classes by tasks
    dependsOn(classes)
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

val javadocJar = task<Jar>("javadocJar") {
    val javadoc = Javadoc by tasks
    dependsOn(javadoc)
    classifier = "javaDoc"
    from(javadoc.destinationDir)
}

/*

artifacts.add("archives", sourcesJar)
artifacts.add("archives", javadocJar)
*/

/*
uploadArchives {
    repositories {
        mavenDeployer {
            repository(url: mavenLocal().url)
            pom.artifactId = 'big-math'
            pom.project {
                licenses {
                    license {
                        name 'MIT License'
                        url 'https://raw.githubusercontent.com/eobermuhlner/big-math/master/LICENSE.txt'
                        distribution 'repo'
                    }
                }
            }
        }
    }
}
*/
