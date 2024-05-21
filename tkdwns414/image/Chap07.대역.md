# Chapter 7. 대역

## 대역의 필요성

test double (대역)
왜 필요한지
스텁 가짜 스파이 모의객체

## 대역을 이용한 테스트

구현 대체  
레포지토리 -> 메모리  
음...

대역을 이용해 외부 상황을 흉내낼 수 있다~!

## 대역의 종류

### 스텁(Stub)

구현을 단순한 것으로 대체, 텍스트에 맞게 단순히 원하는 동작을 수행

그냥 단순하게 코드가 통과하게 가짜로 코드 짜는 것에 가까움  
예를 들어서 회원가입시 비밀번호 강도가 약해서 가입을 못하는 것을 검사하고 싶을 때 비밀번호 강도가 약하다는 것을 반환해줄 메소드를 대충 만들 수 있음

### 가짜(Fake)

제품에는 적합하지 않지만 실제 동작하는 구현을 제공, DB 대신 메모리를 이용해서 구현한 Repository가 그 예시

### 스파이(Spy)

호출된 내역을 기록, 기록한 내용은 테스트 결과를 검증할 때 사용, 스파이는 스텁이기도 함

### 모의(Mock)

기대한 대로 상호작용하는지 행위를 검증, 기대한 대로 동작하지 않으면 익셉션을 발생할 수 있음, 모의 객체는 스텁이자 스파이도 됨

#### Mockito

Mockito를 이용해 모의 객체를 생성할 수 있음. 스프링 부트 테스트에서도 사용될 수 있음 ex) @Mock, InjectMocks

```java
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRegister memberRegister;

    @Mock
    private MemberRetriever memberRetriever;

    @Mock
    private MemberUpdater memberUpdater;

    @Mock
    private MemberRemover memberRemover;


    @Test
    void getMemberById() {
        BDDMockito.given(memberRetriever.findById(1L)).willReturn(
                Member .builder()
                        .age(99)
                        .name("오해영")
                        .nickname("5hae0")
                        .build()
        );

        Assertions.assertThat(memberService.getMemberById(1L))
                .extracting("age", "name", "nickname")
                .containsExactly(99, "오해영", "5hae0");
    }

}
```

### 대역과 개발 속도

TDD 과정에서 대역을 사용하지 않고 실제 구현을 한다면 외부 상황에 따라 대기하거나 해결하지 못하는 일들이 생기게 된다. 그렇기 때문에 대역을 사용하는 것은 전체적으로 개발 진행도를 빠르게 높일 수 있는 방법이다.

외부 상황 외에도 다른 개발자가 맡고 있는 부분에 대해서 그 개발자가 구현을 마칠 때까지 기다리지 않아도 되며 DB가 없어도 DB가 있을 떄와 비슷한 가짜 DB를 만들어 상황을 검사할 수 있다.

### 모의 객체의 과한 사용

모의 객체를 과하게 사용할 경우 테스트 코드가 오히려 복잡해질 수 있다. 대역이 필요할 때는 대역 클래스를 생성하지 않아도 된다는 눈 앞의 당장 편리함에만 초점을 맞춰 바로 모의 객체를 사용하지 말고 다른 대역에 대해서도 생각을 잘 해야한다.
