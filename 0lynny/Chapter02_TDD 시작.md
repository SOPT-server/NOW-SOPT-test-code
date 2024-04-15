# Chapter02. TDD 시작

### ✅ TDD이전의 개발

- 한 번에 작성한 코드가 많은 경우에는 긴 디버깅 시간이 필요했으며 원인을 찾기 위해 코드를 탐색해야했기에 코드작성 시간보다 버그를 찾는 시간이 더 오래 걸리는 문제가 발생
- 코드를 작성하는 개발자와 코드를 테스트하는 개발자가 다른 경우도 있음

→ TDD가 이러한 문제에 도움을 줌~!

<br>

### ✅ 그래서 TDD란?

TDD는 Test-driven Development의 약자로, ‘테스트 주도 개발’이라고 한다.

테스트 : 기능이 올바르게 동작하는지 검증하는 테스트 코드를 작성하는 것

즉, 기능을 검증하는 테스트코드를 먼저 작성하고, 테스트를 통과시키기 위한 개발을 진행한다.

예시 ) 덧셈 기능

```java
package org.example.chap02;

public class Calculator {
    // 현재는 덧셈 기능을 구현하기 위해 새로운 객체를 만들 필요 없음 -> 정적메소드로!
    public static int plus(int a1, int a2) {
        return 0;
    }
}
```

```java
package org.example.chap02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @Test
    void plus() {
        int result = Calculator.plus(1,2);
        assertEquals(3, result); // assertEquals는 인자로 받은 두 값이 동일한지 비교한다. 첫번째 값은 기대값이고, 두번쨰는 실제 값이다. 비교한 결과 두 값이 동일하지 않으면 AssertionFailedError가 발생한다.
    }

}

```

assertEquals는 인자로 받은 두 값이 동일한지 비교한다. 첫번째 값은 기대값이고, 두번쨰는 실제 값이다.
비교한 결과 두 값이 동일하지 않으면 AssertionFailedError가 발생한다. 아래와 같이 말이다!

![테스트 결과](images/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-04-12_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_12.31.28.png)

```java
package org.example.chap02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @Test
    void plus() {
        int result = Calculator.plus(1,2);
        assertEquals(3, result);
        assertEquals(5, Calculator.plus(4, 1));  //경우 하나 추가 !
    }

}

```

```java
public class Calculator {
    // 현재는 덧셈 기능을 구현하기 위해 새로운 객체를 만들 필요 없음 -> 정적메소드로!
    public static int plus(int a1, int a2) {
        if(a1 == 4 && a2 == 1) return 5;
        else return 3;
    }
}

```

경우의 수를 추가한 후 경우의 수만 통과할 수 있도록 Calculator를 변경해보았다. 

하지만 우리는 모든 경우에서 제대로 동작하는 코드를 작성해야하므로 다음과 같이 수정을 해주어야한다.

```java
public class Calculator {
    // 현재는 덧셈 기능을 구현하기 위해 새로운 객체를 만들 필요 없음 -> 정적메소드로!
    public static int plus(int a1, int a2) {
        return a1 + a2;
    }
}
```

이렇게 까지 한 후에는 test → main으로 이동해서 테스트해보자!

test는 배포대상이 아니므로 test에 코드를 만들면 완성되지 않은 코드가 배포되는 것을 방지할 수 있다.

덧셈 기능으로 간단한 테스트코드 작성을 해보았다.
<br>

코드 작성 과정을 다시 한번 생각해보면, 

1 ) 덧셈 기능을 검증하는 테스트 코드를 먼저 작성하고, 2) 테스트 대상이 될 클래스 이름, 매서드 이름, 파라미터 개수, 리턴 타입을 고민했다. 또한 새로운 객체인지 정적 메서드로 구현할지도 함께 고민했다. 

테스트 코드를 작성한 후에는

3) 컴파일 오류를 없애기 위한 추가적인 클래스와 메서드를 작성하였고, 4) 실패한 테스트를 통과하기 위한 코드를 작성하였다.

→ 이런 방식으로 테스트에 실패하면 테스트를 통과시킬만틈 코드를 추가하면서 점진적으로 기능을 완성해나간다.
<br><br>

### TDD 예시: 암호 검사기

암호 검사기는 문자열을 검사해서 규칙을 준수하는지에 따라 암호를 ‘약함’, ‘보통’, ‘강함’으로 구분한다.

