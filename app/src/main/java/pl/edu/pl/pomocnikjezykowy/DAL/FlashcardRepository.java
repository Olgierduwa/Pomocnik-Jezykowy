package pl.edu.pl.pomocnikjezykowy.DAL;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import pl.edu.pl.pomocnikjezykowy.MODELS.Flashcard;

public class FlashcardRepository {
    private FlashcardDao flashcardDao;
    private LiveData<List<Flashcard>> flashcards;
    public FlashcardRepository(Application application) {
        FlashcardDatabase database = FlashcardDatabase.getDatabase(application);
        flashcardDao = database.flashcardDao();
        flashcards = flashcardDao.findAll();
    }
    public LiveData<List<Flashcard>> findAllFlashcards() { return flashcards; }
    public void insert(Flashcard flashcard) { FlashcardDatabase.databaseWriteExecutor.execute(() -> { flashcardDao.insert(flashcard); }); }
    public void update(Flashcard flashcard) { FlashcardDatabase.databaseWriteExecutor.execute(() -> { flashcardDao.update(flashcard); }); }
    public void delete(Flashcard flashcard) { FlashcardDatabase.databaseWriteExecutor.execute(() -> { flashcardDao.delete(flashcard); }); }
}
