package pl.edu.pl.pomocnikjezykowy.MODELS;

import java.io.Serializable;
import java.util.List;

public class DataWrapper implements Serializable {

    private List<Flashcard> flashcards;

    public DataWrapper(List<Flashcard> data) {
        this.flashcards = data;
    }

    public List<Flashcard> getFlashcards() {
        return this.flashcards;
    }

}
