package ru.Bogachev.pet.domain.entity.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TimeOfDayTest {

    @Test
    public void testGetTimeOfDayForTime_DayTime () {
        LocalDateTime dayTime = LocalDateTime.of(2023, 10, 23, 12, 0);
        TimeOfDay result = TimeOfDay.getTimeOfDayForTime(dayTime);
        assertEquals(TimeOfDay.DAY, result);
    }
    @Test
    public void testGetTimeOfDayForTime_NightTime() {
        LocalDateTime nightTime = LocalDateTime.of(2023, 10, 23, 2, 0);
        TimeOfDay result = TimeOfDay.getTimeOfDayForTime(nightTime);
        assertEquals(TimeOfDay.NIGHT, result);
    }
    @Test
    public void testGetTimeOfDayForTime_NullTime() {
        LocalDateTime nullTime = null;
        TimeOfDay result = TimeOfDay.getTimeOfDayForTime(nullTime);
        assertEquals(TimeOfDay.UNDEFINED, result);
    }
}
