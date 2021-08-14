# Kotlin Basic Syntax
## Package definition and imports
패키지 사양은 소스파일의 맨 위에 정의되어야 한다.

```kotlin
packge my.demo

import kotlin.text.*

// something..
```
디렉토리와 패키지를 일치시킬 필요는 없다.  
소스 파일은 파일 시스템에 임의로 배치될 수 있다.

## Program entry point
main function을 이용하여 Kotlin Application의 Entry Point를 사용할 수 있다.  
main function은 2가지 형태로 이루어지며 다른 형태의 main은 가변 String 인수를 허용한다.
```kotlin
1.
fun main() {
    println("Hello Kotlin!")
}

2.
fun main(args: Array<String>) {
    println(args.contentToString())
}
```

## Print to the standard output
print 함수는 인수를 표준 출력으로 인쇄한다.
```kotlin
fun main() {
    print("Hello ")
    print("Kotlin!")
}
```
println 함수는 인수를 출력하고 줄 바꿈을 추가한다. 
그리고 다음 줄에 다음 인자가 출력되도록 한다.
```kotlin
fun main() {
    println("Hello Kotlin!")
    println(42)
}
```
## Functions
아래 function은 2개의 Int를 parameter로 받고 Int 타입을 return 한다.
```kotlin
fun sum(firstValue: Int, secondValue: Int): Int {
    return firstValue + secondValue
}
```
함수의 body를 식으로 표현할 수 있다. 이는 타입 추론을 사용하여 return type이 정해진다.
```kotlin
fun sum(firstValue: Int, secondValue: Int) = firstValue + secondValue
```
함수의 return type을 Unit으로 선언하면 의미 있는 값을 반환하지 않는 함수가 된다.  
이는 java의 void return type과 같다.  
```kotlin
fun printSum(firstValue: Int, secondValue: Int): Unit {
    println("sum of $firstValue and $secondValue is ${firstValue + secondValue}")
}

fun main() {
    printSum(-1, 8)
}
```
return type의 Unit 키워드는 생략될 수 있다.
```kotlin
fun printSum(firstValue: Int, secondValue: Int) {
    println("sum of $firstValue and $secondValue is ${firstValue + secondValue}")
}
```
## Variables
Kotlin의 변수 키워드는 val과 var이 있다.  

