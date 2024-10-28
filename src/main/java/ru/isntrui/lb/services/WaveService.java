package ru.isntrui.lb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.enums.WaveStatus;
import ru.isntrui.lb.models.Wave;
import ru.isntrui.lb.repositories.WaveRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WaveService {
    @Autowired
    private WaveRepository waveRepository;

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
        List<Wave> overlappingWaves = waveRepository.findOverlappingWaves(wave.getStarts_on().toLocalDate(), wave.getEnds_on().toLocalDate());

        if (!overlappingWaves.isEmpty()) {
            throw new IllegalArgumentException("Невозможно создать волну: период пересекается с уже существующей волной.");
        }

        return waveRepository.save(wave);
    }

    public void updateWave(Wave wave) {
        waveRepository.save(wave);
    }

    public Optional<Wave> getLastCreatedWave() {
        return waveRepository.findLastCreatedWave();
    }

    public List<Wave> getOverlappingWaves(Wave wave) {
        return waveRepository.findOverlappingWaves(wave.getStarts_on().toLocalDate(), wave.getEnds_on().toLocalDate());
    }

}
