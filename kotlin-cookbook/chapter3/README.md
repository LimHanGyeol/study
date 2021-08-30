# Chapter3. 코틀린 객체지향 프로그래밍
3장에서는 코틀린 객체지향 프로그래밍을 하며 사용할 수 있는 몇 가지 문법에 대해 알아본다.  
Chapter3에서 눈여겨 보면 좋다고 판단되는 내용을 정리한다.

## 1. const와 val의 차이
### 문제
Runtime보다는 Compile time에 변수가 상수임을 나타내야 한다.

### 해법
컴파일 타임 상수에 const 변경자를 사용한다.  
val 키워드는 변수에 한 번 할당되면 변경이 불가능함을 나타내지만 이러한 할당은 실행 시간에 일어난다.

### 설명
코틀린 키워드 val은 값이 변경 불가능한 불변 변수임을 나타낸다.  
자바에서는 final 키워드가 같은  목적으로 사용된다.  
그렇다면 코틀린에서 const 변경자도 지원하는 이유는 무엇일까?

컴파일 타임 상수는 반드시 객체나 동반 객체(companion object) 선언의 최상위 속성 또는 멤버여야 한다.  
컴파일 타임 상수는 문자열 또는 기본 타입의 래퍼 클래스(Byte, Short, Int, Long ...)이며, 사용자 정의 획득자인 getter를 가질 수 없다.  
컴파일 타임 상수는 컴파일 시점에 값을 사용할 수 있도록 main 함수를 포함한 모든 함수의 바깥쪽에서 할당돼야 한다.  

아래처럼 작업 우선순위를 위해 최소값과 최대값을 정의한다고 가정한다.
```kotlin
// 컴파일 타임에 상수 정의하기
class Task(val name: String, _priority: Int = DEFAULT_PRIORITY) {
    
    companion object {
        const val MIN_PRIORITY = 1
        const val MAX_PRIORITY = 5
        const val DEFAULT_PRIORITY = 3
    }
    
    var priority = validPriority(_priority)
        set(value) {
            field = validPriority(value)
        }
    
    private fun validPriority(priority: Int) = // private 검증 함수
        priority.coerceIn(MIN_PRIORITY, MAX_PRIORITY)
}
```

위 예제에서는 컨벤션에 따라 상수를 모두 대문자로 표기하였다.  
또한 사용자 정의 설정자인 setter를 이용하여 입력한 모든 우선순위를 주어진 범위의 우선순위로 대응시킨다.

코틀린에서 val은 키워드지만 const는 private, inline 등과 같은 변경자임에 유의하자.  
그런이유로 const가 val 키워드를 대체하는 것이 아니라 반드시 같이 쓰여야 한다.

## 2. 사용자 정의 획득자와 설정자 생성하기
### 문제
값을 할당하거나 리턴하는 방법을 사용자 정의하고 싶다.

### 해법
코틀린 클래스의 속성에 get과 set 함수를 추가한다.

### 설명
다른 객체지향 언어처럼 코틀린 클래스는 데이터와 보통 캡슐화로 알려진 해당 데이터를 조작하는 함수로 이뤄진다.  
코틀린은 특이하게도 모든 것이 기본적으로 public이다.  
따라서 정보와 연관된 데이터 구조의 세부 구현이 필요하다고 추정되며 이는 데이터 은닉 원칙을 침해하는 것 처럼 보인다.  
코틀린은 이러한 딜레마를 특이한 방법으로 해결한다.  
코틀린 클래스에서 필드는 직접 선언할 수 없다.  
아래 예제 코드처럼 마치 필드처럼 보이는 속성을 클래스에서 정의하는 것을 보면 코틀린 클래스에서 필드를 직접 선언할 수 없다는 말이 이상하게 느껴질 수 있다.
```kotlin
class Task(val name: String) {
    val priority = 3
    // ...
}
```
Task 클래스는 name과 priority라는 두 가지 속성을 정의한다.  
속성 하나는 주 생성자 안에 선언된 반면 다른 속성은 클래스의 최상위 멤버로 선언되었다.  
이 방식으로 prioirty를 선언할 때의 단점은, apply 블록을 사용해서 priority에 값을 할당할 순 있지만,  
클래스를 인스턴스화 할 때 priority에 값을 할당할 수 없다는 것이다.

