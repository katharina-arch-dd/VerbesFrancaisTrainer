package de.katharina.verbesfrancais.ui;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.katharina.verbesfrancais.R;
import de.katharina.verbesfrancais.data.AppDatabase;
import de.katharina.verbesfrancais.data.ConjugationForm;
import de.katharina.verbesfrancais.data.ReviewProgress;
import de.katharina.verbesfrancais.data.Verb;
import de.katharina.verbesfrancais.helpers.GrammarInfoHelper;
import de.katharina.verbesfrancais.practice.PracticeEngine;
import de.katharina.verbesfrancais.settings.DailyGoalManager;
import de.katharina.verbesfrancais.settings.TenseSelectionManager;
import de.katharina.verbesfrancais.tts.FrenchSpeaker;

public class PracticeActivity extends AppCompatActivity {
    private static final String TAG = "PracticeActivity";
    private AppDatabase db;
    private FrenchSpeaker speaker;
    private PracticeEngine engine;
    private DailyGoalManager goals;

    private ConjugationForm currentForm;
    private Verb currentVerb;
    private ReviewProgress currentProgress;

    private TextView textVerb;
    private TextView textTask;
    private TextView textResult;
    private EditText editAnswer;
    private String filterTense = "";
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        db = AppDatabase.getInstance(this);
        speaker = new FrenchSpeaker(this);
        engine = new PracticeEngine();
        goals = new DailyGoalManager(this);

        textVerb = findViewById(R.id.textVerb);
        textTask = findViewById(R.id.textTask);
        textResult = findViewById(R.id.textResult);
        editAnswer = findViewById(R.id.editAnswer);

        Button speakVerb = findViewById(R.id.buttonSpeakVerb);
        Button showGrammar = findViewById(R.id.buttonShowGrammar);
        Button speakForm = findViewById(R.id.buttonSpeakForm);
        Button check = findViewById(R.id.buttonCheck);
        Button next = findViewById(R.id.buttonNext);

        speakVerb.setOnClickListener(v -> {
            if (currentVerb != null && currentVerb.infinitive != null) {
                speaker.speak(currentVerb.infinitive);
            }
        });

        showGrammar.setOnClickListener(v -> {
            if (currentVerb != null && currentForm != null) {
                GrammarInfoHelper.showGrammarDialog(this, currentVerb.infinitive, currentVerb.translationDe, currentForm.tense, speaker);
            }
        });

        speakForm.setOnClickListener(v -> {
            if (currentForm != null && currentForm.form != null) {
                speaker.speakSlowly(currentForm.form);
            }
        });

        check.setOnClickListener(v -> checkAnswer());
        next.setOnClickListener(v -> loadNextExercise());

        filterTense = getIntent().getStringExtra("tense");
        if (filterTense == null) filterTense = "";

        loadNextExercise();
    }

    private void loadNextExercise() {
        new Thread(() -> {
            try {
                currentForm = null;
                currentProgress = null;
                currentVerb = null;

                TenseSelectionManager tenseManager = new TenseSelectionManager(this);
                Set<String> selectedTensesSet = tenseManager.getSelectedTenses();
                List<String> selectedTensesList = new ArrayList<>(selectedTensesSet);

                List<ReviewProgress> due = db.reviewProgressDao().getDue(System.currentTimeMillis());
                if (due != null && !due.isEmpty()) {
                    List<ReviewProgress> filteredDue = new ArrayList<>();
                    for (ReviewProgress rp : due) {
                        ConjugationForm form = db.conjugationFormDao().findById(rp.formId);
                        if (form != null) {
                            if (!filterTense.isEmpty()) {
                                if (filterTense.equals(form.tense)) filteredDue.add(rp);
                            } else if (selectedTensesSet.contains(form.tense)) {
                                filteredDue.add(rp);
                            }
                        }
                    }
                    if (!filteredDue.isEmpty()) {
                        currentProgress = filteredDue.get(random.nextInt(filteredDue.size()));
                        currentForm = db.conjugationFormDao().findById(currentProgress.formId);
                    }
                }

                if (currentForm == null) {
                    List<ConjugationForm> candidates;
                    if (!filterTense.isEmpty()) {
                        candidates = db.conjugationFormDao().getFilteredForms(filterTense, 0);
                    } else {
                        candidates = db.conjugationFormDao().getFormsByTenses(selectedTensesList);
                    }

                    if (candidates == null || candidates.isEmpty()) {
                        runOnUiThread(() -> {
                            if (isFinishing() || isDestroyed()) return;
                            Toast.makeText(this, "Keine Formen für die ausgewählten Zeiten vorhanden.", Toast.LENGTH_LONG).show();
                        });
                        return;
                    }

                    currentForm = candidates.get(random.nextInt(candidates.size()));
                    if (currentForm != null) {
                        currentProgress = db.reviewProgressDao().getByFormId(currentForm.id);
                        if (currentProgress == null) {
                            currentProgress = new ReviewProgress();
                            currentProgress.formId = currentForm.id;
                            db.reviewProgressDao().insert(currentProgress);
                        }
                    }
                }

                if (currentForm != null) {
                    currentVerb = db.verbDao().findById(currentForm.verbId);
                }

                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    if (currentVerb == null || currentForm == null) return;

                    textVerb.setText(currentVerb.infinitive != null ? currentVerb.infinitive : "");
                    textTask.setText(tenseName(currentForm.tense) + " - " + (currentForm.person != null ? currentForm.person : ""));
                    textResult.setText("");
                    editAnswer.setText("");
                });
            } catch (Exception e) {
                Log.e(TAG, "Error loading next exercise", e);
            }
        }).start();
    }

    private void checkAnswer() {
        if (currentForm == null || currentProgress == null) return;

        boolean ok = engine.isCorrect(currentForm, editAnswer.getText().toString());
        currentProgress = engine.updateProgress(currentProgress, ok);
        goals.registerAnswer();

        if (goals.isDailyGoalReached()) {
            goals.updateStreak();
        }

        final ReviewProgress progressToUpdate = currentProgress;
        new Thread(() -> {
            try {
                db.reviewProgressDao().update(progressToUpdate);
            } catch (Exception e) {
                Log.e(TAG, "Error updating progress", e);
            }
        }).start();

        String p = "<br><br>Heute: " + goals.getTodayCount() + "/" + goals.getDailyGoal() + "<br>Streak: " + goals.getStreak() + " Tage";
        String formName = currentForm.form != null ? currentForm.form : "";
        if (ok) {
            textResult.setText(Html.fromHtml("✅ Richtig: <b>" + formName + "</b>" + p, Html.FROM_HTML_MODE_LEGACY));
            speaker.speak(formName);
        } else {
            textResult.setText(Html.fromHtml("❌ Richtig wäre: <b>" + formName + "</b>" + p, Html.FROM_HTML_MODE_LEGACY));
            speaker.speakSlowly(formName);
        }
    }

    private String tenseName(String t) {
        if (t == null) return "";
        switch (t) {
            case "present": return "Présent";
            case "passe_compose": return "Passé composé";
            case "imparfait": return "Imparfait";
            case "futur_simple": return "Futur simple";
            case "conditionnel_present": return "Conditionnel présent";
            case "subjonctif_present": return "Subjonctif présent";
            default: return t;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speaker != null) {
            speaker.shutdown();
        }
    }
}
