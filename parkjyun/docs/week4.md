# chap 7

# 대역

## 대역의 필요성

외부 요인을 대역으로 설정해보자

1. 테스트 대상에서 파일 시스템을 사용
2. 테스트 대상에서 디비로부터 데이터 조회 or 데이터 추가
3. 테스트 대상에서 외부의 http 서버와 통신

tdd는 테스트 작성 → 통과시킬 만큼 구현 → 리팩토링의 과정으로 반복

외부 요인들은 테스트 작성 어렵게 만들고 테스트 깨질 가능성 높임

tdd를 안 하고 테스트 코드만 작성해도 외부 요인에 의해 테스트가 깨지면 안 된다.

![스크린샷 2024-05-14 오후 8.05.22.png](chap%207%20bf32f662423b476d8c22a8a3d52ac01c/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-05-14_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.05.22.png)

대역을 사용하지 않는다면 다음과 같은 코드에서 외부 서버에서 테스트 목적의 유효한 카드 번호를 받아야함.

게다가 외부 업체에 등록된 카드의 유효기간이 한달 후에 지나면 해당 테스트 실패 ← 외부 요인에 의존적

## 대역을 이용한 테스트

![스크린샷 2024-05-14 오후 8.23.18.png](chap%207%20bf32f662423b476d8c22a8a3d52ac01c/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-05-14_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.23.18.png)

책에서는 단순히 외부 서버와 연결하는 대신 미리 지정한 카드번호와 다르면 invalid를 반환하도록 구현했다.

마찬가지로 test를 작성해나가며 도난카드를 처리하는 test를 만들었다면 

StubCardNumberValidator에 CardValidity.Theft를 반환하도록 추가해주고 테스트 코드를 작성하면 된다.

외부 데이터베이스도 외부 서버와 마찬가지로 대역을 사용하기에 적합하다.

다음과 같이 실제 데이터베이스와 통신하는 AutoDebitInfoRepository의 대역인 MemoryAutoDebitInfoRepository를 만들어 대역으로 사용할 수 있다.

디비 대신 맵을 사용해서 정보를 저장한다.

![스크린샷 2024-05-14 오후 8.28.35.png](chap%207%20bf32f662423b476d8c22a8a3d52ac01c/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-05-14_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.28.35.png)

## 대역을 사용한 외부 상황 흉내와 결과 검증

위에서 

SubCardNumberValidator를 사용하여 외부 카드 정보 api의 대역으로 사용했다.

이 클래스는 카드 정보 api를 대신해서 유효한 카드 번호, 도난 카드번호와 같은 상황을 흉내낸다.

MemoryAutoDebitInfoRepository를 사용하여 자동이체 정보를 저장한 DB의 대역으로 사용했다.

특정 사용자에 대한 자동이체 정보가 이미 등록되어 있거나 등록되어 있지 않은 상황을 흉내 낸다.

→ 대역을 사용하여 외부에 대한 결과를 검증 가능

## 대역의 종류

- stub : 구현을 단순한 것으로 대체. 테스트에 맞게 단순히 원하는 동작을 수행. 위의 stubcardnumbervalidator가 스텁에 해당
- fake : 실제 동작하는 구현을 제공, 디비 대신 실제 동작하는 memoryAutoDebitInfoRepository가 fake에 해당
- spy : 호출된 내용을 기록. 기록한 내용은 테스트 결과를 검증할 때 사용한다.
- mock : 기대한 대로 상호 작용하는지 행위를 검증한다. 기대한 대로 동작하지 않으면 exception을 발생할 수 있다. 스텁이자 spy

mock vs spy

Mock은 실제 객체를 사용하지 않고, 테스트를 위해 완전히 가상의 객체를 생성합니다. 반면, Spy는 실제 객체를 모방하면서, 특정 메소드의 호출 정보를 기록합니다. Spy는 실제 객체의 동작을 부분적으로 사용하고 싶을 때 유용합니다.

mock

컨트롤러를 검증하는 단위테스트에서 memberService를 목으로 등록해서

우리가 원하는 동작을 설정한다.

1. post /api/member로 요청을 보내면
2. status 로 201
3. 헤더에 Location헤더가 들어올 것이다.

책에서 설명한 대로 우리는 컨트롤러 이후의 로직에 대해 우리가 원하는 대로 수행하도록 정하고 이에 대한 test를 진행한다.

```java

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest extends ControllerTestManager {

    @MockBean
    private MemberService memberService;
    @Test
    @DisplayName("신규 회원을 등록한다.")
    void createMember() throws Exception {

        // given
        when(memberService.create(any(MemberCreateRequest.class)))
                .thenReturn("/api/member/1");

        MemberCreateRequest request = new MemberCreateRequest(
                        "오해영",
                        "5hae0",
                        28,
                        SOPT.builder()
                                .part(Part.DESIGN)
                                .build()
                );

      // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/member")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/member/1"))
        ;
    }
}
```

## 상황과 결과 확인을 위한 협업 대상 도출과 대역 사용

제어하기 힘든 외부 상황(ex. 외부 서버, 디비)이 존재하면 다음과 같은 방법으로 의존을 도출하고 이를 대역으로 대신하면 된다.

- 제어하기 힘든 외부 상황을 별도 타입으로 분리
- 테스트 코드는 별도로 분리한탕비의 대역을 생성
- 생성한 대역을 테스트 대상의 생성자 등을 이용해서 전달
- 대역을 이용해서 상황 구성

## 대역과 개발 속도

tdd 과정에서 대역을 실제 구현을 사용한다면 다음과 같은 일들이 벌어진다.

- 외부 서버를 사용한다면 해당 서버에서 테스트하기 위한 값을 받기 위해 대기해야함
- 외부 서버를 사용한다면 테스트 코드가 외부 서버의 변경에 대응해야하낟
- 메일 같은 기능을 테스트 하려면 메일함에 메일이 올때까지 대기해야함
- 테스트하고자 하는 기능을 다른 개발자가 구현하고 있다면 해당 기능이 구현되기 전까지 테스트 불가

→ 대역을 사용하면 의존하는 대상을 실제 구현 없이도 결과를 바로 알 수 있기에 개발 속도 향상

## 모의 객체를 과하게 사용하지 않기