암호 검사의 규칙은 다음과 같다.

- 길이가 8글자 이상
- 0-9 사이의 숫자를 포함
- 대문자 포함
- 3개의 규칙 만족 - 강함, 2개 만족 - 보통, 1개 만족 - 약함

<br>

첫 번째 테스트 : 모든 규칙을 충족하는 경우

첫번째 테스트를 선택할 떄는 가장 쉽거나 예외적인 상황을 선택해야한다. 

- 모든 규칙을 충족하는 경우 / 모든 규칙을 충족하지 않는 경우

와 같은 두가지 케이스 중에서 1번 경우를 선택하게 되면 각 조건을 검사하지 않더라도 등급이 ‘강함’ 인 것만 리턴하면 쉽게 테스트를 통과할 수 있다.

```java
package org.example.chap02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordStrengthMeterTest {
    @Test
    void meetsAllCriteria_Then_Stong() {
        PasswordStrengthMeter meter= new PasswordStrengthMeterTest();
        PasswordStrength result = meter.meter("ab12!@AB");
        assertEquals(PasswordStrength.STRONG, result);
    }
}

```

이렇게 작성하면 필요한 값이 없으므로 컴파일 에러가 발생한다. 이걸 먼저 해결해보자.

```java
package org.example.chap02;

public enum PasswordStrength {
    STRONG
}
```

```java
package org.example.chap02;

public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        return null; // 여기 ! -> return PasswordStrength.STRONG
    }
}

```

이렇게 작성하면 컴파일 오류를 해결하고 테스트를 실행시킬 수 있는데 결과는 실패이다.

이유는 기대값은 STRONG인데 실제값은 null 이기 때문이다! 

`return PasswordStrength.STRONG` 이렇게 변경해주면 테스트를 통과하는 것을 확인할 수 있다.
<br>
<aside>
🔑 테스트 메서드명 :  한글 ? 영어 ?
가독성을 높이기 위해서 한글로 작성! → 개인 취향 (꼭 영어가 아니여도 된다는 뜻!)
JUnit5에서는 @DisplayName 으로 메서드 이름을 한글로 작성하지 않아도 테스트 메서드를 원하는 이름으로 표시할 수도 있다.

</aside>
<br>
두 번째 테스트 : 길이만 8글자 미만이고 나머지 조건은 충족하는 경우

```java
 @Test
    @DisplayName("길이가 8글자 미만이고 나머지 조건은 충족하는 경우")
    void meetsOtherCriteria_excep_for_Length_Then_Normal() {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordStrength result = meter.meter("ab12!@A");
        assertEquals(PasswordStrength.NORMAL, result);
    }
```

```java
public enum PasswordStrength {
    NORMAL, STRONG // Normal 추가
}
```

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        return PasswordStrength.NORMAL; 
    }
}

```

이렇게 하면 1번째 테스트는 통과하지 않지만 2번쨰 테스트는 통과하게 된다 ! 두 테스트를 모두 통과시키기 위해서는 다음과 같이 수정해주면 된다 

문자열 길이가 8보다 작으면 NORMAL을 반환하도록 추가해주자.

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

이렇게 수정해보고 다시 테스트를 돌려보면 두 테스트 모두 통과하는 것을 확인할 수 있다.
<br>
세 번째 테스트 : 숫자를 포함하지 않고 나머지 조건은 충족하는 경우

```java
 @Test
    @DisplayName("숫자를 포함하지 않고 나머지 조건은 충족하는 경우")
    void meetsOtherCriteria_except_for_number_Then_Normal() {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordStrength result = meter.meter("ab!ABqwer");
        assertEquals(PasswordStrength.NORMAL, result);
    }
```

이번 테스트의 경우에는 암호가 숫자를 포함했는지 판단하여 포함하지 않은 경우에는 NORMAL을 반환하면 된다.

입력받은 암호의 각 문자를 비교해서 0-9까지의 문자가 없으면 NORMAL을 반환하도록 코드를 작성하면 다음과 같다.

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
        if (!containsNum) return  PasswordStrength.NORMAL;
        return PasswordStrength.STRONG;
    }
}

```

