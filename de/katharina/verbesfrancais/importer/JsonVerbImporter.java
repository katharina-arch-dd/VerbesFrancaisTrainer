package de.katharina.verbesfrancais.importer;

import android.content.Context;
import android.net.Uri;
import androidx.room.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import de.katharina.verbesfrancais.data.*;

public class JsonVerbImporter {
    public static class ImportResult { public int verbs, forms, examples; }
    private final Context context;
    private final AppDatabase db;

    public JsonVerbImporter(Context c, AppDatabase db) {
        this.context = c;
        this.db = db;
    }

    public ImportResult importFromUri(Uri uri) throws Exception {
        try (InputStream in = context.getContentResolver().openInputStream(uri)) {
            if (in == null) throw new FileNotFoundException("Datei konnte nicht geöffnet werden.");
            return importFromStream(in);
        }
    }

    public ImportResult importFromStream(InputStream in) throws Exception {
        JSONObject root = new JSONObject(readTextFromStream(in));
        if (!root.has("verbs")) throw new IllegalArgumentException("Die Datei enthält kein Feld 'verbs'.");
        JSONArray arr = root.getJSONArray("verbs");
        ImportResult r = new ImportResult();
        for (int i = 0; i < arr.length(); i++) {
            importVerb(arr.getJSONObject(i), r);
        }
        return r;
    }

    @Transaction
    private void importVerb(JSONObject v, ImportResult r) throws Exception {
        String key = v.getString("importKey");
        Verb verb = new Verb();
        verb.importKey = key;
        verb.infinitive = v.getString("infinitive");
        verb.translationDe = v.optString("translationDe", "");
        verb.group = v.optString("group", "irregular");
        verb.auxiliary = v.optString("auxiliary", "avoir");
        verb.reflexive = v.optBoolean("reflexive", false);

        Verb existing = db.verbDao().findByImportKey(key);
        if (existing != null) verb.id = existing.id;
        long id = db.verbDao().insert(verb);
        if (verb.id == 0) verb.id = (int) id;

        r.verbs++;

        List<ConjugationForm> forms = new ArrayList<>();
        if (v.has("forms")) {
            Object formsObj = v.get("forms");
            if (formsObj instanceof JSONArray fa) {
                for (int i = 0; i < fa.length(); i++) {
                    JSONObject f = fa.getJSONObject(i);
                    ConjugationForm form = new ConjugationForm();
                    form.importKey = f.getString("importKey");
                    form.verbId = verb.id;
                    form.tense = f.getString("tense");
                    form.person = f.getString("person");
                    form.form = f.getString("form");
                    form.irregular = f.optBoolean("irregular", false);
                    forms.add(form);
                }
            } else if (formsObj instanceof JSONObject fo) {
                Iterator<String> tenseKeys = fo.keys();
                while (tenseKeys.hasNext()) {
                    String tense = tenseKeys.next();
                    JSONObject tenseObj = fo.optJSONObject(tense);
                    if (tenseObj != null) {
                        Iterator<String> personKeys = tenseObj.keys();
                        while (personKeys.hasNext()) {
                            String person = personKeys.next();
                            String formVal = tenseObj.getString(person);
                            ConjugationForm form = new ConjugationForm();
                            form.importKey = verb.infinitive + "|" + tense + "|" + person;
                            form.verbId = verb.id;
                            form.tense = tense;
                            form.person = person;
                            form.form = formVal;
                            form.irregular = "irregular".equalsIgnoreCase(verb.group);
                            forms.add(form);
                        }
                    }
                }
            }
        }
        db.conjugationFormDao().insertAll(forms);
        r.forms += forms.size();

        if (v.has("examples")) {
            JSONArray ea = v.getJSONArray("examples");
            List<ExampleSentence> exs = new ArrayList<>();
            for (int i = 0; i < ea.length(); i++) {
                JSONObject e = ea.getJSONObject(i);
                ExampleSentence ex = new ExampleSentence();
                ex.importKey = e.getString("importKey");
                ex.verbId = verb.id;
                ex.sentenceFr = e.getString("sentenceFr");
                ex.sentenceDe = e.getString("sentenceDe");
                ex.tense = e.getString("tense");
                ex.context = e.optString("context", "Alltag");
                ex.level = e.optString("level", "B1");
                exs.add(ex);
            }
            db.exampleSentenceDao().insertAll(exs);
            r.examples += exs.size();
        }
    }

    private String readTextFromStream(InputStream in) throws Exception {
        StringBuilder b = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                b.append(line).append("\n");
            }
        }
        return b.toString();
    }
}
