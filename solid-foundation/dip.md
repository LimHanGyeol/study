# Dependency Inversion Principle
의존관계 역전의 원칙
- High Level Policy should not depend on Low Level Details  
  상위 레벨의 정책은 하위 구현체에 의존하면 안된다.
- 둘은 Abstract Type에 의존해야 한다.

![dip-ex1.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/dip-ex1.png)

### Object - Oriented의 핵심

- Inheritance, Encapsulation, Polymorphism은 객체지향의 핵심이 아니라 이를 구성하는 메커니즘에 불과하다.
- 객체지향의 핵심은 IoC를 통해 상위 레벨의 모듈을 하위 레벨의 모듈로부터 보호하는 것 이다.  
  하위 레벨의 변경에 상위 모듈이 영향 받으면 안된다.
    - 이에 더불어 OCP를 통해 새로운 요구사항을 반영할 수 있다.  
      DIP의 경우 OCP와 밀접하게 연관되어 있다.
- Object Oriented Design은 Dependency Management,
  객체지향 설계는 의존성 관리이다.

### Structured Design
- Top - down 방법론
  DIP를 설명하는 예제 중 구조적 설계를 하게 되면 Top - down 방법론으로 설계를 하는 경우가 있다.
    - 소스 코드 의존성의 방향 = 런타임 의존성의 방향  
      하위 구현체에서 변경이 생기면 상위 구현체도 영향을 받게 된다.

![dip-top-down.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/dip-top-down.png)

### Dependency Inversion

![dio-di.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/dip-di.png)

- A와 B 사이에 Polymorphic interface를 추가한다.
- A와 B의 의존 관계를 바꾸어  
  런타임시 의존 관계는 하위 구현체로 흐르지만,  
  소스코드 의존성은 상위 타입으로 흐르도록 한다.
- B는 A가 아닌 Polymorphic Interface를 의존하며 구현한다.
- A는 Interface가 변경되지 않는 이상 변경에 자유롭다.  
  구체적인 하위 레벨 구현체로부터 상위 레벨 모듈의 변경을 보호한다.

### Plugins

![dip-plugins.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/dip-plugins.png)

- plugin(변경 가능한 하위 레벨 구현체)
    - Boundary를 Plugin Interface로 다룬다.  
      Boundary를 만들고자할 때 마다 어떤 의존성을 역전 시킬지 고심하자.
    - 의존성 역전이 SW 모듈 간의 경계를 만드는 수단이 된다.
    - Boundary를 교체하는 의존성의 방향은 항상 한 방향이어야 한다.  
      두 구현체가 있어도 의존성의 방향은 System을 봐야 한다.  
      System에서 소스코드 쪽으로 의존성이 생기면 하위 모듈 구현체가 변경 될때 System도 영향을 받는다.

### Plugins Architecture
Main should be a plugin to the rest of the applications.  
Main Partition은 App Partition의 플러그인이 되어야 하고,  
플러그인은 변경 가능해야 한다.

![dip-plugins-architecture.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/dip-plugins-architecture.png)

## The Inversion

![dip-inversion.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/dip-inversion.png)

- App이 Framework에 의존성을 갖는다.  
  하지만 Framework는 App에 대한 의존성을 갖지 않는다.
- Framework는 High Level Policy이고 App은 Low Level Details 이다.
- 위에서 말한 일반적인 Structured Design(High Level Module이 Low Level Module에 의존성을 갖는 설계)과는 반대의 구조이다.

### DIP는 OCP와 흡사하다.
인터페이스를 분리할 경우 UML 그림이 같아진다.
OCP는 확장이 필요한 행위를 Abstraction 한다.
DIP는 High Level이 Low Level에 의존성을 갖는 부분을 Abstraction 한다.
둘의 의도는 명백하게 다르다.
