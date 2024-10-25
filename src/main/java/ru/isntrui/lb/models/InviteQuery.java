package ru.isntrui.lb.models;

public record InviteQuery(User user, String inviteCode, String email, String token) {
}
