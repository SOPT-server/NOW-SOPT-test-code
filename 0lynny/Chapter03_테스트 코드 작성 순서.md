## Chapter03.  테스트 코드 작성 순서

### 테스트 작성 순서

지난 챕터에서 우리는 ‘암호 강도 측정 기능’ 예제를 구현해보며 테스트코드를 작성해보았다. 다시 한번 어떤 순서로 코드를 작성하였는지 알아보자.

**1. 구현하기 쉬운 테스트부터 시작하기**

→ 가장 구현하기 쉬운 케이스부터 시작하게 되면 빠르게 테스트를 통과시킬 수 있다. 일반적으로 수 분에서 십 여분 이내에 구현을 완료해서 테스트를 통과시킬 수 있는 케이스를 선택하여 테스트한다. 이렇게 하나의 테스트를 통과했으면 그 다음으로 구현하기 쉬운 테스트를 선택하여 점진적으로 완성해나갈 수 있다.

**2. 예외 상황을 먼저 테스트하기**

→ 다양한 예외상황은 복잡한 if-else문을 동반한다. 예외상황을 고려하지 않고 하면 이후에 코드 구조를 바꾸거나 조건문을 중복해서 추가해야하는 일이 생긴다. 따라서 초반에 예외상황을 테스트하여 예외상황에 따른 if-else 구조를 미리 만드는 것이 효율적이다. 또한 미리 처리해주면 예외상황에 따른 버그도 줄일 수 있다. 

**3. 완급 조절**

→ 처음 TDD를 작성할 때, 한 번에 얼마만큼의 코드를 작성할 것인가에 대해 고민할 것이다. 따라서 다음 단계에 따라 작성해보자.

1) 정해진 값을 리턴

2) 값 비교를 이용해서 정해진 값을 리턴

3) 다양한 테스트를 추가하면서 구현을 일반화

**4. 지속적인 리팩토링**

→ 테스트를 통과한 후에는 리팩토링을 진행한다. 코드 중복이나 긴 코드의 경우, 메서드 추출 기법을 활용하여 코드의 의미를 더 명확하게 해주고, 가독성을 높일 수 있다. 가독성이 높아지면 코드 분석이 빨라져 향후 유지보수에도 도움이 된다. 

그렇다면 이제 코드를 직접 작성해보며 연습해보자 !

<br>

### 테스트 작성 연습

매달 비용을 지불해야 사용할 수 있는 유료서비스가 있다고 해보자. 아래의 규칙에 따라 서비스 만료일을 정한다. 

- 서비스를 사용하려면 매달 1만원을 선불로 납부한다. 납부일 기준으로 한 달 뒤가 서비스 만료일이 된다.
- 2개월 이상 요금을 납부할 수 있다.
- 10만원을 납부하면 서비스를 1년 제공한다.

이제 다음의 기능을 TDD로 구현해보자.

<br>

### 1) 쉬운 것부터 테스트

테스트를 추가할 때는 구현하기 쉬운 것, 예외상황을 먼저 테스트해야한다고 하였다. 이 예제에서는 1만원을 납부하면 한달 뒤 같은 날을 만료일로 계산하는 것이 가장 쉬울 것 같다. 아래 코드로 확인해보자.

```java
public class ExpiryDateCalculatorTest {

    @Test
    void 만원_납부하면_한달_뒤가_만료일이_됨() {
        LocalDate billingDate = LocalDate.of(2019,3,1);
        int payAmount = 10_000;

        ExpiryDateCalculator cal = new ExpiryDateCalculator();
        LocalDate expiryDate = cal.calculateExpiryDate(billingDate, payAmount);

        assertEquals(LocalDate.of(2019,4,1), expiryDate);
    }

}

```

```java
public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(LocalDate billingDate, int payAmount) {
        return LocalDate.of(2019,4,1);
    }
    
}
```

이렇게 코드를 작성하면 테스트에 통과한다.

### 2) 예시를 추가하면서 구현을 일반화

이제 동일 조건의 예시를 추가하면서 구현을 일반화 해보자. 

납부일을 추가하여 테스트해보자. 기존 코드로 테스트를 돌려보면 실패한다. 따라서 구현을 일반화하여 다음과 같이 코드를 수정하면 테스트에 통과한다. 

```java
public class ExpiryDateCalculatorTest {

    @Test
    void 만원_납부하면_한달_뒤가_만료일이_됨() {
        LocalDate billingDate = LocalDate.of(2019,3,1);
        int payAmount = 10_000;

        ExpiryDateCalculator cal = new ExpiryDateCalculator();
        LocalDate expiryDate = cal.calculateExpiryDate(billingDate, payAmount);

        assertEquals(LocalDate.of(2019,4,1), expiryDate);
        
        LocalDate billingDate2 = LocalDate.of(2019,5,5);
        int payAmount2 = 10_000;

        ExpiryDateCalculator cal2 = new ExpiryDateCalculator();
        LocalDate expiryDate2 = cal2.calculateExpiryDate(billingDate2, payAmount2);

        assertEquals(LocalDate.of(2019,6,5), expiryDate2);
    }

}

```

