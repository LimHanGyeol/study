# Chapter2. 코틀린 기초
2장에서는 특정 라이브러리에 의존하지 않고 코틀린을 사용하는 방법을 학습한다.  
Chapter2에서 눈여겨 보면 좋다고 판단되는 내용을 정리한다.

## 1. Kotlin Nullable Type
### 문제
변수가 절대 null값을 갖지 못하게 하고싶다.

### 해법
물음표를 사용하지 않고 변수의 타입을 정의한다.  
또한 nullable 타입은 안전 호출 연산자(?.) 혹은 엘비스 연산자(?:)와 결합하여 사용한다.

### 설명
코틀린의 가장 매력적인 기능은 가능한 모든 null값을 제거하는 것이다.  
코틀린에서 변수 타입의 끝에 물음표 없이 변수를 정의하면 코틀린 컴파일러는 해당 변수에 null이 아닌 값을 요구한다.

```kotlin
var name: String
name = "Dolly" // null이 아닌 문자열 할당
// name = null // null을 할당하면 컴파일되지 않는다
```
name 변수를 String 타입으로 선언했다는 것은 name 변수에 null을 할당할 수 없거나,  
name 변수값이 null일 경우 코드가 컴파일되지 않음을 의미한다.  
변수에 null을 할당 가능하게 만드려면 타입 정의에 물음표를 추가하자.
```kotlin
class Person(val first: String,
             val middle: String?,
             val last: String,)

val jkRowling = Person("joanne", null, "Rowling")
val northWest = Person("North", null, "West")
```
위에 예제에서 보이듯이, null값이어도 중간 이름인 middle 파마리터에 값을 제공해야한다.

nullable 변수를 식에서 사용하려고 시도하면 흥미로운 결과를 볼 수 있다.  
코틀린은 개발자가 변수에 null이 아닌 값을 할당했는지 검사하도록 요구하지만 이 작업이 생각처럼 쉽진 않다.
```kotlin
val person = Person(first = "North", middle = null, last = "West")

if (person.middle != null) {
    val middleNameLength = person.middle.length
}
```
if문은 middle 속성이 null이 아닌지 확인하고,  
middle 값이 null이 아니라면 person.middle의 타입을 String? 타입이 아닌 String 타입으로 처리하는 smart cast를 수행한다.  
smart cast를 수행할 수 있는 것은 변수 person이 한번 설정되면 그 값을 바꿀 수 없는 val 키워드로 선언되었기 때문이다.  
반면 아래 예제는 변수 person이 val이 아닌 var로 선언된 경우의 코드를 보여준다.
```kotlin
var person = Person(first = "North", middle = null, last = "West")

if (person.middle != null) {
    // val middleNameLength = person.middle.lengt // 1. person.middle이 복합식(complex expression)이므로 String 타입으로 smart cast가 불가능하다. 
    val middleNameLength = person.middle!!.length // 2. null이 아님을 단언할 수 있다.(assert) 그러나 꼭 필요한 경우가 아니라면 사용하지 않는 것을 권장한다.
}
```
person은 var를 사용하기 때문에 변수 person이 정의된 시점과,  
person의 middle에 접근하는 시점 중간에 값이 변경되었을 수 있다고 가정하여 smart cast를 수행하지 않는다.  
smart cast가 수행되도록 우회하는 한 가지 방법은 not-null assertion operator라고 부르는 거듭 느낌표(!!) 연산자를 사용하는 것이다.  
그러나 **not-null assertion operator(!!)이 하나라도 보인다면 이는 code smell이다.**  
!! 연산자는 변수가 null이 아닌 값으로 다뤄지도록 강제하고 해당 변수가 null이라면 예외를 던진다.  
null값에 !! 연산자를 사용하는 것은 코틀린에서 NPE를 만날 수 있는 몇 가지 상황 중 하나이므로 가능하면 사용하지 않도록 노력하자.

이런 상황에서는 안전 호출 연산자(safe call operator) (?.)을 사용하는 것이 좋다.  
안전 호출 연산자는 쇼트 서킷(short-circuit) 평가 방식이며 아래 예제처럼 값이 null이면 null을 return 한다.
```kotlin
val person = Person(first = "North", middle = null, last = "West")
val middleNameLength = person.middle?.length // safe call. return 타입은 Int?이다.
```

