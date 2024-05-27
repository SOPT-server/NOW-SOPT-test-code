package menu.domain.repository;

import menu.domain.Coach;
import menu.domain.exception.CoachError;
import menu.domain.exception.CoachException;
import menu.global.constant.Standard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CoachRepository {

    private static List<Coach> coaches = new ArrayList<>();


    public static void init(List<String> coachNames) {
        if (coachNames.size() < Standard.MINIMUM_NUMBER_OF_COACH) {
            throw new CoachException(CoachError.TOO_FEW_COACH);
        }
        if (coachNames.size() > Standard.MAXIMUM_NUMBER_OF_COACH) {
            throw new CoachException(CoachError.TOO_MANY_COACH);
        }
        coaches = coachNames.stream()
                .map(Coach::new)
                .collect(Collectors.toList());
    }

    public static List<Coach> coaches() {
        return Collections.unmodifiableList(coaches);
    }
}
