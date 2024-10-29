package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.models.Text;
import ru.isntrui.lb.services.TextService;
import ru.isntrui.lb.services.UserService;
import ru.isntrui.lb.services.WaveService;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/text/")
@Tag(name = "Text")
public class TextController {
    @Autowired
    private TextService ts;
    @Autowired
    private UserService us;
    @Autowired
    private WaveService ws;

    @Operation(summary = "Save new text")
    @PostMapping("save")
    public ResponseEntity<Void> saveText(@RequestParam @Parameter(description = "Title of new text") String title, @RequestParam @Parameter(description = "Body of new text") String text) {
        if (us.getCurrentUser().getRole() == Role.WRITER || us.getCurrentUser().getRole() == Role.ADMIN || us.getCurrentUser().getRole() == Role.HEAD || us.getCurrentUser().getRole() == Role.COORDINATOR) {
            Text t = new Text();
            t.setMadeBy(us.getCurrentUser());
            t.setApproved(false);
            t.setBody(text);
            t.setTitle(title);
            t.setWave(ws.getLastCreatedWave().getFirst());
            ts.create(t);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @Operation(summary = "Approve text")
    @PutMapping("approve")
    public ResponseEntity<Void> approveText(@RequestParam @Parameter(description = "Text id") Long textId, @RequestParam @Parameter(description = "Approve or disapprove") boolean approve) {
        if (us.getCurrentUser().getRole() == Role.HEAD || us.getCurrentUser().getRole() == Role.COORDINATOR || us.getCurrentUser().getRole() == Role.ADMIN) {
            if (ts.getById(textId).isEmpty()) return ResponseEntity.notFound().build();
            try {
                ts.approve(textId, approve);
            } catch (Exception ex) {
                Logger.getLogger("Text").log(Level.SEVERE, "Error while getting current user", ex);
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @Operation(summary = "Get all texts made by current user")
    @GetMapping("getAllMy")
    public ResponseEntity<Iterable<Text>> getAllMy() {
        if (us.getCurrentUser().getRole() == Role.HEAD || us.getCurrentUser().getRole() == Role.COORDINATOR || us.getCurrentUser().getRole() == Role.ADMIN || us.getCurrentUser().getRole() == Role.WRITER) {

            return ResponseEntity.ok(ts.findByMadeById(us.getCurrentUser().getId()));
        }
        return ResponseEntity.status(403).build();
    }

    @Operation(summary = "Search")
    @GetMapping("search")
    public ResponseEntity<Iterable<Text>> search(@RequestParam @Parameter(description = "Search query") String query) {
        if (us.getCurrentUser().getRole() == Role.HEAD || us.getCurrentUser().getRole() == Role.COORDINATOR || us.getCurrentUser().getRole() == Role.ADMIN) {
            return ResponseEntity.ok(ts.findByTitleContaining(query));
        }
        return ResponseEntity.status(403).build();
    }

    @Operation(summary = "Get all texts")
    @GetMapping("getAll")
    public ResponseEntity<Iterable<Text>> getAll() {
        if (us.getCurrentUser().getRole() == Role.HEAD || us.getCurrentUser().getRole() == Role.COORDINATOR || us.getCurrentUser().getRole() == Role.ADMIN) {
            return ResponseEntity.ok(ts.getAll());
        }
        return ResponseEntity.status(403).build();
    }

    @Operation(summary = "Get text by id")
    @GetMapping("{id}")
    public ResponseEntity<Text> getById(@PathVariable @Parameter(description = "Text id") Long id) {
        if (ts.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (us.getCurrentUser().getRole() == Role.HEAD || us.getCurrentUser().getRole() == Role.COORDINATOR || us.getCurrentUser().getRole() == Role.ADMIN || Objects.equals(us.getCurrentUser().getId(), ts.getById(id).get().getId())) {
            return ResponseEntity.ok(ts.getById(id).orElseThrow());
        }
        return ResponseEntity.status(403).build();
    }

    @Operation(summary = "Delete text")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteText(@PathVariable @Parameter(description = "Text id") Long id) {
        if (us.getCurrentUser().getRole() == Role.HEAD || us.getCurrentUser().getRole() == Role.COORDINATOR || us.getCurrentUser().getRole() == Role.ADMIN) {
            ts.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }

    @Operation(summary = "Update text")
    @PutMapping("update")
    public ResponseEntity<Void> updateText(@RequestParam @Parameter(description = "Text id") Long id, @RequestParam @Parameter(description = "Title of updated text") String title, @RequestParam @Parameter(description = "Body of updated text") String text) {
        Optional<Text> t = ts.getById(id);
        if (t.isEmpty()) return ResponseEntity.notFound().build();
        if (us.getCurrentUser().getRole() == Role.HEAD || us.getCurrentUser().getRole() == Role.COORDINATOR || us.getCurrentUser().getRole() == Role.ADMIN || Objects.equals(us.getCurrentUser().getId(), t.get().getId())) {
            Text newT = t.get();
            newT.setTitle(title);
            newT.setBody(text);
            ts.update(id, newT);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build();
    }
}
