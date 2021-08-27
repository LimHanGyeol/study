# Chapter1. 코틀린 설치와 실행
Chapter1에서 눈여겨 보면 좋다고 판단되는 내용을 정리한다.

## 1. 명령줄에서 코틀린 코드를 컴파일하고 실행하고 싶을 경우 명령어
```shell
1. kotlinc-jvm
2. kotlin
```
JVM용 코틀린 SDK에는 코틀린 컴파일 명령어 kotlinc-jvm과, 코틀린 실행 명령어 kotlin이 들어있다.  
이 코틀린 명령어는 자바 파일을 위한 자바의 javac와 java 명령어와 유사하다.

## 2. 일반적인 코틀린 파일의 컴파일과 실행
```shell
1. kotlinc-jvm hello.kt
2-1. ls
2-2. hello.kt HelloKt.class
3. kotlin HelloKt
```
컴파일러는 JVM에서 실행될 수 있는 bytecode가 포함된 HelloKt.class 파일을 만든다.  
코틀린은 자바 소스코드를 생성하진 않는다.  
코틀린 컴파일러이지, 트랜스파일러가 아니기 때문이다.  
코틀린 컴파일러는 JVM이 해석할 수 있는 바이트 코드를 생성한다.  

컴파일된 클래스는 파일의 이름을 받아 첫 글자를 대문자로 변경하고, 파일 이름의 끝부분에 Kt를 추가한다.

java 명령어로 실행할 수 있는 모든 것이 포함된 jar파일을 만들고 싶다면 -include-runtime 인자를 추가하자.
아래 명령어는 java 명령어가 실행 가능한 jar파일을 생성할 수 있게 도와준다.
```shell
kotlin-jvm hello.kt -include-runtime -d hello.jar
```
java 명령어로 실행 가능한 출력 파일의 이름은 hello.jar이다.  
-include-runtime 인자를 제외하고 생성한 jar파일을 실행하려면 classpath에 코틀린 런타임이 필요하다.

## 3. Gradle에 코틀린 플러그인 추가하기(Groovy 문법)
Grooby 도메인 특화 언어인 DSL 문법으로 작성된 Gradle 빌드 파일에 코틀린 플러그인을 추가할 수 있다.

Gradle 빌드 도구는 Jetbrain사가 제공하는 플러그인을 사용해서 JVM용 코틀린 컴파일을 지원한다.  
Gradle Build Script에 추가할 코틀린 그레이들 플러그인은 Gradle Plugin Repository에 등록되어 있고,  
아래와 같이 추가할 수 있다.
```groovy
plugins {
    id "org.jetbrains.kotlin.jvm" version "1.5.20"
}
```
플러그인에 명시되는 version 값은 플러그인과 코틀린 버전 모두에 해당한다.  
Groovy 문법을 사용하는 Gradle 빌드 파일은 문법에서 사용되는 작은 따옴표와 큰 따옴표 모두를 지원한다.  
Groovy는 코틀린처럼 문자열 보간에 큰 따옴표를 사용하지만 문자열 보간을 사용하지 않을 경우 작은 따옴표도 잘 동작한다.

gralde.build 파일의 plugins 블록은 mavenCentral을 가져오는 repositories 블록처럼 플러그인을 검색할 장소를 언급할 필요가 없다.  
이는 gradle plugin repository에 등록된 모든 gradle plugin에 해당하는 사항이다.  
또한 plugins 블록을 사용하면 자동으로 플러그인을 적용하기 때문에 apply 문도 사용할 필요가 없다.

settings.gradle 파일 사용을 권장하지만 필수는 아니다.  
gradle build 과정의 초기화 단계에서 gradle이 어떤 프로젝트 빌드 파일을 분석해야 하는지 결정하는 순간에 settings.gradle이 처리된다.  
멀티 프로젝트 빌드에서는 settings.gradle 파일에 루트 프로젝트의 어떤 하위 디렉토리가 그 또한 gradle 프로젝트인지에 대한 정보가 담겨있다.  
gradle은 설정 정보와 의존성을 하위 프로젝트 사이에서 공유할 수 있고,  
하위 프로젝트가 다른 하위 프로젝트를 의존할 수도 있으며,  
심지어 하위 프로젝트를 병렬로 빌드할 수도 있다.

동일한 폴더에서 코틀린 소스와 자바 소스를 혼합해서 관리할 수도 있고,  
src/main/java 폴더와 src/main/kotlin 폴더를 생성해서 개별적으로 관리할 수도 있다.

