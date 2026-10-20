package de.katharina.verbesfrancais.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.katharina.verbesfrancais.R;
import de.katharina.verbesfrancais.data.AppDatabase;
import de.katharina.verbesfrancais.data.ConjugationForm;
import de.katharina.verbesfrancais.data.ExampleSentence;
import de.katharina.verbesfrancais.data.Verb;
import de.katharina.verbesfrancais.helpers.GrammarInfoHelper;
import de.katharina.verbesfrancais.settings.TenseSelectionManager;
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
    private TextView textPronunciationResult;
    private EditText editMissingForm;
    private final Random random = new Random();

    private final ActivityResultLauncher<Intent> speechLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (matches != null && !matches.isEmpty()) {
                        evaluatePronunciation(matches.get(0));
                    }
                }
            }
    );

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    startSpeechRecognition();
                } else {
                    Toast.makeText(this, "Mikrofon-Berechtigung wird für die Aussprache-Prüfung benötigt.", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_practice);

        db = AppDatabase.getInstance(this);
        speaker = new FrenchSpeaker(this);

        textGermanSentence = findViewById(R.id.textGermanSentence);
        textFrenchSentence = findViewById(R.id.textFrenchSentence);
        textSentenceResult = findViewById(R.id.textSentenceResult);
        textPronunciationResult = findViewById(R.id.textPronunciationResult);
        editMissingForm = findViewById(R.id.editMissingForm);

        Button speak = findViewById(R.id.buttonSpeakSentence);
        Button checkPronunciation = findViewById(R.id.buttonCheckPronunciation);
        Button showGrammar = findViewById(R.id.buttonShowGrammar);
        Button check = findViewById(R.id.buttonCheckSentence);
        Button next = findViewById(R.id.buttonNextSentence);

        speak.setOnClickListener(v -> {
            if (currentSentence != null && currentSentence.sentenceFr != null) {
                speaker.speak(currentSentence.sentenceFr);
            }
        });

        checkPronunciation.setOnClickListener(v -> checkMicrophoneAndStartSpeech());

        showGrammar.setOnClickListener(v -> {
            if (currentVerb != null && currentSentence != null) {
                GrammarInfoHelper.showGrammarDialog(this, currentVerb.infinitive, currentVerb.translationDe, currentSentence.tense, speaker);
            }
        });

        check.setOnClickListener(v -> checkSentence());
        next.setOnClickListener(v -> loadNextSentence());

        loadNextSentence();
    }

    private void checkMicrophoneAndStartSpeech() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startSpeechRecognition();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void startSpeechRecognition() {
        if (currentSentence == null || currentSentence.sentenceFr == null) return;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "fr-FR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Sprechen Sie auf Französisch:\n\"" + currentSentence.sentenceFr + "\"");
        try {
            speechLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Spracherkennung auf diesem Gerät nicht verfügbar.", Toast.LENGTH_SHORT).show();
        }
    }

    private void evaluatePronunciation(String spokenText) {
        if (currentSentence == null || currentSentence.sentenceFr == null) return;
        String targetSentence = currentSentence.sentenceFr;

        double similarity = calculateSimilarity(spokenText, targetSentence);
        int scorePercent = (int) Math.round(similarity * 100);

        textPronunciationResult.setVisibility(View.VISIBLE);

        if (scorePercent >= 85) {
            String feedback = "🟢 <b>Excellente prononciation ! (" + scorePercent + "%)</b><br><br>" +
                    "Satz: <b>" + targetSentence + "</b><br>" +
                    "Erkannt: <i>\"" + spokenText + "\"</i>";
            textPronunciationResult.setText(Html.fromHtml(feedback, Html.FROM_HTML_MODE_LEGACY));
            speaker.speak("Excellente prononciation !");
        } else if (scorePercent >= 60) {
            String feedback = "🟡 <b>Bonne prononciation (" + scorePercent + "%)</b><br><br>" +
                    "Erkannt: <i>\"" + spokenText + "\"</i><br>" +
                    "Original: <b>" + targetSentence + "</b><br><br>" +
                    "<i>Hören Sie sich das Original noch einmal an:</i>";
            textPronunciationResult.setText(Html.fromHtml(feedback, Html.FROM_HTML_MODE_LEGACY));
            speaker.speakSlowly(targetSentence);
        } else {
            String feedback = "🔴 <b>Prononciation à améliorer (" + scorePercent + "%)</b><br><br>" +
                    "Erkannt: <i>\"" + spokenText + "\"</i><br>" +
                    "Sollte sein: <b>" + targetSentence + "</b><br><br>" +
                    "<i>Hören Sie sich das Original genau an und sprechen Sie nach:</i>";
            textPronunciationResult.setText(Html.fromHtml(feedback, Html.FROM_HTML_MODE_LEGACY));
            speaker.speakSlowly(targetSentence);
        }
    }

    private double calculateSimilarity(String s1, String s2) {
        String norm1 = normalizeForComparison(s1);
        String norm2 = normalizeForComparison(s2);

        if (norm1.isEmpty() && norm2.isEmpty()) return 1.0;
        if (norm1.isEmpty() || norm2.isEmpty()) return 0.0;
        if (norm1.equalsIgnoreCase(norm2)) return 1.0;

        int distance = computeLevenshteinDistance(norm1, norm2);
        int maxLength = Math.max(norm1.length(), norm2.length());
        double similarity = 1.0 - ((double) distance / maxLength);
        return Math.max(0.0, Math.min(1.0, similarity));
    }

    private String normalizeForComparison(String text) {
        if (text == null) return "";
        return text.toLowerCase()
                .replaceAll("[^a-zàâäéèêëîïôöùûüçœæ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private int computeLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private void loadNextSentence() {
        new Thread(() -> {
            try {
                TenseSelectionManager tenseManager = new TenseSelectionManager(this);
                List<String> selectedTensesList = new ArrayList<>(tenseManager.getSelectedTenses());

                List<ExampleSentence> all = db.exampleSentenceDao().getByTenses(selectedTensesList);
                if (all == null || all.isEmpty()) {
                    runOnUiThread(() -> {
                        if (isFinishing() || isDestroyed()) return;
                        Toast.makeText(this, "Keine Beispielsätze für die ausgewählten Zeiten vorhanden.", Toast.LENGTH_LONG).show();
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

                    textPronunciationResult.setVisibility(View.GONE);
                    textPronunciationResult.setText("");

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
