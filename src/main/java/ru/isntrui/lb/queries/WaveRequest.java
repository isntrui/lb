package ru.isntrui.lb.queries;

import ru.isntrui.lb.enums.WaveStatus;

import java.time.LocalDate;

public record WaveRequest(String title, LocalDate starts_on, LocalDate ends_on, WaveStatus status) {

}
