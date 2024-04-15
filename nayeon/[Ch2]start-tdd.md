# [TDD 시작]
# TDD 이전의 개발
### 디버깅
- 디버깅을 위한 로그 메세지 추가
- 개발 툴의 디버거를 사용하여 한 줄 한 줄 검사

### 테스트
- 테스트를 위해 톰캣 서버 구동
- 데이터 검증을 위해 DB에 쿼리 실행
- 문제 발생 시 코드 재검토

# TDD란?
## 정의
> 테스트코드란, 기능이 올바르게 동작하는지 검증하는 코드
> 
> TDD는 테스트 코드를 먼저 작성하고, 그 테스트 코드를 통과하는 코드를 작성하는 방법론

?? 구현 코드가 없는데 어떻게 테스트를 하나

```java
public class CalculatorTest {
    
    /* JUnit test annotation */
    @Test
    void plus() {
        /* 값이 동일하지 않으면 AssertionFailedError 발생 */
        assertEquals(3, calculator.plus(1, 2));
        assertEquals(5, calculator.plus(1, 4));
    }
}
```
위와 같은 테스트 코드를 먼저 작성 후

```java
public class Calculator {
    public static int plus(int a, int b) {
        return a + b;
    }
}
```
구현 코드를 `test` 디렉토리 아래에 작성한다.
아직까지는 배포 대상에 포함되지 않는다.

Test 실행 후 테스트 케이스를 만족하는, 올바르게 동작하는 코드라고 생각되는 경우 `main` 디렉토리로 옮겨 배포 대상에 포함되도록 한다.

# TDD 예: 암호 검사기

## Conditions
- 8자 이상
- 0~9 사이의 숫자 포함
- 소문자 포함

> 조건 3개 충족 - 암호 강도: 강함 <br>
> 조건 2개 충족 - 암호 강도: 보통 <br>
> 조건 1개 충족 - 암호 강도: 약함

## 1st Test: 모든 규칙 충족
> 첫번째 테스트는 가장 쉽거나 가장 예외적인 상황을 선정

```java
public class PasswordStrengthMeterTest {
    @Test
    void meetsAllCriteria_Then_Strong() {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordStrength result = meter.meter("ab12!@AB");
        assertEquals(PasswordStrength.STRONG, result);
    }
}
```
위와 같이 PasswordStrengthMeter를 테스트하는 클래스를 작성하면 PasswordStrengthMeter 클래스가 없어 컴파일 에러가 발생한다.
그에 따라 PasswordStrengthMeter와 PasswordStrength 클래스를 작성해야 한다.

먼저 PasswordStrength enum 클래스부터 작성한다.
```java
public enum PasswordStrength {
    STRONG
}
```
TDD에서는 테스트 코드를 통과하는 최소한의 구현을 반복하면서 이루어진다. 따라서 WEAK와 같은 다른 필드는 지금 작성하지 않는다.

다음으로 PasswordStrengthMeter 클래스를 작성한다.
```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        return PasswordStrength.STRONG;
    }
}
```
마찬가지로 최소한의 구현을 통해 테스트 코드를 통과하게 한다.

그 후 모든 규칙을 충족하는 테스트케이스를 하나 더 추가한다.
```java
public class PasswordStrengthMeterTest {
    @Test
    void meetsAllCriteria_Then_Strong() {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordStrength result = meter.meter("ab12!@AB");
        assertEquals(PasswordStrength.STRONG, result);
        PasswordStrength result2 = meter.meter("abc1!Add");
        assertEquals(PasswordStrength.STRONG, result2);
    }
}
```
이 후 다시 테스트를 실행한다.

## 2nd Test: 8자 미만이면서 나머지 조건은 충족
```java
public class PasswordStrengthMeterTest {
    @Test
    void meetsAllCriteria_Then_Strong() {
        // ...
    }

    @Test
    void meetsOtherCriteria_except_for_Length_Then_Normal() {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordStrength result = meter.meter("ab12!@A");
        assertEquals(PasswordStrength.NORMAL, result);
    }
}
```
두번째 테스트 메소드를 추가했다. 이 때 PasswordStrength.NORMAL이 없어 컴파일 에러가 발생하므로 PasswordStrength.NORMAL을 추가한다.

