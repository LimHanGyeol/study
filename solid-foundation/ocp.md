# OCP(Open - Closed Principle)
개방 폐쇄의 원칙

## Open and Closed
확장에 대해선 열려있고, 변경에 대해선 닫혀있다.  
Open for Extension But Closed for modification.  
확장이란 새로운 타입을 추가함으로써 새로운 기능을 추가할 수 있다.  
확장이 일어날 때 상위 레벨이 영향을 받지 말아야 한다.

OCP를 준수하면 모듈의 변경 없이 손쉽게 기능을 추가할 수 있다.  
OCP를 하기위해 필요한 것은 추상화와 Inversion 이다.  
상위레벨의 모듈이 하위레벨 모듈에 의존성을 갖게 되는 경우,  
상위레벨과 하위레벨 사이에 추상 인터페이스 & 다형성 인터페이스를 두어 의존 관계를 분리한다.

### Copy 예
Copy라는 상위 모듈이 있고, 이를 구현하는 하위 구현체 디바이스가 있다.  
디바이스가 추가될 경우 각 타입을 확인하는 조건문이 늘어나게 되는데  
여기서 추상 인터페이스를 추가하여 의존 관계를 분리한다.

해당 작업을 하게되면 이후
- 디바이스가 추가 되어도 해당 디바이스를 담당하는 구현 클래스만 추가 (Open for Extension)
- Copy 로직의 수정은 발생하지 않는다. (Closed for modification)

### POS 예
![ocp-pos-ex1.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/ocp-pos-ex1.png)
- 현찰을 받는 경우 잘 동작 한다.
- 신용카드를 받고 싶을 경우에는 확장을 해야 한다.  
  신용카드 외에 추가 요구사항이 생길 경우 if - else가 계속해서 추가되게 된다.  
  그럴 경우 Fan out Problem이 발생한다.  
  여러 메서드에서 문어발식 구현이 되어 그 중 하나만 변경되더라도 모든 기능에 영향이 끼쳐진다.  
  굉장히 Fragiled 해진다. 깨지기 쉬워진다.

![ocp-pos-ex2.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/ocp-pos-ex2.png)

- OCP 위반  
  확장을 위해 소스를 수정한다.

![ocp-pos-ex3.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/ocp-pos-ex3.png)

- 해결책  
  확장이 필요한 행위를 Abstraction한다.  
  그리고 추상화한 인터페이스를 외부에서 주입해준다.
  그러면 결과적으로 OCP를 준수하게 된다.

![ocp-pos-ex4.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/ocp-pos-ex4.png)

- 의존성 변화에 주목하자
    - CheckOut 알고리즘은 구현체에 의존하지 않는다.
    - PaymentMethod의 구현체는 Abstraction(PaymentMethod)에 의존한다.  
      기능이 추가 될 경우 PaymentMethod의 구현체만 추가하면 된다.
    - CheckOut 모듈의 **수정 없이**, PaymentMethod를 **확장 할 수 있다**.

## Is This Possible?
- OCP를 준수하면 Modification을 완벽하게 제거할 수 있을까?
    - 이론적으로는 가능하지만 실용적이지 않다.
    - 2 Problems
        - Main Partition
          Main에서 의존성 주입을 해줄 경우,
          if - else로 구성된 팩토리 패턴의 구현이 있을 수 밖에 없다.
        - Crystal ball problem (미래를 볼 수 있는 마법의 수정구)
          우리는 미래를 예측할 수 없다.
          확장을 위해 모든 인터페이스를 다 준비하는 것은 불가능하다.
## The Lie
- 고객이 기존 기능 관련된 **새로운 기능을 요구하면 대책이 없다**.
- 아무도 이런 얘길 해주지 않았다.  
  **만일 미리 얘기해줬다면** 이런 요구사항을 수용할 수 있도록 **Abstraction을 적용했을 것**이다.
- 내가 **알았더라면 OCP를 준수하도록** 했을텐데.  
  새로운 기능을 수정없이 확장할 수 있도록 설계했을 것이다.
  **내가 미리 알았다면 말이다.**
- 그럼 **OCP는 앞으로 어떤 확장이 필요할지 알아야만 제대로 할 수 있다는 말일까?**
- 당신이 아무리 잘 찾고, 잘 예측해도  
  고객은 **반드시 당신이 준비하지 못한 것에 대한
  기능 추가/변경을 요구**할 것이다. - **Unknown Unknowns**
- 미래의 변경으로부터 보호 받도록 Abstraction을 적용하여 설계하는 것은 쉽다.  
  만일 미래에 어떤 변경이 있을지 알 수 있다면 말이다.  
  하지만 우리는 그런 미래를 알 수 있는 **Crystal ball**이 없다.
- 고객은 우리가 놓치고 준비하지 않은 부분에 대해서 변경을 요구하는 능력을 갖고 있다.
- 이게 사람들이 말하기를 꺼려하는 OCP, Object Oriented Design에 대한 하나의 비밀이다.  
  OCP, OOD는 당신이 미래를 예측할 수 있을 때만 해당 기능을 보호할 수 있다.

