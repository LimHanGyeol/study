# Coding Conventions
Coding Conventions(코딩 관습)은 띄어쓰기 이름 작성 시 대소문자 등 문법오류와는 관계 없으나 많은 사람들이 따르는 일반적인 코드 작성 규칙을 말한다.  
이런 규칙을 따르는 것이 다른 사람의 코드를 분석하거나 자신이 작성한 코드이더라도 가독성을 높이는 방법이 된다.  

Kotlin Coding Conventions의 자세한 내용은 공식 문서의 링크로 대체한다.  
그리고 공식 문서를 한글 번역한 블로그가 있어 이를 참고한다.

이번 파트는 Coding Conventions를 보며 혼동의 여지가 있을 수 있다고 생각되는 부분들 위주로 정리한다.

---
## 이름 지정 규칙
Kotlin은 Java 이름 지정 규칙을 따른다.  
특히, 패키지 이름은 항상 소문자이며 밑줄은 사용하지 않는다.  
여러 단어로 된 이름을 사용하는 것은 일반적으로 권장되지 않지만 여러 단어를 사용해야 할 경우 간단히 붙여서 사용하거나 카멜 표기법을 사용할 수 있다.
(Ex: org.example.myProject)

클래스와 객체의 이름은 대문자로 시작하고 카멜 표기법을 사용한다.
```kotlin
open class DeclarationProcessor { ... }

object EmptyDeclarationProcessor : DeclarationProcessor() { ... } 
```

### 함수 이름
함수, 속성 및 지역 변수의 이름은 소문자로 시작하고 카멜 표기법을 사용한다.  
밑줄은 사용하지 않는다.

```kotlin
fun processDeclarations() { ... }

var declarationCount = ...
```

예외적으로 클래스의 인스턴스를 생성하는데 사용되는 팩토리 함수는 생성되는 클래스와 동일한 이름을 가질 수 있다.

```kotlin
abstract class Foo { ... }

class FooImpl : Foo { ... }

fun Foo(): Foo { return FooImple() }
```