이 코드를 리랙토링해보자 ! (숫자가 들어있는지 비교하는 메서드를 따로 분류하자)

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }
        boolean containsNum = meetsContainingNumberCriteria(s);
        if (!containsNum) return  PasswordStrength.NORMAL;
        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}

```
<br>
테스트 코드 정리 I - 중복되는 코드 정리**

테스트코드도 유지보수 대상인데, 테스트 메서드에서 발생하는 중복을 알맞게 제거하거나 의미가 잘 드러나게 수정할 필요가 있다.

다음 중복되는 PasswordStrenghtMeter 객체를 생성하는 코드의 중복은 다음과 같이 해결할 수 있다.

```java
public class PasswordStrengthMeterTest {

    // 추가된 부분
    private PasswordStrengthMeter meter= new PasswordStrengthMeter();
    
    @Test
    @DisplayName("모든 규칙은 만족하는 경우")
    void meetsAllCriteria_Then_Stong() {
        PasswordStrength result = meter.meter("ab12!@AB");
        assertEquals(PasswordStrength.STRONG, result);
    }

    @Test
    @DisplayName("길이가 8글자 미만이고 나머지 조건은 충족하는 경우")
    void meetsOtherCriteria_excep_for_Length_Then_Normal() {
        PasswordStrength result = meter.meter("ab12!@A");
        assertEquals(PasswordStrength.NORMAL, result);
        PasswordStrength result2 = meter.meter("Ab12!c");
        assertEquals(PasswordStrength.NORMAL, result2);
    }

    @Test
    @DisplayName("숫자를 포함하지 않고 나머지 조건은 충족하는 경우")
    void meetsOtherCriteria_except_for_number_Then_Normal() {
        PasswordStrength result = meter.meter("ab!ABqwer");
        assertEquals(PasswordStrength.NORMAL, result);
    }
}
```

추가적으로 암호 강도 측정 기능부분에도 중복이 발생하고 있다.

해당 부분은 메서드를 이용하여 중복을 제거할 수 있다 . asserStrength 의 메서드를 생성하여 사용해주면 된다. 

```java
private void asserStrength(String password, PasswordStrength expStr) {
        PasswordStrength result = meter.meter(password);
        assertEquals(expStr, result);
    }
    
@Test
    @DisplayName("모든 규칙은 만족하는 경우")
    void meetsAllCriteria_Then_Stong() {
        assertStrength("ab12!@AB", PasswordStrength.STRONG);
        assertStrength("abc!Add", PasswordStrength.STRONG);
    }
```

테스트 코드 중복을 제거하여 정리하는 것은 좋지만 무턱대고 제거하면 안된다. 가독성과 수정이 용이한 경우에만 중복을 제거하도록 하자 !
<br>
네 번쨰 테스트 : 값이 없는 경우

meter()메서드에 null을 전달하게 되면 NPE가 발생하게된다. 우리가 만드는 것에서는 null에 대해서도 알맞게 동작해야한다. 따라서 다음 두 가지 방식으로 문제를 해결할 수 있다.

- IllegalArgumentException을 발생한다
- 유효하지 않은 암호를 의미하는 INVALID를 return한다.

nul인 경우 이외에도 빈 문자열인 경우도 추가해보면 다음과 같이 코드를 수정할 수 있다.

```java
public enum PasswordStrength {
    INVALID, NORMAL, STRONG
}
```

```java
@Test
    @DisplayName("값이 없는 경우")
    void nullInput_Then_Invalid() {
        assertStrength(null, PasswordStrength.INVALID);
        assertStrength("", PasswordStrength.INVALID);
    }
```

```java
public PasswordStrength meter(String s) {
            // 해당 부분 추가 null인 경우, 빈 문자열인 경우
        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;
        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }
        boolean containsNum = meetsContainingNumberCriteria(s);
        if (!containsNum) return  PasswordStrength.NORMAL;
        return PasswordStrength.STRONG;
    }
```
<br>
다섯 번째 테스트 : 대문자를 포함하지 않고 나머지 조건을 충족하는 경우

이번 경우에도 대문자가 포함되어있는지 코드를 작성해야하는데 이를 별도의 메서드로 분리하여 작성해보자 !

```java
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
       /*
       생략
       */
       
        boolean containsUpp = meetsContainingUppercaseCriteria(s);
        if (!containsUpp) return PasswordStrength.NORMAL;
        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String s) {
        /*
        생략
        */
    }

    private boolean meetsContainingUppercaseCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }
}
```

```java
@Test
    @DisplayName("대문자를 포함하지 않고 나머지 조건을 충족하는 경우")
    void meetsOtherCriteria_except_for_Uppercase_Then_Normal() {
        assertStrength("ab12!@df", PasswordStrength.NORMAL);
    }
