# 테코 56

## JUnit 5

- JUnit 플랫폼 : 테스팅 프레임워크를 구동하기 위한 런처와 테스트 엔진을 위한 api제공
- JUnit Jupiter : junit 5를 위한 테스트 api와 실행엔진을 제공
- JUnit Vintage : 이전 버전으로 작성된 테스트를 JUnit 5 플랫폼에서 실행하기 위한 모듈

```groovy
dpendencies {
	testImplementation('org.junit.jupiter:junit-jupiter:5.5.0')//add junit jupiter
}
test {
	useJUnitPlatform()//use junit5 platform
	testLogging {
	events "passed", "skipped", "failed"
	}
}
```

다음과 같이 gradle 의존성 추가

하나는 `org.junit.jupiter.api.Assertions`이고, 하나는 `org.assertj.core.api.Assertions`이다.

강의를 듣거나 다른 사람들의 코드들을 보면, 보동 후자를 사용하곤 한다.

심지어 [JUnit5 공식 문서](https://junit.org/junit5/docs/current/user-guide/)에 가도 서드파티 라이브러리인 AssertJ 사용을 권장한다.

그래서 책에 나오는 junit의 jupiter의 Assertions를 공부하기 보다는 

Assertj의 Assertions의 함수들을 공식문서에서 찾아봤다.

assertEquals,

ass

```groovy
import static org.assertj.core.api.Assertions.*;

// basic assertions
assertThat(frodo.getName()).isEqualTo("Frodo");
assertThat(frodo).isNotEqualTo(sauron);

// chaining string specific assertions
assertThat(frodo.getName()).startsWith("Fro")
                           .endsWith("do")
                           .isEqualToIgnoringCase("frodo");

// collection specific assertions (there are plenty more)
// in the examples below fellowshipOfTheRing is a List<TolkienCharacter>
assertThat(fellowshipOfTheRing).hasSize(9)
                               .contains(frodo, sam)
                               .doesNotContain(sauron);

// as() is used to describe the test and will be shown before the error message
assertThat(frodo.getAge()).as("check %s's age", frodo.getName()).isEqualTo(33);

// exception assertion, standard style ...
assertThatThrownBy(() -> { throw new Exception("boom!"); }).hasMessage("boom!");
// ... or BDD style
Throwable thrown = catchThrowable(() -> { throw new Exception("boom!"); });
assertThat(thrown).hasMessageContaining("boom");

// using the 'extracting' feature to check fellowshipOfTheRing character's names
assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName)
                               .doesNotContain("Sauron", "Elrond");

// extracting multiple values at once grouped in tuples
assertThat(fellowshipOfTheRing).extracting("name", "age", "race.name")
                               .contains(tuple("Boromir", 37, "Man"),
                                         tuple("Sam", 38, "Hobbit"),
                                         tuple("Legolas", 1000, "Elf"));

// filtering a collection before asserting
assertThat(fellowshipOfTheRing).filteredOn(character -> character.getName().contains("o"))
                               .containsOnly(aragorn, frodo, legolas, boromir);

// combining filtering and extraction (yes we can)
assertThat(fellowshipOfTheRing).filteredOn(character -> character.getName().contains("o"))
                               .containsOnly(aragorn, frodo, legolas, boromir)
                               .extracting(character -> character.getRace().getName())
                               .contains("Hobbit", "Elf", "Man");
```

## 테스트 라이프 사이클

1. 테스트 메서드를 포함한 객체 생성
2. 존재하면 @BeforeEach annotation이 붙은 메서드 실행 ← 테스트를 실행하기 위한 준비 작업 ex. 객체 생성
3. @Test annotion이 붙은 메서드 실행
4. 존재하면 @AfterEach annotation이 붙은 메서드 실행  ← 테스트 이후 공통적으로 정리할 것이 있을때 ex. 파일 삭제

@BeforeEach, @AfterEach, @Test붙은 메서드 모두 private이상의 접근 제어자 

@BeforeAll, @AfterAll

한 클래스의 모든 테스트 메서드가 실행되기전에 beforeall붙은 정적 함수 한번 실행

모든 테스트 메서드를 실행한 뒤에 한번 실행

둘다 정적 메서드에만 붙일 수 있음

## 테스트 메서드 간 실행 순서의존과 필드 공유하지 않기

![스크린샷 2024-05-14 오후 7.27.17.png](%E1%84%90%E1%85%A6%E1%84%8F%E1%85%A9%2056%20f2d99553694a4465a1d5aa8ace418ce0/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-05-14_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_7.27.17.png)

테스트 메서드가 특정 순서대로 실행된다는 가정 하게 테스트 메서드를 작성하면 안 된다.

순서가 달라지면 순서를 고려한 상태로 작성한 테스트는 실패할 수도 있기 때문이다.

위의 예시는 위에서부터 순차적으로 테스트를 실행할거라는 가정하게 테스트 코드를 작성한 것이다.

순서대로 작동시키면 잘 되지만 아래 테스트부터하면 테스트에 실패한다.

테스트코드도 유지보수의 대상이다. 테스트 메서드 간에 의존이 생기면 테스트 코드의 유지보수 어려워진다.

각각의 테스트는 독립적으로 작동하도롥 작성

→ 테스트 메서드가 서로 필드를 공유한다거나 실행순서를 가정하고 테스트 작성하면 안 된다.

## 추가 annotation

@DisplayName : 메서드 이름만으로 테스트 내용을 설명하기에 부족할 때 사용

@DisplayName(”합이 같은지 테스트”)와 같이 사용

@Disabled :  해당 annotation이 붙은 클래스나 메서드는 테스트 실행 대상에서 제회된다.

## 테스트 코드의 구성 요소 : 상황 실행 결과 확인

각각 given when then

![스크린샷 2024-05-14 오후 7.36.25.png](%E1%84%90%E1%85%A6%E1%84%8F%E1%85%A9%2056%20f2d99553694a4465a1d5aa8ace418ce0/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-05-14_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_7.36.25.png)

숫자야구에서

정답이 456인 상황이 주어짐(given)

참여자가 정담을 456이라 했음(when)

그러면 strike는 3, ball은 0이 나와야한다.(then)

이렇게 테스트 마다 상황(given)을 설정할 수도 있지만..

만약 모든 테스트에서 공통적인 상황을 사용한다면 @BeforeEach를 통해 공통적인 상황들을 설정할 수도 있따.

```groovy
  @Test
    @DisplayName("사용자 정보를 입력하면 회원을 등록할 수 있다.")
    void save() {
        // given 해당 객체들이 있는 상황에서
        SOPT sopt = SOPT.builder()
                .part(Part.SERVER)
                .build();

        Member member = Member.builder()
                .age(99)
                .name("오해영")
                .sopt(sopt)
                .nickname("5hae0")
                .build();

        // when 다음을 실행
        Member savedMember = memberJpaRepository.save(member);

        // then 결과는??
        Assertions.assertThat(savedMember)
                .extracting("age", "name", "sopt", "nickname")
                .containsExactly(99, "오해영", sopt, "5hae0");

    }
```

## 외부 상태가 테스트 결과에 영향을 주지 않게 하기.

만약 회원 가입기능에서

1. 회원 가입 성공
2. 중복된 아이디 사용시 exception 발생

2개의 테스트를 해야 한다 가정해보자 그러면 테스트를 다음과 같이 짤 수 있다.

![스크린샷 2024-05-14 오후 7.50.07.png](%E1%84%90%E1%85%A6%E1%84%8F%E1%85%A9%2056%20f2d99553694a4465a1d5aa8ace418ce0/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-05-14_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_7.50.07.png)

하지만 이 테스트를 매번 성공적으로 수행하기 위해서는

1. 회원 가입 성공을 위해서는 데이터베이스에서 “bkchoi”를 삭제해 줘야하며
2. 중복id사용시 exception발생 test에서는 테스트 전에 “bkchoidup”를 데이터베이스에 미리 넣어줘야한다.

이런 외부환경(db)을 테스트에 맞게 구성하는 것은 어렵다.

테스트 대상이 아닌 외부 요인은 테스트 코드에서 다루기 힘들다.

이때 외부 요인을 사용하기 위해서 대역을 사용한다.

7단원에서 계속…