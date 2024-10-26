package ru.isntrui.lb.controllers;

import cn.hutool.log.dialect.log4j.Log4jLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.models.Invite;
import ru.isntrui.lb.models.InviteQuery;
import ru.isntrui.lb.services.InviteService;

import java.util.List;

@RestController
@RequestMapping("/api/invite/")
public class InviteController {
    @Autowired
    private InviteService inviteService;

    @PostMapping("create")
    public ResponseEntity<Invite> create(@RequestBody InviteQuery query) {
        System.out.println(query.user());
        if (query.user().getRole().toString().equals(Role.HEAD.toString()) || query.user().getRole().equals(Role.COORDINATOR)) {
            inviteService.create(query.email(), query.inviteCode());
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
    }
/*
    @GetMapping("all")
    public ResponseEntity<List<Invite>> getAll(@RequestParam JWT)

 */
}