```java
public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(LocalDate billingDate, int payAmount) {
        return billingDate.plusMonths(1);
    }
    
}
```

### 3) 리팩토링 : 중복 제거

테스트 메서드에서는 다음과 같은 코드 중복이 발생한다. 보통은 코드 중복을 제거하는 것이 좋지만 테스트메서드는 스스로 무엇을 테스트하는지 명확하게 설명할 수 있어야하기에 고민이 필요하다. 

메서드를 이용해서 중복을 제거한 코드는 아래와 같다. 

```java
public class ExpiryDateCalculatorTest {

    @Test
    void 만원_납부하면_한달_뒤가_만료일이_됨() {
            assertExpiryDate(LocalDate.of(2019,3,1), 10_000, LocalDate.of(2019,4,1));
            assertExpiryDate(LocalDate.of(2019,5,5), 10_000, LocalDate.of(2019,6,5));
        }
    
    private void assertExpiryDate(LocalDate billingDate, int payAmount, LocalDate expectedExpiryDate) {
            ExpiryDateCalculator cal = new ExpiryDateCalculator();
        LocalDate realExpiryDate = cal.calculateExpiryDate(billingDate, payAmount);

        assertEquals(expectedExpiryDate, realExpiryDate);
    }

}

```

### 4) 예외상황 처리

이제 쉬운 테스트 구현을 했으니 예외 상황을 찾아서 테스트해보자. 

**Test. 납부일과_한달_뒤_일자가_같지_않음**

- 납부일: 2019-01-31 / 납부액: 1만원 / 만료일 : 2019-02-28
- 납부일: 2019-05-31 / 납부액 : 1만원 / 만료일 : 2019-06-30
- 납부일 : 2020-01-31 / 납부액 : 1만원 / 만료일 : 2020-02-29

다음과 같은 상황으로 테스트를 해보면 바로 테스트에 통과한다. 이유는 billingDate.plusMonths(1)의 plusMonths가 자동적으로 한 달 추가처리를 해주기 때문이다.

```java
@Test
    void 납부일과_한달_뒤_일자가_같지_않음() {
            assertExpiryDate(LocalDate.of(2019,1,31), 10_000, LocalDate.of(2019,2,28));
            assertExpiryDate(LocalDate.of(2019,5,31), 10_000, LocalDate.of(2019,6,30));
            assertExpiryDate(LocalDate.of(2020,1,31), 10_000, LocalDate.of(2020,2,29));
        }
```

이어서 1개월 요금 지불에 대한 예외 상황 테스트를 추가해보자.

해당 테스트를 진행하기 위해서는 파라미터가 3개로 늘어나게 된다. 유지보수 측면에서 파라미터가 세 개 이상이면 객체로 바뀌는 것을 고려하는 것이 좋다. 따라서 해당 리팩토링을 먼저 진행해보자. 

```java
public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(PayData payData) {
        return payData.getBillingDate().plusMonths(1);
    }

}
```

```java
public class PayData {

    private LocalDate billingDate;
    private int payAmount;

    private PayData() {}
    public PayData(LocalDate billingDate, int payAmount) {
        this.billingDate = billingDate;
        this.payAmount = payAmount;
    }

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PayData data = new PayData();

        public Builder billingDate(LocalDate billingDate) {
            data.billingDate = billingDate;
            return this;
        }

        public Builder payAmount(int payAmount) {
            data.payAmount = payAmount;
            return this;
        }

        public PayData build() {
            return data;
        }
    }

}

```

```java
package org.example.chap03;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpiryDateCalculatorTest {

    @Test
    void 만원_납부하면_한달_뒤가_만료일이_됨() {
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,3,1))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,4,1));
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,5,5))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,6,5));
    }

    @Test
    void 납부일과_한달_뒤_일자가_같지_않음() {
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,1,31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,2,28));
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,5,31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,6,30));
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2020,1,31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2020,2,29));
    }

    private void assertExpiryDate(PayData payData, LocalDate expectedExpiryDate) {
        ExpiryDateCalculator cal = new ExpiryDateCalculator();
        LocalDate realExpiryDate = cal.calculateExpiryDate(payData);

        assertEquals(expectedExpiryDate, realExpiryDate);
    }

}
```

이제 리팩토링을 완료했으니 테스트하려던 것을 이어서 구현해보자. 

**Test. 첫_납부일과_만료일_일자가_다를때_만원_납부**

다음 사례를 추가하자.

- 첫 납부일 : 2019-01-31 / 납부금액 : 1만원 / 만료일 : 2019-02-28 / 다음 만료일 : 2019-03-31

