package ru.isntrui.lb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.enums.WaveStatus;
import ru.isntrui.lb.models.Wave;
import ru.isntrui.lb.repositories.WaveRepository;

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
        return waveRepository.save(wave);
    }

    public void updateWave(Wave wave) {
        waveRepository.save(wave);
    }

    public Optional<Wave> getLastCreatedWave() {
        return waveRepository.findLastCreatedWave();
    }
}
