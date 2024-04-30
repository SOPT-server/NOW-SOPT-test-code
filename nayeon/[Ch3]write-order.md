# [Test Code Writing Order]
## 테스트 코드 작성 순서
### 복잡한 테스트부터 시작한다면
2장에서 살펴보았던 테스트 코드 작성 예시에서, 아래와 같은 순서로 테스트 코드를 작성한다고 해보자.
- 대문자 포함 규칙만 충족하는 경우
- 모든 규칙을 충족하는 경우
- 숫자를 포함하지 않고 나머지 규칙은 충족하는 경우

대문자 포함 규칙만 충족하는 경우, 아래와 같은 테스트 코드를 작성할 수 있다.
```java
@Test
void meetsOnlyUpperCreteria_Then_Weak() {
    PasswordStrengthMeter meter = new PasswordStrengthMeter();
    PasswordStrength result = meter.meter("abcDef");
    assertEquals(PasswordStrength.WEAK, result);
}
```

모든 규칙을 충족하는 경우, 아래와 같은 테스트 코드를 작성할 수 있다.
```java
@Test
void meetsAllCriteria_Then_Strong() {
    PasswordStrengthMeter meter = new PasswordStrengthMeter();
    PasswordStrength result = meter.meter("abcDef12");
    assertEquals(PasswordStrength.STRONG, result);
}
```
위 테스트를 통과하기 위해, 어떤 규칙부터 충족해야 할 지 선택해야 한다.
모든 경우에 대해 알맞은 강도 타입을 반환하기 위해 어떤 규칙부터 고려해야 좋을지 고민하는 시간이 필요하다.

### 쉬운 테스트 코드부터 작성하기
가장 구현하기 쉬운 테스트부터 작성하는 경우를 보자.
여기서 가장 쉬운 경우는 아래 두 가지이다.
- 모든 조건을 충족하는 경우
- 모든 조건을 충족하지 않는 경우

모든 조건을 충족하는 경우부터 시작해보자.
```java
@Test
void meetsAllCriteria_Then_Strong() {
    PasswordStrengthMeter meter = new PasswordStrengthMeter();
    PasswordStrength result = meter.meter("abcDef12!");
    assertEquals(PasswordStrength.STRONG, result);
}
```
위 테스트를 통과하기 위해, 아래와 같은 코드를 작성할 수 있다.
```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        return PasswordStrength.STRONG;
    }
}
```

이제 다음으로 쉬운 조건을 테스트해보자.
- 모든 조건을 충족하지 않는 경우
- 한 규칙만 충족하는 경우
- 두 규칙만 충족하는 경우

앞서 모든 규칙을 충족하는 경우에 대한 테스트를 진행했다.
그런데 갑자기 모든 조건을 충족하지 않는 경우를 테스트하려면, 모든 조건 충족 여부에 대한 경우를 나누어야 한다.
따라서 두 가지 규칙만 충족하는 경우를 선택할 수 있다.

- 대문자 포함 규칙을 제외하고 충족하는 경우
- 8글자 이상 규칙을 제외하고 충족하는 경우
- 숫자 포함 규칙을 제외하고 충족하는 경우

위 조건 중에서 가장 쉬운 조건은, 문자열 검사 없이도 확인할 수 있는 글자 수 규칙일 것이다.
따라서 8글자 미만이면서 다른 규칙들은 충족하는 경우에 대한 테스트를 작성한다.

```java
@Test
void meetsOtherCriteria_except_for_Length_Then_Normal() {
    PasswordStrengthMeter meter = new PasswordStrengthMeter();
    PasswordStrength result = meter.meter("abcDef1");
    assertEquals(PasswordStrength.NORMAL, result);
}
```