```java
@Test
    void **첫_납부일과_만료일_일자가_다를때_만원_납부**() {
        assertExpiryDate(
                PayData.builder()
                                .firstBillingDate(LocalDate.of(2019,1,31))
                        .billingDate(LocalDate.of(2019,2,28))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,3,31));
     }

```

이렇게 테스트하기 위해서는 PayData에 첫 납부일도 추가해주어야한다. 

```java
package org.example.chap03;

import java.time.LocalDate;

public class PayData {

    private LocalDate firstBillingDate;
    private LocalDate billingDate;
    private int payAmount;

    private PayData() {}
    public PayData(LocalDate firstBillingDate, LocalDate billingDate, int payAmount) {
        this.firstBillingDate = firstBillingDate;
        this.billingDate = billingDate;
        this.payAmount = payAmount;
    }

    public LocalDate getFirstBillingDate() {
        return firstBillingDate;
    }

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PayData data = new PayData();

        public Builder firstBillingDate(LocalDate firstBillingDate) {
            data.firstBillingDate = firstBillingDate;
            return this;
        }
        
        public Builder billingDate(LocalDate billingDate) {
            data.billingDate = billingDate;
            return this;
        }

        public Builder payAmount(int payAmount) {
            data.payAmount = payAmount;
            return this;
        }

        public PayData build() {
            return data;
        }
    }

}

```

이렇게 테스트하면 테스트 통과를 하지 못한다. 
우선 상수로 테스트를 통과시키려고 하였지만 결과는 앞선 테스트들에서 NPE가 발생한다. 따라서 다음과 같이 Null을 검사하는 코드를 추가하여 테스트를 통과시켜보자. 

```java
public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(PayData payData) {
        if(payData.getFirstBillingDate() != null) {
            if(payData.getFirstBillingDate().equals(LocalDate.of(2019,1,31))) {
                return LocalDate.of(2019, 3, 31);
            }
        }
        return payData.getBillingDate().plusMonths(1);
    }

}

```

이렇게 상수화를 통해서 테스트를 통과시켰다면 이제 일반화를 할 차례다. 다음의 사례를 추가하여 테스트해보자.

- 첫 납부일 : 2019-01-30 / 납부 금액 : 1만원 / 만료일 : 2019-02-28 / 다음 만료일 : 2019-03-30

```java
@Test
    void **첫_납부일과_만료일_일자가_다를때_만원_납부**() {
        assertExpiryDate(
                PayData.builder()
                                .firstBillingDate(LocalDate.of(2019,1,31))
                        .billingDate(LocalDate.of(2019,2,28))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,3,31));
           
         //새로 추가한 테스트     
         assertExpiryDate(
                PayData.builder()
                                .firstBillingDate(LocalDate.of(2019,1,30))
                        .billingDate(LocalDate.of(2019,2,28))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,3,30));   
     }

```

이렇게 실행하면 테스트에 실패할 것이다. 코드를 수정하여 테스트를 통과할만큼만 구현을 일반화해보자.

```java
public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(PayData payData) {
        if(payData.getFirstBillingDate() != null) {
            LocalDate candidateExp = payData.getBillingDate().plusMonths(1);
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExp.getDayOfMonth()) {
                return candidateExp.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }
        return payData.getBillingDate().plusMonths(1);
    }

}
```

후보 만료일을 구한 후, 첫 납부일의 일자와 후보 만료일의 일자가 다르면 첫 납부일의 일자를 후보 만료일의 일자로 사용하도록 수정하였다. 이렇게 코드를 수정하면 추가한 테스트도 통과한다. 

이후 다음의 사례도 추가하여 테스트해보아도 통과한다. 

```java
@Test
    void **첫_납부일과_만료일_일자가_다를때_만원_납부**() {
        /*
        생략
        */
        
        assertExpiryDate(
                PayData.builder()
                                .firstBillingDate(LocalDate.of(2019,5,31))
                        .billingDate(LocalDate.of(2019,6,30))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,7,31));  
        
 }

```

### 5) 코드 정리 : 상수를 변수로

현재 month를 추가할 때 1이라는 상수를 추가해주고 있다. 이 1은 만료일을 계산할 때 추가할 개월수를 의미하므로 다음과 같이 변수로 바꾸어줄 수 있다. 

```java
public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(PayData payData) {
            int addedMonths = 1;
        if(payData.getFirstBillingDate() != null) {
            LocalDate candidateExp = payData.getBillingDate().plusMonths(addedMonths);
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExp.getDayOfMonth()) {
                return candidateExp.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }
        return payData.getBillingDate().plusMonths(addedMonths);
    }

}
```

### 6) 다음 테스트 선택 : 쉬운 테스트

**Test. 이만원_이상_납부하면_비례해서_만료일_계산()** 

