package ru.isntrui.lb.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.models.Song;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.repositories.SongRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Transactional
    public void approveSong(Long songId, boolean isApproved, LocalDateTime dateTime, User user) {
        songRepository.approveSong(songId, isApproved, dateTime, user);
    }

    public List<Song> getAllSongs() {
        return songRepository.getAllSongs();
    }

    @Transactional
    public void deleteSong(Long songId) {
        songRepository.deleteById(songId);
    }
    public List<Song> search(String title) {
        return songRepository.findByTitleContaining(title);
    }

    public void createSong(Song song) {
        songRepository.save(song);
    }

    public List<Song> getSongsForWave(Long waveId) {
        return songRepository.findByWaveId(waveId);
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id).orElse(null);
    }

    public List<Song> getSongsByUser(User user) {
        return songRepository.getAllByMadeBy(user);
    }
}