```
<br>
여섯번째 테스트 : 길이가 8글자 이상인 조건만 충족하는 경우

한 가지 조건만 충족하는 경우 암호 강도는 약함이다. 이를 테스트하기 위한 코드는 다음과 같다.

```java
public enum PasswordStrength {
    INVALID, WEAK, NORMAL, STRONG
}
```

해당 테스트를 모두 통과하기 위해서는 if절의 위치를 적절하게 변경해주어야하는데 다음과 같은 이유로 변경이 필요하다.

- 개별 규칙을 검사하는 로직
- 규칙을 검사한 결과에 따라 암호 강도를 계산하는 로직

```java
public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;
        boolean lengthEnough = s.length() >= 8;
        boolean containsNum = meetsContainingNumberCriteria(s);
        boolean containsUpp = meetsContainingUppercaseCriteria(s);
        
        if (lengthEnough && !containsNum && !containsUpp) return PasswordStrength.WEAK;
        if (!lengthEnough) return PasswordStrength.NORMAL;
        if (!containsNum) return  PasswordStrength.NORMAL;
        if (!containsUpp) return PasswordStrength.NORMAL;
        
        return PasswordStrength.STRONG;
    }
```

이렇게하면 작성한 모든 테스트를 다 통과할 수 있게 된다.
<br>
일곱번째 테스트 : 숫자 포함 조건만 충족하는 경우

```java
public PasswordStrength meter(String s) {
        /*
        생략
        */
        if (!lengthEnough && containsNum && !containsUpp) return PasswordStrength.WEAK;
        /*
        생략
        */
    }
```

```java
@Test
    @DisplayName("숫자 포함 조건만 충족하는 경우")
    void meetsOnlyNumCriteria_Then_Weak() {
        assertStrength("12345", PasswordStrength.WEAK);
    }
```
<br>
여덟번째 테스트 : 대문자 포함 조건만 충족하는 경우

```java
public PasswordStrength meter(String s) {
        /*
        생략
        */
        if (!lengthEnough && !containsNum && containsUpp) return PasswordStrength.WEAK;
        /*
        생략
        */
    }
```

```java
@Test
    @DisplayName("대문자 포함 조건만 충족하는 경우")
    void meetsOnlyUpperCriteria_Then_Weak() {
        assertStrength("ABERD", PasswordStrength.WEAK);
    }
```
<br>
테스트 코드 정리 II - meter()메서드 리팩토링

```java
if (lengthEnough && !containsNum && !containsUpp) return PasswordStrength.WEAK;
if (!lengthEnough && containsNum && !containsUpp) return PasswordStrength.WEAK;
if (!lengthEnough && !containsNum && containsUpp) return PasswordStrength.WEAK;
```

이 코드는 3가지 조건 중 하나의 조건만 만족한 경우, WEAK를 반환하도록 한 코드이다. 그렇다면 다음과 같이 코드를 리팩토링 할 수 있다.

조건수에 해당하는 metCounts를 0으로 초기화하고 이를 만족하는 경우에만 count를 하게 된다. 

따라서 metCounts == 1 인 경우에는 조건을 하나만 충족하는 경우이기 떄문에 WEAK를 반환해주면 된다. 

```java
public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;
        int metCounts = 0; 
        boolean lengthEnough = s.length() >= 8;
        if (lengthEnough) metCounts++;
        boolean containsNum = meetsContainingNumberCriteria(s);
        if (containsNum) metCounts++;
        boolean containsUpp = meetsContainingUppercaseCriteria(s);
        if (containsUpp) metCounts++;
        
        if (metCounts == 1) return PasswordStrength.WEAK;
        // ===========================
        if (!lengthEnough) return PasswordStrength.NORMAL;
        if (!containsNum) return  PasswordStrength.NORMAL;
        if (!containsUpp) return PasswordStrength.NORMAL;
        // ============================
        return PasswordStrength.STRONG;
    }
