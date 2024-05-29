## BDDMockito를 활용한 moonshot Testcode

### Mocktio란?

Spring은 DI(*Dependency Injection*)를 지원해주는데, 이는 객체 간의 의존성을 Spring이 관리해주는 것이다. 그 덕분에 개발자는 의존성 주입을 신경 쓰지 않고 객체 간의 의존 관계만 잘 고민해서 객체를 설계하면 된다.

그런데 이런 의존성은 테스트를 하는 시점에서 문제를 발생시킨다. 

단위 테스트를 작성할 경우 해당 객체에 대한 기능만을 테스트하고 싶은데 의존성을 가지는 다른 객체에 의해 테스트 결과가 영향을 받을 수 있다는 것이다. 

이렇게 의존을 가지는 객체를 우리가 원하는 동작만 하도록 만든 것이 `Mock 객체`이다. 따라서 테스트 하려는 코드가 의존하고 있는 객체를 가짜로 만들어 의존성 제거하고 객체의 동작을 통제하기 위해 사용한다. 

이러한 Mock객체를 직접 만들고 관리하기 쉽지 않은데, **`Mockito`**는 단위 테스트를 위한 Java Mocking Framework로, JUnit에서 가짜 객체인 Mock객체를 생성해주고 관리하고 검증할 수 있도록 지원해주는 Framework이다. 구현체가 아직 없는 경우나 구현체가 있더라도 특정 단위만 테스트하고 싶을 경우 사용할 수 있도록 적절한 환경을 제공해준다.

<br>

Mockito의 수행과정은 다음과 같다.

> Mock : 모의 객체 생성 → Stub : 메서드 호출 예상 동작 설정 → Verify : 메서드 호출 검증
> 

 첫 번째 `**모의 객체 생성 : Mock**` 단계에서는 Mockito를 사용하여 테스트에 필요한 객체의 모의(가짜) 객체를 생성한다. 이 모의 객체는 실제 객체와 비슷한 행동을 하지만, 프로그래머가 원하는 대로 조작할 수 있다.

두 번째 `**메서드 호출 예상 동작 설정 : Stub**` 단계에서는 모의 객체의 메서드 호출에 대한 ‘예상 동작’을 정의합니다.

세 번째 `**메서드 호출 검증 : Verify**`에서는 모의 객체에 대해 특정 메서드가 호출되고 예상된 인자와 함께 호출되었는지를 검증하는 메서드를 제공한다. 

mockito를 사용하여 작성한 예시는 다음과 같다.

```java
@Test
void test() {
    // given
    when(drawMock.getId()).thenReturn(1L);

    // when

    // then
}
```

**Mock Annotation**

| Annotation | 설명 |
| --- | --- |
| @Mock | 모의 객체(Mock Object)를 생성하는데 사용됩니다. 
(사용 타입 : Repository / Mapper) |
| @Spy | 스파이 객체(Spy Object)를 생성하는데 사용됩니다. |
| @Captor | 모의 객체에 전달된 메서드 인수를 캡처하는데 사용됩니다. |
| @InjectMocks | 테스트 대상이 되는 객체에 ‘모의 객체를 생성하고 자동으로 주입’할때 사용이 됩니다.
(사용타입 : Service (implements)) |
| @MockBean | 스프링 프레임워크에서 사용되며, 테스트용 Mock 객체를 생성하고 주입하는 데 사용됩니다. |
| @MockitoSettings | Mockito의 설정을 변경하거나 커스터마이즈할 때 사용됩니다. |
| @MockitoJUnitRunner | JUnit 테스트에서 Mockito를 사용하기 위해 실행할 때 사용됩니다. |
| @BDDMockito | BDD 스타일의 테스트를 위해 Mockito를 사용할 때 사용됩니다. |

### BDDMockito란?

먼저 BDD란 무엇일까? 

Behavior-Driven Development의 약자로 행위 주도 개발을 말한다. 테스트 대상의 상태의 변화를 테스트하는 것이고, 시나리오를 기반으로 테스트하는 패턴을 권장한다.

