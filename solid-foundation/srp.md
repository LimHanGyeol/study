# SRP(Single Responsibility Principle)

단일 책임의 원칙

## Responsibility

- 클래스는 하나의 책임을 가져야 한다.
- 책임이란 무엇인가?
    - 책임은 같은 부류로 엮을 수 있다.
    - 부류
        - 메서드의 Client에 의해 결정할 수 있다.
        - 누가 해당 메서드의 변경을 유발하는 사용자(Actor)인가?

이런 부류를 나눔으로써 메서드의 책임을 Grouping 할 수 있다.

## It's About Users

- SRP는 사용자에 관한 것이다.
- 책임
    - SW의 변경을 요청하는 특정 사용자들에 대해 클래스/함수가 갖는 것
    - **변경의 근원**으로 볼 수 있음
- Employee : 클래스의 변경을 요구하는 사용자들
    - Actor : 서로 다른 Needs, Expectation(기대치)을 가짐.  
    밑의 예시에서 Actor는 Policy, Architect, Operations로 나뉘어짐.

![srp-uml.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/srp-uml.png)

## It's About Roles

- User들은 그들이 수행하는 Role(역할)에 따라 나뉘어야 한다.
- User가 특정 Role을 수행할 때 Actor라고 부른다.
    - 책임은 개인이 아니라 Actor와 연결된다.
- Employee 클래스에는 3개의 Actor가 있다. 즉 3개의 Role을 가진 User가 있다.
    - Policy, Architect, Operations

## Reprise

- Responsibility  
특정 Actor의 요구사항을 만족시키기 위한 일련의 함수의 집합
- Actor의 요구사항 변경이 일련의 함수들의 변경의 근원이 된다.

---

# Two Values of SW

- Primary and Secondary Values
- Secondary value of SW is it's behavior  
현재의 SW가 현재 사용자의 현재 요구사항을 만족하는가?
- Primary Value of SW
    - 지속적으로 변화하는 요구사항을 수용(tolerate, facilitate) 하는것
    - 대부분의 SW는 현재의 요구사항은 잘 만족하지만 변경하긴 어렵다.

## Collision (책임의 충돌)

하나의 클래스에 Actor의 역할을 전부 부여할 경우 Primary Value가 저하된다.

- Policy Actors : Business Rule의 변경을 필요
- Architecture Actors : DB Schema의 변경을 필요
- Operations Actors : Business Rule의 변경을 필요
- **동일 모듈을 변경**하게 되어 Merge할 경우 충돌이 일어난다.

## Fan Out (전개, 산개)

- Employee는 너무 많은 것을 안다.
    - Business Rules
    - DB
    - Reports, Formatting
- 너무 많은 것을 알아 많은 책임을 갖는다.
- 각각의 책임은 Employee가 다른 클래스들을 사용하도록 해야 한다.
- Employee 클래스에는 거대한 Fan Out이 존재한다.
- 변경에 민감하게 될 수 밖에 없다. Employee User는 더 민감해진다.
- Fan out을 최대한 제한하는 것이 좋다.
    - 좋은 방법은 책임을 최소화 하는 것 이다.
    → **클래스당 하나의 책임만 갖게 하는 것**. 이것이 **SRP**이다.
    - 하나의 클래스에 여러개의 책임이 부여될 경우
    Actor들의 Coupling(연결)을 유발한다.

![fan-out.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/fan-out.png)

# SRP

- 모듈은 반드시 하나의 변경 사유를 가져야 한다.
- One and only one responsibility  
동일한 이유(Actor)로 변경되어야 하는 것들은 동일 모듈에, 다른 이유로 변경되어야 하는 것들은 다른 모듈에 구현해야 한다.
- SRP는 시스템 설계할 때 가장 적용하기가 좋다. (UML을 그리며 설계)
    - Actor 파악에 주의해야 한다.
    - Actor들을 service하는 책임들을 식별하고,
    반드시 하나의 책임을 갖도록 유지하며 모듈에 할당해야 한다.
    - 책임을 분리하는 이유
    다른 이유로 인해 변경되고, 다른 때에 변경되기 때문

## Solutions

1. Inverted Dependencies - 의존성 역전
    - OOP에서 이런 의존성을 다루는 전략  
    클래스를 인터페이스와 클래스로 분리한다. (Polymorphism Interface)
    런타임 의존성은 의도대로 흐르지만, 소스코드 의존성은 역전되어 흐른다.
    - Actor를 클래스에서 Decouple  
    모든 Actor들이 하나의 인터페이스에 coupled된다.  
    의존성 분리는 했지만 하나의 구현 클래스에 coupled되게 된다.
2. Extract Classes - 클래스 분리
    - 각각의 책임을 클래스로 분리하는 전략
    - 결과
        - Actor들은 분리된 구현 클래스에 의존하게 된다.
        - 책임에 의해 분리된 구현 클래스가 변경되어도 다른 책임에 영향이 미치지 않는다.
    - 문제점
        - Transitive Dependency
        Employee (의존하는 인터페이스)가 변경될 경우 구현 클래스에게 영향이 미칠 수 있다.
3. Facade - 어디에 구현이 있는지 찾기 쉽게
    - 각 Actor의 책임을 갖고 있는 클래스로 분리하고,
    Facade 패턴을 이용하여 각각의 책임을 하나의 구현 클래스에서 구현한다.
    - 장점 : 어디에 구현되었는지 찾기 쉽다.
    - 단점 : Actor들은 여전히 구현 클래스에 Coupled 되어 있다.
4. Interface Segregation
    - 각 Actor의 책임을 갖고 있는 인터페이스로 분리하고,
    각각의 책임을 하나의 구현 클래스에서 구현한다.
    - 장점 : Actor들은 완전히 decoupled 된다.
    - 단점
        - 어디에 구현되었는지 찾기 어렵다.
        - 하나의 클래스에 구현되어 구현은 여전히 coupled 되어 있다.

>Inverted Dependencies와 Extract Classes를 조합하여 쓰면 각각의 단점을 보완하여 더욱 효율적인 의존성 관리를 할 수 있다.

>Interface Segregation의 구현 클래스를 각각의 책임으로 분리하면 가장 좋은 형태가 된다.  하지만 많은 노력이 필요하다. 모든 Actor의 책임과 구현을 찾아 분리해야 한다.

## Faking It

- 실제 작업 흐름 with TDD
    - 가장 먼저 테스트를 작성하고 통과하도록 한다.
    - 필요 행위를 동작하는 함수를 만든다.
    - 위의 행위를 위한 로직의 동작하는 함수를 만든다.
    - 설계가 드러날 때까지 각 함수들을 리팩토링 한다.
    - 동작하는 전체 기능을 얻을때까지 모든 행위들이 테스트에 성공하도록 설계를 적용했다.
    - 그리고 아키텍처를 살핀다. 테스트가 의도한 설계의 80%를 유도했다.
    - 그리고 테스트가 n개의 책임을 식별하는 것을 도왔다.
    - Unit Test가 확보된 후에 디자인을 향상시키기 위해 무차별적인 리팩토링을 수행한다.
    - 이런 후에만 의도한 n개의 Actor들이 식별된다. 그러면 식별한 클래스들을 연관된 n개의 패키지로 분리한다.
    - 마지막으로 이쁜 다이어그램을 그린다. 이쁜 다이어그램을 그리기 가장 좋은 때는 완료된 후이다.