여기서 문제는 결과 추론 타입도 nullable 타입이라는 것이다.  
따라서 middleNameLength의 타입은 아마 사용하고자 했던 타입이 아니라 Int? 타입이므로  
아래 예제처럼 safe call operator과 엘비스 연산자(?:)을 병행해서 사용하는 것이 유용하다.
```kotlin
var person = Person(first = "North", middle = null, last = "West")
val middleNameLength = person.middle?.length ?: 0 // middle이 null일 경우 엘비스 연산자는 0을 리턴한다.
```

**엘비스 연산자는 자신의 왼쪽에 위치한 식의 값을 확인해서 해당 값이 null이 아니면 그 값을 리턴한다.  
만약 엘비스 연산자 왼쪽의 값이 null이면 엘비스 연산자는 자신의 오른쪽에 위치한 값을 돌려준다.**

위의 예제는 person.middle?.length의 값을 확인해서 값이 정수면 해당 정수값을 리하고, null이면 0을 리턴한다.

> 엘비스 연산자의 오른쪽은 식이므로 함수의 인자를 확인할 때 return이나 throw를 사용할 수 있다.

마지막으로 코틀린은 안전 타입 변환 연산자(safe cast operator)인 as?를 제공한다.  
안전 타입 변환 연산자의 목적은 타입 변환이 올바르게 동작하지 않는 경우 ClasssCastException이 발생하는 상황을 방지하는 것이다.  
예를 들어 어떤 인스턴스를 Person 타입으로 변환을 시도했지만 해당 인스턴스가 null일수도 있는 상황이라면 아래처럼 코드를 작성할 수 있다.
```kotlin
val person1 = person as? Person // person1의 타입은 Person? 이다.
```
타입 변환은 성공하여 결과가 Person 타입이 되던가, 실패하여 person1이 null값을 받는다.

## 2. 자바에 null 허용 지시자 추가하기
### 문제
코틀린 코드가 자바 코드와 상호 작용이 필요하고 null 허용성(ability) 어노테이션을 강제하고 싶다.

### 해법
코틀린 코드에 JSR-305 nullability 어노테이션을 강제하려면 컴파일 타임 파라미터 Xjsr305=strict를 사용한다.

### 설명
코틀린의 주요 기능 중 하나는 컴파일 타임에 타입 시스템에 null 허용성을 강제하는 것이다.  
String 타입으로 변수를 정의하면 해당 변수는 절대 null이 될 수 없다.  
반면 String? 타입으로 정의하면 해당 변수는 null이 될 수 있다.
```kotlin
var first: String = "Hello, world!"
var second: String? = null
```

위 코드는 nullability 메커니즘이 없는 자바 코드와 상호작용하기 전까지는 잘 동작한다.  
java는 javax.annotation 패키지에 @Nonnull 어노테이션이 정의되어 있지만 이 스펙은 개발이 중단된 상태이다.  
하지만 많은 라이브러리가 JSR-305 호환 어노테이션을 사용 중이며 코틀린은 해당 호환 라이브러리를 지원한다.  
예를 들면 Spring Framework를 사용할 때 아래처럼 Gradle build 파일에 다음 예제의 코드를 추가함으로써 호환성을 강제할 수 있다.
```groovy
sourceCompatibility = 1.8
compileKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = ["-Xjsr305=strict"]
    }
}

compileTestKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = ["-Xjsr305=strict"]
    }
}
```

동일한 작업을 gradle KotlinDSL로 표현하면 아래와 같다.
```groovy
tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjjsr305=strict")
    }
}
```

JSR-305에 정의된 @Nonnull 어노테이션은 when이라는 속성을 갖는다.  
when 속성 값이 When.ALWAYS인 경우 해당 어노테이션의 타입은 null 비허용 타입으로 다뤄진다.  
when 속성 값이 When.MAYBE 또는 When.NEVER이라면 null 허용 타입으로 다뤄진다.  
when 속성 값이 When.UNKNOWN이라면 해당 타입은 null 허용성을 알 수 없는 플랫폼 타입으로 간주한다.

## 3. 명시적 타입 변환
### 문제
코틀린은 자동으로 기본 타입을 더 넓은 타입으로, 예를 들어 Int를 Long으로 승격하지 않는다.