여기서 권장하는 기본 패턴은 Given, When, Then 구조를 가진다.

이는 테스트 대상이 A 상태에서 출발하며(Given) 어떤 상태 변화를 가했을 때(When) 기대하는 상태로 완료되어야 한다(Then).

```java
@Test
void test() {
    // given
    when(drawMock.getId()).thenReturn(1L);

    // when

    // then
}
```

mock 객체의 동작을 미리 설정할 때 when then 을 사용하면 given 동작에서 when 을 쓰게 되므로 의미가 불분명해진다. 

```java
void test() {
    // given
    given(drawMock.getId()).willReturn(1L);

    // when

    // then
}
```

BDDMockito는 Mockito와 기능이 같으나, BDD를 사용하여 테스트코드를 작성할 때, 사용자의 시나리오에 맞게 테스트 코드가 읽힐 수 있도록 도와주는 프레임워크이다.

### moonshot 서비스 간단 설명

mooshot 서비스의 경우 objective → keyResult → task 와 같이 의존성을 가지고 있으므로 테스트를 하기 위해서는 많은 세팅이 필요하다. 따라서 mock객체를 활용한 테스트코드를 작성하기로 하였고, 그 중에서 given절의 가독성을 높이기 위하여 BDDMockito를 사용하여 테스트코드를 작성하였다. 

### TaskService TestCode

Task 생성과 관련된 메서드 테스트 코드는 다음과 같다. TaskService의 createTask() 에 대하여 작성하였다.

**`TaskService Code`**

```java
public class TaskService implements IndexService {

    private final KeyResultRepository keyResultRepository;
    private final TaskRepository taskRepository;
    public void createTask(final TaskSingleCreateRequestDto request, final Long userId) {
        KeyResult keyResult = keyResultRepository.findKeyResultAndObjective(request.keyResultId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_KEY_RESULT));
        validateUserAuthorization(keyResult.getObjective().getUser().getId(), userId);

        List<Task> taskList = taskRepository.findAllByKeyResultOrderByIdx(keyResult);
        validateActiveTaskSizeExceeded(taskList.size());
        validateIndexUnderMaximum(request.taskIdx(), taskList.size());

        taskRepository.bulkUpdateTaskIdxIncrease(request.taskIdx(), taskList.size(), keyResult.getId(), -1L);

        saveTask(keyResult, request);
    }
    
    
    /*
    생략
    */
    
   }
```

**`TaskValidator`**

```java
 public static void validateActiveTaskSizeExceeded(final int taskListSize) {
        if (taskListSize >= ACTIVE_TASK_NUMBER) {
            throw new BadRequestException(ACTIVE_TASK_NUMBER_EXCEEDED);
        }
    }

    public static void validateIndexUnderMaximum(final int requestIndex, final int totalTaskListSize) {
        if (requestIndex > totalTaskListSize || requestIndex < 0) {
            throw new BadRequestException(INVALID_TASK_INDEX);
        }
    }
```

**`TaskService TestCode`**

