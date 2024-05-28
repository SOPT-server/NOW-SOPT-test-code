# TDD 1주차

## TDD이전의 개발

1. 만들 기능에 대한 설계 → 어떤 클래스, 인터페이스를 생성할 것인가
2. 과정 1을 하며 구현애 대해서 고민, 기능 구현
3. 구현한 기능에 대한 테스트, 오류시 작성한 코드 디버깅하며 원인 찾음

→ 디버깅 하는데 시간이 오래 걸림, 테스트 완료되지 않은 코드가 배포될 수도, 테스트 하는데도 시간 오래걸림

## TDD란?

- 구현보다 테스트를 먼저
- 어떻게 코드가 없는데 테스트?

  테스트를 먼저 한다는 것은 기능이 올바르게 작동하는지 검증하는 테스트 코드를 작성한다는 것을 의미

- 검증을 위한 테스트 코드를 먼저 작성하고 테스트를 통과시키기 위해 개발 진행

## 간단한 tdd예시

```jsx

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals; 06

public class CalculatorTest {
	@Test
	void plus() {
		int result = Calculator plus(1, 2);
	}
}
```

→ 아직 구현이 되지 않은 코드에 대한 검증하는 테스트를 먼저 작성한다.

검증을 위한 테스트 코드를 작성하면서 다음과 같은 생각을 할 수 있음

- 메서드 이름은?
- 메서드의 파라미터 타입은? 반환 값은?
- 정적 메서드로? 인스턴스로?
- 메서드를 제공할 클래스의 이름은?

이제 해당 테스트 코드를 위한 구현을 시작한다.

```jsx
public class Calculator {
	public static int plus(int a1, int a2) {
			//return 3; -> 테스트 코드는 원래 점진적으로 구현을 완성해나가야함, 아래 다른 예시에서 설명
			return a1 + a2
	}
}
```

→ 구현을 위한 코드는 **src/test/java에** 작성 후 테스트가 모두 완료되면 **src/main/java로 옮김**

## 암호검사기를 통한 TDD이해

### 검사할 규칙

- 길이가 8글자 이상
- 0-9까지의 숫자를 포함
- 대문자 포함
- 규칙 3개 충족 → 강함
- 규칙 2개 충족 → 보통
- 규칙 1개 충족 → 약함

<aside>
💡 첫번째 테스트는 가장 쉽거나 예외적인 상황을 선택

모든 조건을 충족하는 경우는 → 각 조건 검사하는 코드 만들지 않고 강함만 reutrn하면 됨
모든 조건을 충족하지 못하는 경우 → 각 조건을 모두 구현해야함 → 구현 다하고 테스트하는 방식과 같음

</aside>

### 첫번째 테스트 모든 규칙을 충족하는 경우

```jsx

import org.junit.jupiter.api. Test;

import static org.junit.jupiter.api.Assertions.assertEquals; 06

public class PasswordStrengthMeterTest {

@Test
void meetsAllCriteria_Then_Strong() {
		PasswordStrengthMeter meter =new PasswordStrengthMeter(); 
		PasswordStrength result = meter.meter"ab12!@AB");
		assertEquals(PasswordStrength.STRONG, result);
		PasswordStrength result2 = meter.meter("abel!Add");
		assertEquals(PasswordStrength.STRONG, result2);
}

```
```jsx
public class PasswordStrengthMeter{
		public PasswordStrengthMeter(String s) {
				return PasswordStrength.STRONG;
		}
}
```
### 두번째 테스트 길이만 8글자 미만이고 나머지 조건은 충족하는 경우
```jsx
@Test
void meetsOtherCriteria_except_for_Length_Then_Normal() {
		PasswordStrengthMeter meter = new PasswordStrengthMeter();
		PasswordStrength result = meter.meter("ab12!@A");
		assertEquals(PasswordStrength.NORMAL, result);
}
```
마찬가지로 테스트 코드 먼저 작성 이후 구현 시작
```jsx
public class PasswordStrengthMeter{
		public PasswordStrengthMeter(String s) {
				if(s.length() < 8) {
						return PasswordStrength.NORMAL;
				}
				return PasswordStrength.STRONG;
		}
}
```
### 세번째 테스트 : 숫자를 포함하지 않고 나머지 조건은 충족하는 경우
다음과 같은 테스트 코드 작성
```jsx
@Test
void meetsOtherCriteria_except_for_number_Then_Normal() {
		PasswordStrengthMeter meter = new PasswordStrengthMeter();
		PasswordStrength result = meter.meter("ab!@ABqwer");
		assertEquals(PasswordStrength.Normal, result);
}
```
마찬가지로 테스트 코드 작성 후 구현을 위한 코드 작성
```jsx
public class PasswordStrengthMeter{
		public PasswordStrengthMeter(String s) {
				if (s.length() < 8) {
						return PasswordStrength.NORMAL;
				}
				boolean containsNumber = meetsContainingNumberCriteria(s);
				if (!containsNumber) return PasswordStrength.NORMAL;
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
어느정도 테스트 코드를 작성하다가 보면 테스트 코드도 리팩토링해야 한다.
테스트 코드도 리팩토링 대상이기 때문이다.
테스트 코드에서도 중복되는 코드들을 정리해보자
```jsx
public class PasswordStrengthMeterTest {
    private PasswordStrengthMeter meter = new PasswordStrengthMeter();