## 4. Gralde에 코틀린 플러그인 추가하기(Kotlin 문법)
Kotlin DSL을 사용해서 Gradle 빌드 파일에 코틀린 플러그인을 추가할 수 있다.

gradle 5.0이상은 gradle 빕ㄹ드 파일을 설정할 때 사용할 수 있는 새로운 Kotlin DSL이 들어있다.  
5.0 이상 버전의 gradle에서는 gradle plugin repository에 등록되어 있는 kotlin-gradle-plugin 또한 사용 가능하다.
아래 예제처럼 gradle build script에 kotlin gradle plugin을 추가할 수 있다.  
또한 예전 buildscript 문법을 사용할 수도 있다.
```groovy
plugins {
    kotlin("jvm") version "1.5.20"
}

// 예전 문법을 사용해서 코틀린 플러그인 추가하기(Kotlin DSL)
buildscript {
    repositories {
        mavenCentral()
    }
    
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.5.20"))
    }
}

plugins {
    kotlin("jvm")
}
```
최신 문법인 첫번째 문법을 사용하면 plugins 블록은 예전 문법의 repositories 블록처럼 플러그인을 찾아야할 장소를 언급할 필요가 없다.  
이는 gradle plugin repository에 등록된 모든 gradle plugin에 해당하는 사항이다.  
또한 plugins 블록을 사용하면 자동으로 plugin이 적용되기 때문에 apply 문도 사용할 필요가 없다.

> gradle에서 Kotlin DSL을 사용하는 기본 빌드 파일 이름은 settings.gradle.kts와 build.gradle.kts이다.

Groovy 문법과 Kotlin DSL의 가장 큰 차이점은 다음과 같다.
* 모든 문자열에 큰 따옴표를 사용한다.
* Kotlin DSL에서는 괄호 사용이 필수다.
* 코틀린은 콜론(:) 대신 등호 기호(=)를 사용하여 값을 할당한다.

## 5. Gradle을 이용하여 Kotlin 프로젝트 빌드하기
코틀린 코드가 포함된 프로젝트를 Gradle을 사용해서 빌드할 수 있다.

위의 예제에서 Gradle에 Kotlin Plugin을 추가하는 방법을 살펴봤다.  
이번 단락에서는 프로젝트에 있는 모든 코틀린 코드를 처리하는 기능을 빌드 파일에 추가한다.  
Gradle에서 Kotlin 코드를 컴파일하려면 아래와 같이 gradle build file에 있는 dependencies 블록에 항목을 하나 추가해야 한다.
```groovy
plugins {
    'java-library' // 자바 라이브러리 플러그인에서 gradle 작업을 추가
    kotlin("jvm") version "1.5.20" // 코틀린 플러그인을 gralde에 추가
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib")) // 코틀린 표준 라이브러리를 프로젝트 컴파일 타임에 추가
}
```
java-library 플러그인에는 JVM을 기반으로 하는 프로젝트를 위한 build, compileJava, compileTestJava, javadoc, jar와 같은 더 많은 gradle 작업이 정의되어 있다.

> gradle.build 파일에서 plugins 블록은 반드시 먼저 정의되어야 하지만  
> 다른 최상의 블록(repositories, dependencies 등)은 순서에 상관없이 정의할 수 있다.

* dependencies 블록은 코틀린 표준 라이브러리가 컴파일 시간에 추가된다는 것을 나타낸다.  
* repositories 블록은 코틀린 의존성이 공개 아티팩트 빈트레이(artifactory Bintray) 리포지토리인 jcenter에서 다운로드 된다는 사실을 나타낸다.

코틀린 플러그인은 compileKotlin, inspectClassesForKotlinIC, compileTestKotlin 작업을 추가한다.

## 코틀린 의존성
* kotlin-stdlib : 코틀린 표준 라이브러리 의존성
  * gradle처럼 java 1.7, 1.8의 확장 함수를 사용하려면 kotlin-stdlib-jdk7 / kotlin-stdlib-jdk8을 명시해야 한다.
* kotlin-reflect : 리플렉션을 위한 코틀린 라이브러리 의존성
* kotlin-test, kotlin-test-junit : 테스트를 위한 코틀린 라이브러리 의존성

프로젝트에 코틀린과 자바 코드가 둘 다 있다면 코틀린 컴파일러가 먼저 호출되어야 한다.
