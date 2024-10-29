package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.models.Song;
import ru.isntrui.lb.services.SongService;
import ru.isntrui.lb.services.UserService;

@RestController
@Tag(name = "Song")
@RequestMapping("/api/songs/")
public class SongController {
    @Autowired
    private SongService songService;
    @Autowired
    private UserService us;

    private boolean checkPermission() {
        return us.getCurrentUser().getRole() == Role.ADMIN || us.getCurrentUser().getRole() == Role.COORDINATOR || us.getCurrentUser().getRole() == Role.HEAD || us.getCurrentUser().getRole() == Role.SOUNDDESIGNER;
    }

    @Operation(summary = "Create song")
    @PostMapping("create")
    public ResponseEntity<Void> create(@RequestBody @Parameter(description = "New song") Song song) {
        song.setMadeBy(us.getCurrentUser());
        songService.createSong(song);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all songs")
    @GetMapping("all")
    public ResponseEntity<?> getAll() {
        if (!checkPermission()) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(songService.getAllSongs());
    }

    @Operation(summary = "Delete song")
    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable @Parameter(description = "Song to remove") Long id) {
        if (!checkPermission()) return ResponseEntity.status(403).build();
        songService.deleteSong(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Approve song")
    @PutMapping("{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable @Parameter(description = "Song to approve") Long id) {
        if (!(us.getCurrentUser().getRole() == Role.ADMIN || us.getCurrentUser().getRole() == Role.COORDINATOR || us.getCurrentUser().getRole() == Role.HEAD)) {
            return ResponseEntity.status(403).build();
        }
        if (songService.getSongById(id) == null) return ResponseEntity.notFound().build();
        songService.approveSong(id, true);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get songs for wave")
    @GetMapping("wave")
    public ResponseEntity<?> get(@RequestParam @Parameter(description = "Wave id to get") Long waveId) {
        if (!checkPermission()) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(songService.getSongsForWave(waveId));
    }

    @Operation(summary = "Get song by id")
    @GetMapping("{id}")
    public ResponseEntity<?> getSong(@PathVariable @Parameter(description = "SongID to get") Long id) {
        if (!checkPermission()) return ResponseEntity.status(403).build();
        Song song = songService.getSongById(id);
        if (song == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(song);
    }

    @Operation(summary = "Disapprove song")
    @PutMapping("{id}/disapprove")
    public ResponseEntity<Void> disapprove(@PathVariable @Parameter(description = "Song to disapprove") Long id) {
        if (!checkPermission()) return ResponseEntity.status(403).build();
        if (!(us.getCurrentUser().getRole() == Role.ADMIN || us.getCurrentUser().getRole() == Role.COORDINATOR || us.getCurrentUser().getRole() == Role.HEAD)) {
            return ResponseEntity.status(403).build();
        }
        if (songService.getSongById(id) == null) return ResponseEntity.notFound().build();
        songService.approveSong(id, false);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update song")
    @PutMapping("{id}/update")
    public ResponseEntity<Void> update(@PathVariable @Parameter(description = "SongID to update") Long id, @RequestBody @Parameter(description = "New song's obj") Song song) {
        if (songService.getSongById(id) == null) return ResponseEntity.notFound().build();
        song.setId(id);
        songService.createSong(song);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Get my songs")
    @GetMapping("my")
    public ResponseEntity<?> getMy() {
        return ResponseEntity.ok(songService.getSongsByUser(us.getCurrentUser()));
    }
}