    private void assertStrength(String password, PasswordStrength expStr) {
        PasswordStrength result = meter.meter(password);
        assertEquals(expStr, result);
    }

    @Test
    void meetsAllCriteria_Then_Strong() {
        assertStrength("ab12!@AB", PasswordStrength.STRONG);
        assertStrength("abc1!Add", PasswordStrength.STRONG);

    }

    @Test
    void meetsOtherCriteria_except_for_Length_Then_Normal() {
        PasswordStrength result = meter.meter("ab12!@A");
        assertEquals(PasswordStrength.NORMAL, result);
        assertStrength("ab12!@A", PasswordStrength.NORMAL);
    }

    @Test
    void meetsOtherCriteria_except_for_number_Then_Normal() {
        assertStrength("ab!@ABqwer", PasswordStrength.NORMAL);
    }
```
### 네번째 테스트 : 값이 없는 경우
값이 없거나 빈값을 주는 경우도 테스트 코드에 추가해야 한다.
```jsx
    @Test
    void nullInput_Then_Invalid() {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @Test
    void emptyInput_Then_Invalid() {
        assertStrength("", PasswordStrength.INVALID);
    }

```
구현을 위한 코드는 다음과 같다
```jsx
public class PasswordStrengthMeter{
		public PasswordStrengthMeter(String s) {
				if ( s == null || s.isEmpty()) return PasswordStrength.INVALID;
				if (s.length() < 8) {
						return PasswordStrength.NORMAL;
				}
				boolean containsNumber = meetsContainingNumberCriteria(s);
				if (!containsNumber) return PasswordStrength.NORMAL;
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
### 다섯번째 테스트 : 대문자를 포함하지 않고 나머지 조건을 충족하는 경우
테스트 코드 추가
```jsx
    @Test
    void meetsOtherCriteria_except_for_Uppercase_Then_Normal() {
        assertStrength("ab12!@df", PasswordStrength.NORMAL);
    }
```
테스트를 통과하기 위한 구현
```jsx
public class PasswordStrengthMeter{
		public PasswordStrengthMeter(String s) {
				if ( s == null || s.isEmpty()) return PasswordStrength.INVALID;
				if (s.length() < 8) {
						return PasswordStrength.NORMAL;
				}
				boolean containsNumber = meetsContainingNumberCriteria(s);
				if (!containsNumber) return PasswordStrength.NORMAL;
				boolean containsUppercase = meetsContainingUppercaseCriteria(s);
				if(!containsUppercase) return PasswordStrength.NORMAL;
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
}
```
### 여섯번째 테스트 : 길이가 8글자 이상인 조건만 충족하는 경우
```jsx
    @Test
    void meetsOnlyLengthCriteria_Then_Weak() {
        assertStrength("abdefghi", PasswordStrength.WEAK);
    }
```
통과하기 위한 구현
```jsx
public class PasswordStrengthMeter{
		public PasswordStrengthMeter(String s) {
				if ( s == null || s.isEmpty()) return PasswordStrength.INVALID;
				boolean lengthEnough = s.length() >= 9;
				boolean containsNumber = meetsContainingNumberCriteria(s);
				boolean containsUppercase = meetsContainingUppercaseCriteria(s);
				if (lengthEnough && !containsNumber && !containsUppercase) return PasswordStrength.WEAK;
				if (!lengthEnough) return PasswordStrength.NORMAL;
				if (!containsNumber) return PasswordStrength.NORMAL;
				if(!containsUppercase) return PasswordStrength.NORMAL;
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
}
```
### 일곱번째 테스트 : 숫자 포함 조건만 충족하는 경우
테스트를 위한 코드
```jsx
   @Test
    void meetsOnlyNumCriteria_Then_Weak() {
        assertStrength("12345", PasswordStrength.WEAK);
    }
```
통과하기 위한 구현
```jsx
public class PasswordStrengthMeter{
		public PasswordStrengthMeter(String s) {
				if ( s == null || s.isEmpty()) return PasswordStrength.INVALID;
				boolean lengthEnough = s.length() >= 9;
				boolean containsNumber = meetsContainingNumberCriteria(s);
				boolean containsUppercase = meetsContainingUppercaseCriteria(s);
				if (lengthEnough && !containsNumber && !containsUppercase) return PasswordStrength.WEAK;
				if (!lengthEnough && containsNumber && !containsUppercase) return PasswordStrength.WEAK;
				if (!lengthEnough) return PasswordStrength.NORMAL;
				if (!containsNumber) return PasswordStrength.NORMAL;
				if(!containsUppercase) return PasswordStrength.NORMAL;
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
}
```
### 여덟번째 테스트 : 대문자 포함 조건만 충족하는 경우
```jsx
    @Test
    void meetsOnlyUpperCriteria_Then_Weak() {
        assertStrength("ABZEF", PasswordStrength.WEAK);
    }
```
테스트 통과 위한 구현
```jsx
public class PasswordStrengthMeter{
		public PasswordStrengthMeter(String s) {
				if ( s == null || s.isEmpty()) return PasswordStrength.INVALID;
				boolean lengthEnough = s.length() >= 9;
				boolean containsNumber = meetsContainingNumberCriteria(s);
				boolean containsUppercase = meetsContainingUppercaseCriteria(s);
				if (lengthEnough && !containsNumber && !containsUppercase) return PasswordStrength.WEAK;
				if (!lengthEnough && containsNumber && !containsUppercase) return PasswordStrength.WEAK;
				if (!lengthEnough && !containsNumber && containsUppercase) return PasswordStrength.WEAK;
				if (!lengthEnough) return PasswordStrength.NORMAL;
				if (!containsNumber) return PasswordStrength.NORMAL;
				if(!containsUppercase) return PasswordStrength.NORMAL;
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
}
```
### 테스트 통과 위한 구현 코드 리팩토링
강도를 리턴하기 위한 분기 처리들이 너무 복잡하다.
핵심은 몇개의 조건을 충족했느냐 이기에 다음과 같이 리팩토링한다.
```jsx
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) return  PasswordStrength.INVALID;

        int meterCounts = getMeterCriteriaCounts(s);

        if (meterCounts == 1) {
            return PasswordStrength.WEAK;
        }

        if (meterCounts == 2) {
            return PasswordStrength.NORMAL;
        }

        return PasswordStrength.STRONG;
    }

    private int getMeterCriteriaCounts(String s) {
        int meterCounts = 0;
        if (s.length() >= 8) {
            meterCounts++;
        }

        if (meetsContainingNumberCriteria(s)) {
            meterCounts++;
        }

        if (meetsContainingUppercaseCriteria(s)) {
            meterCounts++;
        }
        return meterCounts;
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
}

```
### 아홉번쨰 테스트 : 아무조건도 만족하지 않는 경우
```jsx
    @Test
    void meetsNoCriteria_Then_Weak() {
        assertStrength("abc", PasswordStrength.WEAK);
    }
```
→ PasswordStrength에서 meterCounts가 1이하이면 WEAK을 반환하도록 변경
```jsx
public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) return  PasswordStrength.INVALID;

        int meterCounts = getMeterCriteriaCounts(s);

        if (meterCounts <= 1) {
            return PasswordStrength.WEAK;
        }

        if (meterCounts == 2) {
            return PasswordStrength.NORMAL;
        }

        return PasswordStrength.STRONG;
    }

    private int getMeterCriteriaCounts(String s) {
        int meterCounts = 0;
        if (s.length() >= 8) {
            meterCounts++;
        }

        if (meetsContainingNumberCriteria(s)) {
            meterCounts++;
        }

        if (meetsContainingUppercaseCriteria(s)) {
            meterCounts++;
        }
        return meterCounts;
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
}

```
### 패키지 이동
앞서 말했던 것처럼 테스트를 진행하는 동안의 구현 코드는 src/test/java에 위치
테스트를 모두 통과했다면 src/main/java로 이동시켜야 한다.
## TDD흐름
1. TDD는 기능을 검증하는 테스트를 먼저 작성한다.
2. 작성한 테스트를 통과하지 못하면 테스트를 통과할 만큼만 코드를 작성
3. 테스트를 통과한 뒤에는 개선할 코드가 있으면 리팩토링한다.
4. 리팩토링을 수행한 뒤에는 다시 테스트를 실행해서 기존 기능이 망가지지 않았는지 확인한다.
반복
### 레드 그린 리팩터
방금말한 tdd 사이클을 레드 그린 리팩터로 부르기도 한다
레드는 **테스트 실패**를 의미
그린은 **성공한 테스트**를 의미 → 코드를 구현해서 실패하는 테스트를 통과시켜라
이후 리팩토링을 해라
