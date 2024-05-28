# [테스트 코드의 구성]
## 기능에서의 상황
기능은 상황에 따라 다르게 동작한다.<br>
파일로부터 숫자를 읽어와, 그 숫자들의 합을 구하는 기능을 생각해보자.<br>

이 기능을 `MathUtils.sumNumbersFromFile`라는 이름의 함수로 구현했다고 가정하자.<br>
```java
File datafile = new File("data.txt");
long sum = MathUtils.sumNumbersFromFile(datafile);
```

그러나 위 코드에서 고려해야 하는 점이 있다.
1. 파일이 존재하지 않을 수 있다.
2. 파일이 잘못된 형식일 수 있다.
3. 파일에 숫자가 아닌 값이 포함되어 있을 수 있다.

위와 같은 상황을 고려하여 각각 다른 값을 반환하도록 설계해야 한다.

개발자는 이런 다양한 정상 동작 시나리오 뿐만 아니라 얘외 상황에 대해 생각하고, 코드를 반영해야 코드 퀄리티를 높일 수 있다.

## 테스트 코드 구성 요소
테스트 코드는 상황, 실행, 결과 확인으로 구성된다.<br>
상황, 실행, 결과 확인은 각각 `given`, `when`, `then`으로 표현한다.<br>

```java
@Test
public void testSumNumbersFromFile() {
    // given
    File datafile = new File("data.txt");
    
    // when
    long sum = MathUtils.sumNumbersFromFile(datafile);
    
    // then
    assertEquals(10, sum);
}
```

또는 @BeforeEach를 사용하여 `given`에 해당하는 상황을 설정할 수 있다.<br>

실행 결과를 확인하는 가장 쉬운 방법은 return 값을 확인하는 것이다.<br>
그러나 결과가 꼭 값이 아닐 수도 있다.<br>
예외를 던지는 것이 결과인 경우, `assertThrows`를 사용하여 예외를 확인할 수 있다.<br>


## 외부 상황 / 외부 결과
상황 설정이 테스트 대상에 국한되지 않았을 수도 있다.
```java
File datafile = new File("data.txt");
long sum = MathUtils.sum(datafile);
```

위 코드에서 `data.txt` 파일이 존재하지 않는다면, `FileNotFoundException`이 발생할 것이다.<br>
`data.txt`가 우연으로라도 존재하지 않는 상황을 명시적으로 나타낼 수 있다.<br>
```java
@Test
public void noDataFile_Then_Exception() {
    givenNoFile("non-exist-file.txt");
    File datafile = new File("non-exist-file.txt");
    
    assertThrows(FileNotFoundException.class, 
            () -> MathUtils.sum(datafile));
}

private void givenNoFile(String filename) {
    File file = new File(filename);
    if (file.exists()) {
        boolean deleted = file.delete();
        if (!deleted) {
            throw new RuntimeException("fail givenNoFile");
        }
    }
}
```

이렇게 하면, 테스트 코드 안에 필요한 것이 모두 포함되어 있다는 장점이 있다.<br>

### 외부 상태가 테스트 코드에 영향을 주지 않게
테스트는 DB와 같은 외부 상태에 따라 결과가 달라질 수 있다.<br>
이런 경우, 테스트 코드 안에서 외부 상태를 초기화하거나, 테스트 전후에 초기화하는 코드를 작성해야 한다.<br>


### 외부 상태와 테스트의 어려움
반면 외부 상태가 테스트에 필연적으로 영향을 주는 경우, 테스트 권한이 없다면 테스트를 작성하기 어렵다.<br>
이런 경우는 7장에서 다룬다.