### 해법
더 작은 타입을 명시적으로 변환하려면 toInt, toLong 등 구체적인 변환 함수를 사용한다.

### 설명
자바와 코틀린의 다른 점 중 하나는 더 짧은 타입이 자동으로 더 긴 타입으로 승격되지 않는다는것이다.  
예를 들어 자바에서의 아래 코드는 정상이다.
```java
int myInt = 3;
long myLong = myInt; // int가 long으로 자동 승격
```
자바 1.5에서 오토박싱이 등장했을 때 기본 타입을 래퍼 타입으로 변환하기는 쉬워졌지만,  
한 래퍼 타입에서 다른 래퍼 타입으로 변환하려면 여전히 추가 코드가 필요하다.
```java
// Integer 타입을 Long 타입으로 변환하기
Integer myInteger = 3;
// Long myWrappedLong = myInteger; // 컴파일되지 않음
Long myWrappedLong = myInteger.longValue(); // long으로 추출한 다음 래퍼 타입으로 감쌈
myWrappedLong = Long.valueOf(myInteger); // 래퍼 타입을 벗겨 int 타입을 얻고 long으로 승격시킨 다음 다시 wrapped
```

다시 설명하면 래퍼 타입을 직접 다루는 것은 언박싱을 개발자 스스로 해야 한다는 의미이다.  
먼저 포장된 값을 추출하는 작업 없이는 간단하게 Integer 인스턴스를 Long에 할당할 수 없다.

코틀린에서는 기본 타입을 직접적으로 제공하지 않는다.  
바이트코드에서는 아마 해당되는 기본 타입을 생성하겠지만 개발자가 코드를 직접 작성할 때는 기본 타입이 아닌 클래스를 다룬다는 것에 명심해야 한다.  
다행히 코틀린은 toInt, toLong 같은 형태의 변환 메서드를 제공한다.
```kotlin
// 코틀린에서 int를 Long으로 승격시키기
val intVar: Int = 3
// val longVar: Long = intVar // 컴파일되지 않음
val longVar: Long = intVar.toLong() // 명시적 타입 변환
```
비록 intVar와 longVar는 코틀린 클래스의 인스턴스지만 Int의 인스턴스를 Long 타입의 변수에 자동으로 할당할 수 없다는 사실이 놀랍지만은 않을 것이다.  
하지만 자바 개발자라면 쉽게 잊어버릴 수 있는 부분이다. 사용 가능한 타입 변환 메서드는 다음과 같다.
* toByte(): Byte
* toChar(): Char
* toShort(): Short
* toInt(): Int
* toLong(): Long
* toFloat(): Float
* toDouble(): Double

다행히도 코틀린은 타입 변환을 투명하게 수행하는 연산자 중복 장점이 있기 때문에 다음 코드는 명시적 타입 변환이 필요하지 않다.
> val longSum = 3L + intVar
더하기 연산자는 자동으로 intVar의 값을 long으로 변환하고 long 리터럴에 그 값을 더한다.

## 4. 숫자를 거듭제곱하기
### 문제
숫자를 거듭제곱하고 싶지만 코틀린에는 미리 정의된 거듭제곱 연산자가 없다.

### 해법
Int와 Long에 정의되어 있는 코틀린 확장 함수 pow에 위임하는 중위(infix) 함수를 정의한다.

### 설명
코틀린에는 자바처럼 내장(built-in) 거듭제곱 연산자가 없다.  
자바에는 최소한 java.lang.Math 클래스에 다음과 같은 메서드 시그니처를 갖는 정적 pow 메서드가 있다.
> public static double Math.pow(double a, double b)

비록 자바는 자동으로 더 짧은 기본 타입을 더 긴 기본 타입으로 승격 시켜주지만 이는 거듭제곱을 수행하는 유일한 함수다.  
그렇지만 코틀린에서는 기본 타입이 없어서 Int같은 클래스 인스턴스가 자동으로 Long 또는 Double로 승격되지 않는다.  
이러한 코틀린 동작 방식으로 인해 코틀린 표준 라이브러리의 Float와 Double에는 확장 함수 pow가 정의돼있지만,  
Int나 Long에 상응하는 pow 함수가 없다는 사실을 알게되면 짜증이 밀려온다.

