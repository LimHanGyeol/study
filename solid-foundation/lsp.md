# Liskov Substitution Principle
리스코브 치환의 원칙

### 상위 모듈과 하위 모듈이 구현되어 있을 때
그 두 타입은 대체되어도 작동에 문제가 없어야 한다.  
리스코브 치환의 원칙은 다형성의 다운 캐스팅을 사용하지 말자를 주장한다.  
다운 캐스팅을 사용할 경우 타입 의존성이 생긴다.  
타입에 대한 의존성은 아주 강한 의존성이다. 절대 잊지 말자.

Substitution을 치환이라고 말한다.  
객체지향의 사실과 오해에서는 Substitution을 대체 가능으로 인식한다.  
유념하여 이해하면 도움 될 것이라고 생각 한다.
## OCP vs LSP
- **OCP**  
  Abstraction, Polymorphism(inheritance)를 이용하여 구현한다.
- **LSP**
    - OCP를 받쳐주는 Polymorphism에 대한 원칙을 제공한다.  
      (Subtype과 Supertype은 대체 가능해야 한다.)
    - LSP가 위반되면 OCP도 위반된다.
    - LSP를 위반하면 Subtype이 추가될 때마다
      이를 사용하는 클라이언트 코드들이 수정되어야 한다.
    - instanceof, downcasting을 사용하는 것은 전형적인 LSP 위반의 징후이다.

## 정사각형, 사각형 문제

- Rectangle 예제
    - Rectangle은 시스템의 여러 곳에 퍼져있다.
    - 정사각형(Square)을 서브 타입으로 추가하려고 한다.
    - Square IS - A Rectangle, 정사각형은 사각형이다. 상속관계로 표현할 수 있다.
        - 하지만 width, height에서 한계가 있다.  
          width를 입력할 경우 height와 맞춰주어야 하고,  
          height를 입력할 경우 width를 맞춰주어야 한다.  
          이를 편하게 구현할 경우 instanceof 를 사용하게 될 수 있다.  
          아래와 같이 구현할 경우 LSP 위반이다.

![lsp-rectangle-ex.png](https://github.com/LimHanGyeol/study/blob/%2312/solid-foundation/solid-foundation/image/lsp-rectangle-ex.png)

## The Representative Rule
- 대리인은 자신이 대리하는 어떤 것들에 대한 관계까지 대리(공유)하지 않는다.
- 변호사(대리인)들이 의뢰자의 관계를 대리(공유)하지 않는 것 처럼 말이다.
- 따라서 기하학에 따르면 Square IS - A Rectangle 이지만  
  이들을 표현/대리(represent)하는 SW는 그들의 관계 (IS - A)를 공유하지 않는다.