```

그렇다면 다음 부분도 리팩토링이 가능할 것 같다 ! 해당 부분은 조건 중 2개만 만족하는 경우이므로 다음과 같이 코드를 수정해줄 수 있다. 또한 각 조건에 해당하는 3가지 변수도 제거하면 다음과 같이 리팩토링할 수 있다.

```java
public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;
        int metCounts = 0;
        if (s.length() >= 8) metCounts++;
        if (meetsContainingNumberCriteria(s)) metCounts++;
        if (meetsContainingUppercaseCriteria(s)) metCounts++;

        if (metCounts == 1) return PasswordStrength.WEAK;
        if (metCounts == 2) return PasswordStrength.NORMAL;
        return PasswordStrength.STRONG;
    }
```
<br>
아홉번째 테스트 : 아무 조건도 충족하지 않은 경우

```java
@Test
    @DisplayName("아무 조건도 충족하지 않은 경우")
    void meetsNoCriteria_Then_Weak() {
        assertStrength("abc", PasswordStrength.WEAK);
    }
```

![테스트 결과](images/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-04-15_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_3.57.42.png)

해당 테스트를 추가하고 실행해보면 실제값이 STRONG이 나와서 테스트에 통과하지 못한다.

따라서 우리는 다음과 같은 3가지 방식 중 하나를 선택하여 테스트를 통과할 수 있도록 코드를 수정해야한다.

```java
public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;
        int metCounts = 0;
        if (s.length() >= 8) metCounts++;
        if (meetsContainingNumberCriteria(s)) metCounts++;
        if (meetsContainingUppercaseCriteria(s)) metCounts++;
        
        if (metCounts <= 1) return PasswordStrength.WEAK; // 변경된 부분
        if (metCounts == 2) return PasswordStrength.NORMAL;
        return PasswordStrength.STRONG;
    }
```
<br>
테스트 코드 정리 III - 코드 가독성 개선

metCounts를 계산하는 부분을 별도의 메서드로 분리하여 코드의 가독성을 높일 수 있다.

```java
private int getMetCriteriaCounts(String s) {
        int metCounts = 0;
        if (s.length() >= 8) metCounts++;
        if (meetsContainingNumberCriteria(s)) metCounts++;
        if (meetsContainingUppercaseCriteria(s)) metCounts++;
        return metCounts;
    }
```

해당 메서드를 추가하고 meter()함수를 다음과 같이 수정해주면 된다.

```java
public PasswordStrength meter(String s) {
                // 1) 암호가 null이거나 빈 문자열이면 INVALID
        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;
        // 2) 충족하는 규칙의 개수를 구한다.
        int metCounts = getMetCriteriaCounts(s);

                // 3) 1개 이하면, WEAK, 2개면 NORMAL, 이외의 경우에는 STRONG
        if (metCounts <= 1) return PasswordStrength.WEAK;
        if (metCounts == 2) return PasswordStrength.NORMAL;
        return PasswordStrength.STRONG;
    }