```kotlin
var myTask = Task().apply { priority = 4 }
```
이 방식으로 속성을 정의하면 장점은 쉽게 사용자 정의 획득자와 설정자를 추가할 수 있다는 것이다.  
속성을 정의하는 전체 문법은 다음과 같다.
```kotlin
val <propertyName>[: <PropertyType>] [= <property_initializer>]
    [<getter>]
    [<setter>]
```
속성 초기화 블록, 획득자, 설정자는 선택사항이다([]).  
속성 타입이 초기값 또는 획득자의 리턴 타입에서 추론 가능하다면 속성 타입 또한 선택사항이다.  
하지만 생성자에서 선언한 속성에서는 타입 선언이 필수이다.

> 생성자에서 선언한 속성에는 할당된 기본값이 있더라도 반드시 타입 정의가 들어있어야 한다.

아래 예제는 isLowPriority를 계산하는 사용자 정의 획득자를 보여준다.
```kotlin
// 파생 속성을 위한 사용자 정의 획득자
val isLowPriority
    get() = priority < 3
```
앞서 설명한 바와 같이 isLowPriority의 타입은 get 함수의 리턴 타입으로부터 추론되며, 이 경우 Boolean 타입이다.  
사용자 정의 설정자는 속성에 값을 할당할 때마다 사용된다.  
priority 갑싱 반드시 1과 5 사이의 값이 되게 하려면 아래처럼 사용자 정의 획득자를 사용해야 한다.
```kotlin
var priority = 3
    set(value) {
        field = value.coerceIn(1..5)
    }
```
이렇게 앞서 언급한 public 속성, private 필드 딜레마의 해법을 살펴봤다.  
일반적으로 속성에는 지원 필드인 backing field가 필요하지만 코틀린은 자동으로 지원 필드를 생성한다.  
사용자 정의 획득자에서는 field 식별자가 코틀린이 생성한 지원 필드를 참조하는데 사용됐다.  
field  식별자는 오직 사용자 정의 획득자나 설정자에서만 사용할 수 있다.  

속성에서 코틀린이 생성하는 기본 획득자 또는 설정자를 사용하거나 사용자 정의 획득자 또는 설정자에서 field 속성을 통해 지원 필드를 참조하는 경우에는 코틀린이 지원 필드를 생성한다.  
이는 파생 속성 isLowPriority에는 지원 필드가 없다는 뜻이다.

> 다른 문헌에서는 획득자(getter), 설정자(setter)라는 용어가 공식적으로 접근자(accessor), 변경자(mutator)로 불린다.

생성자를 통해 우선순위를 할당하고 싶다고 가정하자.  
이를 수행하는 한 가지 방법은 var또는 val 키워드를 생략함으로써 속성이 아닌 생성자 파라미터를 사용하는 것이다.  
아래 Task의 구현을 살펴보자.
```kotlin
class Task(
    val name: String,
    _priority: Int = DEFAULT_PRIORITY
) {
    companion object {
        const val MIN_PRIORITY = 1
        const val MAX_PRIORITY = 5
        const val DEFAULT_PRIORITY = 3
    }
    
    var priority = validPriority(_priority)
        set(value) {
            field = validPriority(value)
        }
    
    private fun validPriority(p: Int) = p.coerceIn(MIN_PRIORITY, MAX_PRIORITY)
}
```

파라미터 _priority는 속성이 아니라 생성자의 인자일 뿐이다.(매개변수)  
이 인자는 실제 priority 속성을 초기화 하는 데 사용되고 사용자 정의 설정자는 우선순위 값을 원하는 범위로 강제하기 위해  
그 값이 변경될 때마다 실행된다.  
사용자 정의 설정자에서 value는 임의의 명칭임을 유의하자. 이 명칭은 원하는 이름으로 바꿀 수 있다.

