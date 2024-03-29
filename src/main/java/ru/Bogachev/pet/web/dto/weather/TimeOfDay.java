package ru.Bogachev.pet.web.dto.weather;

import java.time.LocalDateTime;

public enum TimeOfDay {
    DAY,
    NIGHT,
    UNDEFINED;

    public static TimeOfDay getTimeOfDayForTime(final LocalDateTime time) {
        if (time == null) {
            return UNDEFINED;
        }
        int currentTime = time.getHour();
        return currentTime >= 8 && currentTime <= 20 ? DAY : NIGHT;
    }
}
