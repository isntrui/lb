package ru.isntrui.lb.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.models.Song;
import ru.isntrui.lb.repositories.SongRepository;

import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Transactional
    public void approveSong(Long songId, boolean isApproved) {
        songRepository.approveSong(songId, isApproved);
    }

    public List<Song> getAllSongs() {
        return songRepository.getAllSongs();
    }

    @Transactional
    public void deleteSong(Long songId) {
        songRepository.deleteById(songId);
    }

    public Song createSong(Song song) {
        return songRepository.save(song);
    }

    public List<Song> getSongsForWave(Long waveId) {
        return songRepository.findByWaveId(waveId);
    }
}