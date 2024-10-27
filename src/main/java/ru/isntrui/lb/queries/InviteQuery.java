package ru.isntrui.lb.queries;

import ru.isntrui.lb.models.User;

public record InviteQuery(User user, String inviteCode, String email, String token) {
}