```java
public enum PasswordStrength {
    STRONG, NORMAL
}
```
그러나 위와 같이 추가하면, PasswordStrengthMeter 클래스의 meter 메소드에서 PasswordStrength.STRONG을 반환하고 있으므로 두번째 테스트는 실패하게 된다. 따라서 PasswordStrengthMeter 클래스를 수정해야 한다.

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
위와 같이 수정하면 두번째 테스트도 통과하게 된다.
또 다른 테스트 케이스를 추가하여 다시 테스트를 실행한다.

```java
public class PasswordStrengthMeterTest {
    @Test
    void meetsAllCriteria_Then_Strong() {
        // ...
    }

    @Test
    void meetsOtherCriteria_except_for_Length_Then_Normal() {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordStrength result = meter.meter("ab12!@A");
        assertEquals(PasswordStrength.NORMAL, result);
        PasswordStrength result2 = meter.meter("Ab12!c");
        assertEquals(PasswordStrength.NORMAL, result2);
    }
}
```

## 3rd Test: 숫자를 포함하지 않고 나머지 조건은 충족
```java
public class PasswordStrengthMeterTest {
    @Test
    void meetsAllCriteria_Then_Strong() {
        // ...
    }

    @Test
    void meetsOtherCriteria_except_for_Length_Then_Normal() {
        // ...
    }

    @Test
    void meetsOtherCriteria_except_for_Number_Then_Normal() {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordStrength result = meter.meter("ab!@ABqwer");
        assertEquals(PasswordStrength.NORMAL, result);
    }
}
```
위와 같이 숫자를 포함하지 않는 경우에 대한 테스트를 통과하도록 PasswordStrengthMeter 클래스를 수정한다.

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }
        boolean containsNum = false;
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                containsNum = true;
                break;
            }
        }
        if (!containsNum) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }
}
```
위와 같이 수정하면 세번째 테스트도 통과하게 된다.
그러나 가독성을 위해 숫자 포함 여부를 확인하는 코드를 메소드로 분리하자.

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }
        if (!containsNum(s)) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }

    private boolean containsNum(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}
```
위와 같이 수정하면 가독성이 높아진다.

## Test code 정리
테스트 코드도 유지 보수의 대상이므로, 중복되는 부분은 분리해주는 것이 좋다.
```java
public class PasswordStrengthMeterTest {
    
    PasswordStrengthMeter meter = new PasswordStrengthMeter();
    
    @Test
    void meetsAllCriteria_Then_Strong() {
        assertStrength("ab12!@AB", PasswordStrength.STRONG);
        assertStrength("abc1!Add", PasswordStrength.STRONG);
    }

    @Test
    void meetsOtherCriteria_except_for_Length_Then_Normal() {
        assertStrength("ab12!@A", PasswordStrength.NORMAL);
        assertStrength("Ab12!c", PasswordStrength.NORMAL);
    }

    @Test
    void meetsOtherCriteria_except_for_Number_Then_Normal() {
        assertStrength("ab!@ABqwer", PasswordStrength.NORMAL);
    }

    private void assertStrength(String password, PasswordStrength expStr) {
        PasswordStrength result = meter.meter(password);
        assertEquals(expStr, result);
    }
}
```
위와 같이 assertStrength 메소드를 추가하여 중복되는 부분을 분리하였다.

## 4th Test: 값이 없는 경우
만약 meter 메소드에 어떠한 값도 넣지 않는다면, NPE가 발생할 것이다.
이를 방지하기 위해 null 값에 대해서도 동작하도록 meter 메소드를 구현하여야 한다.

```java
public class PasswordStrengthMeterTest {
    // ...

    @Test
    void nullInput_Then_Invalid() {
        assertStrength(null, PasswordStrength.INVALID);
    }
} 
```
위와 같이 null 값을 넣었을 때 PasswordStrength.INVALID를 반환하도록 수정한다.
그에 따라 PasswordStrength enum 클래스에 INVALID를 추가한다.

```java
public enum PasswordStrength {
    STRONG, NORMAL, INVALID
}
```
그러나 여전히 null 값이 전달된 경우에 대한 테스트는 실패한다. 따라서 PasswordStrengthMeter 클래스를 수정하여 null 값에 대한 처리를 추가한다.

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s == null) {
            return PasswordStrength.INVALID;
        }
        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }
        if (!containsNum(s)) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }

    private boolean containsNum(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}
