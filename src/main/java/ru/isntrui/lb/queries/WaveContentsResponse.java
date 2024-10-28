package ru.isntrui.lb.queries;

import ru.isntrui.lb.models.Song;
import ru.isntrui.lb.models.Text;

import java.util.List;

public record WaveContentsResponse(List<Song> songs, List<Text> texts) {
}