val는 value의 약어이며 읽기 전용 지역 변수 키워드로 사용된다.
val은 불변으로 사용되며 한 번만 값을 할당할 수 있다.  
java의 final 키워드를 사용한 것과 같다.
```kotlin
fun main() {
    val first: Int = 1 // immediate assignment (선언과 동시에 초기화)
    val second = 2 // 'Int' 타입이 추론된다
    val third: Int // 초기화가 되지 않은 경우 해당 방식으로 변수를 선언해야 한다
    third = 3 // 초기화 시점을 늦출 수 있다
    println("first = $first, second = $second, third = $third")
}
```
var 키워드는 variable의 약어이며 값을 재할당 할 수 있다.
```kotlin
fun main() {
    var x = 5 // 'Int' 타입이 추론된다
    x += 1
    println("x = $x")
}
```
소스파일의 최상위 레벨에서 변수를 선언할 수도 있다.
```kotlin
val PI = 3.14
var x = 0

fun incrementX() {
    x += 1
}

fun main() {
    println("x = $x; PI = $PI")
    incrementX()
    println("incrementX()")
    println("x = $x; PI = $PI")
}
```
## Creating classes and instances
class 키워드를 사용해서 class를 정의할 수 있다.
```kotlin
class Shape
```
클래스의 Properties(속성, 멤버 변수)은 클래스의 선언과 body에 작성 할 수 있다.
```kotlin
class Rectangle(var height: Double, var length: Double) {
    var perimeter = (height + length) * 2
}
```
클래스에 멤버 변수를 작성할 경우 생성자는 자동으로 사용할 수 있다.
```kotlin
class Rectangle(var height: Double, var length: Double) {
    var perimeter = (height + length) * 2
}

fun main() {
    var rectangle = Rectangle(5.0, 2.0)
    println("The perimeter is ${rectangle.perimeter}")
}
```
클래스의 상속은 콜론(:) 키워드를 이용하여 할 수 있다.  
클래스는 기본적으로 final이다.  
클래스를 상속 가능하게 하려면 open 키워드를 앞에 명시해야 한다.
```kotlin
open class Shape

class Rectangle(var height: Double, var length: Double): Shape() {
    var perimeter = (height + length) * 2
}
```
## Comments (주석)
대부분의 최신 언어와 마찬가지로 Kotlin은 한줄 또는 라인의 끝과 multi-line 주석을 지원한다.
```kotlin
// This is an end-of-line comment

/* This is a block comment
   on multiple lines.*/
```
Kotlin의 블록 주석은 중첩될 수 있다.
```kotlin
/* The comment starts here
/* contains a nested comment */
and ends here. */
```
## String Templates (문자열 템플릿)
```kotlin
fun main() {
    var a = 1
    // simple name in template:
    val simpleNameA = "a is $a"
    
    a = 2
    // arbitrary expression in template:
    val simpleNameA2 = "${simpleNameA.replace("is", "was")}, but now is $a"
    println(simpleNameA2)
}
```
Conditional Expressions (조건식)
Kotlin의 조건문은 값으로 리턴하여 사용할 수 있어 조건식이라고도 부른다.
```kotlin
fun maxOfV1(first: Int, second: Int): Int {
    if (first > second) {
        return first
    }
    return second
}

fun maxOfV2(first: Int, second: Int): Int {
    return if (a > b) {
        a
    } else {
        b
    }
}

fun main() {
    println("max of 0 and 42 is ${maxOfV1(0, 42)}")
    println("max of 0 and 42 is ${maxOfV2(0, 42)}")
}
```
Kotlin에서는 if를 표현식으로도 사용할 수 있다.
```kotlin
fun maxOf(first: Int, second: Int) = if (first > second) first else second

fun main() {
    println("max of 0 and 42 is ${maxOf(0, 42)}")
}
```
## for loop
```kotlin
fun main() {
    val items = listOf("apple", "banana", "kiwi")
    for (item in items) {
        println(item)
    }
}

// of

fun main() {
    val items = listOf("apple", "banana", "kiwi")
    for (index in items.indices) { // IntRange를 순회한다.
        println("item at $index is ${items[index]}")
    }
}
```
## while loop
```kotlin
fun main() {
    val items = listOf("apple", "banana", "kiwi")
    var index = 0
    while (index < items.size) {
        println("item at $index is ${items[index]}")
        index++
    }
}
```
## when expression
```kotlin
fun describe(obj: Any): String =
    when (obj) {
        1 -> "One"
        "Hello" -> "Greeting"
        is Long -> "Long"
        !is String -> "Not a string"
        else -> "Unknown"
    }

fun main() {
    println(describe(1))
    println(describe("Hello"))
    println(describe(1000L))
    println(describe(2))
    println(describe("other"))
}
```
## Ranges
in 연산자를 사용하여 숫자가 특정 범위 내에 속하는지 확인할 수 있다.
```kotlin
fun main() {
    val x = 10
    val y = 9
    if (x in 1..y+1) {
        println("fits in range")
    }
}
```
반대로 !in 연산자를 사용해서 숫자가 특정 범위 내에 없는지도 확인할 수 있다.
```kotlin
fun main() {
    val list = listOf("a", "b", "c")
    
    if (-1 !in 0..list.lastIndex) {
        println("-1 is out of range")
    }
    if (list.size !in list.indices) {
        println("list size is out of valid list indices range, too")
    }
}
```
in을 사용하여 해당 Range (범위)를 반복할 수 있다.
```kotlin
fun main() {
    for (x in 1..5) {
        println(x)
    }
}
```
in 키워드 이후 step을 사용하면 n의 수만큼 증가하며 반복할 수 있다.  
in 키워드 이후 downTo n1 step n2를 사용하면 역순으로 n1까지 n2만큼 감소하며 반복할 수 있다.
```kotlin
fun main() {
    for (x in 1..10 step 2) {
        print(x)
    }
    // 13579
    // or
    for (x in 9 downTo 0 step 3) {
        println(x)
    }
    // 9630
}
```
## Collections
컬렉션을 반복할 경우 for loop를 사용할 수 있다.
```kotlin
fun main() {
    val items = listOf("apple", "banana", "kiwi")
    for (item in items) {
        println(item)
    }
}
```
in 연산자를 사용하여 컬렉션에 해당 객체가 포함되어 있는지 확인할 수 있다.
```kotlin
fun main() {
    val items = setOf("apple", "banana", "kiwi")
    when {
        "orange" in items -> println("juicy")
        "apple" in items -> println("apple is fine too")
    }
}
```
람다식을 사용하여 컬렉션에서 filter와 map 연산을 할 수 있다.
```kotlin
fun main() {
    val fruits = listOf("banana", "avocado", "apple", "kiwi")
    fruits
        .filter { it.startsWith("a") }
        .sortedBy { it }
        .map { it.uppercase() }
        .forEach { println(it) }
}
```
## Nullable values and null checks
Kotlin에서는 null 값이 가능한 경우 해당 참조는 명시적으로 nullable로 표시될 수 있다.  
Nullable Type은 맨 뒤에 ? 연산자를 갖는다.  

