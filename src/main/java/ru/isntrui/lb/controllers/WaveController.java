package ru.isntrui.lb.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isntrui.lb.enums.Role;
import ru.isntrui.lb.models.Song;
import ru.isntrui.lb.models.Text;
import ru.isntrui.lb.models.Wave;
import ru.isntrui.lb.queries.WaveContentsResponse;
import ru.isntrui.lb.queries.WaveRequest;
import ru.isntrui.lb.services.SongService;
import ru.isntrui.lb.services.TextService;
import ru.isntrui.lb.services.UserService;
import ru.isntrui.lb.services.WaveService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wave/")
@Tag(name="Wave")
public class WaveController {
    @Autowired
    private WaveService waveService;

    @Autowired
    private SongService songService;

    @Autowired
    private TextService textService;
    @Autowired
    private UserService userService;

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
    public ResponseEntity<?> createWave(@RequestBody WaveRequest waveR) {
        Wave wave;
        try {
            wave = new Wave(waveR.title(), Date.valueOf(waveR.starts_on()), Date.valueOf(waveR.ends_on()), waveR.status());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
        Role currentUserRole = userService.getCurrentUser().getRole();
        if (currentUserRole != Role.COORDINATOR && currentUserRole != Role.HEAD && currentUserRole != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }
        try {
            Wave createdWave = waveService.createWave(wave);
            return ResponseEntity.ok(createdWave);
        } catch (IllegalArgumentException e) {
            List<Wave> w = waveService.getOverlappingWaves(wave);
            StringBuilder sb = new StringBuilder();
            w.forEach(s -> sb.append(s.getId()).append("\"").append(s.getTitle()).append("\", "));
            return ResponseEntity.badRequest().body("Невозможно создать волну: период пересекается с уже существующими волнами: " + sb);
        }
    }
}
