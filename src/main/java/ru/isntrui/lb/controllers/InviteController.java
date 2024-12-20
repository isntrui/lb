package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.models.Invite;
import ru.isntrui.lb.queries.InviteQuery;
import ru.isntrui.lb.services.InviteService;
import ru.isntrui.lb.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Invite")
@RestController
@RequestMapping("/api/invite/")
public class InviteController {
    private final InviteService inviteService;
    private final UserService userService;

    @Autowired
    public InviteController(InviteService inviteService, UserService userService) {
        this.inviteService = inviteService;
        this.userService = userService;
    }

    @Operation(summary = "Create new invite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invite created"),
            @ApiResponse(responseCode = "403", description = "Forbidden: user is not allowed to create an invite")
    })
    @PostMapping("create")
    public ResponseEntity<Void> create(@RequestBody InviteQuery query) {
        if (userService.getCurrentUser().getRole().toString().equals(Role.HEAD.toString()) || userService.getCurrentUser().getRole().equals(Role.COORDINATOR) || userService.getCurrentUser().getRole().equals(Role.ADMIN)) {
            try {
                inviteService.create(query.email(), query.code());
            } catch (RuntimeException ex) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
    }

    @Operation(summary = "Get invite by code", description = "Only available for admins")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invite found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: user is not allowed to get an invite")
    })
    @GetMapping("all")
    public ResponseEntity<List<Invite>> getAll() {
        if (!(userService.getCurrentUser().getRole().equals(Role.COORDINATOR) || userService.getCurrentUser().getRole().equals(Role.HEAD) || userService.getCurrentUser().getRole().equals(Role.ADMIN))) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
        }
        ArrayList<Invite> invites = new ArrayList<>();
        Iterable<Invite> all = inviteService.getAllInvites();
        all.forEach(invites::add);
        return ResponseEntity.ok(invites);
    }

    @DeleteMapping("delete")
    public ResponseEntity<Void> delete(@RequestParam String code) {
        if (!(userService.getCurrentUser().getRole().equals(Role.COORDINATOR) || userService.getCurrentUser().getRole().equals(Role.HEAD) || userService.getCurrentUser().getRole().equals(Role.ADMIN))) {
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
        }
        if (inviteService.findByCode(code) == null) {
            return ResponseEntity.notFound().build();
        }
        inviteService.delete(code);
        return ResponseEntity.ok().build();
    }
}