코틀린 pow 확장 함수의 시그니처는 다음과 같다.
```kotlin
fun Double.pow(x: Double): Double
fun Float.pow(x: Float): Float
```
이것은 정수를 지수로 만드려면 Float 또는 Double로 변환한 후에 pow를 호출하고 마지막으로 원래의 타입으로 되돌려줘야 한다는 의미이다.
```kotlin
// 정수를 지수로 만들기
@Test
fun `raise an Int to a power`() {
    assertThat(256).isEqualTo(2.toDouble().pow(8).toInt())
}
```
위의 코드는 잘 동작하지만 Int와 Long에 다음과 같은 시그니처의 확장 함수를 정의함으로써 처리 과정을 자동화 할 수 있다.

```kotlin
fun Int.pow(x: Int) = toDouble().pow(x).toInt()
fun Long.pow(x: Int) = toDouble().pow(x).toLong()
```
이는 infix 연산자로 정의하는 것이 더 나을 수도 있다.  
미리 정의된 연산자 심볼만 중복할 수 있지만 아래처럼 백틱(`)으로 감싸 가상의 연산자를 만들 수 있다.

```kotlin
// 거듭제곱 계산을 위한 중위 연산자 infix 정의하기
import kotlin.math.pow

infix fun Int.`**`(x: Int) = toDouble().pow(x).toInt()
infix fun Long.`**`(x: Int) = toDouble().pow(x).toLong()
infix fun Float.`**`(x: Int) = pow(x)
infix fun Double.`**`(x: Int) = pow(x)

// Float과 Double에 존재하는 함수와 비슷한 패턴
fun Int.pow(x: Int) = `**`(x)
fun Long.pow(x: Int) = `**`(x)
```
infix 키워드를 ** 함수 정의에 사용했지만 Float과 Double의 pow 함수의 사용 패턴을 지키기 위해  
Int와 Long의 확장 함수에서는 infix 키워드를 사용하지 않았다.  
그 결과 ** 심볼을 아래 예제처럼 중위 거듭제곱 연산자로 사용할 수 있다.
```kotlin
// ** 확장 함수 사용하기
@Test
fun `raise to power`() {
    assertAll (
        { assertThat(1).isEqualTo(2 `**` 0) },
        { assertThat(2).isEqualTo(2 `**` 1) },
        { assertThat(4).isEqualTo(2 `**` 2) },
        { assertThat(8).isEqualTo(2 `**` 3) },

        { assertThat(1L).isEqualTo(2L `**` 0) },
        { assertThat(2L).isEqualTo(2L `**` 1) },
        { assertThat(4L).isEqualTo(2L `**` 2) },
        { assertThat(8L).isEqualTo(2L `**` 3) },

        { assertThat(1F).isEqualTo(2F `**` 0) },
        { assertThat(2F).isEqualTo(2F `**` 1) },
        { assertThat(4F).isEqualTo(2F `**` 2) },
        { assertThat(8F).isEqualTo(2F `**` 3) },

        { assertThat(1.0, closeTo(2.0 `**` 0, 1e-6)) },
        { assertThat(2.0, closeTo(2.0 `**` 1, 1e-6)) },
        { assertThat(4.0, closeTo(2.0 `**` 2, 1e-6)) },
        { assertThat(8.0, closeTo(2.0 `**` 3, 1e-6)) },

        { assertThat(1).isEqualTo(2.pow(0)) },
        { assertThat(2).isEqualTo(2.pow(1)) },
        { assertThat(4).isEqualTo(2.pow(2)) },
        { assertThat(8).isEqualTo(2.pow(3)) },

        { assertThat(1L).isEqualTo(2L.pow(0)) },
        { assertThat(2L).isEqualTo(2L.pow(1)) },
        { assertThat(4L).isEqualTo(2L.pow(2)) },
        { assertThat(8L).isEqualTo(2L.pow(3)) }
    )
}
```

Double.**의 테스트에서는 double에서의 동치 비교를 피하기 위해 Hamcrest matcher의 closeTo를 사용한다.  
Float에 대한 일련의 테스트도 동치 비교를 피해야 할테지만 있는 그대로의 Float 테스트 케이슨느 테스트를 통과한다.

백틱으로 감싼 두 개의 별표 연산자가 맘에 들지 않는다면, exp 같은 실질적인 이름으로 함수를 정의해도 된다.

> 어떤 수를 2제곱하고 싶다면 비트 시프트 연산자 shl과 shr 함수를 사용하는 것이 가장 이상적이다.

## 5. to로 Pair 인스턴스 생성하기
### 문제
보통 맵(map)의 항목으로서 Pair 클래스의 인스턴스를 생성하여 사용할 수 있다.

### 해법
직접 Pair 클래스의 인스턴스를 생성하기보다는 중위(inflix) to 함수를 사용한다.

### 설명
맵은 키와 밸류로 결합된 항목으로 구성된다.  
코틀린은 Pair 인스턴스의 리스트로부터 맵을 생성하는 mapOf와 같은 맵 생성을 위한 최상위 함수를 몇 가지 제공한다.  
코틀린 표준 라이브러리에 있는 mapOf 함수의 시그니처는 다음과 같다.  
> fun <K, V> mapOf(vararg paris: Pair<K, V>): Map<K, V>

Pair는 first와 second라는 이름의 두 개의 원소를 갖는 데이터 클래스이다.  
Pair 클래스의 시그니처는 다음과 같다.  
> data class Pair<out A, out B> : Serializable

Pair 클래스의 first와 second 속성은 A와 B의 제네릭 값에 해당한다.  
2개의 인자를 빧는 생성자를 사용해서 Pair 클래스를 생성할 수 있지만 to 함수를 사용하는 것이 더 일반적이다.  
to 함수는 다음과 같이 정의되어 있다.
> public infix fun<A, B> A.to(that: B): Pair<A, B> = Pair(this, that)

to 함수 구현은 Pair 클래스의 인스턴스를 생성하는 것이다.  
이러한 기능을 하나로 모아 아래 예제에서는 to 함수로 생성된 pair를 사용해서 map을 생성하는 방법을 보여준다.
```kotlin
@Test
fun `create map using infix to function`() {
    val map = mapOf("a" to 1, "b" to 2, "c" to 2)
    
    assertAll(
        { assertThat(map, hasKey("a")) },
        { assertThat(map, hasKey("b")) },
        { assertThat(map, hasKey("c")) },
        { assertThat(map, hasValue(1)) },
        { assertThat(map, hasValue(2)) }
    )
}

@Test
fun `create a Pair from constructor vs to function`() {
    val p1 = Pair("a", 1)
    val p2 = "a" to 1
    
    assertAll(
        { assertThat(p1.first).isEqualTo("a") },
        { assertThat(p1.second).isEqualTo("1") },
        { assertThat(p2.first).isEqualTo("a") },
        { assertThat(p2.second).isEqualTo("1") },
        { assertThat(p1, `is`(equalTo(p2))) }
    )
}
```

to 함수는 제네릭 인자 B와 함께 모든 제네릭 타입 A에 추가된 확장 함수이고,  
이 확장 함수는 A와 B를 위해 제공된 값을 결합한 Pair 인스턴스를 돌려준다.  
to 함수는 더 적고 쉬운 코드를 사용해서 map 리터럴을 생성하는 방법이다.  
하지만 Pair는 데이터 클래스이므로 아래 예제처럼 구조 분해를 통해서만 개별 원소에 접근할 수 있다.

```kotlin
@Test
fun `destructuring a Pair`() {
    val pair = "a" to 1
    val (x,y) = pair
    
    assertThat(x).isEqualTo("a")
    assertThat(y).isEqualTo(1)
}
```

> 3개의 값을 나타내는 Triple이라는 이름의 클래스가 코틀린 표준 라이브러리에 들어있다.
> Pair와는 다르게 Triple 인스턴스를 생성하는 확장 함수는 존재하지 않지만 3개의 인자를 받는 생성자를 직접 사용해서 인스턴스를 생성할 수 있다.

## 그 외
> actual 키워드는 멀티 플랫폼 프로젝트에서 특정 플랫폼에 종속적인 구현을 나타낸다.

> 숫자 리터럴에 언더바(_)를 넣어 가독성을 높일 수 있다.
> 컴파일러는 숫자 리터럴에 있는 언더바는 무시한다.
