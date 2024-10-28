package ru.isntrui.lb.queries;

import ru.isntrui.lb.enums.WaveStatus;

public record WaveRequest(String title, String starts_on, String ends_on, WaveStatus status) {

}
