# Idioms (관용구)

## Create DTOs (POJOs / POCOs)
```kotlin
data class Customer(val name: String, val email: String)
```
data class로 정의한 클래스는 다음 기능을 제공한다.
* 모든 멤버변수에 대해 getter
* var로 선언된 멤버변수에 대해 setter
* equals()
* hashCode()
* toString()
* copy()
* component1(), component2()...

## Default values for function parameters
함수의 멤버변수들에 기본값을 설정할 수 있다.
```kotlin
fun foo(first: Int = 0, second: String = "") {
    // ... something
}
```

## Filter a list
list에 filter를 적용할 수 있다. 이는 Java의 Stream.filter와 같다.
```kotlin
1.
val positives = list.filter {
    x -> x > 0
}
2.
val positives = list.filter { it > 0 }
```

## Check the presence of an element in a collection
컬렉션에 해당 요소가 있는지 확인할 수 있다.
```kotlin
1.
if ("john@gmail.com" in emailsList) {
    // ... something
}
2.
if ("jane@gmail.com" !in emailsList) {
    // ... something
}
```

## String Interpolation (문자열 보간)
보간이란 큰따옴표 안에 변수명을 넣어도 값을 출력하도록 하는 방식이다.  
Java에서는 문자열을 연결할 경우 + 연산자 혹은 StringBuilder 등을 사용하지만,  
코틀린에서는 문자열 템플릿을 이용하여 값을 출력할 수 있다.
```kotlin
println("Name $name")
```

## Instance checks
```kotlin
when (x) {
    is Foo -> ...
    is Bar -> ...
    else -> ...
}
```

## Read-only list
```kotlin
val list = listOf("a", "b", "c")
```

## Read-only map
```kotlin
val map = mapOf("a" to 1, "b" to 2, "c" to 3)
```

## Access a map entry
```kotlin
println(map["key"])
map["key"] = value
```

## map의 key, value 탐색
```kotlin
for ((key, value) in map) {
    println("$key -> $value")
}
```

## Iterate over a range
```kotlin
for (i in 1..100) {} // closed range: 100을 포함한다.
for (i in 1 until 100) {} // half-open range: 100을 포함하지 않는다.
for (x in 2..10 step 2) {}
for (x in 10 downTo 1) {}
```

## Lazy Property
```kotlin
val value: String by lazy {
    // ...
}
```

## Extension functions
확장함수는 초기화 된 값에 바로 접근할 수 있다.  
이는 Java와 같다. 하지만 권장되는 방법은 아니라고 생각한다.
```kotlin
fun String.spaceToCamelCase() {}
"Converter this to camelcase".spaceToCamelCase()
```

## Create a singleton
object로 class를 생성하면 singleton Type이 된다.
```kotlin
object Resource {
    val name = "Name"
}
```

## Instantiate an abstract class (추상 클래스 인스턴스화)
추상 클래스를 재정의하여 변수에 할당한 후 사용할 수 있다.

```kotlin
abstract class MyAbstractClass {
    abstract fun doSomething()
    abstract fun sleep()
}

fun main() {
    val myObject = object : MyAbstractClass() {
        override fun doSomething() {
            // ...
        }
        
        override fun sleep() {
            // ...
        }
    }
    myObject.doSomething()
}
```

## If-not-null shorthand
만약 files가 null이 아니면 size가 출력된다.
```kotlin
val files = File("Test").listFiles()
println(files?.size)
```

## If-not-null-else shorthand
만약 files가 null이라면 empty가 출력된다.
```kotlin
val files = File("Test").listFiles()
println(files?.size ?: "empty")
```

## Execute a statement if null
해당 값이 null일 경우 명령문을 실행할 수 있다.
```kotlin
val values = ...
val email = values["email"] ?: throw IllegalStateException("Email is missing!")
```

## Get first item of a possibly empty collection
Collection의 첫 번째 아이템을 가져오고, 비어있다면 ""을 가져온다.
```kotlin
val emails = ...
val mainEmail = emails.firstOrNull() ?: ""
```