```

이후에 테스트에서 메인으로 코드를 이동하여 구현을 마무리하면 된다.
<br>
<details>
<summary>실습 코드 첨부</summary>
<div>


    ```java
    package org.example.chap02;
    
    import org.junit.jupiter.api.DisplayName;
    import org.junit.jupiter.api.Test;
    
    import static org.junit.jupiter.api.Assertions.assertEquals;
    
    public class PasswordStrengthMeterTest {
    
        private PasswordStrengthMeter meter = new PasswordStrengthMeter();
    
        private void assertStrength(String password, PasswordStrength expStr) {
            PasswordStrength result = meter.meter(password);
            assertEquals(expStr, result);
        }
    
        @Test
        @DisplayName("모든 규칙은 만족하는 경우")
        void meetsAllCriteria_Then_Stong() {
            assertStrength("ab12!@AB", PasswordStrength.STRONG);
            assertStrength("abc!Adasa", PasswordStrength.NORMAL);
        }
    
        @Test
        @DisplayName("길이가 8글자 미만이고 나머지 조건은 충족하는 경우")
        void meetsOtherCriteria_excep_for_Length_Then_Normal() {
            assertStrength("ab12!@A", PasswordStrength.NORMAL);
            assertStrength("Ab12!c", PasswordStrength.NORMAL);
        }
    
        @Test
        @DisplayName("숫자를 포함하지 않고 나머지 조건은 충족하는 경우")
        void meetsOtherCriteria_except_for_number_Then_Normal() {
            assertStrength("ab!ABqwer", PasswordStrength.NORMAL);
        }
    
        @Test
        @DisplayName("값이 없는 경우")
        void nullInput_Then_Invalid() {
            assertStrength(null, PasswordStrength.INVALID);
            assertStrength("", PasswordStrength.INVALID);
        }
    
        @Test
        @DisplayName("대문자를 포함하지 않고 나머지 조건을 충족하는 경우")
        void meetsOtherCriteria_except_for_Uppercase_Then_Normal() {
            assertStrength("ab12!@df", PasswordStrength.NORMAL);
        }
    
        @Test
        @DisplayName("길이가 8글자 이상인 조건만 충족하는 경우")
        void meetsOnlyLengthCriteria_Then_Weak() {
            assertStrength("abedfghjk", PasswordStrength.WEAK);
        }
    
        @Test
        @DisplayName("숫자 포함 조건만 충족하는 경우")
        void meetsOnlyNumCriteria_Then_Weak() {
            assertStrength("12345", PasswordStrength.WEAK);
        }
    
        @Test
        @DisplayName("대문자 포함 조건만 충족하는 경우")
        void meetsOnlyUpperCriteria_Then_Weak() {
            assertStrength("ABERD", PasswordStrength.WEAK);
        }
    
        @Test
        @DisplayName("아무 조건도 충족하지 않은 경우")
        void meetsNoCriteria_Then_Weak() {
            assertStrength("abc", PasswordStrength.WEAK);
        }
    }
    
    ```
    
    ```java
    package org.example.chap02;
    
    public class PasswordStrengthMeter {
        public PasswordStrength meter(String s) {
            if (s == null || s.isEmpty()) return PasswordStrength.INVALID;
            int metCounts = getMetCriteriaCounts(s);
    
            if (metCounts <= 1) return PasswordStrength.WEAK;
            if (metCounts == 2) return PasswordStrength.NORMAL;
            return PasswordStrength.STRONG;
        }
    
        private boolean meetsContainingNumberCriteria(String s) {
            for (char ch : s.toCharArray()) {
                if (ch >= '0' && ch <= '9') {
                    return true;
                }
            }
            return false;
        }
    
        private boolean meetsContainingUppercaseCriteria(String s) {
            for (char ch : s.toCharArray()) {
                if (Character.isUpperCase(ch)) {
                    return true;
                }
            }
            return false;
        }
    
        private int getMetCriteriaCounts(String s) {
            int metCounts = 0;
            if (s.length() >= 8) metCounts++;
            if (meetsContainingNumberCriteria(s)) metCounts++;
            if (meetsContainingUppercaseCriteria(s)) metCounts++;
            return metCounts;
        }
    }
    
    ```
    
    ```java
    package org.example.chap02;
    
    public enum PasswordStrength {
        INVALID, WEAK, NORMAL, STRONG
    }
    
    ```

</div>
</details>
<br>
    
### ✅ 결론 : TDD 흐름

![TDD흐름](images/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-04-12_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_1.04.26.png)

TDD는 테스트를 먼저 작성하고 테스트를 통과시킬만큼 코드를 작성하고 리팩토링으로 마무리하는 과정을 반복한다. 

<aside>
🔑  레드-그린-리팩터 

- 레드 : 실패하는 테스트
- 그린 : 성공한 테스트
- 리팩터 : 리팩토링 과정

![레드-그린-리팩터 이미지](images/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-04-12_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%258C%25E1%2585%25A5%25E1%2586%25AB_1.05.22.png)

</aside>

테스트 코드를 먼저 작성하면 테스트가 개발을 주도하게 된다. 테스트 코드를 만들면 다음 개발 범위가 정해지고, 테스트 코드가 추가되면서 검증하는 범위가 넓어질수록 구현도 점점 완성된다.

구현을 완료한 후에는 리팩토링을 통해 지속적인 코드 정리를 지속적으로 하므로 코드 품질이 급격히 나빠지지 않게 해주는 효과가 있어, 유지보수 비용을 낮출 수 있다.

또한 새로운 코드를 추가하거나 기존 코드를 수정하면 테스트를 돌려 빠른 피드백이 가능하여 잘못된 코드가 배포되는 것을 방지할 수 있다.