```

또한 null이 아닌 빈 문자열이 전달된 경우에 대한 테스트도 추가한다.

```java
public class PasswordStrengthMeterTest {
    // ...

    @Test
    void emptyInput_Then_Invalid() {
        assertStrength("", PasswordStrength.INVALID);
    }
}
```
위와 같이 빈 문자열이 전달된 경우에 대한 테스트를 추가하고, PasswordStrengthMeter 클래스를 수정하여 빈 문자열에 대한 처리를 추가한다.

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) {
            return PasswordStrength.INVALID;
        }
        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }
        if (!containsNum(s)) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }

    private boolean containsNum(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}
```

## 5th Test: 대문자를 포함하지 않고 나머지 조건은 충족
5번째 테스트 역시 NORMAL을 반환하는 경우로, 앞서 구현한 방법으로 테스트 코드 작성 및 클래스를 추가 구현할 수 있다.

## 6th Test: 길이가 8 이상인 조건만 충족
```java
public class PasswordStrengthMeterTest {
    // ...

    @Test
    void meetsOnlyLengthCriteria_Then_Weak() {
        assertStrength("abdefghi", PasswordStrength.WEAK);
    }
}
```
그러나 위 코드는 PasswordStrength.WEAK이 없어 컴파일 에러가 발생한다. 따라서 PasswordStrength enum 클래스에 WEAK을 추가한다.

```java
public enum PasswordStrength {
    STRONG, NORMAL, WEAK, INVALID
}
```
여전히 테스트는 통과하지 못한다. 따라서 PasswordStrengthMeter 클래스를 수정하여 길이가 8 이상이지만 나머지 조건들은 충족하지 않는 경우에 대한 처리를 추가한다.

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) {
            return PasswordStrength.INVALID;
        }
        boolean lengthEnough = s.length() >= 8;
        boolean containsNum = meetsContainingNumberCriteria(s);
        boolean containsUpp = meetsContainingUppercaseCriteria(s);
        if (!lengthEnough) {
            return PasswordStrength.NORMAL;
        }
        if (!containsNum) {
            return PasswordStrength.NORMAL;
        }
        if (!containsUpp) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }
    /* ... */
}
```
여전히 테스트는 통과하지 않을 것이다.
그러나 검사해야하는 조건이 복잡해짐에 따라 위와 같이 개별 조건 충족 여부를 확인하고, 암호 강도를 계산하는 로직을 분리함으로써 가독성을 높일 수 있다.

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) {
            return PasswordStrength.INVALID;
        }
        boolean lengthEnough = s.length() >= 8;
        boolean containsNum = meetsContainingNumberCriteria(s);
        boolean containsUpp = meetsContainingUppercaseCriteria(s);
        
        /* 길이가 8 이상이고 나머지 조건은 충족하지 않는 경우 */
        if (lengthEnough && !containsNum && !containsUpp) {
            return PasswordStrength.WEAK;
        }
        if (!lengthEnough) {
            return PasswordStrength.NORMAL;
        }
        if (!containsNum) {
            return PasswordStrength.NORMAL;
        }
        if (!containsUpp) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }
    /* ... */
}
```
위와 같이 수정하면 6번째 테스트도 통과하게 된다.

## 7th & 8th Test: 숫자 포함 조건만 충족, 대문자 포함 조건만 충족
위 두 테스트 역시 PasswordStrength.WEAK를 반환하는 경우로, 앞서 구현한 방법으로 테스트 코드 작성 및 클래스를 추가 구현할 수 있다.

