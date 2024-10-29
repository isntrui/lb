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

import java.util.List;

@Tag(name = "Invite")
@RestController
@RequestMapping("/api/invite/")
public class InviteController {
    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserService userService;

    @Operation(summary = "Create new invite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invite created"),
            @ApiResponse(responseCode = "403", description = "Forbidden: user is not allowed to create an invite")
    })
    @PostMapping("create")
    public ResponseEntity<Void> create(@RequestBody InviteQuery query) {
        System.out.println(userService.getCurrentUser());
        if (userService.getCurrentUser().getRole().toString().equals(Role.HEAD.toString()) || userService.getCurrentUser().getRole().equals(Role.COORDINATOR) || userService.getCurrentUser().getRole().equals(Role.ADMIN)) {
            try {
                inviteService.create(query.email(), query.inviteCode());
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
            System.out.println(userService.getCurrentUser().getRole().toString() + " - Forbidden");
            return ResponseEntity.status(HttpStatusCode.valueOf(403)).build();
        }
        return ResponseEntity.ok(inviteService.getAllInvites());
    }
}
