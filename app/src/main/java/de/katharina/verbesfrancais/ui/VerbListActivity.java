package de.katharina.verbesfrancais.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import de.katharina.verbesfrancais.R;
import de.katharina.verbesfrancais.data.AppDatabase;
import de.katharina.verbesfrancais.data.ConjugationForm;
import de.katharina.verbesfrancais.data.Verb;
import de.katharina.verbesfrancais.tts.FrenchSpeaker;

public class VerbListActivity extends AppCompatActivity {
    private static final String TAG = "VerbListActivity";
    private AppDatabase db;
    private FrenchSpeaker speaker;
    private Spinner spinnerTense;
    private Button buttonPracticeFiltered;
    private RecyclerView recyclerVerbForms;
    private VerbFormAdapter adapter;

    private static final String[] TENSE_KEYS = {
        "",
        "present",
        "passe_compose",
        "imparfait",
        "futur_simple",
        "conditionnel_present",
        "subjonctif_present"
    };

    private static final String[] TENSE_LABELS = {
        "Alle Zeitformen",
        "Présent",
        "Passé composé",
        "Imparfait",
        "Futur simple",
        "Conditionnel présent",
        "Subjonctif présent"
    };

    private String selectedTenseKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verb_list);

        db = AppDatabase.getInstance(this);
        speaker = new FrenchSpeaker(this);

        spinnerTense = findViewById(R.id.spinnerTense);
        buttonPracticeFiltered = findViewById(R.id.buttonPracticeFiltered);
        recyclerVerbForms = findViewById(R.id.recyclerVerbForms);

        recyclerVerbForms.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VerbFormAdapter(form -> {
            if (form != null && form.form != null) {
                speaker.speak(form.form);
            }
        });
        recyclerVerbForms.setAdapter(adapter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_item,
            TENSE_LABELS
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTense.setAdapter(spinnerAdapter);

        spinnerTense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < TENSE_KEYS.length) {
                    selectedTenseKey = TENSE_KEYS[position];
                    loadForms();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonPracticeFiltered.setOnClickListener(v -> {
            Intent intent = new Intent(this, PracticeActivity.class);
            intent.putExtra("tense", selectedTenseKey);
            startActivity(intent);
        });

        loadForms();
    }

    private void loadForms() {
        new Thread(() -> {
            try {
                List<Verb> verbs = db.verbDao().getAll();
                List<ConjugationForm> forms = db.conjugationFormDao().getFilteredForms(selectedTenseKey, 0);
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    List<Verb> safeVerbs = verbs != null ? verbs : Collections.emptyList();
                    List<ConjugationForm> safeForms = forms != null ? forms : Collections.emptyList();
                    adapter.setForms(safeForms, safeVerbs);
                    if (safeForms.isEmpty()) {
                        Toast.makeText(this, "Keine Verbformen für diese Auswahl gefunden.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error loading forms", e);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speaker != null) {
            speaker.shutdown();
        }
    }
}
