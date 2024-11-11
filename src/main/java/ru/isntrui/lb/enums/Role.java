package ru.isntrui.lb.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    TECHNICAL(false),
    DESIGNER(false),
    WRITER(false),
    COORDINATOR(true),
    HEAD(true),
    DIRECTOR(false),
    GRADUATED(false),
    ADMIN(true),
    SOUNDDESIGNER(false);

    private final boolean isAdmin;
}