```java
package org.moonshot.task.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.moonshot.exception.BadRequestException;
import org.moonshot.keyresult.model.KeyResult;
import org.moonshot.keyresult.repository.KeyResultRepository;
import org.moonshot.keyresult.service.KeyResultService;
import org.moonshot.objective.model.Objective;
import org.moonshot.objective.repository.ObjectiveRepository;
import org.moonshot.task.dto.request.TaskSingleCreateRequestDto;
import org.moonshot.task.repository.TaskRepository;
import org.moonshot.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private KeyResultRepository keyResultRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private static User fakeUser;

    @BeforeAll
    static void setUp() {
        Long fakeUserId = 1L;
        String fakeUserNickname = "tester";
        fakeUser = User.buildWithId().id(fakeUserId).nickname(fakeUserNickname).build();
    }

    @Test
    @DisplayName("단일 Task를 생성합니다")
    void 단일_Task를_생성합니다() {
        // given
        Objective testObjective = mock(Objective.class);
        KeyResult testKeyResult = mock(KeyResult.class);
        TaskSingleCreateRequestDto request = new TaskSingleCreateRequestDto(
                1L, "test task", 0);

        given(keyResultRepository.findKeyResultAndObjective(request.keyResultId())).willReturn(Optional.of(testKeyResult));
        given(testKeyResult.getObjective()).willReturn(testObjective);
        given(testObjective.getUser()).willReturn(fakeUser);
        given(testKeyResult.getId()).willReturn(1L);

        // when
        taskService.createTask(request, fakeUser.getId());

        /*
        생성을 검증할 때, 현재 mock객체를 사용하고 있기 때문에 
        직접적으로 task가 생성되었는지 검증할 방법이 없으므로, 
			  bulkUpdateIdx를 이용해서 검증을 진행하였습니다.
			  */
			  // then
        verify(taskRepository, times(1)).bulkUpdateTaskIdxIncrease(any(Integer.class), any(Integer.class), eq(1L), eq(-1L));
    }

    @Test
    @DisplayName("Task 생성 시 최대 보유 갯수를 넘어 예외가 발생합니다")
    void Task_생성_시_최대_보유_갯수를_넘어_예외가_발생합니다() {
        // given
        Objective testObjective = mock(Objective.class);
        KeyResult testKeyResult = mock(KeyResult.class);
        List taskList = mock(List.class);
        TaskSingleCreateRequestDto request = new TaskSingleCreateRequestDto(
                1L, "test task", 0);

        given(keyResultRepository.findKeyResultAndObjective(request.keyResultId())).willReturn(Optional.of(testKeyResult));
        given(testKeyResult.getObjective()).willReturn(testObjective);
        given(testObjective.getUser()).willReturn(fakeUser);
        given(taskList.size()).willReturn(3);
        given(taskRepository.findAllByKeyResultOrderByIdx(testKeyResult)).willReturn(taskList);

        // when, then
        assertThatThrownBy(() -> taskService.createTask(request, fakeUser.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("허용된 Task 개수를 초과하였습니다.");
    }

    @Test
    @DisplayName("Task 생성 시 인덱스가 0 ~ ListSize(최대 개수의 범위)를 벗어나 요청된 경우 예외가 발생합니다")
    void Task_생성_시_인덱스가_0_ListSize를_벗어나_요청된_경우_예외가_발생합니다() {
        // given
        Objective testObjective = mock(Objective.class);
        KeyResult testKeyResult = mock(KeyResult.class);
        List taskList = mock(List.class);
        TaskSingleCreateRequestDto request = new TaskSingleCreateRequestDto(
                1L, "test task", -1);

        given(keyResultRepository.findKeyResultAndObjective(request.keyResultId())).willReturn(Optional.of(testKeyResult));
        given(testKeyResult.getObjective()).willReturn(testObjective);
        given(testObjective.getUser()).willReturn(fakeUser);
        given(taskList.size()).willReturn(2);
        given(taskRepository.findAllByKeyResultOrderByIdx(testKeyResult)).willReturn(taskList);

        // when, then
        assertThatThrownBy(() -> taskService.createTask(request, fakeUser.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("정상적이지 않은 Task 위치입니다.");
    }

}

```

마지막 테스트를 하면서 -1로 요청을 보냈을 때 테스트에 실패하는 것을 확인하고, TaskValidator를 수정하여 테스트를 통과시켰다.

### 참고
[mock을 왜 사용하고 있었지?](https://velog.io/@myspy/mock을-왜-사용하고-있었지)

[Mockito 와 BDDMockito 는 어떻게 구분하여 사용해야 할까?](https://velog.io/@yhlee9753/Mockito-와-BDDMockito-는-어떻게-구분하여-사용해야-할까)

[Mockito와 BDDMockito는 뭐가 다를까?](https://tecoble.techcourse.co.kr/post/2020-09-29-compare-mockito-bddmockito/)
