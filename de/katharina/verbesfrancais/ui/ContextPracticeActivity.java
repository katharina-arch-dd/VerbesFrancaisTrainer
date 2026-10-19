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
import de.katharina.verbesfrancais.data.ExampleSentence;
import de.katharina.verbesfrancais.data.Verb;
import de.katharina.verbesfrancais.tts.FrenchSpeaker;

public class ContextPracticeActivity extends AppCompatActivity {
    private static final String TAG = "ContextPracticeActivity";
    private AppDatabase db;
    private FrenchSpeaker speaker;
    private ExampleSentence currentSentence;
    private Verb currentVerb;
    private String currentExpectedForm;

    private TextView textGermanSentence;
    private TextView textFrenchSentence;
    private TextView textSentenceResult;
    private EditText editMissingForm;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_practice);

        db = AppDatabase.getInstance(this);
        speaker = new FrenchSpeaker(this);

        textGermanSentence = findViewById(R.id.textGermanSentence);
        textFrenchSentence = findViewById(R.id.textFrenchSentence);
        textSentenceResult = findViewById(R.id.textSentenceResult);
        editMissingForm = findViewById(R.id.editMissingForm);

        Button speak = findViewById(R.id.buttonSpeakSentence);
        Button check = findViewById(R.id.buttonCheckSentence);
        Button next = findViewById(R.id.buttonNextSentence);

        speak.setOnClickListener(v -> {
            if (currentSentence != null && currentSentence.sentenceFr != null) {
                speaker.speak(currentSentence.sentenceFr);
            }
        });

        check.setOnClickListener(v -> checkSentence());
        next.setOnClickListener(v -> loadNextSentence());

        loadNextSentence();
    }

    private void loadNextSentence() {
        new Thread(() -> {
            try {
                List<ExampleSentence> all = db.exampleSentenceDao().getAll();
                if (all == null || all.isEmpty()) {
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        Toast.makeText(this, "Keine Beispielsätze vorhanden.", Toast.LENGTH_LONG).show();
                    });
                    return;
                }

                currentSentence = all.get(random.nextInt(all.size()));
                if (currentSentence != null) {
                    currentVerb = db.verbDao().findById(currentSentence.verbId);
                    currentExpectedForm = findExpectedForm(currentSentence);
                } else {
                    currentVerb = null;
                    currentExpectedForm = null;
                }

                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    if (currentSentence == null) return;

                    textGermanSentence.setText(currentSentence.sentenceDe != null ? currentSentence.sentenceDe : "");

                    if (currentExpectedForm == null) {
                        String rawFr = currentSentence.sentenceFr != null ? currentSentence.sentenceFr : "";
                        textFrenchSentence.setText(Html.fromHtml("<b>" + rawFr + "</b>", Html.FROM_HTML_MODE_LEGACY));
                        textSentenceResult.setText("Keine passende Verbform gefunden.");
                        return;
                    }

                    String verbInfinitive = (currentVerb != null && currentVerb.infinitive != null) ? currentVerb.infinitive : "";
                    String displayFr = (currentSentence.sentenceFr != null)
                            ? currentSentence.sentenceFr.replace(currentExpectedForm, "___")
                            : "";

                    String formattedText = "Verb: <b>" + verbInfinitive + "</b><br>Zeit: " + tenseName(currentSentence.tense) + "<br><br><b>" + displayFr + "</b>";
                    textFrenchSentence.setText(Html.fromHtml(formattedText, Html.FROM_HTML_MODE_LEGACY));
                    textSentenceResult.setText("");
                    editMissingForm.setText("");
                });
            } catch (Exception e) {
                Log.e(TAG, "Error loading next sentence", e);
            }
        }).start();
    }

    private void checkSentence() {
        if (currentSentence == null) return;

        String answer = editMissingForm.getText().toString().trim();

        new Thread(() -> {
            try {
                String expected = currentExpectedForm != null ? currentExpectedForm : findExpectedForm(currentSentence);
                currentExpectedForm = expected;

                final String finalExpected = expected;
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;

                    if (finalExpected == null) {
                        textSentenceResult.setText("Keine passende Verbform gefunden.");
                        return;
                    }

                    String sentenceFr = (currentSentence != null && currentSentence.sentenceFr != null) ? currentSentence.sentenceFr : "";
                    if (answer.equalsIgnoreCase(finalExpected)) {
                        textSentenceResult.setText(Html.fromHtml("✅ Richtig!<br><br><b>" + sentenceFr + "</b>", Html.FROM_HTML_MODE_LEGACY));
                        speaker.speak(sentenceFr);
                    } else {
                        textSentenceResult.setText(Html.fromHtml("❌ Richtig wäre: <b>" + finalExpected + "</b><br><br><b>" + sentenceFr + "</b>", Html.FROM_HTML_MODE_LEGACY));
                        speaker.speakSlowly(sentenceFr);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error checking sentence", e);
            }
        }).start();
    }

    private String findExpectedForm(ExampleSentence sentence) {
        if (sentence == null || sentence.sentenceFr == null || sentence.tense == null) return null;
        try {
            List<ConjugationForm> forms = db.conjugationFormDao().getByVerb(sentence.verbId);
            if (forms == null) return null;

            for (ConjugationForm f : forms) {
                if (f != null && f.tense != null && f.form != null) {
                    if (f.tense.equalsIgnoreCase(sentence.tense) &&
                            sentence.sentenceFr.toLowerCase().contains(f.form.toLowerCase())) {
                        return f.form;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error finding expected form", e);
        }
        return null;
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
