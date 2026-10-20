package de.katharina.verbesfrancais.ui;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

import de.katharina.verbesfrancais.R;
import de.katharina.verbesfrancais.data.AppDatabase;
import de.katharina.verbesfrancais.data.ConjugationForm;
import de.katharina.verbesfrancais.data.StatsResult;
import de.katharina.verbesfrancais.data.TenseStats;

public class StatsActivity extends AppCompatActivity {
    private static final String TAG = "StatsActivity";
    private AppDatabase db;
    private TextToSpeech tts;
    private boolean ttsReady = false;

    private TextView textOverview;
    private TextView textTenseStats;
    private TextView textRecommendation;
    private String currentRecommendationText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        db = AppDatabase.getInstance(this);
        textOverview = findViewById(R.id.textOverview);
        textTenseStats = findViewById(R.id.textTenseStats);
        textRecommendation = findViewById(R.id.textRecommendation);

        Button buttonSpeakRecommendation = findViewById(R.id.buttonSpeakRecommendation);
        Button weakest = findViewById(R.id.buttonWeakestForms);

        initTts();

        buttonSpeakRecommendation.setOnClickListener(v -> speakRecommendation());
        weakest.setOnClickListener(v -> openWeakestForms());

        loadStats();
    }

    private void initTts() {
        tts = new TextToSpeech(getApplicationContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                try {
                    int r = tts.setLanguage(Locale.GERMAN);
                    ttsReady = (r != TextToSpeech.LANG_MISSING_DATA && r != TextToSpeech.LANG_NOT_SUPPORTED);
                } catch (Exception e) {
                    Log.e(TAG, "Error setting TTS language", e);
                    ttsReady = false;
                }
            }
        });
    }

    private void speakRecommendation() {
        if (!ttsReady || tts == null || currentRecommendationText.isEmpty()) {
            Toast.makeText(this, "Sprachausgabe noch nicht bereit.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            tts.speak(currentRecommendationText, TextToSpeech.QUEUE_FLUSH, null, "stats_summary");
        } catch (Exception e) {
            Log.e(TAG, "Error in speakRecommendation", e);
        }
    }

    private void loadStats() {
        new Thread(() -> {
            try {
                int total = db.reviewProgressDao().countAll();
                int learned = db.reviewProgressDao().countLearned();
                int due = db.reviewProgressDao().countDue(System.currentTimeMillis());
                int problems = db.reviewProgressDao().countProblems();

                StatsResult stats = new StatsResult(total, learned, due, problems);
                List<TenseStats> tenseStats = db.reviewProgressDao().getStatsByTense();

                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    showStats(stats, tenseStats);
                });
            } catch (Exception e) {
                Log.e(TAG, "Error loading stats", e);
            }
        }).start();
    }

    private void showStats(StatsResult s, List<TenseStats> ts) {
        if (s == null) return;

        textOverview.setText("Verbformen insgesamt: " + s.totalForms +
                "\nMindestens einmal richtig: " + s.learnedForms +
                "\nHeute fällig: " + s.dueForms +
                "\nFormen mit Fehlern: " + s.problemForms);

        StringBuilder b = new StringBuilder();
        if (ts == null || ts.isEmpty()) {
            b.append("Noch keine Übungen gespeichert.");
        } else {
            for (TenseStats t : ts) {
                if (t != null) {
                    int percent = t.total == 0 ? 0 : (t.wrong * 100) / t.total;
                    b.append(tenseName(t.tense)).append(": ").append(t.wrong)
                            .append(" Fehler in ").append(t.total).append(" Formen (")
                            .append(String.format(Locale.GERMAN, "%d", percent)).append(" %)\n");
                }
            }
        }
        textTenseStats.setText(b.toString());

        currentRecommendationText = generateSummaryAndRecommendation(s, ts);
        textRecommendation.setText(currentRecommendationText);
    }

    private String generateSummaryAndRecommendation(StatsResult s, List<TenseStats> ts) {
        if (s == null || s.totalForms == 0) {
            return "Willkommen! Du hast noch keine Übungen absolviert. Starte am besten mit den Verben im Présent, um dein erstes Tagesziel zu erreichen!";
        }

        StringBuilder text = new StringBuilder();
        int masteryPercent = (s.learnedForms * 100) / s.totalForms;

        text.append("Fortschritt: Du hast ").append(s.learnedForms).append(" von ").append(s.totalForms)
                .append(" Formen gelernt (").append(masteryPercent).append("%). ");

        if (s.dueForms > 0) {
            text.append("Heute sind noch ").append(s.dueForms).append(" Formen fällig. ");
        } else {
            text.append("Für heute hast du alle fälligen Wiederholungen gemeistert! ");
        }

        TenseStats worstTense = null;
        double maxErrorRate = -1;

        if (ts != null) {
            for (TenseStats t : ts) {
                if (t != null && t.total > 0 && t.wrong > 0) {
                    double rate = ((double) t.wrong) / t.total;
                    if (rate > maxErrorRate) {
                        maxErrorRate = rate;
                        worstTense = t;
                    }
                }
            }
        }

        if (worstTense != null) {
            int errPercent = (int) Math.round(maxErrorRate * 100);
            text.append("\n\nEmpfehlung: Deine schwächste Zeitform ist ").append(tenseName(worstTense.tense))
                    .append(" mit einer Fehlerrate von ").append(errPercent).append(" %. ")
                    .append("Empfohlen: Nutze 'Fehler wiederholen', um gezielt diese Formen zu festigen.");
        } else if (s.problemForms > 0) {
            text.append("\n\nEmpfehlung: Du hast ").append(s.problemForms).append(" knifflige Formen. ")
                    .append("Nutze die Funktion 'Fehler wiederholen', um deine Fehlerquote zu senken.");
        } else if (masteryPercent >= 80) {
            text.append("\n\nEmpfehlung: Hervorragend! Du beherrschst bereits über 80 % der Verbformen. ")
                    .append("Halte deine tägliche Serie aufrecht!");
        } else {
            text.append("\n\nEmpfehlung: Absolviere täglich dein Tagesziel, um deinen Lernfortschritt schrittweise auszubauen.");
        }

        return text.toString();
    }

    private void openWeakestForms() {
        new Thread(() -> {
            try {
                List<ConjugationForm> weakest = db.conjugationFormDao().getWeakestForms(20);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    if (weakest == null || weakest.isEmpty()) {
                        Toast.makeText(this, "Noch keine schwachen Formen vorhanden.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(this, weakest.size() + " schwächste Formen ausgewählt.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, ErrorPracticeActivity.class));
                });
            } catch (Exception e) {
                Log.e(TAG, "Error fetching weakest forms", e);
            }
        }).start();
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
        if (tts != null) {
            try {
                tts.stop();
                tts.shutdown();
            } catch (Exception e) {
                Log.e(TAG, "Error shutting down TTS", e);
            }
            tts = null;
        }
    }
}