## 3. 데이터 클래스 정의하기
### 문제
equals, hashCode, toString 등이 완벽하게 갖춰진 엔티티를 나타내는 클래스를 생성하고 싶다.

### 해법
클래스를 정의할 때 data 키워드를 사용한다.

### 설명
코틀린은 데이터를 담는 특정 클래스의 용도를 나타내기 위해 data 키워드를 제공한다.  
자바에서는 이런 클래스가 데이터베이스 테이블의 정보를 나타내면 엔티티라고 부르는데, 데이터 클래스의 개념도 이와 비슷하다.  
클래스 정의에 data를 추가하면 코틀린 컴파일러는 일관된 equals와 hashCode, 클래스의 속성값을 보여주는 toString, copy와 구조 분해를 위한 component 함수 등 일련의 함수를 자동으로 생성한다.  

```kotlin
data class Product(
    val name: String,
    val price: Double,
    val onSale: Boolean = false
)
```
코틀린 컴파일러는 주 생성자에 선언된 속성을 바탕으로 equals와 hashCode 함수를 생성한다.  
두 함수의 자세한 설명이 궁금하다면 Effective Java를 보도록 하자.  
아래 코드로 자동으로 생성된 함수가 올바르게 동작함을 알 수 있다.
```kotlin
@Test
fun `check equivalence`() {
    val product1 = Product("baseball", 10.0)
    val product2 = Product("baseball", 10.0, false)
    
    assertEquals(product1, product2) // true
    assertEquals(product1.hashCode(), product2.hashCode()) // true
}

@Test
fun `create set to check equals and hashCode`() {
    val product1 = Product("baseball", 10.0)
    val product2 = Product(name = "baseball", price = 10.0, onSale = false)
    
    val products = setOf(product1, product2)
    assertEquals(1, products.size) // true
}
```
setOf 함수에 product1, product2를 추가했지만 실제로는 두 객체는 동등하기 때문에 오직 한개의 product만 추가된다.  
toString 구현은 Product를 다음과 같은 문자열로 변환한다.  
```kotlin
Product(name = baseball, price = 10.0, onSale = false)
```

copy는 원본과 같은 속성값으로 시작해서 copy 함수에 제공된 속성 값만을 변경하여 새로은 객체를 생성하는 인스턴스 메서드이다.  
**copy 함수는 깊은 복사가 아니라 얕은 복수로 수행된다.**

데이터 클래스에는 추가로 속성 값을 리턴하는 함수 component1, component2 등이 있다.
```kotlin
// Product 인스턴스 구조 분해
@Test
fun `destructure using component functions`() {
    val product = Product("baseball", 10.0)
    
    val (name, price, sale) = product // product 구조 분해
    assertAll(
        { assertEquals(product.name, name) },
        { assertThat(product.price, `is`(closeTo(price, 0.01))) },
        { assertFalse(sale) }
    )
}
```
원한다면 equals, hashCode, toString, copy나 모든 _componentN_ 함수를 자유롭게 재정의할 수 있다.

> 코틀린이 생성하는 함수가 포함된 속성을 사용하고 싶지 않다면 주 생성자가 아닌 클래스 몸체에 속성을 추가하자.

데이터 클래스는 기본적으로 데이터가 담긴 클래스를 손쉽게 표현한다.  
코틀린 표준 라이브러리에는 2, 3개의 제네릭 타입 속성을 담는 Pair과 Triple이라는 2개의 데이터 클래스가 들어있다.

## 4. 연산자 중복(Overloading)
### 문제
라이브러리에 정의된 클래스와 더불어 +과 *같은 연산자를 사용할 수 있는 클라이언트를 만들고 싶다.

### 해법
코틀린의 연산자 중복(Overloading) 메커니즘을 사용해서 +과 * 등의 연산자와 연관된 함수를 구현한다.

