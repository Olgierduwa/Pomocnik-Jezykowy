package pl.edu.pl.pomocnikjezykowy.DAL;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import pl.edu.pl.pomocnikjezykowy.MODELS.Flashcard;

@Database(entities = {Flashcard.class}, version = 1, exportSchema = false)
public abstract class FlashcardDatabase extends RoomDatabase {

    public abstract FlashcardDao flashcardDao();

    private static volatile FlashcardDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static FlashcardDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (FlashcardDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FlashcardDatabase.class, "flashcard_db").build();
                }
            }
        }
        return INSTANCE;
    }
}
