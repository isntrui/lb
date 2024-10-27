package ru.isntrui.lb.queries;

import ru.isntrui.lb.models.User;

public record InviteQuery(String inviteCode, String email){}
