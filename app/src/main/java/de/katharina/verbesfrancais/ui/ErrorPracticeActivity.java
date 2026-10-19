package de.katharina.verbesfrancais.ui;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Random;

import de.katharina.verbesfrancais.R;
import de.katharina.verbesfrancais.data.AppDatabase;
import de.katharina.verbesfrancais.data.ConjugationForm;
import de.katharina.verbesfrancais.data.ReviewProgress;
import de.katharina.verbesfrancais.data.Verb;
import de.katharina.verbesfrancais.practice.PracticeEngine;
import de.katharina.verbesfrancais.tts.FrenchSpeaker;

public class ErrorPracticeActivity extends AppCompatActivity {
    private static final String TAG = "ErrorPracticeActivity";
    private AppDatabase db;
    private FrenchSpeaker speaker;
    private PracticeEngine engine;

    private ConjugationForm currentForm;
    private Verb currentVerb;
    private ReviewProgress currentProgress;

    private TextView textVerb;
    private TextView textTask;
    private TextView textResult;
    private EditText editAnswer;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        db = AppDatabase.getInstance(this);
        speaker = new FrenchSpeaker(this);
        engine = new PracticeEngine();

        textVerb = findViewById(R.id.textVerb);
        textTask = findViewById(R.id.textTask);
        textResult = findViewById(R.id.textResult);
        editAnswer = findViewById(R.id.editAnswer);

        Button speakVerb = findViewById(R.id.buttonSpeakVerb);
        Button speakForm = findViewById(R.id.buttonSpeakForm);
        Button check = findViewById(R.id.buttonCheck);
        Button next = findViewById(R.id.buttonNext);

        speakVerb.setOnClickListener(v -> {
            if (currentVerb != null && currentVerb.infinitive != null) {
                speaker.speak(currentVerb.infinitive);
            }
        });

        speakForm.setOnClickListener(v -> {
            if (currentForm != null && currentForm.form != null) {
                speaker.speakSlowly(currentForm.form);
            }
        });

        check.setOnClickListener(v -> checkAnswer());
        next.setOnClickListener(v -> loadNextError());

        loadNextError();
    }

    private void loadNextError() {
        new Thread(() -> {
            try {
                currentForm = null;
                currentVerb = null;
                currentProgress = null;

                List<ReviewProgress> problems = db.reviewProgressDao().getProblemForms();
                if (problems == null || problems.isEmpty()) {
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        Toast.makeText(this, "Aktuell keine Fehler zum Wiederholen.", Toast.LENGTH_LONG).show();
                    });
                    return;
                }

                currentProgress = problems.get(random.nextInt(Math.min(problems.size(), 10)));
                if (currentProgress != null) {
                    currentForm = db.conjugationFormDao().findById(currentProgress.formId);
                    if (currentForm != null) {
                        currentVerb = db.verbDao().findById(currentForm.verbId);
                    }
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
                Log.e(TAG, "Error loading next error form", e);
            }
        }).start();
    }

    private void checkAnswer() {
        if (currentForm == null || currentProgress == null) return;

        boolean ok = engine.isCorrect(currentForm, editAnswer.getText().toString());
        currentProgress = engine.updateProgress(currentProgress, ok);

        final ReviewProgress progressToUpdate = currentProgress;
        new Thread(() -> {
            try {
                db.reviewProgressDao().update(progressToUpdate);
            } catch (Exception e) {
                Log.e(TAG, "Error updating progress", e);
            }
        }).start();

        String formName = currentForm.form != null ? currentForm.form : "";
        if (ok) {
            textResult.setText(Html.fromHtml("✅ Richtig: <b>" + formName + "</b>", Html.FROM_HTML_MODE_LEGACY));
            speaker.speak(formName);
        } else {
            textResult.setText(Html.fromHtml("❌ Richtig wäre: <b>" + formName + "</b>", Html.FROM_HTML_MODE_LEGACY));
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