### 설명
더하기, 빼기, 곱하기 연산자를 비롯해 많은 연산자가 코틀린에서 함수로 구현되어 있다.  
+, -, * 기호를 사용하면 해당 연산자와 연관된 함수에 처리를 위임한다.  
이는 연관 함수를 제공해 클라이언트가 연산자를 사용할 수 있게 한다는 의미이다.  
코틀린 공식 문서의 예제에서는 Point 클래스에 unaryMinus 멤버 함수를 제공하는 방법을 보여준다.

```kotlin
// Point의 unaryMinus 연산자 재정의
data class Point(val x: Int, val y: Int)

operator fun Point.unaryMinus() = Point(-x, -y)

val point = Point(10, 20)

fun main() {
    println(-point) // "Point(x = -10, y = -20)"을 출력 
}
```

> equals를 제외한 모든 연산자 함수를 재정의할 때 operator 키워드는 필수이다.

자신이 작성하지 않은 클래스에 연산자 관련 함수를 추가하고 싶다면 어떻게 해야할까?  
확장 함수를 사용해 자신이 작성하지 않은 클래스에도 연산자와 연관된 함수를 추가할 수 있다.  
예를 들어 자바에서 아파치 커먼스 Math 라이브러리에서 (실수부와 허수부가 하나의 수인) 복소수를 표현하는 Complex 클래스를 생각해보자.  
해당 라이브러리의 javadoc을 살펴보면 add, subtract, multiply 같은 메서드가 Complex 클래스에 들어있다.  
코틀린에서 +, -, * 연산자는 plus, minus, times 함수에 대응된다.  
Complex 클래스에 이와 같은 기존 함수에 연산을 위임하는 확장 함수를 추가하면 아래와 같이 +, -, * 연산자를 대신 사용할 수 있다.  
```kotlin
// Complex의 확장 함수
import org.apache.commons.math3.complex.Complex

operator fun Complex.plus(c: Complex) = this.add(c)
operator fun Complex.plus(d: Double) = this.add(d)
operator fun Complex.minus(c: Complex) = this.subtract(c)
operator fun Complex.minus(d: Double) = this.subtract(d)
operator fun Complex.div(c: Complex) = this.divide(c)
operator fun Complex.div(d: Double) = this.divide(d)
operator fun Complex.times(c: Complex) = this.multiply(c)
operator fun Complex.times(d: Double) = this.multiply(d)
operator fun Complex.times(i: Int) = this.mutiply(i)
operator fun Double.times(c: Complex) = c.multiply(this)
operator fun Complex.unaryMinus() = this.negate()
```

각각의 경우 이 확장 함수는 java 클래스의 기존 메서드에 연산을 위임한다.

## 5. 나중 초기화를 위해 lateinit 사용하기
### 문제
생성자에 속성 초기화를 위한 정보가 충분하지 않으면 해당 속성을 null 비허용 속성으로 만들고 싶다.

### 해법
속성에 lateinit 변경자를 사용한다.

### 설명
> lateinit은 꼭 필요한 경우만 사용하자.
> 의존성 주입의 경우 유용하지만 일반적으로 가능하다면 지연 평가 같은 대안을 먼저 고려하자.

null 비허용으로 선언된 클래스 속성은 생성자에서 초기화되어야 한다.  
하지만 가끔은 속성에 할당할 값의 정보가 충분하지 않은 경우가 있다.  
이것은 모든 객체가 생성될 때까지 의존성 주입이 일어나지 않는 의존성 주입 프레임워크에서 발생하거나,  
유닛 테스트의 설정 메서드안에서 발생한다.  
이러한 경우를 대비하여 속성에 lateinit 변경자를 사용할 수 있다.

예를 들어 Spring Framework는 ApplicationContext로부터 의존성에 값을 할당하기 위해 @Autowired 어노테이션을 사용한다.  
다시 말하면, 값은 인스턴스가 이미 생성된 후에 설정되므로 해당 값을 lateinit으로 표기해야 한다.

