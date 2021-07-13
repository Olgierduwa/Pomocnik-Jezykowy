package pl.edu.pl.pomocnikjezykowy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pl.pomocnikjezykowy.MODELS.DataWrapper;
import pl.edu.pl.pomocnikjezykowy.MODELS.Flashcard;
import com.google.android.material.snackbar.Snackbar;

public class LearnActivity extends AppCompatActivity {

    //public static final String EXTRA_ADAPTER = "pb.edu.pl.ADAPTER";

    private EditText editAnswerEditText;
    private TextView viewQuestionTextView;
    private TextView viewSkippedTextView;
    private TextView viewCorrectTextView;
    private TextView viewWrongTextView;
    private Button buttonAnswer;
    private Button buttonSkip;

    private int skipCount = 0;
    private int correctCount = 0;
    private int wrongCount = 0;
    private int listCount = 0;
    private int currentIndex = 0;
    private Flashcard flashcard;
    private String viewAnswerText;

    private List<Flashcard> flashcards;
    private List<Flashcard> flashcardsToUse;
    private MainActivity.FlashcardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_flashcard);

        DataWrapper dw = (DataWrapper) getIntent().getSerializableExtra("data");
        flashcards = dw.getFlashcards();
        flashcardsToUse = new ArrayList<>();

        flashcardsToUse.addAll(flashcards);
        listCount = flashcards.size();

        editAnswerEditText = findViewById(R.id.answer_text_view);
        viewQuestionTextView = findViewById(R.id.question_text_view);
        viewSkippedTextView = findViewById(R.id.skiped_count);
        viewCorrectTextView = findViewById(R.id.correct_count);
        viewWrongTextView = findViewById(R.id.wrong_count);
        buttonAnswer = findViewById(R.id.answer_button);
        buttonSkip = findViewById(R.id.skip_button);

        setParametrs();

        buttonAnswer.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                if(viewAnswerText.trim().toLowerCase().equals(editAnswerEditText.getText().toString().trim().toLowerCase())) {
                    correctCount++;
                    currentIndex = flashcards.indexOf(flashcard);
                    flashcard.progress++;
                    flashcards.set(currentIndex,flashcard);
                } else { wrongCount++; }
                setParametrs();
            }
        });
        buttonSkip.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                if(buttonAnswer.isEnabled() == false) { finish(); }
                else { skipCount++; setParametrs(); }
            }
        });
    }

    private void setParametrs() {
        if(flashcardsToUse != null && flashcardsToUse.size() > 0) {
            currentIndex = 0;
            flashcard = flashcardsToUse.remove(currentIndex);
            viewAnswerText = flashcard.answer;
            viewQuestionTextView.setText(flashcard.question);
        } else {
            viewQuestionTextView.setText(R.string.label_learn_finished);
            editAnswerEditText.setEnabled(false);
            buttonAnswer.setEnabled(false);
            buttonSkip.setText(R.string.finish_learn);
            Intent intent = new Intent();
            intent.putExtra("data", new DataWrapper(flashcards));
            setResult(RESULT_OK, intent);
        }
        editAnswerEditText.setText("");
        String skipCountString = Integer.toString(skipCount);
        String correctCountString = Integer.toString(correctCount) + " / " + Integer.toString(listCount);
        String wrongCountString = Integer.toString(wrongCount);
        viewSkippedTextView.setText(skipCountString);
        viewCorrectTextView.setText(correctCountString);
        viewWrongTextView.setText(wrongCountString);
    }
}
