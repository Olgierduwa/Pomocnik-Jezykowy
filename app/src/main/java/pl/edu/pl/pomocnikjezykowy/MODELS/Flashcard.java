package pl.edu.pl.pomocnikjezykowy.MODELS;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "flashcard")
public class Flashcard implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String question;
    public String answer;
    public String category;
    public int progress;

    public Flashcard(String question, String answer, String category, int progress) {
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.progress = progress;
    }
    public String getProgress(){
        return Integer.toString(progress);
    }
}
