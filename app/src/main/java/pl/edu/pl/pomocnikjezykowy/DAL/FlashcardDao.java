package pl.edu.pl.pomocnikjezykowy.DAL;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.edu.pl.pomocnikjezykowy.MODELS.Flashcard;

@Dao
public interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) void insert (Flashcard flashcard);
    @Update public void update (Flashcard flashcard);
    @Delete public void delete (Flashcard flashcard);
    @Query("DELETE FROM flashcard") public void deleteAll();
    @Query("SELECT * FROM flashcard ORDER BY question") public LiveData<List<Flashcard>> findAll();
    @Query("SELECT * FROM flashcard WHERE question LIKE :question") public List<Flashcard> findFlashcardWithQuestion(String question);
}
