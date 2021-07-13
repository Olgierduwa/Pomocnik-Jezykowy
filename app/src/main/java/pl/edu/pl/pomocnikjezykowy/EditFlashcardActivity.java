package pl.edu.pl.pomocnikjezykowy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class EditFlashcardActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_EDIT_FLASHCARD_QUESTION = "pb.edu.pl.EDIT_FLASHCARD_QUESTION";
    public static final String EXTRA_EDIT_FLASHCARD_ANSWER = "pb.edu.pl.EDIT_FLASHCARD_ANSWER";
    public static final String EXTRA_EDIT_FLASHCARD_CATEGORY = "pb.edu.pl.EDIT_FLASHCARD_CATEGORY";
    public static final String EXTRA_EDIT_FLASHCARD_PROGRESS = "pb.edu.pl.EDIT_FLASHCARD_PROGRESS";

    private EditText editQuestionEditText;
    private EditText editAnswerEditText;
    private EditText editCategoryEditText;
    private TextView viewProgressTextView;
    private TextView viewProgressLabelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flashcard);

        editQuestionEditText = findViewById(R.id.edit_flashcard_question);
        editAnswerEditText = findViewById(R.id.edit_flashcard_answer);
        editCategoryEditText = findViewById(R.id.edit_flashcard_category);
        viewProgressTextView = findViewById(R.id.view_flashcard_progress);
        viewProgressLabelTextView = findViewById(R.id.view_flashcard_progress_label);

        if(getIntent().hasExtra(EXTRA_EDIT_FLASHCARD_QUESTION)){
            editQuestionEditText.setText(getIntent().getExtras().getString(EXTRA_EDIT_FLASHCARD_QUESTION));
            editAnswerEditText.setText(getIntent().getExtras().getString(EXTRA_EDIT_FLASHCARD_ANSWER));
            editCategoryEditText.setText(getIntent().getExtras().getString(EXTRA_EDIT_FLASHCARD_CATEGORY));
            viewProgressTextView.setText(getIntent().getExtras().getString(EXTRA_EDIT_FLASHCARD_PROGRESS));
            viewProgressLabelTextView.setText(R.string.label_flashcard_progress);
        } else {
            viewProgressLabelTextView.setText("");
            viewProgressTextView.setText("");
        }

        Button button = findViewById(R.id.save_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(editQuestionEditText.getText())
                || TextUtils.isEmpty(editAnswerEditText.getText())
                || TextUtils.isEmpty(editCategoryEditText.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
            finish();
        } else if(!getIntent().hasExtra(EXTRA_EDIT_FLASHCARD_QUESTION)) {
            replyIntent.putExtra(EXTRA_EDIT_FLASHCARD_QUESTION, editQuestionEditText.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_FLASHCARD_ANSWER, editAnswerEditText.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_FLASHCARD_CATEGORY, editCategoryEditText.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_FLASHCARD_PROGRESS, "0");
            setResult(RESULT_OK, replyIntent);
            finish();
        } else if( getIntent().getExtras().getString(EXTRA_EDIT_FLASHCARD_QUESTION).equals(editQuestionEditText.getText().toString())
                && getIntent().getExtras().getString(EXTRA_EDIT_FLASHCARD_ANSWER).equals(editAnswerEditText.getText().toString())) {
            replyIntent.putExtra(EXTRA_EDIT_FLASHCARD_QUESTION, editQuestionEditText.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_FLASHCARD_ANSWER, editAnswerEditText.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_FLASHCARD_CATEGORY, editCategoryEditText.getText().toString());
            replyIntent.putExtra(EXTRA_EDIT_FLASHCARD_PROGRESS, viewProgressTextView.getText().toString());
            setResult(RESULT_OK, replyIntent);
            finish();
        } else {
            AlertDialog deletingDialog = new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.saving_title).setMessage(R.string.saving_question)
                .setPositiveButton(R.string.save_button, (dialogInterface, i) -> {
                    replyIntent.putExtra(EXTRA_EDIT_FLASHCARD_QUESTION, editQuestionEditText.getText().toString());
                    replyIntent.putExtra(EXTRA_EDIT_FLASHCARD_ANSWER, editAnswerEditText.getText().toString());
                    replyIntent.putExtra(EXTRA_EDIT_FLASHCARD_CATEGORY, editCategoryEditText.getText().toString());
                    replyIntent.putExtra(EXTRA_EDIT_FLASHCARD_PROGRESS, "0");
                    setResult(RESULT_OK, replyIntent);
                    dialogInterface.dismiss();
                    finish();
                }).setNegativeButton(R.string.cancel_save_button, ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    setResult(RESULT_CANCELED, replyIntent);
                })).create();
            deletingDialog.show();
        }
    }
}