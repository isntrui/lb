package ru.isntrui.lb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.enums.WaveStatus;
import ru.isntrui.lb.models.Wave;
import ru.isntrui.lb.repositories.WaveRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WaveService {
    private final WaveRepository waveRepository;

    @Autowired
    public WaveService(WaveRepository waveRepository) {
        this.waveRepository = waveRepository;
    }

    public void deleteWave(Long waveId) {
        waveRepository.deleteById(waveId);
    }

    public void updateWaveStatus(Long waveId, WaveStatus status) {
        waveRepository.updateStatus(waveId, status);
    }

    public Optional<Wave> getWaveById(Long waveId) {
        return waveRepository.findById(waveId);
    }

    public Wave createWave(Wave wave) {
        List<Wave> overlappingWaves = waveRepository.findOverlappingWaves(wave.getStartsOn(), wave.getEndsOn());

        if (!overlappingWaves.isEmpty()) {
            throw new IllegalArgumentException("Невозможно создать волну: период пересекается с уже существующей волной.");
        }

        return waveRepository.save(wave);
    }

    public void updateWave(Wave wave) {
        waveRepository.save(wave);
    }

    public List<Wave> getLastCreatedWave() {
        Pageable pb = PageRequest.of(0, 1);
        return waveRepository.findAllWaves(pb);
    }

    public List<Wave> getOverlappingWaves(Wave wave) {
        return waveRepository.findOverlappingWaves(wave.getStartsOn(), wave.getEndsOn());
    }

    public Optional<Wave> getCurrentWave() {
        LocalDate today = LocalDate.now();
        return waveRepository.findCurrentWave(today);
    }
}