```kotlin
// Spring Controller Test
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OfficerControllerTests {
    @Autowired
    private lateinit var client: HttpClient
    
    @Auworied
    private lateinit var repository: OfficerRepository
    
    @Before
    fun setUp() {
        repository.addTestData()
    }
    
    @Test
    fun `GET to route returns all officers in db`() {
        client.get().url("/route")
        // 데이터를 얻고 값 확인
    }
}
```

lateinit 변경자는 클래스 몸체에서만 선언되고, 사용자 정의 획득자(getter)와 설정자(setter)가 없는 var 속성에서만 사용할 수 있다.  
코틀린 1.2부터 최상위 속성과 지역 변수에서도 lateinit을 사용 가능하다.  
lateinit을 사용할 수 있는 속성 타입은 null 할당이 불가능한 타입이어야 하며 기본 타입에는 lateinit을 사용할 수 없다.  
lateinit을 추가하면 해당 변수가 처음 사용되기 전에 초기화 할 수 있다.  
사용 전 초기화에 실패하면 아래와 같이 예외를 던진다.

```kotlin
// lateinit 속성의 동작 방식
class LateInitDemo {
    lateinit var name: String
}

class LateInitDemoTest {
    @Test
    fun `uninitialized lateinit property throws exception`() {
        assertThrows<UninitializedPropertyAccessException> {
            LateInitDemo.name
        }
    }
    
    @Test
    fun `set the lateinit property and no exception is thrown`() {
        assertDoesNotThrow { LateInitDemo().apply { name = "Dolly" } }
    }
}
```

테스트에서 보듯이 초기화되기 이전에 name 속성에 접근하면 UninitializedPropertyAccessException을 던진다.  
아래처럼 클래스 내부에서 속성 레퍼런스의 isInitialized를 사용하면 해당 속성의 초기화 여부를 확인할 수 있다.

```kotlin
// 속성 레퍼런스에 isInitialized 사용하기
class LateInitDemo {
    lateinit var name: String
    
    fun initializedName() {
        println("before assignment: ${::name.isInitialized}")
        name = "World"
        println("after assignment: ${::name.isInitialized}")
    }
}

fun main() {
    LateInitDemo().initializedName()
    // before assignment: false
    // after assignment: true
}
```

> lateinit과 lazy의 차이
> lateinit 변경자는 위에 설명한 제약 사항과 함께 var 속성에 사용된다.  
> lazy 대리자는 속성에 처음 접근할 때 평가되는 람다를 받는다.
> 
> 초기화 비용이 높은데 lazy를 사용한다면 초기화는 반드시 실패한다.  
> 또한 lazy는 val 속성에 사용할 수 있는 반면 lateinit은 var 속성에만 적용할 수 있다.  
> 마지막으로 lateinit 속성은 속성에 접근할 수 있는 모든 곳에서 초기화 할 수 있기 때문에 아프이 예제처럼 객체 바깥쪽에서도 초기화할 수 있다.

## 6. equals 재정의를 위해 안전 타입 변환, 레퍼런스 동등, 엘비스 연산자 사용하기
### 문제
논리적으로 동등한 인스턴스인지 확인하도록 클래스의 equals 메서드를 잘 구현하고 싶다.

### 해법
레퍼런스 동등 연산자(===), 안전 타입 변환 함수(as?), 엘비스 연산자(?:)를 다 같이 사용한다.

### 설명
모든 객체지향 언어에는 객체의 동일(equivalence)과 객체의 동등(equality) 개념이 있다.  
자바에서는 두개의 등호 연산자(==)가 서로 다른 레퍼런스에 같은 객체가 할당됐는지 여부를 확인하는 데 사용된다.  
반면 Object 클래스의 일부인 equals 메서드는 재정의되어 두 객체의 동등 여부를 확인하기 위해 사용된다.  
코틀린에서 == 연산자는 자동으로 equals 함수를 호출한다.  
아래에서 보듯이 open Any 클래스는 equals 함수와 더불어 hashCode, toString을 선언한다.