## 9th Test: 대문자 포함 조건만 충족
```java
public class PasswordStrengthMeterTest {
    // ...

    @Test
    void meetsOnlyNumCriteria_Then_Weak() {
        assertStrength("12345", PasswordStrength.WEAK);
    }
    
    @Test
    void meetsOnlyUppercaseCriteria_Then_Weak() {
        assertStrength("ABZEF", PasswordStrength.WEAK);
    }
}
```
위와 같이 테스트코드를 작성한 후,

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) {
            return PasswordStrength.INVALID;
        }
        boolean lengthEnough = s.length() >= 8;
        boolean containsNum = meetsContainingNumberCriteria(s);
        boolean containsUpp = meetsContainingUppercaseCriteria(s);
        
        /* WEAK */
        if (lengthEnough && !containsNum && !containsUpp) {
            return PasswordStrength.WEAK;
        }
        if (!lengthEnough && containsNum && !containsUpp) {
            return PasswordStrength.WEAK;
        }
        if (!lengthEnough && !containsNum && containsUpp) {
            return PasswordStrength.WEAK;
        }
        
        /* NORMAL */
        if (!lengthEnough) {
            return PasswordStrength.NORMAL;
        }
        if (!containsNum) {
            return PasswordStrength.NORMAL;
        }
        if (!containsUpp) {
            return PasswordStrength.NORMAL;
        }
        
        /* STRONG */
        return PasswordStrength.STRONG;
    }
    /* ... */
}
```
위와 같이 수정하여 8개의 테스트를 모두 통과하도록 한다.

## meter() Refactoring
위와 같이 9개의 테스트를 모두 통과하였다. 그러나 meter 메소드는 너무 길다.
이에 충족하는 조건의 개수를 세고, 그 개수에 따라 암호 강도를 반환하는 방법으로 리팩토링한다.

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) {
            return PasswordStrength.INVALID;
        }
        int metCounts = getMetCriteriaCounts(s);
        
        if (metCounts <= 1) {
            return PasswordStrength.WEAK;
        }
        if (metCounts == 2) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }

    private int getMetCriteriaCounts(String s) {
        int metCounts = 0;
        if (s.length() >= 8) {
            metCounts++;
        }
        if (meetsContainingNumberCriteria(s)) {
            metCounts++;
        }
        if (meetsContainingUppercaseCriteria(s)) {
            metCounts++;
        }
        return metCounts;
    }
    /* ... */
}
```
위와 같이 수정하면, 조건 충족 개수에 따라 암호 강도를 다르게 하는 로직을 직관적으로 이해할 수 있다.

## 9th Test: 아무 조건도 충족하지 않는 경우
```java
public class PasswordStrengthMeterTest {
    // ...

    @Test
    void meetsNoCriteria_Then_Weak() {
        assertStrength("abc", PasswordStrength.WEAK);
    }
}
```
그러나 위 테스트 코드에서 암호 강도는 STRONG을 반환하므로 테스트는 실패한다.
따라서 충족 개수가 1개 이하인 경우에 대한 처리를 추가한다.

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) {
            return PasswordStrength.INVALID;
        }
        int metCounts = getMetCriteriaCounts(s);
        
        /* 1개 이하인 경우 WEAK */
        if (metCounts <= 1) {
            return PasswordStrength.WEAK;
        }
        if (metCounts == 2) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }
    /* ... */
}
```
위와 같이 1개 이하인 경우에 WEAK를 반환하도록 수정하였다.

# TDD 흐름
> 1. 기능을 검증하는 테스트 코드 작성
> 2. 테스트 코드를 통과하는 최소한의 구현 코드 작성
> 3. 테스트 통과 후 리팩토링

이렇게 테스트 코드 작성과 실행을 중심으로 점진적인 개발을 하면, 여러 번의 테스트를 거친 안정적인 구현을 할 수 있을 것이다.
이러한 TDD의 흐름을 Red-Green-Refactor라고 한다.

## Red-Green-Refactor
- Red: 실패하는 테스트 코드 작성
- Green: 테스트를 통과하는 최소한의 구현 코드 작성
- Refactor: 테스트 통과 후 리팩토링

이렇게 요구 사항에 대한 테스트를 먼저 작성하고, 그 테스트를 충족하는 코드를 작성함으로 프로그램에서 요구하는 정도의 개발 범위를 선정하고, 구현할 수 있다.
오래 걸리더라도 반복적인 검증을 통해 어느 정도 보장된 코드 품질의 개발을 할 수 있다.

또한 TDD는 잘못된 구현에 대한 피드백에 빠르다. 
기존의 코드를 수정했을 때 지금까지 작성했던 모든 테스트를 실행하여 올바르게 동작하는지 확인할 수 있다.
