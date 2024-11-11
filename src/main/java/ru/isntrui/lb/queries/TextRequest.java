package ru.isntrui.lb.queries;

import jakarta.annotation.Nullable;
import lombok.Data;
import ru.isntrui.lb.models.Wave;

@Data
public class TextRequest {
    private String text;
    private String title;

    @Nullable
    private Wave wave;
}
