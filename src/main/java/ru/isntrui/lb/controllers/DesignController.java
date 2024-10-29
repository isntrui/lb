package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.models.Design;
import ru.isntrui.lb.services.DesignService;
import ru.isntrui.lb.services.UserService;
import ru.isntrui.lb.services.WaveService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@Tag(name = "Design")
@RequestMapping("/api/design/")
public class DesignController {
    @Autowired
    private DesignService designService;
    @Autowired
    private UserService userService;
    @Autowired
    private WaveService ws;
    private boolean isPermitted() {
        return userService.getCurrentUser().getRole() != Role.ADMIN && userService.getCurrentUser().getRole() != Role.COORDINATOR && userService.getCurrentUser().getRole() != Role.HEAD && userService.getCurrentUser().getRole() != Role.DESIGNER;
    }

    @Operation(summary = "Create design")
    @PostMapping("create")
    public ResponseEntity<Void> create(@RequestBody @Parameter(description = "New design") Design design) {
        design.setCreatedBy(userService.getCurrentUser());
        design.setWave(ws.getLastCreatedWave().getFirst());
        designService.createDesign(design);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all designs")
    @GetMapping("all")
    public ResponseEntity<?> getAll() {
        if (isPermitted()) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(designService.getAllDesigns());
    }

    @Operation(summary = "Delete design")
    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable @Parameter(description = "Design to remove") Long id) {
        if (isPermitted()) return ResponseEntity.status(403).build();
        designService.deleteDesign(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Approve design")
    @PutMapping("{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable @Parameter(description = "Design to approve") Long id, @RequestParam @Parameter(description = "Approve or disapprove") boolean approve) {
        if (!(userService.getCurrentUser().getRole() == Role.ADMIN || userService.getCurrentUser().getRole() == Role.COORDINATOR || userService.getCurrentUser().getRole() == Role.HEAD)) {
            return ResponseEntity.status(403).build();
        }
        if (designService.getDesignById(id) == null) return ResponseEntity.notFound().build();
        designService.approveDesign(id, approve, LocalDateTime.now(), userService.getCurrentUser());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get designs for wave")
    @GetMapping("wave")
    public ResponseEntity<List<Design>> get(@RequestParam @Parameter(description = "Wave id to get") Long waveId) {
        if (isPermitted()) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(designService.getDesignsForWave(waveId));
    }
    @Operation(summary = "Search")
    @GetMapping("search")
    public ResponseEntity<?> search(@RequestParam @Parameter(description = "Search query") String query) {
        if (isPermitted()) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(designService.search(query));
    }
    @Operation(summary = "Get design by id")
    @GetMapping("{id}")
    public ResponseEntity<?> getDesign(@PathVariable @Parameter(description = "DesignID to get") Long id) {
        if (isPermitted()) return ResponseEntity.status(403).build();
        Design design = designService.getDesignById(id);
        if (design == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(design);
    }


    @Operation(summary = "Update design")
    @PutMapping("{id}/update")
    public ResponseEntity<Void> update(@PathVariable @Parameter(description = "DesignID to update") Long id, @RequestBody @Parameter(description = "New design's obj") Design design) {
        if (designService.getDesignById(id) == null) return ResponseEntity.notFound().build();
        if (userService.getCurrentUser().getRole() == Role.HEAD || userService.getCurrentUser().getRole() == Role.COORDINATOR || userService.getCurrentUser().getRole() == Role.ADMIN || Objects.equals(userService.getCurrentUser().getId(), designService.getDesignById(id).getId())) {
            Design t = designService.getDesignById(id);
            t.setTitle(design.getTitle());
            t.setUrl(design.getUrl());
            designService.createDesign(t);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @Operation(summary = "Get my designs")
    @GetMapping("my")
    public ResponseEntity<?> getMy() {
        if (isPermitted()) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(designService.getDesignsByUser(userService.getCurrentUser()));
    }
}