아래 함수는 String을 정수로 반환할 수 없을 경우 null을 반환한다.
```kotlin
fun parseInt(value: String): Int? {
    // ...
}
```
nullable 값을 반환하는 함수를 아래와 같이 사용할 수 있다.
```kotlin
fun parseInt(value: String): Int? {
    return value.toIntOrNull()
}

fun printProduct(first: String, second: String) {
    val x = parseInt(first)
    val y = parseInt(second)
    
    // x와 y는 null일 수 있다. x * y의 경우 error가 생길 수 있다.
    if (x != null && y != null) {
        // x와 y는 null validation을 거친 후 non-nullable로 cast된다.
        println(x * y)
    } else {
        prlntln("$first or $second is not a number")
    }
}

fun main() {
    printProduct("6", "7")
    printProduct("a", "7")
    printProduct("a", "b")
}
```
혹은 아래와 같이 사용할 수 있다.
```kotlin
fun parseInt(value: String): Int? {
    return value.toIntOrNull()
}

fun printProduct(first: String, second: String) {
    val x = parseInt(first)
    val y = parseInt(second)
    
    if (x == null) {
        prlntln("Wrong number format in first: $first")
        return
    }
    
    if (y == null) {
        prlntln("Wrong number format in second: $second")
        return
    }

    // x와 y는 null validation을 거친 후 non-nullable로 cast된다.
    println(x * y)
}

fun main() {
    printProduct("6", "7")
    printProduct("a", "7")
    printProduct("a", "b")
}
```
## Type Check 및 자동 캐스팅
is 연산자는 표현식이 어떤 유형의 인스턴스 타입인지 확인한다.
해당 유형이 불변 지역 변수, 특정 타입의 속성일 경우 명시적으로 캐스팅할 필요가 없다.
아래 getStringLength 함수는 function을 작성할 수 있는 여러 형태를 보여준다.

```kotlin
1.
fun getStringLength(obj: Any): Int? {
    if (obj is String) {
        // obj가 String 타입이면 자동으로 캐스팅 된다.
        return obj.length
    }
    // 위의 분기를 타지 않으면 obj는 여전히 Any 타입으로 확인된다.
    return null
}

2.
fun getStringLength(obj: Any): Int? {
    if (obj !is String) {
        return null
    }
    // obj가 String 타입이면 자동으로 캐스팅 된다.
    return obj.length
}

3.
fun getStringLength(obj: Any): Int? {
    // 해당 분기에서 obj는 && 오른쪽에 있는 String으로 자동 캐스팅 된다.
    if (obj is String && obj.length > 0) {
        return obj.length
    }
    return null
}

fun main() {
    fun printLength(obj: Any) {
        println("Getting the length of $obj. Result: ${getStringLength(obj) ?: "Error: The object is not a string"}")
    }
    printLength("Incomprehensibilities")
    printLength(1000)
    printLength(listOf(Any()))
}
```