그 후 아래와 같은 코드를 작성하여 위 규칙에 대해 NORMAL을 반환하도록 한다.
```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }
}
```
**이렇게 가장 쉬운 규칙부터 시작하여 하나씩 테스트를 확장하는 식으로 테스트를 작성하면,
테스트를 통과하기 위해 당장 구현해야 하는 것이 명확**하기 때문에, 더 수월하게 개발할 수 있다.

### 예외 상황을 먼저 테스트
예외 상황이 복잡할 수록, 여러 개의 if-else 문을 사용해야 한다.<br>
예외 상황을 고려하지 않고 코드를 구현했을 때, 뒤늦게 예외 상황에 대처하기 위해 코드를 수정하다 보면 복잡성이 높아지기 쉽고, 그만큼 버그가 발생할 가능성이 높아진다.<br>
TDD를 하면서 예외 상황을 미리 고려하면 버그를 예방하고 더 좋은 코드를 구현할 수 있다.
<br><br>
실제로 운영되는 서버에 NPE가 발생한다면, 동작이 멈추는 상황으로 이어질 수도 있다.
<br>
**NPE와 같은 아주 작은 예외도, 큰 문제로 이어질 수 있으므로 예외 사항을 항상 염두에 두고 코드를 작성하는 것이 중요하다.**

### 완급 조절
TDD를 처음 시작할 때에는, 한 번에 얼만큼의 코드를 작성해야 하는지 모를 수도 있다.
<br>
1. 정해진 값을 반환
2. 값 비교를 통해 정해진 값 반환
3. 테스트 추가하면서 일반화

위의 세 단계를 거치면서 구현하는 연습을 하면, 구현을 하려다가 막히는 경우가 생겨도 금방 대처할 수 있다.
<br>
한 번에 많은 것을 구현하려고 하다 보면, 조금만 막혀도 쉽게 실패하게 된다.

### 지속적인 리팩토링
테스트를 통과하고 나면 계속해서 리팩토링을 진행해야 한다.
중복 코드를 제거하는 것은 대표적인 리팩토링이다.
<br>
중복되거나 복잡한 코드를 메소드로 분리하는 것도 대표적인 리팩토링 전략이다.
<br><br>
동작하는 코드를 작성하는 것이 최우선이지만, **소프트웨어의 생존 시간이 길어질 수록 유지보수에 용이한 코드 작성의 중요성은 높아진다.**

## 테스트 작성 순서 연습
유료 구독 서비스에 대한 테스트를 작성해보자.
- 서비스 사용을 위해 매달 1만원을 선불한다.
- 2개월 이상 요금을 납부할 수 있다.
- 10만원을 결제하면 1년 동안 서비스를 이용할 수 있다.

### 쉬운 것부터 테스트
테스트를 추가할 때에는 아래 두 가지를 고려해야 한다.
1. 가장 쉬운 테스트부터 시작
2. 예외 상황을 먼저 테스트

