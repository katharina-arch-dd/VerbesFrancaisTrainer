package de.katharina.verbesfrancais.settings;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TenseSelectionManager {
    private static final String PREF_NAME = "verbes_francais_settings";
    private static final String KEY_SELECTED_TENSES = "selected_tenses";

    public static final String TENSE_PRESENT = "present";
    public static final String TENSE_PASSE_COMPOSE = "passe_compose";
    public static final String TENSE_IMPARFAIT = "imparfait";
    public static final String TENSE_FUTUR_SIMPLE = "futur_simple";
    public static final String TENSE_CONDITIONNEL_PRESENT = "conditionnel_present";
    public static final String TENSE_SUBJONCTIF_PRESENT = "subjonctif_present";

    public static final List<String> ALL_TENSE_KEYS = Arrays.asList(
            TENSE_PRESENT,
            TENSE_PASSE_COMPOSE,
            TENSE_IMPARFAIT,
            TENSE_FUTUR_SIMPLE,
            TENSE_CONDITIONNEL_PRESENT,
            TENSE_SUBJONCTIF_PRESENT
    );

    public static final List<String> ALL_TENSE_LABELS = Arrays.asList(
            "Présent",
            "Passé composé",
            "Imparfait",
            "Futur simple",
            "Conditionnel présent",
            "Subjonctif présent"
    );

    private final SharedPreferences prefs;

    public TenseSelectionManager(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public Set<String> getSelectedTenses() {
        Set<String> saved = prefs.getStringSet(KEY_SELECTED_TENSES, null);
        if (saved == null || saved.isEmpty()) {
            return new HashSet<>(ALL_TENSE_KEYS);
        }
        return new HashSet<>(saved);
    }

    public List<String> getSelectedTensesList() {
        Set<String> selected = getSelectedTenses();
        if (selected == null || selected.isEmpty()) {
            return new ArrayList<>(ALL_TENSE_KEYS);
        }
        return new ArrayList<>(selected);
    }

    public void setSelectedTenses(Set<String> tenses) {
        Set<String> targetTenses = (tenses == null || tenses.isEmpty()) ? new HashSet<>(ALL_TENSE_KEYS) : tenses;
        prefs.edit().putStringSet(KEY_SELECTED_TENSES, new HashSet<>(targetTenses)).apply();
    }

    public boolean isTenseSelected(String tenseKey) {
        if (tenseKey == null || tenseKey.isEmpty()) return true;
        return getSelectedTenses().contains(tenseKey);
    }

    public void setTenseSelected(String tenseKey, boolean selected) {
        Set<String> current = new HashSet<>(getSelectedTenses());
        if (selected) {
            current.add(tenseKey);
        } else {
            current.remove(tenseKey);
        }
        setSelectedTenses(current);
    }

    public String getSelectionSummaryText() {
        Set<String> selected = getSelectedTenses();
        if (selected.size() == ALL_TENSE_KEYS.size()) {
            return "Alle 6 Zeitformen aktiv";
        }
        if (selected.isEmpty()) {
            return "Keine Zeitform ausgewählt";
        }
        List<String> names = new ArrayList<>();
        for (int index = 0; index < ALL_TENSE_KEYS.size(); index++) {
            if (selected.contains(ALL_TENSE_KEYS.get(index))) {
                names.add(ALL_TENSE_LABELS.get(index));
            }
        }
        return selected.size() + " Zeitformen aktiv: " + String.join(", ", names);
    }

    public static String getLabelForTenseKey(String key) {
        int index = ALL_TENSE_KEYS.indexOf(key);
        if (index >= 0 && index < ALL_TENSE_LABELS.size()) {
            return ALL_TENSE_LABELS.get(index);
        }
        return key;
    }
}