### 테스트 메서드 이름
테스트 메서드의 이름을 백틱(`)을 사용해서 메서드 이름에 공백을 사용하는 것이 허용된다.
메서드 이름에 밑줄도 테스트 코드에선 사용할 수 있다.
```kotlin
class MyTestCase {
    @Test
    fun `ensure everything works`() { ... }
    
    @Test
    fun ensureEverythingWorks_onAndroid() { ... }
}
```

### 속성 이름
상수는 const로 표시되거나 Source File의 Top-Level에 정의된 변수,  
또는 Custom Getter를 가지지 않는 object의 val로 선언된 속성처럼 불변의 데이터를 의미한다.  
상수의 경우 Java와 같이 밑줄로 분리되는 대문자를 사용한다.
```kotlin
const val MAX_COUNT = 9

val USER_NAME_FIELD = "UserName"
```

행위(behavior) 또는 상수를 갖는 객체를 가지는 Top-Level또는 object 속성의 이름은 일반적인 카멜 표기법을 사용한다.
```kotlin
val mutableCollection: MutableSet = HashSet()
```

싱글톤 객체에 대한 참조를 가지는 속성의 이름은 object 선언과 동일한 명명 스타일을 사용할 수 있다.
```kotlin
val PersonComparator: Comparator = ...
```

열거형 타입인 enum class의 경우 밑줄로 구분되는 대문자 이름 또는 대문자로 시작하는 일반적인 카멜 표기법 이름을 사용할 수 있다.
```kotlin
enum class Color {
    RED, GREEN
}
```

### 후위 속성(Backing Properties)의 이름
클래스에 개념적으로는 동일하지만 하나는 public API의 일부이고,  
다른 하나는 상세 구현으로 private인 경우 private 속성의 이름에 접두사로 밑줄(_)을 사용한다.
```kotlin
class C {
    private val _elementList = mutableListOf()
    
    val elementList: List
        get() = _elementList
}
```

### 좋은 이름 선택하기
클래스의 이름은 보통 명사 또는 클래스가 무엇인지 설명하는 명사구이다. (List, PersonReader)

메서드의 이름은 일반적으로 동사 또는 메서드가 무엇을 하려는지 말하는 동사 구문이다. (close, readPersons)  
메서드는 메서드가 객체를 변형시키거나 새 객체를 반환하는지 알려줘야 한다.  
예를들어 sort는 컬렉션 그 자체를 정렬하는 행위이다.
sorted는 컬렉션의 정렬된 복사본을 반환하는 것이다.

이름은 엔티티의 목적이 무엇인지 분명히 해야한다.  
그래서 이름에 Manager, Wrapper 등의 의미없는 단어를 사용하는 것은 권장되지 않는다.

약어 이름을 선언의 일부로 사용하는 경우, 그리고 그 약어가 두 개의 문자로 구성되는 경우 대문자로 시작한다. (IOStream)  
약어가 두 글자 이상으로 길다면, 첫 글자만 대문자로 표기한다. (XmlFormatter, HttpInputStream)

## 서식 지정(Formatting)
대부분의 경우 Kotlin은 Java Coding Conventions를 따른다.
들여쓰기는 공백 4개를 사용한다. Tab을 사용한 들여쓰기를 허용하지 않는다.

중괄호(curly braces)의 경우,  
문장 행의 끝에 여는 괄호를 사용하고, 닫는 괄호는 별도의 라인에 여는 괄호가 있는 문장의 시작 열의 위치에 둔다.
```kotlin
if (elements != null) {
    for (element in elements) {
        // ...
    }
}
```

### 콜론(:)
다음과 같은 경우 콜론(:) 앞에 공백을 넣는다.
* 타입과 수퍼타입을 분리할 때
* 수퍼 클래스 생성자 또는 같은 클래스의 다른 생성자에 위임할 때
* object 키워드 뒤에

콜론이 선언과 그것의 타입을 분리하는데 사용될 경우 콜론 앞에 공백을 두지 않는다.
콜론(:) 뒤에는 항상 공백을 놓는다.
```kotlin
abstract class Foo : IFoo { // 타입과 수퍼타입을 분리할 때
    abstract fun foo(a: Int): T
}

class FooImpl : Foo() {
    constructor(x: String) : this(x) { ... } // 수퍼 클래스 생성자 또는 같은 클래스의 다른 생성자에 위임할 때
    
    val x = object : IFoo { ... } // object 키워드 뒤에
}
```

### 클래스 헤더 서식
주 생성자 매개변수가 몇 개 밖에 되지 않는 클래스는 한 줄에 작성할 수 있다.
```kotlin
class Person(id: Int, name: String)
```

더 긴 헤더가 있는 클래스는 각 주 생성자 매개 변수가, 들여쓰기가 있는 별도의 줄에 있도록 서식을 지정해야 한다.
또한 닫는 괄호는 새 줄에 있어야 한다.  
상속을 사용하는 경우, 수퍼 클래스 생성자 호출 또는 구현된 인터페이스 목록은 괄호와 같은 줄에 있어야 한다.
```kotlin
class Person(
    id: Int,
    name: String,
    email: String
) : Human(id, name) { ... }
```

다중 인터페이스의 경우 수퍼 클래스 생성자 호출이 먼저 위치해야하며 각 인터페이스는 다른 행에 있어야 한다.
```kotlin
class Person(
    id: Int,
    name: String,
    email: String
) : Human(id, name),
    KotlinMaker { ... }
```

긴 상위 유형 목록이 있는 클래스의 경우 콜론 다음에 줄 바꿈을 넣고 모든 상위 유형이름의 시작열을 맞춘다.
```kotlin
class MyFavoriteVeryLongClassHolder :
    MyLongHolder(),
    SomeOtherInterface,
    AndAnotherOne {
        
    fun Foo() { ... }
}
```

클래스 헤더가 길 때 클래스 헤더와 본문을 명확하게 구분하려면 클래스 헤더 다음에 빈 줄을 넣거나 여는 중괄호를 별도의 줄에 넣는다.
```kotlin
class MyFavoriteVeryLongClassHolder :
    MyLongHolder(),
    SomeOtherInterface,
    AndAnotherOne
{
    fun foo() { ... }
}
```

생성자 매개 변수에 일반 들여쓰기 (공백 4개)를 사용한다.
이유는 주 생성자에서 선언된 속성이 클래스 본문에 선언된 속성만큼 들여쓰기 되도록하기 위해서이다.

### 수정자
모든 어노테이션은 수정자 앞에 놓는다.
```kotlin
@Named("Foo")
private val foo: Foo
```
라이브러리에서 작업하지 않는이상 public과 같은 중복 수정자를 생략한다.

### 어노테이션 서식
어노테이션은 일반적으로 어노테이션이 붙여질 선언 위에 같은 들여쓰기로 별도의 라인에 위치한다.
```kotlin
@Target(AnnotationTarget.PROPERTY)
annotation class JsonExclude
```

인수가 없는 어노테이션은 여러개를 같은 라인에 위치할 수 있다.
```kotlin
@JsonExclude @JvmField
var x: String
```

인수가 없는 단일 어노테이션은 선언과 같은 라인에 위치할 수 있다.
```kotlin
@Test fun foo() { ... }
```

### File 어노테이션
파일 어노테이션은 파일 관련 주석이 있을 경우 해당 주석 다음에,
package 명령문 앞에 놓이며 패키지가 아닌 파일을 대상으로 한다는 사실을 강조하기 위해 package와 빈 행으로 구분된다.
```kotlin
/** License, copyright and whatever **/
@file:JvmName("FooBar")

package foo.bar
```

### 함수 서식
함수의 signature이 한 줄에 들어가지 않으면 다음 구분을 사용한다.
```kotlin
fun longMethodName(
    argument: ArgumentType = defaultValue,
    argument2: AnotherArguemntType
): ReturnType {
    // body
}
```

함수 매개 변수에는 생성자 매개 변수와의 일관성을 위해 일반 들여쓰기인 공백 4개를 이용한다.
단일 표현식으로 구성된 바디를 가진 함수는 표현식 바디를 사용하는 것이 권장된다.
```kotlin
fun foo(): Int { // bad
    return 1
}

fun foo() = 1 // good
```

### 표현식 바디 서식
만약 함수가 선언과 같은 한 라인에 맞지 않는 표현식 바디를 가지면,  
= 기호를 첫 행에 놓는다. 표현 바디는 공백 4개 만큼 들여쓴다.
```kotlin
fun foo(x: String) =
    x.length
```

### 속성 서식
매우 간단한 읽기 전용 속성의 경우 한 줄 형식을 사용한다.
```kotlin
val isEmpty: Boolean get() = size == 0
```

보다 복잡한 속성의 경우 항상 get, set 키워드를 별도의 줄에 입력한다.
```kotlin
val foo: String
    get() { ... }
```

초기화를 하는 속성의 경우 초기화 코드가 길어지면 등호 다음에 줄 바꿈을 추가하고 공백 4개 만큼 들여쓴 후 초기화를 한다.
```kotlin
private val defaultCharset: Charset? =
    EncodingRegistry.getInstance().getDefaultCharsetForPropertiesFiles(file)
```

### 흐름 제어문의 서식
if나 when문의 조건이 여러개일 경우, 항상 문장의 본문 주위에 중괄호{}를 사용한다.  
조건의 두 번째 줄부터는 앞에 공백 4개의 들여쓰기를 한다.  
조건을 닫는 소괄호)를 여는 중괄호{와 함께 별도의 줄에 놓는다.
```kotlin
if (!component.isSyncing &&
    !hasAnyKotlinRuntimeInScope(module)
) {
    return createKotlinNotConfiguredPanel(module)
}
```

else, catch, finally, do/while문의 while 키워드의 같은 줄에 중괄호로 시작한다.
이는 조건 및 명령문 본문의 깔끔한 정렬 및 명확한 분리를 위한 서식이다.
```kotlin
if (condition) {
    // body
} else {
    // else body
}

try {
    // body
} finally {
    // cleanup
}
```

when 문장에서 실행 부분이 한 라인 이상일 경우 인접한 실행 블록을 빈줄로 분리한다.
```kotlin
private fun parsePropertyValue(propertyName: String, token: Token) {
    when (token) {
        is Token.ValueToken ->
            callback.visitValue(propertyName, token.value)
        
        Token.LBRACE -> { // ...
        }
    }
}
```

짧은 조건/실행 라인은 중괄호 없이 한 라인에 놓는다.
```kotlin
when (foo) {
    true -> bar() // good
    false -> { baz() } // bad
}
```

### 메서드 호출 서식
긴 인수 목록에서 여는 괄호 뒤에서 줄 바꿈을 한다.  
인자는 공백 4개로 들여쓰기 한다.  
밀접하게 관계있는 여러 인자는 같은 라인에 적어서 그룹화 한다.
```kotlin
drawSquare(
    x = 10, y = 10,
    width = 100, height = 100,
    fill = true
)
```
인자 이름과 값을 구분하는 = 등호 앞, 뒤에 공백을 넣는다.

### 연결된 호출은 줄 바꿈(메서드 체이닝)
연결된 호출을 줄 바꿈 할 때 (., ?.) 연산자에서 줄 바꿈하여 다음 라인에 하나의 인덴트로 배치한다.
```kotlin
val anchor = owner
    ?.firstChild!!
    .siblings(forward = true)
    .dropWhile { it is PsiComment || it is PsiWhiteSpace }
```

### 람다(Lambda) 서식
람다식에서는 매개 변수를 본문에서 분리하는 화살표 주위뿐만 아니라 중괄호 주위에 공백을 사용해야 한다.  
호출이 매개변수로 람다 하나를 받는다면, 람다는 괄호 밖에 적을 수 있다.
람다의 경우 매개변수가 하나뿐이고, 그 타입을 컴파일러가 추론 가능할 경우 it을 바로 사용할 수 있다.
```kotlin
{ x: Int, y: Int -> x + y }

list.filter { it > 10 }
```

람다를 위한 레이블을 할당하는 경우 레이블과 여는 중괄호 사이에 공백을 두지 않는다.
```kotlin
fun foo() {
    ints.forEach lit@{
        // ...
    }
}
```

람다에서 매개 변수 이름을 선언할 때 내부 로직이 길어지는 경우 첫 번째 라인에 이름을 넣고,  
그 다음 라인에 화살표와 줄 바꿈을 넣는다.
```kotlin
appendCommaSeparated(properties) { properties ->
    val propertyValue = properties.get(obj) // ...
}
```

만약 매개 변수 개수가 많아 한 줄에 들어갈 수 없는 경우,  
화살표를 별도의 줄에 입력한다.
```kotlin
foo {
    context: Context,
    environment: Env
    ->
    context.configureEnv(environment)
}
```

## 문서 주석(Documentation comments)
@param, @return 태그 사용을 권장하지 않는다.
매개 변수 및 반환 값에 대한 설명을 문서 설명에 직접하여 통합시키고,  
언급되는 모든 위치에 매개 변수에 대한 링크를 추가한다.  
@param, @return은 본문의 흐름에 맞지 않는 긴 설명이 필요한 경우에만 사용한다.
```kotlin
// bad

/**
 * returns the absolute value of the given number.
 * @param number The number to return the absolute value for.
 * @return The absolute value.
 */
fun abs(number: Int) = ...

// good

/**
 * Returns the absolute value of the given [number].
 */
fun abs(number: Int) = ...
```

## 불필요한 구성 사용하지 않기
### Unit
함수가 Unit을 반환하면 반환 타입을 생략한다.
```kotlin
fun foo() { // : Unit
    
}
```

### 세미콜론(;)
세미콜론은 생략한다.

### 문자열 템플릿
문자열 템플릿에 간단한 변수를 삽입할 경우 중괄호를 사용하지 않는다.  
더 긴 표현식에 대해서만 중괄호를 사용한다.
```kotlin
println("$name has ${children.size} children")
```

## 언어의 기능들을 관용적으로 사용하기
### 불변성
불변 데이터를 사용하는 것을 권장한다.  
로컬 변수와 속성이 초기화된 후에 수정되지 않는다면 항상 var보다는 val로 선언한다.

컬렉션을 사용할 때 불변 콜렉션 유형을 반환하는 함수를 사용하는 것을 권장한다.
```kotlin
// bad
fun validateValue(actualValue: String, allowedValues: HashSet) { ... }

// good
fun validateValue(actualValue:String, allowedValues: Set) { ... }

// bad
val allowedValues = arrayListOf()

// good
val allowedValues = listOf()
```

### Default 매개 변수값
오버로드 함수를 선언하는 것보다 기본 매개변수 값이 있는 함수를 선언하는 것을 권장한다.
```kotlin
// bad
fun foo() = foo("a")
fun foo(a: String) { ... }

// good
fun foo(a: String = "a") { ... }
```

### 람다(Lambda) 매개변수
짧고 중첩되지 않은 람다에서는 매개 변수를 명시적으로 선언하는 대신,  
it 을 사용하는 것을 권장한다.  
매개 변수가 있는 중첩되는 람다에서는 매개변수를 항상 명시적으로 선언해야 한다.

### 명명된 인수
메서드의 매개변수에서 명명된 인수를 사용하여 가독성을 높일 수 있다.
```kotlin
drawSquare(x = 10, y = 10, width = 100, height = 100, fill = true)
```

### 조건문의 사용
try, if, when의 표현 양식을 사용하는 것을 권장한다.
```kotlin
return if (x) foo() else bar()

return when(x) {
    0 -> "zero"
    else -> "nonzero"
}

// 아래 코드보다 위의 코드를 더 권장한다.

if (x)
    return foo()
else
    return bar()

when(x) {
    0 -> return "zero"
    else -> return "nonzero"
}
```

### if vs when
조건이 2개일 경우 when 보다 if를 권장한다.
조건이 3개 이상일 경우 if보다 when을 더 권장한다.
```kotlin
when (x) {
    null -> ...
    else -> ...
}

// 위의 코드보다 아래의 코드를 더 권장한다.
if (x == null) {
    // ...
} else {
    // ...
}
```

### nullable한 Boolean
조건문에 nullable Boolean 값을 사용해야 하는 경우,  
if (value == true) of if (value == false)의 형태로 사용해야 한다.

### 반복문(loop) 사용하기
반복문에는 고차함수(filter, map ...)를 사용하는 것이 좋다.  
하지만 forEach의 경우는 일반적인 for loop를 사용하는 것이 권장된다.

* 고차함수(higher-order function) : 하나 이상의 함수를 인수로 취하거나, 함수를 결과로 반환하는 함수  

복잡한 표현식에서 다중의 복잡한 고차함수를 사용할지,  
루프를 사용할지를 선택할 때 실행에 드는 비용(cost)과 성능 문제를 고려해야 한다.

### 범위를 사용하는 반복문
루프에서 끝 범위가 하나 작을 때 until을 사용한다.
```kotlin
for (i in 0..n - 1) { ... } // bad

for (i in 0 until n) { ... } // good
```
* closed-range : (i in 1..5) 해당 구문의 의미는 1 <= i 이면서 i >= 5 이다.
* half-open range : (i in 1 until 5) 해당 구문의 의미는 1 <= i 이면서 i > 5 이다.

### 문자열 사용하기
문자열 연결보다 문자열 템플릿을 사용하는 것을 권장한다.  
여러 라인의 문자열을 사용하는 것이 일반 문자열 리터럴에 \n 이스케이프 시퀀스를 넣는것보다 권장된다.  
여러 라인 문자열에서 결과 문자열이 내부에서 들여쓰기 되어야 한다면 trimIndent를 사용하고,  
그렇지 않으면 trimMargin을 사용한다.
```kotlin
assertEquals(
    """
    Foo
    Bar
    """.trimIndent(),
    value
)

val a = """if(a > 1) {
            |    return a
            |}""".trimMargin()
```

### 함수 vs 속성
경우에 따라 인수가 없는 함수는 읽기 전용 속성과 호환될 수 있다.  
비록 의미는 유사하지만, 둘중 하나를 선택하는 양식적인 관습이 있다.
아래의 상황의 경우 함수보다 속성을 선호한다.
* throw 하지 않는다.
* 계산이 쉽거나 첫 번째 실행에서 캐쉬된다.
* 객체의 상태가 변경되지 않는다면 반복 호출에 대해 동일한 결과를 반환한다.

### 확장함수 사용하기
확장함수를 자유롭게 사용하는 것을 권장한다.  
항상 하나의 객체를 대상으로 주된 작업을 하는 함수를 갖고 있다면,  
그 객체를 수신자(receiver)로 받아들이는 확장 함수를 고려해라.  
API의 오염을 최소화 하기 위해 확장 함수의 가시성을 필요한 범위내로 제한한다.  
필요에 따라 로컬 확장 함수, 멤버 확장 함수 또는 private 가시성을 가진 최상위(top-level) 확장 함수를 사용할 수 있다.

### infix 함수의 사용
유사한 역할을 수행하는 두 객체에서만 infix 함수를 선언한다.  
좋은 상황 : and, to, zip  
좋지 않은 상황 : add

수신자(receiver) 객체를 변경하는 함수는 infix로 선언하면 안된다.
* infix 표기법 : Kotlin에서 어떤 함수는 . 이나 () 없이 호출이 가능한데, 이것을 infix 메서드라고 한다.

### 팩토리(factory) 함수
클래스에 대해 팩토리 함수를 선언하는 경우 클래스 자체와 동일한 이름을 지정하지 않는 것을 권장한다.  
팩토리 함수의 동작이 특별한 이유를 명확히 하기 위해 고유한 이름을 사용하는 것을 권장한다.  
정말 특별한 의미가 없는 경우에만 클래스와 같은 이름을 사용할 수 있다.
```kotlin
class Point(val x: Double, val y: Double) {
    companion object {
        fun fromPolar(angle: Double, radius: Double) = Point(...)
    }
}
```
여러 개의 오버로드 된 생성자가 있는 객체가 서로 다른 수퍼 클래스 생성자를 호출하지 않고,  
기본 인수 값을 가진 단일 생성자로 축소할 수 없는 경우, 오버로드된 생성자를 팩토리 함수로 대체하는 것이 좋다.

### 플랫폼 타입(Platform type)
플랫폼 타입의 표현(expression)을 반환하는 public function/method는 Kotlin 타입을 명시적으로 선언해야 한다.
```kotlin
fun apiCall(): String = MyJavaApi.getProperty("name")
```

플랫폼 타입의 표현식으로 초기화되는 모든 속성(패키지 레벨, 클래스 레벨)은 Kotlin 타입을 명시적으로 선언해야 한다.
```kotlin
class Person {
    val name: String = MyJavaApi.getProperty("name")
}
```

플랫폼 타입의 표현식으로 초기화한 로컬 값에는 타입 선언이 있을 수도 있고, 없을 수도 있다.
```kotlin
fun main() {
    val name = MyJavaApi.getProperty("name")
    println(name)
}
```

### Scope 함수 (apply, with, run, also, let) 사용
Kotlin은 주어진 객체의 Context에서 코드의 블록을 실행하는 다양한 기능을 제공한다.