- 2만원을 지불하면 만료일이 두 달 뒤가 된다.
- 3만원을 지불하면 만료일이 세 달 뒤가 된다.

다음의 사례는 지불한 금액으로 만료일이 된다. 

```java
public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(PayData payData) {
            int addedMonths = payData.getPayAmount() / 10_000;
        if(payData.getFirstBillingDate() != null) {
            LocalDate candidateExp = payData.getBillingDate().plusMonths(addedMonths);
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExp.getDayOfMonth()) {
                return candidateExp.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }
        return payData.getBillingDate().plusMonths(addedMonths);
    }

}
```

```java
@Test 
void 이만원_이상_납부하면_비례해서_만료일_계산() {
        assertExpiryDate(
                    PayData.builder()
                   .billingDate(LocalDate.of(2019, 3, 1)
                   .payAmount(20_000)
                   .build(),
          LocalDate.of(2019, 5, 1));  
          
    assertExpiryDate(
                    PayData.builder()
                   .billingDate(LocalDate.of(2019, 3, 1)
                   .payAmount(30_000)
                   .build(),
          LocalDate.of(2019, 6, 1));  
  }
```

### 7) 예외테스트 추가

**Test. 첫_납부일과_만료일_일자가_다를때_이만원_이상_납부()**

하지만 첫 납부일과 납부일의 일자가 다를 때 2만원 이상 납부하게 되면 예외가 발생한다.

- 첫 납부일 : 2019-01-31 / 납부 금액 : 2만원 / 만료일 : 2019-02-28 / 다음 만료일 : 2019-04-30

```java
@Test
    void 첫_납부일과_만료일_일자가_다를때_이만원_이상_납부() {
        assertExpiryDate(
                PayData.builder()
                        .firstBillingDate(LocalDate.of(2019, 1, 31))
                        .billingDate(LocalDate.of(2019, 2, 28))
                        .payAmount(20_000)
                        .build(),
                    LocalDate.of(2019, 4, 30));
    }        
```

이렇게 실행하면 테스트에 실패한다. 오류 내용을 보면 4월 31일이 없는데 4월 31일로 설정하여 발생한 오류이다. 따라서 다음과 같이 코드를 수정해주어야한다. 

```java
public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(PayData payData) {
        int addedMonths = payData.getPayAmount() / 10_000;
        if(payData.getFirstBillingDate() != null) {
            LocalDate candidateExp = payData.getBillingDate().plusMonths(addedMonths);
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExp.getDayOfMonth()) {
                if(YearMonth.from(candidateExp).lengthOfMonth() < payData.getFirstBillingDate().getDayOfMonth()) {
                    return candidateExp.withDayOfMonth(YearMonth.from(candidateExp).lengthOfMonth());
                }
                return candidateExp.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }
        return payData.getBillingDate().plusMonths(addedMonths);
    }

}
```

### 8) 코드 정리

날짜 관련 계산 코드가 중복해서 존재하기 때문에 코드가 복잡해보인다. 

1) 후보 만료일이 속한 월의 마지막 일자를 구하는 코드 중복 제거

2) 첫 납부일의 일자를 구하는 코드 중복 제거

### 9) 다음 테스트 : 10개월 요금을 납부하면 1년 제공

10만원을 납부하면 서비스를 1년 제공한다는 규칙을 구현해보자.

```java
@Test
    void 십만원을_납부하면_1년_제공() {
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019, 2, 28))
                        .payAmount(100_000)
                        .build(),
                LocalDate.of(2019, 2, 28));
    }
```

```java
public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(PayData payData) {
        // 해당 코드 추가!
        int addedMonths = payData.getPayAmount() == 100_000 ? 12 : payData.getPayAmount() / 10_000;
        if(payData.getFirstBillingDate() != null) {
            LocalDate candidateExp = payData.getBillingDate().plusMonths(addedMonths);
            if(payData.getFirstBillingDate().getDayOfMonth() != candidateExp.getDayOfMonth()) {
                if(YearMonth.from(candidateExp).lengthOfMonth() < payData.getFirstBillingDate().getDayOfMonth()) {
                    return candidateExp.withDayOfMonth(YearMonth.from(candidateExp).lengthOfMonth());
                }
                return candidateExp.withDayOfMonth(payData.getFirstBillingDate().getDayOfMonth());
            }
        }
        return payData.getBillingDate().plusMonths(addedMonths);
    }

}
```

이렇게 코드를 작성하면 테스트에 통과할 수 있다.

테스트 목록을 적고 테스트를 한 번에 다 작성하면 안되고 하나의 테스트 코드를 만들고, 이를 통과시키고 리팩토링하고 하는 과정을 반복하면서 작성하면 된다. 

이 두 가지만 기억하자 ! 쉬운테스트, 예외적인 테스트, 완급 조절
