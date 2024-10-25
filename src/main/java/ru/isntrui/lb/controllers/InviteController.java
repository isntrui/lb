package ru.isntrui.lb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.models.Invite;
import ru.isntrui.lb.models.InviteQuery;
import ru.isntrui.lb.services.InviteService;

@RestController
@RequestMapping("/api/invite/")
public class InviteController {
    @Autowired
    private InviteService inviteService;

    @PostMapping("create")
    public ResponseEntity<Invite> create(@RequestBody InviteQuery query) {
        if (query.user().getRole().equals(Role.HEAD) || query.user().getRole().equals(Role.COORDINATOR)) {
            inviteService.create(query.email(), query.inviteCode());
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
    }
}
