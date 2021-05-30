# Interface Segregation Principle
인터페이스 분리의 원칙
- Don't depend on things that you don't need
- 사용하지 않지만 의존성을 갖고 있다면..  
  예를들어 100개의 인터페이스 중 A가 30개를 쓰고, B가 30개를 쓴다면  
  각각 70개의 인터페이스는 의존만 하고 사용하지 않는다.  
  하지만 자기가 사용하지 않는 인터페이스가 변경되어도 A와 B는 변경의 영향을 받는다.  
    - 그 인터페이스가 변경되면 재컴파일/빌드/배포가 된다.
    - 독립적인 개발/배포가 불가능하다는 의미이다.
- ISP는 SRP와도 연관이 된다.  
  예를들어 100개의 의존성 중 30%는 A Method가 사용하고 30%는 B Method가 사용한다.  
  그러면 그 클래스는 2개의 Responsibility를 갖고 있는 것이다.  
  한 기능에 변경이 발생하면 다른 기능을 사용하는 클라이언트 코드들에게도 영향이 미친다.
- 사용하는 기능만 제공하도록 인터페이스를 분리함으로써 한 기능에 대한 변경 여파를 최소화 해야 한다.  
  SRP를 잘 준수하기 위해서 ISP를 적용할 수 있다.
- 클라이언트 코드 입장에서 인터페이스를 분리하라는 원칙이다.

## Fat class 예제

![isp-ex1.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/isp-ex1.png)

- Job 클래스가 너무 많은 일을 하고 있어 많은 Fan In이 발생한다.
- 각 서브 시스템은 서로 다른 이유로 Job에 의존하고 있다.
- 문제점
    - rebuild에 많은 시간 소요
    - 독립적인 배포/개발 불가능
- 해결책
    - One Interface for a sub system
    - 어떤 인터페이스에 변경이 발생하면  
      Job 클래스와 해당 인터페이스를 사용하는 서브 시스템만 영향을 받고 rebuild 된다.  
      Job이 변경되어도 나머지 클라이언트는 인터페이스에 의해 보호된다.  
      하지만 이럴 경우 각 클라이언트마다 인터페이스를 추가해줘야 하는 너무 많은 변경이 생긴다.

![isp-ex2.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/isp-ex2.png)

## Fat class를 만나면
- Interface를 생성하여 Fat Class를 클라이언트 코드로부터 isolate 시켜야 한다.
- Fat Class에서 다수의 Interface를 구현한다.
- Interface는 구현체보다는 클라이언트 코드와 논리적으로 결합되므로,  
  클라이언트 코드가 호출하는 메서드만 Interface에 정의되었다는 것을 확신할 수 있다.  
  **ISP 준수**
- ISP를 준수하여 특정 Interface의 변경으로 인한 다른 클라이언트 코드의 영향을 없애면
    - 재컴파일/빌드를 줄일 수 있다.
    - 클라이언트 코드들을 다른 독립된 컴포넌트에 배치할 수 있다.  
      (클라이언트 코드 + Interface가 배치 단위이다.)
    - 독립적으로 개발 및 배포 가능하다.
