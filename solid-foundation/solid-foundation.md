# SOLID Foundation
[백명석 - 클린 코더스 강의. SOLID Foundation](https://www.youtube.com/watch?v=HIWJ8sF8lO8) 을 보며 공부내용 정리.

## The Source Code is the Design
* 1992, "What is Software Design?" By Jack W. Reeves  
  -Q : 엔지니어가 생산하는 것은 무엇인가?  
  -A : 엔지니어는 Product를 만들기 위한 Document를 만든다.  
  -Q : 공학의 결과물이 Document일 경우 소프트웨어 공학의 결과물은 무엇인가?  
  -A : 실제로 실행되는 Binary Code가 결과물이다.
- Binary가 나오기 위해 작성하는 소스코드가 Document이다.  
  공학자가 만들어야하는 Document는 설계도인데, 소프트웨어 엔지니어가 만들어야하는 것은 소스코드이다.

## The Source Code is the Design
* 건물, 회로, 기계는 설계 비용이 저렴하지만, 생산이 진행된 후 수정 비용이 비싸다.
* 소프트웨어는 컴파일과 빌드 등 구축 비용이 저렴하지만, 소스코드를 작성하는 설계 비용이 비싸다.


> 우리에게 요구사항을 주는 기획자는 그 요구사항이 작동해야만, 본인이 원하는 것인지 확답할 수 있다.  
> 과거 건축에 기반한 전통적인 설계방식 프로세스로는 현대 소비자의 니즈를 만족할 수 없다.

## 개발자는 비용이 많이 드는 소스 코드 작성을 위해 좋은 설계를 해야 한다.
지금 작성하는 소스코드가 좋지 않은 코드인지 확인할 수 있는 3가지 징후가 있다
* rigidity : 단단함, 경직도
* Fragility : 부서지기 쉬움, 취약도
* Immobility : 부동성, 정지

이런 징후가 보일 경우, 그리고 짠 코드가 테스트 코드를 작성하기 어렵다면..
내가 짠 코드가 잘못 작성한 것이다.

## Design Smells - Rigidity(코드의 단단함, 경직도)
* 정의 : 시스템의 의존성으로 인해 변경하기 어려워지는 것
* Rigid하게 하는 원인
  - 많은 시간이 소요되는 테스트와 빌드
  - 전체 리빌드를 유발하는 아주 작은 변화
* 테스트와 리빌드 시간을 줄이면 Rigidity가 줄어들고 수정이 용이해짐

`Rigidity를 줄이려면 SOLID를 지키면 된다.`

## Design Smells - Fragility(코드의 취약도, 부서지기 쉬운 코드)
* 정의 : 한 모듈의 작은 수정이 다른 모듈에 영향을 미칠 때 코드가 취약하다
* 예를 들어 자동자 SW를 구현하는 도중, 라디오 버튼 작동 로직을 수정했는데 자동차 문이 영향을 받는 경우
* 해결책
  - 모듈간의 의존성을 제거한다.
  - 런타임 의존성은 그렇게 서비스가 흘러야 하기 때문에 어쩔 수 없다.  
    하지만 소스코드 의존성은 Polymorphism Interface를 통해 없앨 수 있다.

## Design Smells - Immobility(코드를 분리하지 못하는 상태)
* 정의 : 모듈이 쉽게 추출되지 않고 재사용 되지 않는 경우 부동성이 있다고 말한다.
* 예를 들어 로그인 모듈이 특정 DB의 스키마 혹인 특정 UI에 종속될 경우  
  이 로그인 모듈은 다른 시스템에서 재사용하지 못한다. Immobile 하다.
* 해결책
    * DB, UI, Framework 등 Details와 결합도를 없애야 한다.

## 추가적인 문제
### Viscosity(점성)
* 빌드/테스트 같은 필수 오퍼레이션들이 오래 걸려 수행이 어렵다면 그 시스템은 역겨운(disgust) 것이다.  
  * 체크인, 체크아웃, 머지 등은 비용이 크고 역겹다.   
    여러 레이어를 가로질러 의존성을 갖는 것은 역겹다.
  * 레이어드 아키텍처를 얘기할 때,  
    "상위 레이어는 하위 레이어를 호출할 수 있다"라는 말은 런타임의 의존성 방향을 얘기하는거지 소스 코드의 의존성 방향을 얘기하는게 아니다.
* 항상 같은 역겨움의 원인  
  * Irresponsible tolerance - 개발자의 무책임한 용인  
    개발할 때 이렇게 하면 안좋을텐데.. 지금 바쁘니 다음에 리팩토링 하지뭐..
* 강하게 Coupling(연관)된 시스템은 테스트, 빌드, 수정을 어렵게 한다.
* 해결책
  * Dependency는 유지한채로 decoupling 해야 한다. 
  * 런타임 의존성은 유지하지만, 소스코드 의존성은 Polymorphism Interface를 두어 연관성을 끊어준다.

### Needless Complexity(불필요한 복잡성)
> **SW 설계의 이슈**  
> 미래의 요구사항을 어떻게 다뤄야 하는가
* 현재 요구사항만 구현 VS 미래를 예측하여 구현  
  "나중에 이 구현이 확장될 수도 있으니.. 추상 클래스와 인터페이스로 분리하며 개발하면 도움이 될거야!"  
  하지만 확장 될 니즈가 안생기면 이런 설계는 복잡만 해질 뿐이다.  
  니즈가 생길때 리팩토링을 하면 inheritance 할지, delegate 할지 생각할 수 있다.  
  미리 예측을 하고 개발했다가 예측의 니즈가 없을 경우 나중에 수정하기 매우 어려워진다.  
  **미래를 예측하지말고 현재를 잘 하자.**
* 앞으로의 확장을 고려하며 설계할 경우
  * 시스템은 불필요하게 복잡해짐
  * 개발자는 현재를 제어할 수 없음
* 불필요한 복잡함 : 강한 Coupling
* 해결책
  * 현재의 요구사항에 집중하자.

> 진화적인 설계 - 먼저 디자인 하고 개발한게 아니라 리팩토링 하다보니 디자인이 나왔다!  
> 크리스탈 볼. 미래를 예측할 수 있는 마법의 수정구는 없다!
---
>OOP는 Depenency Inverted 이다. 레이어 간의 의존성이 역전되어야 한다.  
>런타임 의존성은 상위에서 하위로 가지만 소스코드 의존성은 하위에서 상위로 가야 한다.

# What is Object-Oriented?
* o.f(x) != f(o, x)  
  객체지향에서는 o.f(x)의 형태가 되어야 한다.
* Dynamic Polymorphism  
  Object-Oriented는 메세지를 전달하는 것 이다.  
  메세지를 호출하는 곳에선 내부가 어떻게 동작하는지 몰라야 하고 인터페이스가 요구 하는것만 전달해줘야 한다.

## Dependency Inversion이 Object-Oriented의 정수이다.
* Object-Oriented는 실세계를 똑같이 모델링 하는 것이다.  
  그 과정에서 Inheritance(상속), Encapsulation(캡슐화), Polymorphism(다형성)이 언급되는데,  
  이들은 OOP의 메커니즘이다. 핵심이 아니다.

> OOP의 핵심은 IoC를 통해 상위 레벨의 모듈을 하위 레벨의 모듈로부터 보호하는 것이다.  
> low level의 구현체가 변경이 되어도 high level의 타입은 영향을 받지 말아야 한다.

## Object-Oriented-Design
* Dependency Management  
  객체지향 설계는 의존성을 잘 관리하는 것이다.
* Inversion of key dependencies that isolate the high level policies from low level details  
  키 디펜던시를 역전시켜 하위레벨과 로우레벨을 격리 시켜야 한다.

## Dependency Management
의존성 관리에 대한 중요한 규칙 - SOLID 원칙
* [SRP : Single Responsibility Principle](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/srp.md)
* [OCP : Open Closed Principle](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/ocp.md)
* [LSP : Liskov Substitution Principle](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/lsp.md)
* [ISP : Interface Segregation Principle](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/isp.md)
* [DIP : Dependency Inversion Principle](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/dip.md)
