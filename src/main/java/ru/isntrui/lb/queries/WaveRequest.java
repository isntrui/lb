package ru.isntrui.lb.queries;

import ru.isntrui.lb.enums.WaveStatus;

import java.time.LocalDate;

public record WaveRequest(String title, LocalDate startsOn, LocalDate endsOn, WaveStatus status) {

}
