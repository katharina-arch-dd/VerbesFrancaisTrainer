package de.katharina.verbesfrancais.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import de.katharina.verbesfrancais.R;
import de.katharina.verbesfrancais.backup.BackupManager;
import de.katharina.verbesfrancais.data.AppDatabase;
import de.katharina.verbesfrancais.importer.JsonVerbImporter;
import de.katharina.verbesfrancais.settings.DailyGoalManager;
import de.katharina.verbesfrancais.settings.TenseSelectionManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AppDatabase db;
    private DailyGoalManager goals;
    private TenseSelectionManager tenseManager;

    private TextView textDailyOverview;
    private TextView textTenseSelectionSummary;
    private EditText editDailyGoal;

    private final ActivityResultLauncher<Intent> jsonPicker = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            r -> {
                if (r.getResultCode() == RESULT_OK && r.getData() != null && r.getData().getData() != null) {
                    importJson(r.getData().getData());
                }
            }
    );

    private final ActivityResultLauncher<Intent> exportPicker = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            r -> {
                if (r.getResultCode() == RESULT_OK && r.getData() != null && r.getData().getData() != null) {
                    exportBackup(r.getData().getData());
                }
            }
    );

    private final ActivityResultLauncher<Intent> importBackupPicker = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            r -> {
                if (r.getResultCode() == RESULT_OK && r.getData() != null && r.getData().getData() != null) {
                    restoreBackup(r.getData().getData());
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        goals = new DailyGoalManager(this);
        tenseManager = new TenseSelectionManager(this);

        textDailyOverview = findViewById(R.id.textDailyOverview);
        textTenseSelectionSummary = findViewById(R.id.textTenseSelectionSummary);
        editDailyGoal = findViewById(R.id.editDailyGoal);

        Button saveGoal = findViewById(R.id.buttonSaveGoal);
        Button selectTenses = findViewById(R.id.buttonSelectTenses);
        Button importJson = findViewById(R.id.buttonImportJson);
        Button practice = findViewById(R.id.buttonPractice);
        Button list = findViewById(R.id.buttonVerbList);
        Button context = findViewById(R.id.buttonContextPractice);
        Button errors = findViewById(R.id.buttonErrorPractice);
        Button exportB = findViewById(R.id.buttonExportBackup);
        Button importB = findViewById(R.id.buttonImportBackup);

        showDailyOverview();
        showTenseSelectionSummary();
        ensureDefaultVerbsLoaded();

        saveGoal.setOnClickListener(v -> saveDailyGoal());
        selectTenses.setOnClickListener(v -> showTenseSelectionDialog());
        importJson.setOnClickListener(v -> openJsonPicker());
        practice.setOnClickListener(v -> startActivity(new Intent(this, PracticeActivity.class)));
        list.setOnClickListener(v -> startActivity(new Intent(this, VerbListActivity.class)));
        context.setOnClickListener(v -> startActivity(new Intent(this, ContextPracticeActivity.class)));
        errors.setOnClickListener(v -> startActivity(new Intent(this, ErrorPracticeActivity.class)));
        exportB.setOnClickListener(v -> openExportPicker());
        importB.setOnClickListener(v -> openImportPicker());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_stats) {
            startActivity(new Intent(this, StatsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ensureDefaultVerbsLoaded() {
        new Thread(() -> {
            try {
                if (db.verbDao().count() == 0) {
                    try (InputStream in = getAssets().open("verbes_core_01.json")) {
                        JsonVerbImporter importer = new JsonVerbImporter(this, db);
                        importer.importFromStream(in);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error importing default verbs from assets", e);
            }
        }).start();
    }

    private void showDailyOverview() {
        textDailyOverview.setText("Heute: " + goals.getTodayCount() + "/" + goals.getDailyGoal() + " Formen\nStreak: " + goals.getStreak() + " Tage");
    }

    private void showTenseSelectionSummary() {
        if (textTenseSelectionSummary != null && tenseManager != null) {
            textTenseSelectionSummary.setText(tenseManager.getSelectionSummaryText());
        }
    }

    private void showTenseSelectionDialog() {
        Set<String> currentSelected = tenseManager.getSelectedTenses();
        String[] labels = TenseSelectionManager.ALL_TENSE_LABELS.toArray(new String[0]);
        boolean[] checkedItems = new boolean[labels.length];

        for (int i = 0; i < labels.length; i++) {
            checkedItems[i] = currentSelected.contains(TenseSelectionManager.ALL_TENSE_KEYS.get(i));
        }

        new AlertDialog.Builder(this)
                .setTitle("Zu trainierende Verbformen auswählen")
                .setMultiChoiceItems(labels, checkedItems, (dialog, which, isChecked) -> checkedItems[which] = isChecked)
                .setPositiveButton("Speichern", (dialog, which) -> {
                    Set<String> newSelection = new HashSet<>();
                    for (int i = 0; i < checkedItems.length; i++) {
                        if (checkedItems[i]) {
                            newSelection.add(TenseSelectionManager.ALL_TENSE_KEYS.get(i));
                        }
                    }
                    if (newSelection.isEmpty()) {
                        Toast.makeText(this, "Bitte mindestens eine Zeitform auswählen.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tenseManager.setSelectedTenses(newSelection);
                    showTenseSelectionSummary();
                    Toast.makeText(this, "Zeitformen gespeichert.", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton("Alle auswählen", (dialog, which) -> {
                    tenseManager.setSelectedTenses(new HashSet<>(TenseSelectionManager.ALL_TENSE_KEYS));
                    showTenseSelectionSummary();
                    Toast.makeText(this, "Alle Zeitformen ausgewählt.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Abbrechen", null)
                .show();
    }

    private void saveDailyGoal() {
        String v = editDailyGoal.getText().toString();
        if (v.isEmpty()) {
            Toast.makeText(this, "Bitte eine Zahl eingeben.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            goals.setDailyGoal(Integer.parseInt(v));
            showDailyOverview();
            Toast.makeText(this, "Tagesziel gespeichert.", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Bitte eine gültige Zahl eingeben.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openJsonPicker() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("application/json");
        jsonPicker.launch(i);
    }

    private void importJson(Uri uri) {
        new Thread(() -> {
            try {
                JsonVerbImporter im = new JsonVerbImporter(this, db);
                JsonVerbImporter.ImportResult r = im.importFromUri(uri);
                runOnUiThread(() -> Toast.makeText(this, r.verbs + " Verben, " + r.forms + " Formen und " + r.examples + " Beispielsätze importiert.", Toast.LENGTH_LONG).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Import fehlgeschlagen: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void openExportPicker() {
        Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("application/json");
        i.putExtra(Intent.EXTRA_TITLE, "verbes_francais_backup.json");
        exportPicker.launch(i);
    }

    private void openImportPicker() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("application/json");
        importBackupPicker.launch(i);
    }

    private void exportBackup(Uri uri) {
        new Thread(() -> {
            try {
                new BackupManager(this, db).exportToUri(uri);
                runOnUiThread(() -> Toast.makeText(this, "Backup erfolgreich exportiert.", Toast.LENGTH_LONG).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Export fehlgeschlagen: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void restoreBackup(Uri uri) {
        new Thread(() -> {
            try {
                BackupManager.RestoreResult r = new BackupManager(this, db).restoreFromUri(uri);
                runOnUiThread(() -> Toast.makeText(this, r.verbs + " Verben, " + r.forms + " Formen, " + r.examples + " Sätze und " + r.progress + " Fortschritte wiederhergestellt.", Toast.LENGTH_LONG).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Wiederherstellung fehlgeschlagen: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
