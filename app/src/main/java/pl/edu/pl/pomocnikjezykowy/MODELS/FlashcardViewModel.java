package pl.edu.pl.pomocnikjezykowy.MODELS;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import pl.edu.pl.pomocnikjezykowy.DAL.FlashcardRepository;

public class FlashcardViewModel extends AndroidViewModel {
    private FlashcardRepository flashcardRepository;
    private LiveData<List<Flashcard>> flashcards;
    public FlashcardViewModel(@NonNull Application application) { super(application);
        flashcardRepository = new FlashcardRepository(application);
        flashcards = flashcardRepository.findAllFlashcards();
    }
    public LiveData<List<Flashcard>> findAll() { return flashcards; }
    public void insert(Flashcard flashcard) { flashcardRepository.insert(flashcard); }
    public void update(Flashcard flashcard) { flashcardRepository.update(flashcard); }
    public void delete(Flashcard flashcard) { flashcardRepository.delete(flashcard); }
}
