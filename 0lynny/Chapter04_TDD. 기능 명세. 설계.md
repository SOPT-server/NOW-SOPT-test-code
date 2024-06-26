## Chapter04. TDD. 기능 명세. 설계

어떤 형태로든 사용자에게 제공할 기능을 구현하려면 입력과 결과 두가지 기능으로 나누어볼 수 있다. 

3장에서 작성한 만료일 계산 기능의 입력과 결과는 다음과 같다.

- 입력 : 첫 납부일, 납부일, 납부액
- 출력 : 만료일

결과는 여러 형식으로 정의할 수 있는데 리턴값, 예외상황을 결과로 사용할 수 있다. 

설계는 기능 명세로부터 시작한다. 스토리보드를 포함한 다양한 형태의 요구사항 문서를 이용해서 기능 명세를 구체화한다. 기능 명세를 구체화하는 동안 입력과 결과를 도출하고 도출한 기능을 바탕으로 코드에 반영한다. 

앞서 계속해서 살펴봤듯이 테스트 코드 작성 과정은 다음과 같다.

테스트를 먼저 만들고 테스트를 통과시키기 위해 코드를 구현하고 리팩토링하는 과정을 반복한다. 테스트 코드를 만들기 위해서는 다음 두가지를 할 수 있어야한다.

- 테스트할 기능을 실행
- 실행 결과를 검증

테스트는 기능이 올바르게 동작하는 것인지 검증하는 것이므로 실행결과를 검증해야한다. 앞선 예제들에서 검증을 하기 위해 리턴 타입을 정했다. 테스트 코드를 작성하는 과정에서는 클래스 이름, 메서드 이름, 메서드 파라미터, 실행 결과를 고민해야하는데 이렇게 타입이 제공해야할 기능을 결정하는 것은 기본적인 설계 행위이다. TDD 자체가 설계는 아니지만 작성하는 과정에서 일부 설계를 진행하게 된다. 

TDD는 테스트를 통과할 만큼만 코드를 작성한다. 실제 테스트 사례를 추가하고 통과시키는 과정에서 필요한 만큼 설계를 변경한다. 

TDD로 개발할 때 필요한 만큼 설계를 한다고 해서 초반에 설계 활동을 생략하는 것은 아니다. 

### 기능 명세 구체화

테스트 코드를 작성하기 위해 개발자는 기능 명세를 정리해야한다. 

테스트코드를 작성하려면 파라미터와 결과 값을 정해야 하므로 개발자는 요구사항 문서에서 기능의 입력과 결과를 도출해야한다. 테스트 코드는 구체적인 입력과 결과를 이용해서 작성하므로 개발자는 예시를 통해 기능 명세를 구체화할 수 있다. 

복잡한 로직을 구현해야하는 것은 개발자이므로 최대한 예외적인 상황이나 복잡한 상황에 해당하는 구체적인 예시를 끄집어 내서 테스트를 진행하는 것이 좋겠다.