먼저, 1만원을 납부하면 납부일 기준으로 한 달 뒤로 만료일을 계산하는 조건을 테스트해보자.
```java 
@Test
@DisplayName("1만원 납부하면 한 달 뒤가 만료일이 됨")
void payment_Then_ExpireDate() {
    LocalDate billingDate = new Long(2019, 3, 1);
    int payAmount = 10_000;
    
    ExpiryDateCalculator cal = new ExpiryDateCalculator();
    LocalDate expireDate = cal.calculateExpiryDate(billingDate, payAmount);
    
    assertEquals(LocalDate.of(2019, 4, 1), expireDate);
}
```
>메소드 명만으로는 어떤 기능을 테스트하는지 알기 어려울 때가 있다.
한글로 메소드 명을 작성하는 것도 좋은 방법일 수 있지만, `@DisplayName` 어노테이션을 사용하는 방법도 있다.
>
>한글로 메소드 명을 작성하는 경우를 생각해보자.
> ```java
> @Test
> @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
> void 납부하면_한달_뒤가_만료일() {
>   ...
> }
> ```
> 이렇게 DisplayNameGenerator를 사용하면, 테스트 실행 시 테스트 메소드 명의 _(언더바)를 제거하여 보여준다.<br>
> (ex. 납부하면_한달_뒤가_만료일 -> 납부하면 한달 뒤가 만료일)<br>
> (참고 글: https://covenant.tistory.com/270)

테스트를 통과하기 위해 아래와 같은 코드를 작성할 수 있다.

```java
public class ExpiryDateCalculator {
    public LocalDate calculateExpiryDate(LocalDate billingDate, int payAmount) {
        return LocalDate.of(2019, 4, 1);
    }
}
```
테스트를 통과하는 정해진 값을 반환하는 것을 고려하여 상수를 사용한 코드를 작성하였다. 

### 예를 추가하면서 구현 일반화
예를 더 추가하여 테스트를 작성해보자.

```java
@Test
@DisplayName("1만원 납부하면 한 달 뒤가 만료일이 됨")
void payment_Then_ExpireDate() {
    LocalDate billingDate = new Long(2019, 3, 1);
    int payAmount = 10_000;

    ExpiryDateCalculator cal = new ExpiryDateCalculator();
    LocalDate expireDate = cal.calculateExpiryDate(billingDate, payAmount);

    assertEquals(LocalDate.of(2019, 4, 1), expireDate);

    LocalDate billingDate2 = LocalDate.of(2019, 5, 5);
    
    ExpiryDateCalculator cal2 = new ExpiryDateCalculator();
    LocalDate expireDate2 = cal2.calculateExpiryDate(billingDate2, payAmount);
    
    assertEquals(LocalDate.of(2019, 6, 5), expireDate2);
}
```

테스트를 통과하기 위해 if 절로 구현하여 각 경우에 맞는 상수 값을 반환하도록 할 수도 있지만,<br>
바로 구현을 일반화할 수 있다.
```java
public class ExpiryDateCalculator {
    public LocalDate calculateExpiryDate(LocalDate billingDate, int payAmount) {
        return billingDate.plusMonths(1);
    }
}
```

### 중복 제거
테스트를 통과했으니 중복을 제거해야 한다.
```java
@Test
@DisplayName("1만원 납부하면 한 달 뒤가 만료일이 됨")
void payment_Then_ExpireDate() {
    // 중복 1 시작
    LocalDate billingDate = new Long(2019, 3, 1);
    int payAmount = 10_000;

    ExpiryDateCalculator cal = new ExpiryDateCalculator();
    LocalDate expireDate = cal.calculateExpiryDate(billingDate, payAmount);

    assertEquals(LocalDate.of(2019, 4, 1), expireDate);
    // 중복 1 끝

    // 중복 2 시작
    LocalDate billingDate2 = LocalDate.of(2019, 5, 5);
    int payAmount2 = 10_000;
    
    ExpiryDateCalculator cal2 = new ExpiryDateCalculator();
    LocalDate expireDate2 = cal2.calculateExpiryDate(billingDate2, payAmount2);
    
    assertEquals(LocalDate.of(2019, 6, 5), expireDate2);
    // 중복 2 끝
}
```
테스트코드에서, 납부일과 금액, 만료일을 매개변수로 받아 LocalDate 타입 변수를 생성하고, assertEquals를 수행하는 private 메소드를 정의할 수 있다.

```java
@Test
@DisplayName("1만원 납부하면 한 달 뒤가 만료일이 됨")
void payment_Then_ExpireDate() {
    assertExpireDate(LocalDate.of(2019, 3, 1), 10_000, LocalDate.of(2019, 4, 1));
    assertExpireDate(LocalDate.of(2019, 5, 5), 10_000, LocalDate.of(2019, 6, 5));
}

private void assertExpireDate(LocalDate billingDate, int payAmount, LocalDate expectedExpireDate) {
    ExpiryDateCalculator cal = new ExpiryDateCalculator();
    LocalDate expireDate = cal.calculateExpiryDate(billingDate, payAmount);
    assertEquals(expectedExpireDate, expireDate);
}
```
메소드를 사용하여 중복된 부분을 제거하고, 테스트 코드를 더 깔끔하게 만들었다.

### 예외 상황 처리
한 달 추가로는 잘못된 동작을 하는 예외 상황을 테스트해보자.
예를 들어 1월 29일에 결제한 경우, 5월 31일에 결제한 경우 등이 있다.
```java
@Test
@DisplayName("1만원 납부하면 한 달 뒤가 만료일이 됨")
void payment_Then_ExpireDate() {
    assertExpireDate(LocalDate.of(2019, 3, 1), 10_000, LocalDate.of(2019, 4, 1));
    assertExpireDate(LocalDate.of(2019, 5, 5), 10_000, LocalDate.of(2019, 6, 5));
}

@Test
@DisplayName("1만원 납부했을 때 납부일과 한 달 뒤 날짜가 다름")
void payment_Then_ExpireDateDiffer() {
    assertExpireDate(LocalDate.of(2019, 1, 31), 10_000, LocalDate.of(2019, 2, 28));
    assertExpireDate(LocalDate.of(2019, 5, 31), 10_000, LocalDate.of(2019, 6, 30));
}

private void assertExpireDate(LocalDate billingDate, int payAmount, LocalDate expectedExpireDate) {
    ExpiryDateCalculator cal = new ExpiryDateCalculator();
    LocalDate expireDate = cal.calculateExpiryDate(billingDate, payAmount);
    assertEquals(expectedExpireDate, expireDate);
}
```
그러나 위 예외 사항에 대해서 `LocalDate#plusMonths()` 메서드가 알아서 처리를 해주므로 테스트는 통과한다.
<br>
그러나 이렇게 예외 사항을 고려해주는 것이 중요하다.

### 다음 테스트 선택: 예외 상황
1만원을 납부했을 때 테스트를 통과했으니, 다음 테스트도 생각해볼 수 있다.
- 2만원을 납부했을 때
- 3만원을 납부했을 때

이 때 생각할 수 있는 예외 상황이다.
- 1월 31일에 1만원을 납부하면 2월 28일이 만료일이고, 2월 28에 1만원을 납부하면 3월 31일이 만료일이다.

위 예외 상황은 2개월 납부를 기준으로 생각한 것이지만, 
<br>1개월 납부 상황에서도 발생할 수 있는 예외 상황이므로 1개월 납부에 대한 예외를 확실히 처리하고 다음 테스트를 진행하는 것이 좋다.

### 테스트 추가 전 리팩토링
2개월 납부를 테스트 하기 위해 필요한 인자가 늘어났다.<br>
인자는 적을 수록 가독성과 유지 보수 측면에서 좋다.<br>
현재 파라미터의 개수가 3개 이상이므로, 하나의 객체로 묶어서 전달하는 방법을 고려해야 한다.<br>
```java
@Getter
public class PayData {
    private LocalDate billingDate;
    private int payAmount;

    @Builder
    private PayData(LocalDate billingDate, int payAmount) {
        this.billingDate = billingDate;
        this.payAmount = payAmount;
    }
}
```
위와 같이, 납부일과 납부 금액을 필드로 갖는 PayData 클래스를 생성하였다.
<br>
<br>
이제 ExpireDateCalculator 클래스를 리팩토링해보자.
```java
public class ExpiryDateCalculator {
    public LocalDate calculateExpiryDate(PayData payData) {
        return payData.getBillingDate().plusMonths(1);
    }
}
```
<br>
이제 PayData를 사용하는 새로운 ExpiryDateCalculator로 assertExpireDate 메소드를 수정해보자.

```java
private void assertExpireDate(PayData payData, LocalDate expectedExpireDate) {
    ExpiryDateCalculator cal = new ExpiryDateCalculator();
    LocalDate expireDate = cal.calculateExpiryDate(payData);
    assertEquals(expectedExpireDate, expireDate);
}
```

마지막으로 수정된 assertExpireDate 메소드를 사용하여 테스트 코드를 수정해보자.
```java
@Test
@DisplayName("1만원 납부하면 한 달 뒤가 만료일이 됨")
void payment_Then_ExpireDate() {
    assertExpireDate(
            PayData.builder()
                .billingDate(LocalDate.of(2019, 3, 1))
                .payAmount(10_000)
                .build(), 
            LocalDate.of(2019, 4, 1));
    assertExpireDate(
            PayData.builder()
                    .billingDate(LocalDate.of(2019, 5, 5))
                    .payAmount(10_000)
                    .build(), 
            LocalDate.of(2019, 6, 5));
}

@Test
@DisplayName("1만원 납부했을 때 납부일과 한 달 뒤 날짜가 다름")
void payment_Then_ExpireDateDiffer() {
    assertExpireDate(
            PayData.builder()
                    .billingDate(LocalDate.of(2019, 1, 31))
                    .payAmount(10_000)
                    .build(), 
            LocalDate.of(2019, 2, 28));
    assertExpireDate(
            PayData.builder()
                    .billingDate(LocalDate.of(2019, 5, 31))
                    .payAmount(10_000)
                    .build(), 
            LocalDate.of(2019, 6, 30));
}
```

### 예외 상황 테스트 계속 진행
2개월 납부를 테스트할 때, 첫 납부일을 전달해야 하므로 PayData 객체에 첫 납부일을 추가한다.
```java
@Getter
class PayData {
    private LocalDate firstBillingDate;
    private LocalDate billingDate;
    private int payAmount;

    @Builder
    private PayData(LocalDate firstBillingDate, LocalDate billingDate, int payAmount) {
        this.firstBillingDate = firstBillingDate;
        this.billingDate = billingDate;
        this.payAmount = payAmount;
    }
}
```
첫 납부일로 LocalDate(2019, 1, 31)을 전달하면, 실행은 되지만 테스트를 통과하지 못한다.
```
org.opentest4j.AssertionFailedError:
Expected :2019-03-31
Actual   :2019-03-28
```

if문으로 상수를 반환하게 하여 테스트를 통과하도록 할 수 있다.
```java
public class ExpiryDateCalculator {
    public LocalDate calculateExpiryDate(PayData payData) {
        if (payData.getFirstBillingDate().equals(LocalDate.of(2019, 1, 31))) {
            return LocalDate.of(2019, 3, 31);;
        }
        return payData.getBillingDate().plusMonths(1);
    }
}
```
그러나 이렇게 하면 1개월 납부를 테스트하는 메소드에서 NPE가 발생하였다.
<br>
이는 1개월 납부를 테스트하는 메소드에서 firstBillingDate를 전달하지 않은 상태에서 
<br>
`payData.getFirstBillingDate`가 호출되었기 때문이다.
<br>
이를 방지하기 위해 null을 검사하는 코드를 추가하자.

```java
public class ExpiryDateCalculator {
    public LocalDate calculateExpiryDate(PayData payData) {
        if (payData.getFirstBillingDate() != null) {
            if (payData.getFirstBillingDate().equals(LocalDate.of(2019, 1, 31))) {
                return LocalDate.of(2019, 3, 31);
            }
        }
        return payData.getBillingDate().plusMonths(1);
    }
}
```
2개월 납부에 대한 예외사항을 테스트하는 코드를 추가한다.
```java
@Test
@DisplayName("1만원 납부했을 때 납부일과 한 달 뒤 날짜가 다름")
void payment_Then_ExpireDateDiffer() {
    assertExpireDate(
            PayData.builder()
                    .firstBillingDate(LocalDate.of(2019, 1, 31))
                    .billingDate(LocalDate.of(2019, 2, 28))
                    .payAmount(10_000)
                    .build(), 
            LocalDate.of(2019, 3, 31));
}
```
새로운 테스트는 나와야 하는 값과 실제 값이 달라 실패한다.
```
org.opentest4j.AssertionFailedError:
Expected :2019-03-31
Actual   :2019-03-28
```
이제 테스트를 통과할 만큼 구현을 일반화해주어야 한다.<br><br>
첫 납부일과 납부일의 일자가 다르면, 첫 납부일의 일자를 납부일의 일자로 사용한다.
```java
public class ExpiryDateCalculator {
    public LocalDate calculateExpiryDate(PayData payData) {
        if (payData.getFirstBillingDate() != null) {
            LocalDate candidateExp = payData.getBillingDate().plusMonths(1);
            if (payData.getFirstBillingDate().getDayOfMonth() != candidateExp.getDayOfMonth()) {
                return candidateExp.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }
        return payData.getBillingDate().plusMonths(1);
    }
}
```
이제 예외사항에 대한 테스트 케이스도 통과한다.<br>
같은 조건의 다른 테스트도 추가했을 때 통과하는 것을 확인 후 다음 단계로 넘어가자.

### 상수를 변수로
ExpireDateCalculator 클래스의 상수를 변수로 변경해보자.
여기서 상수는 `LocalDate.plusMonths(1)`에서 1과 같은 값이다.

```java
public class ExpiryDateCalculator {
    private static final int ONE_MONTH = 1;

    public LocalDate calculateExpiryDate(PayData payData) {
        if (payData.getFirstBillingDate() != null) {
            LocalDate candidateExp = payData.getBillingDate().plusMonths(ONE_MONTH);
            if (payData.getFirstBillingDate().getDayOfMonth() != candidateExp.getDayOfMonth()) {
                return candidateExp.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }
        return payData.getBillingDate().plusMonths(ONE_MONTH);
    }
}
```
**이렇게 리팩토링 하면, 상수 값으로 관리할 때보다 일괄적으로 변경할 수 있다.<br>
또한 상수 값이 어떤 의미를 가지는지도 명확하게 알 수 있어 가독성 측면에서 좋다.**

> 테스트 코드 작성 순서
> 1. 쉬운 테스트 코드 작성
> 2. 테스트를 통과하는 코드 구현
> 3. 테스트 통과 후 중복 제거/리팩토링
> 4. 예외 상황 테스트 추가
> 5. 리팩토링 (ex. 상수 -> 변수)
> 6. 다음 테스트 선택 (이하 반복)

## 테스트 할 목록 정리
TDD를 시작할 때, 테스트 할 목록들을 정리하는 것이 좋다.
<br>
테스트 목록 중에서 쉬운 것을 먼저 선택하고, 예외적인 테스트도 우선적으로 선택한다.
<br>
테스트 진행 중에 새로운 테스트 케이스가 발견되면, 목록에 같이 추가해준다.
<br>
>'지라', '트렐로'와 같은 툴을 사용하면 해당 테스트 케이스를 하위 작업으로 등록하여 테스트 통과 여부를 트래킹할 수 있다.
## 시작이 안된다면 단언부터 정리
테스트 코드를 어떻게 작성해야 할 지 모르겠다면, 검증하는 코드를 먼저 작성하는 것이 좋다.
>검증하는 코드가 뭔가 했는데,
> assertEquals(~)를 먼저 작성하고, 메소드 인자에 대해서는 구체적으로 한번에 생각 안날 수도 있으니
> '기대만료일', '실제만료일' 처럼 직관적으로 써놓고 이후에 하나씩 수정/보충하라는 의미이다.
## 구현이 막히면
어떤 구현을 해야할지 떠오르지 않으면, 작성했던 코드를 지우고 다시 시작한다.<br>
이 때, 테스트 케이스를 쉬운 것부터 선택할 수 있도록 유의하자.
- 쉬운 테스트
- 예외적인 테스트
- 완급 조절