```kotlin
// Any 클래스에 equals, hashCode, toString 선언
open class Any {
    open operator fun equals(other: Any?): Boolean
    
    open fun hashCode(): Int
    
    open fun toString(): String
}
```
equals 문법에서 equals 구현은 반사성(reflexive), 대칭성(symmetric), 추이성(transitive), 일관성(consistent)이 있어야 하며,  
null도 적절하게 처리할 수 있어야 한다.  
hashCode 문법에서 equals 함수가 두 객체를 동등하다고 판단하면 두 객체의 hashCode도 같아야 한다.  
equals 함수가 재정의되면 hashCode 함수도 재정의돼야 한다.  

그렇다면 어떻게 equals 함수를 잘 구현할 수 있을까?  
equals 구현의 좋은 예는 아래에서 보듯이 코틀린 표준 라이브러리가 제공하는 KotlinVersion 클래스의 equals이다.
```kotlin
// KotlinVersion 클래스의 equals 함수
override fun equals(other: Any?): Boolean {
    if (this === other) return true
    val otherVersion = (other as? KotlinVersion) ?: return false
    return this.version == otherVersion.version
}
```

코틀린의 여러가지 장점을 갖고 있는 간단하고 우아한 equals 구현에 주목하자.  
* 먼저 ===으로 레퍼런스 동등성을 확인한다.
* 이후 인자를 원하는 타입으로 변환하거나 null을 리턴하는 안전 타입 변환 연산자 as?를 사용한다.
* 안전 타입 변환 연산자가 null을 리턴하면 같은 클래스의 인스턴스가 아니므로 동등일 수 없기 때문에 엘비스 연산자 ?:는 false를 리턴한다.
* 마지막으로 equals 함수의 마지막줄은 현재 인스턴스의 version 속성이 == 연산자를 이용해서 other객체의 version 속성과의 동등 여부를 검사해 결과를 리턴한다.

코드 세 줄에서 필요한 모든 경우를 다룬다.  
완벽한 동작을 위한 hashCode의 구현은 다음과 같이 간단하다.
```kotlin
override fun hashCode(): Int = version
```

이 코드는 흥미롭긴 하지만 스스로 equals 함수를 작성하는 방법을 이해하는 것과 직접적인 연관성은 없다.  
예를 들어 문자열 속성을 갖는 간단한 Customer 클래스 name이 있다고 하자.
equals와 hashCode의 일관성 있는 구현은 아래 코드에서 볼 수 있다.
```kotlin
// Customer 클래스의 equals와 hashCode 구현
class Customer(val name: String) {
    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        val otherCustomer = (other as? Custoer) ?: return false
        return this.name == otherCustomer.name
    }
    
    override fun hashCode() = name.hashCode()
}
```

개발자 대신 인텔리제이 IDEA가 만들어주는 equals와 hashCode 구현은 아래와 같다.
```kotlin
// 인텔리제이 IDEA에서 생성된 equals와 hashCode 함수
class Customer(val name: String) {
    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as Customer
        
        if (name != other.name) return false
        return true
    }
    
    override fun hashCode(): Int{
        return name.hashCode()
    }
}
```
직접 구현한 equals 함수와 인텔리제이 IDEA에서 생성된 equals 함수의 차이점은  
인텔리제이 IDEA에서 생성된 equals 함수는 타입 변환 전에 KClass의 javaClass 속성을 검사하고,  
as 연산자를 사용하고, name 속성을 검사하기 위해 smart cast의 결과에 의존한다는 점이다.  
이 구현은 앞서 설명한 equals의 구현 절차와 본질적으로 같지만 약간 더 장황하다.

데이터 클래스에는 자동으로 생성된 자신만의 equals와 hashCode 등의 구현이 있다.  
하지만 이 레시피에서는 이러한 함수를 스스로 구현하는 것도 아주 쉽다는 사실을 알 수 있다.

## 그 외
> 검증을 할 때 부동 소수점 값의 동등성 검사는 오차가 있을 수 있기 때문에 Hamcrest의 closeTo 판정 함수를 사용했다.

> 주소값이 동등한지 확인할 수 있는 수단으로 레퍼런스 동등 연산자 ===를 사용할 수 있다.
