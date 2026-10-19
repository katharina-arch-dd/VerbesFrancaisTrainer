package de.katharina.verbesfrancais.ui;

import android.content.Intent;
import android.os.Bundle;
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
    private TextView textOverview;
    private TextView textTenseStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        db = AppDatabase.getInstance(this);
        textOverview = findViewById(R.id.textOverview);
        textTenseStats = findViewById(R.id.textTenseStats);

        Button weakest = findViewById(R.id.buttonWeakestForms);
        weakest.setOnClickListener(v -> openWeakestForms());

        loadStats();
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

        StringBuilder b = new StringBuilder("Fehlerrate nach Zeit:\n\n");
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
}
