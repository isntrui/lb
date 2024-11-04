package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.enums.WaveStatus;
import ru.isntrui.lb.models.Song;
import ru.isntrui.lb.models.Text;
import ru.isntrui.lb.models.Wave;
import ru.isntrui.lb.queries.WaveContentsResponse;
import ru.isntrui.lb.queries.WaveRequest;
import ru.isntrui.lb.services.SongService;
import ru.isntrui.lb.services.TextService;
import ru.isntrui.lb.services.UserService;
import ru.isntrui.lb.services.WaveService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wave/")
@Tag(name="Wave")
public class WaveController {
    private final WaveService waveService;
    private final SongService songService;
    private final TextService textService;
    private final UserService userService;

    @Autowired
    public WaveController(WaveService waveService, SongService songService, TextService textService, UserService userService) {
        this.waveService = waveService;
        this.songService = songService;
        this.textService = textService;
        this.userService = userService;
    }

    @Operation(summary = "Get all songs and texts of a wave")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Songs and texts found"),
            @ApiResponse(responseCode = "404", description = "Wave not found")
    })
    @GetMapping("{id}/contents")
    public ResponseEntity<WaveContentsResponse> getWaveContents(@PathVariable Long id) {
        Optional<Wave> waveOpt = waveService.getWaveById(id);
        if (waveOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Song> songs = songService.getSongsForWave(id);
        List<Text> texts = textService.findByWaveId(id);

        WaveContentsResponse contents = new WaveContentsResponse(songs, texts);
        return ResponseEntity.ok(contents);
    }


    @PostMapping("create")
    public ResponseEntity<String> createWave(@RequestBody WaveRequest waveR) {
        Wave wave;
        try {
            wave = new Wave(waveR.title(), waveR.starts_on(), waveR.ends_on(), waveR.status());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        Role currentUserRole = userService.getCurrentUser().getRole();
        if (currentUserRole != Role.COORDINATOR && currentUserRole != Role.HEAD && currentUserRole != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }
        try {
            waveService.createWave(wave);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            List<Wave> w = waveService.getOverlappingWaves(wave);
            StringBuilder sb = new StringBuilder();
            w.forEach(s -> sb.append(s.getId()).append("\"").append(s.getTitle()).append("\", "));
            return ResponseEntity.badRequest().body("Невозможно создать волну: период пересекается с уже существующими волнами: " + sb);
        }
    }

    @Operation(summary = "Update wave")
    @PutMapping("{id}/update")
    public ResponseEntity<Void> updateWave(@PathVariable Long id, @RequestBody WaveRequest waveR) {
        Optional<Wave> waveOpt = waveService.getWaveById(id);
        if (waveOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!(userService.getCurrentUser().getRole() == Role.COORDINATOR || userService.getCurrentUser().getRole() == Role.HEAD || userService.getCurrentUser().getRole() == Role.ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        Wave wave = waveOpt.get();
        wave.setTitle(waveR.title() == null ? wave.getTitle() : waveR.title());
        wave.setStartsOn(waveR.starts_on() == null ? wave.getStartsOn() : waveR.starts_on());
        wave.setEndsOn(waveR.ends_on() == null ? wave.getEndsOn() : waveR.ends_on());
        wave.setStatus(waveR.status() == null ? wave.getStatus() : waveR.status());
        waveService.updateWave(wave);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Change status")
    @PutMapping("{id}/changeStatus")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id, @RequestParam WaveStatus status) {
        Optional<Wave> waveOpt = waveService.getWaveById(id);
        if (waveOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!(userService.getCurrentUser().getRole() == Role.COORDINATOR || userService.getCurrentUser().getRole() == Role.HEAD || userService.getCurrentUser().getRole() == Role.ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        waveService.updateWaveStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete wave")
    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void> deleteWave(@PathVariable Long id) {
        Optional<Wave> waveOpt = waveService.getWaveById(id);
        if (waveOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!(userService.getCurrentUser().getRole() == Role.COORDINATOR || userService.getCurrentUser().getRole() == Role.HEAD || userService.getCurrentUser().getRole() == Role.ADMIN)) {
            return ResponseEntity.status(403).build();
        }
        waveService.deleteWave(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get current wave")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Current wave found"),
            @ApiResponse(responseCode = "404", description = "No current wave found")
    })
    @GetMapping("current")
    public ResponseEntity<Wave> getCurrentWave() {
        Optional<Wave> currentWave = waveService.getCurrentWave();
        return currentWave.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