## Execute if not null
null이 아니면 아래 블럭을 실행할 수 있다.
```kotlin
val value = ...
value?.let {
    // ... execute this block if not null
}
```
## Map nullable value if not null
value 또는 변환한 value의 결과가 null인 경우 defaultValue를 반환한다.
```kotlin
val value = ...
val mapped = value?.let {
    transformValue(it)
} ?: defaultValue
```

## Return on when statement
when을 식으로 하여 return할 수 있다.
```kotlin
fun transform(color: String): Int {
    return when (color) {
        "Red" -> 0
        "Green" -> 1
        "Blud" -> 2
        else -> throw IllegalArgumentException("Invalid color param value")
    }
}
```

## try-catch expression
```kotlin
fun test() {
    val result = try {
        count()
    } catch (e: ArithmeticException) {
        throw IllegalStateException(e)
    }
    
    // Working with result
}
```

## if expression (조건식)
```kotlin
fun foo(param: Int) {
    val result = if (param == 1) {
        "one"
    } else if (param == 2) {
        "two"
    } else {
        "three"
    }
}
```

## Builder-style usage of methods that return Unit
Unit(void)를 반환하는 Builder style의 메소드를 사용할 수 있다.
```kotlin
fun arrayOfMinusOnes(size: Int): IntArray {
    return IntArray(size).apply {
        fill(-1)
    }
}
```

## Single-expression functions
```kotlin
fun theAnswer() = 42
// 위의 Single expression은 아래 함수와 같다.
fun theAnswer(): Int {
    return 42
}
```
Single expression은 다른 Idioms(관용구)와 결합하여 코드를 좀 더 줄일 수 있다.  
아래 function에서는 when 구문을 Single expression으로 사용하는 것을 나타낸다.
```kotlin
fun transform(color: String): Int = when (color) {
    "Red" -> 0
    "Green" -> 1
    "Blud" -> 2
    else -> throw IllegalArgumentException("Invalid color param value")
}
```

## Call multiple methods on an object instance(with)
with 함수를 이용하여 객체 인스턴스에서 여러 메서드를 호출할 수 있다.
```kotlin
class Turtle {
    fun penDown()
    fun penUp()
    fun turn(degrees: Double)
    fun forward(pixels: Double)
}

val myTurtle = Turtle()
with(myTurtle) { // draw a 100 pix square
    penDown()
    for (i in 1..4) {
        forward(100.0)
        turn(90.0)
    }
    penUp()
}
```

## Configure properties of an object(apply)
apply 함수를 이용하여 객체의 속성을 설정할 수 있다.  
해당 함수는 객체의 생성자에 없는 속성을 구성하는데 사용하기 좋다.
```kotlin
val myRectangle = Rectangle().apply {
    length = 4
    breadth = 5
    color = 0xFAFAFA
}
```

## Java7's try-with-resources
```kotlin
val stream = Files.newInputStream(Paths.get("/some/file.txt"))
stream.buffered()
    .reader()
    .use { reader -> println(reader.readText()) }
```

## Generic function that requires the generic type information
Generic 유형의 정보가 필요할 경우 아래와 같이 사용할 수 있다.
```kotlin
// public final class Gson {
// ...
// public <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
// ...

inline fun <reified T: Any> Gson.fromJson(json: JsonElement): T = this.fromJson(json, T::class.java)
```

## Nullable Boolean
Boolean 타입을 Nullable하게 사용할 수 있다.
```kotlin
val result: Boolean? = ...
if (result == true) {
    ...
} else {
    // `result`는 false 혹은 null로 반환된다.
}
```

## Swap two variables
2개의 변수를 교환할 수 있다.
```kotlin
var first = 1
var second = 2
first = second.also {
    second = first
}
```

## Mark code as incomplete (TODO)
Kotlin의 표준 라이브러리에는 NotImplementedError를 발생시키는 TODO() function이 있다.    
return type은 "Nothing"으로 예상한 타입에 관계없이 사용할 수 있다.    
해당 함수는 매개변수를 허용하는 오버로드를 사용할수도 있다.
```kotlin
fun calculateTaxes(): BigDecimal = TODO("Waiting for feedback from accounting")
```
TODO() function을 이용하면 IntelliJ는 사용처를 하이라이트 하여 TODO Tool에 해당 부분을 추가한다. 
