package pl.edu.pl.pomocnikjezykowy;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pl.pomocnikjezykowy.MODELS.DataWrapper;
import pl.edu.pl.pomocnikjezykowy.MODELS.Flashcard;
import pl.edu.pl.pomocnikjezykowy.MODELS.FlashcardViewModel;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_FLASHCARD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_FLASHCARD_ACTIVITY_REQUEST_CODE = 2;
    public static final int LEARN_FLASHCARD_ACTIVITY_REQUEST_CODE = 3;
    private FlashcardViewModel flashcardViewModel;
    private Flashcard editedFlashcard;
    private FlashcardAdapter adapter;
    private boolean categoriesVisible = false;
    private boolean answerVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new FlashcardAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton addFlashcardButton = findViewById(R.id.add_button);
        addFlashcardButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditFlashcardActivity.class);
            startActivityForResult(intent, NEW_FLASHCARD_ACTIVITY_REQUEST_CODE);
        });

        flashcardViewModel = ViewModelProviders.of(this).get(FlashcardViewModel.class);
        flashcardViewModel.findAll().observe(this, adapter::setFlashcards);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        MenuItem learnItem = menu.findItem(R.id.start_learn);
        MenuItem selectItem = menu.findItem(R.id.select_category);
        MenuItem showCategoriesItem = menu.findItem(R.id.show_categories);
        MenuItem showAnswerItem = menu.findItem(R.id.show_answer);

        if(categoriesVisible) showCategoriesItem.setTitle(R.string.hide_categories);
        else                  showCategoriesItem.setTitle(R.string.show_categories);
        if(answerVisible) showAnswerItem.setTitle(R.string.hide_answer);
        else              showAnswerItem.setTitle(R.string.show_answer);

        SearchView searchView = (SearchView) selectItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String s) { return true; }
            @Override public boolean onQueryTextChange(String s) { adapter.getFilter().filter(s); return false; }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.start_learn:
                Intent intent = new Intent(MainActivity.this,LearnActivity.class);
                intent.putExtra("data", new DataWrapper(adapter.getFlashcards()));
                startActivityForResult(intent, LEARN_FLASHCARD_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.show_categories: categoriesVisible = !categoriesVisible;invalidateOptionsMenu();adapter.Refresh(); return true;
            case R.id.show_answer: answerVisible = !answerVisible;invalidateOptionsMenu();adapter.Refresh(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LEARN_FLASHCARD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DataWrapper dw = (DataWrapper) data.getSerializableExtra("data");
                List<Flashcard> flashcardsLearned = dw.getFlashcards();
                for (Flashcard f : flashcardsLearned) { flashcardViewModel.update(f); }
                Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.finished_learn), Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.canceled_learn), Snackbar.LENGTH_LONG).show();
            }
        } else if (requestCode == NEW_FLASHCARD_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Flashcard flashcard = new Flashcard(
                        data.getStringExtra(EditFlashcardActivity.EXTRA_EDIT_FLASHCARD_QUESTION),
                        data.getStringExtra(EditFlashcardActivity.EXTRA_EDIT_FLASHCARD_ANSWER),
                        data.getStringExtra(EditFlashcardActivity.EXTRA_EDIT_FLASHCARD_CATEGORY), 0);
                flashcardViewModel.insert(flashcard);
                Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.flashcard_saved), Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.empty_field_saved),Snackbar.LENGTH_LONG).show();
            }
        } else if (requestCode == EDIT_FLASHCARD_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                editedFlashcard.question = data.getStringExtra(EditFlashcardActivity.EXTRA_EDIT_FLASHCARD_QUESTION);
                editedFlashcard.answer = data.getStringExtra(EditFlashcardActivity.EXTRA_EDIT_FLASHCARD_ANSWER);
                editedFlashcard.category = data.getStringExtra(EditFlashcardActivity.EXTRA_EDIT_FLASHCARD_CATEGORY);
                editedFlashcard.progress = Integer.parseInt(data.getStringExtra(EditFlashcardActivity.EXTRA_EDIT_FLASHCARD_PROGRESS));
                flashcardViewModel.update(editedFlashcard);
                Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.flashcard_updated), Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.flashcard_cancel_saved),Snackbar.LENGTH_LONG).show();
            }
        } else { Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.undefined_request), Snackbar.LENGTH_LONG).show();}
    }

    private class FlashcardHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        public TextView flashcardQuestionTextView;
        public TextView flashcardAnswerTextView;
        public TextView flashcardCategoryTextView;
        private ImageView iconImageView;
        private Flashcard flashcard;
        public FlashcardHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.flashcard_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            flashcardQuestionTextView = itemView.findViewById(R.id.flashcard_question);
            flashcardAnswerTextView = itemView.findViewById(R.id.flashcard_answer);
            flashcardCategoryTextView = itemView.findViewById(R.id.flashcard_category);
            iconImageView = itemView.findViewById(R.id.icon_check);
        }
        public void bind(Flashcard flashcard) { this.flashcard = flashcard;
            flashcardQuestionTextView.setText(flashcard.question);
            flashcardAnswerTextView.setText(flashcard.answer);

            if(categoriesVisible) flashcardCategoryTextView.setText(flashcard.category);
            else flashcardCategoryTextView.setText("");

            if(answerVisible) flashcardAnswerTextView.setText(flashcard.answer);
            else flashcardAnswerTextView.setText("");

            if(flashcard.progress > 6) iconImageView.setImageResource(R.drawable.ic_donncheck);
            else if(flashcard.progress > 3) iconImageView.setImageResource(R.drawable.ic_progresscheck);
            else iconImageView.setImageResource(R.drawable.ic_unchecked);
        }
        @Override
        public void onClick(View view) {
            editedFlashcard = flashcard;
            Intent intent = new Intent(view.getContext(), EditFlashcardActivity.class);
            intent.putExtra(EditFlashcardActivity.EXTRA_EDIT_FLASHCARD_QUESTION, flashcard.question);
            intent.putExtra(EditFlashcardActivity.EXTRA_EDIT_FLASHCARD_ANSWER, flashcard.answer);
            intent.putExtra(EditFlashcardActivity.EXTRA_EDIT_FLASHCARD_CATEGORY, flashcard.category);
            intent.putExtra(EditFlashcardActivity.EXTRA_EDIT_FLASHCARD_PROGRESS, flashcard.getProgress());
            startActivityForResult(intent, EDIT_FLASHCARD_ACTIVITY_REQUEST_CODE);
        }
        @Override
        public boolean onLongClick(View view) {
            AlertDialog deletingDialog = new AlertDialog.Builder(view.getContext())
                    .setTitle(R.string.deleting_title).setMessage(R.string.deleting_question)
                    .setPositiveButton(R.string.delete_button, (dialogInterface, i) -> {
                        flashcardViewModel.delete(flashcard);
                        dialogInterface.dismiss();
                        Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.flashcard_deleted), Snackbar.LENGTH_LONG).show();
                    }).setNegativeButton(R.string.cancel_button, ((dialogInterface, i) -> { dialogInterface.dismiss(); })).create();
            deletingDialog.show();
            return false;
        }
    }

    public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardHolder> implements Filterable {
        private List<Flashcard> flashcards;
        private List<Flashcard> flashcardsFullList;
        private List<Flashcard> filteredList = new ArrayList<>();

        @NonNull
        @Override
        public FlashcardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FlashcardHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FlashcardHolder holder, int position) {
            if (flashcards != null) {
                Flashcard flashcard = flashcards.get(position);
                holder.bind(flashcard);
            } else {
                Log.d("MainActivity", "Brak Fiszek");
            }
        }

        @Override
        public int getItemCount() { if (flashcards != null) return flashcards.size(); else return 0; }

        @Override
        public Filter getFilter() { return categoryFilter; }

        private Filter categoryFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filteredList = new ArrayList<>();
                if(constraint == null || constraint.length() == 0) { filteredList.addAll(flashcardsFullList); }
                else { String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Flashcard flashcard : flashcardsFullList) {
                        if(flashcard.category.toLowerCase().contains(filterPattern)) {
                            filteredList.add(flashcard);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                flashcards.clear();
                flashcards.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };

        public void Refresh()
        {
            flashcards.clear();
            flashcards.addAll(filteredList);
            notifyDataSetChanged();
        }

        void setFlashcards(List<Flashcard> flashcards) {
            this.flashcards = flashcards;
            this.flashcardsFullList = new ArrayList<>(flashcards);
            this.filteredList = new ArrayList<>(flashcards);
            notifyDataSetChanged();
        }
        List<Flashcard> getFlashcards() {
            return flashcards;
        }
    }
}