## Two Solutions
- **미래는 예측하지못한다.**  
  **완벽한 선견지명이 필요하면 객체지향의 장점은 무엇인가..?**  
  지난 시간동안 SW 산업은 이 문제와 투쟁해왔다.
- 이런 노력의 일환으로 **Crystal Ball의 필요성을 제거하기 위한 2가지 주요 접근법**이 만들어졌다.

1. Big Design Up Front(BDUF) - 친밀하지만 과도한 설계
    - 조심스럽게 고객과 문제 영역을 고찰한다.
    - 고객의 요구사항을 예측하여 도메인 모델을 만든다.
    - OCP가 가능하도록 도메인 모델에 추상화를 적용한다.
    - 변경될 가능성이 있는 모든 것들에 대한 청사진을 얻을 때까지 이 일을 반복한다.
    - 문제
        - 대부분의 경우 필요하지 않은 추상화로 도배된 매우 크고,
          무겁고 복잡한 쓰레기 설계를 만든다.
        - 추상화는 유용하고 강력한 만큼 비용도 크다.
2. Agile Design
    - 실용적이고, 반응을 하는 방법.
    - 가장 좋은 예시법은 은유법(메타포)이다.
        - 일련의 병사들이 적군의 사격에 포위됐다.
        - 총탄이 난발하고 있는 가운데 참호에 숨어있다.
        - 적을 향해 집중 사격할 수 있으면 전투에서 승리한다.
        - 문제는 적이 어디에 있는지 모른다.
        - 어떤 방향에서 적들이 총을 쏘는지 모른다.
          만일 일어나서 방향을 살펴보려고 한다면 적을 찾고 겨냥하기 전에 총을 맞을 것 이다.
        - 그래서 상사가 실행 가능한 결정을 내린다.
        - 한명의 병사를 일어나게 하여 총알이 날아온 방향을 파악한다.
        - Agile Design은 이런 것 이다.
        - **최대한 빨리 고객의 요구사항을 끌어낼 수 있는 가장 단순한 일을 한다.**
        - 그럼 **고객은 그 결과물에 대해 요구사항 (변경)을 시작한다.  
          나의 물질적인 손실이 있어야만 사용자의 요구사항을 최대한 빨리 알 수 있다.**
        - 그럼 **어떤 변경이 요구되는지 알게된다.  
          고객이 지금 요구한 것 만 최대한 빠르게 구현하여 넘겨주자.
          그때가서 확장을 하여 안전하게 하자.**

## Agile Design
- **변화에 대한 가장 좋은 예측은 변화를 경험하는 것**이다.
- 발생할 것 같은 변화를 발견한다면 향후 해당 변화와 같은 종류의 변화로부터 코드를 보호할 수 있다.
- 고객이 요구할 모든 종류의 변경을 완벽하게 예측하고
  이에 대한 변경에 대응하기 위해 Abstraction을 적용하는 대신에
- **고객이 변경을 요구할 때까지 기다리고 점진적으로 Abstraction을 만들어
  향후 추가적으로 재발하는 변화로부터 보호될 수 있도록 하라.**
- Agile Desinger는 **주단위 정도로 간단한 뭔가를 Deploy(Deliver)** 한다.
- 고객이 변경을 요구하면 Agile Designer는 코드를 리팩토링해서
  그런 종류의 변경을 쉽게 할 수 있도록 OCP를 준수하는 Abstraction을 추가한다.

## Agile Design In Practice
- 우리는 실제로 BDUF와 Agile 두 극단 사이에 살고 있다.
  **BDUF를 피해야 하지만 No DUF도 피해야 한다**.
- 시스템에 대해서 사고하고 Decoupled 모델을 **사전 설계**하는 것은 분명 가치있는 일이다.
- 하지만 **간단하고 적은 면**에서 의미있다.
- 우리의 목적은 시스템의 **기본 모양을 수립**하는 것이지 모든 **작은 상세**까지 수립하는 것은 아니다.
- 문제에 대해서 과하게 생각하면 유지보수 비용이 높은 많은 **불필요한 추상화**를 만들게 된다.
- **빨리 자주 Deploy**(Deliver)하고, **고객의 요구사항 변화에 기반하여 리팩토링** 하는 것은 매우 가치있다.
- 이럴 때 **OCP가 진가를 발휘**한다.
- 하지만 **간단한 도메인 모델없이 이렇게 진행하면 방향성 없는 혼란한 구조**를 유발할 수 있다.

## Reprise
- 마술이 아니라 공학이다.
- OCP를 완벽하게 준수하는 것은 불가능하다.
  모든 것을 생각해 낼 수는 없다.
- 아무리 철저히 규칙을 준수하고 조심해도 결국 고객은 시스템 전반에 걸친
  대대적인 수정이 필요한 변경을 생각해 낼 것이다.
- 개발자의 목적은 변경의 고통을 완전히 제거하는 것이 아니다.
  이것은 불가능하다.
  **개발자의 목적은 변경을 최소화하는 것** 이다.
  이게 **OCP를 준수하는 디자인이 당신에게 주는 이점**이다.
- OCP는 시스템 아키텍처의 핵심이다.
- **변경으로부터 완벽한 보호를 얻을 수는 없지만, 얻기위해 투쟁할 필요는 있다.**