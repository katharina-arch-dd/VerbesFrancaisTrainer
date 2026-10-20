package de.katharina.verbesfrancais.helpers;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.katharina.verbesfrancais.tts.FrenchSpeaker;

public class GrammarInfoHelper {
    private static final String TAG = "GrammarInfoHelper";

    public static class TenseGrammar {
        public String tenseKey;
        public String tenseName;
        public String rule;
        public String formation;
        public Map<String, String> table = new HashMap<>();
    }

    public static class VerbGrammar {
        public String infinitive;
        public String translationDe;
        public Map<String, TenseGrammar> tenses = new HashMap<>();
    }

    private static final Map<String, VerbGrammar> cache = new HashMap<>();

    public static synchronized VerbGrammar getVerbGrammar(Context context, String infinitive) {
        if (infinitive == null) return null;
        if (cache.containsKey(infinitive.toLowerCase())) {
            return cache.get(infinitive.toLowerCase());
        }

        loadCacheFromAssets(context);
        return cache.get(infinitive.toLowerCase());
    }

    public static TenseGrammar getTenseGrammar(Context context, String infinitive, String tenseKey) {
        VerbGrammar vg = getVerbGrammar(context, infinitive);
        if (vg != null && tenseKey != null) {
            return vg.tenses.get(tenseKey);
        }
        return null;
    }

    private static void loadCacheFromAssets(Context context) {
        if (!cache.isEmpty()) return;
        try (InputStream in = context.getAssets().open("verbes_core_01.json");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            JSONObject root = new JSONObject(sb.toString());
            if (root.has("verbs")) {
                JSONArray verbsArr = root.getJSONArray("verbs");
                for (int index = 0; index < verbsArr.length(); index++) {
                    JSONObject vObj = verbsArr.getJSONObject(index);
                    VerbGrammar vg = new VerbGrammar();
                    vg.infinitive = vObj.optString("infinitive", "");
                    vg.translationDe = vObj.optString("translationDe", "");

                    if (vObj.has("grammarInfo")) {
                        JSONObject gObj = vObj.getJSONObject("grammarInfo");
                        Iterator<String> tenseKeys = gObj.keys();
                        while (tenseKeys.hasNext()) {
                            String tKey = tenseKeys.next();
                            JSONObject tObj = gObj.getJSONObject(tKey);
                            TenseGrammar tg = new TenseGrammar();
                            tg.tenseKey = tKey;
                            tg.tenseName = tObj.optString("tenseName", tKey);
                            tg.rule = tObj.optString("rule", "");
                            tg.formation = tObj.optString("formation", "");

                            if (tObj.has("table")) {
                                JSONObject tableObj = tObj.getJSONObject("table");
                                Iterator<String> personKeys = tableObj.keys();
                                while (personKeys.hasNext()) {
                                    String person = personKeys.next();
                                    tg.table.put(person, tableObj.getString(person));
                                }
                            }
                            vg.tenses.put(tKey, tg);
                        }
                    }
                    if (!vg.infinitive.isEmpty()) {
                        cache.put(vg.infinitive.toLowerCase(), vg);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading grammar info from assets", e);
        }
    }

    public static void showGrammarDialog(Context context, String infinitive, String translationDe, String tenseKey, FrenchSpeaker speaker) {
        TenseGrammar tg = getTenseGrammar(context, infinitive, tenseKey);
        VerbGrammar vg = getVerbGrammar(context, infinitive);

        StringBuilder body = new StringBuilder();

        if (tg != null) {
            if (tg.rule != null && !tg.rule.isEmpty()) {
                body.append("<b>Verwendung:</b><br>").append(tg.rule).append("<br><br>");
            }
            if (tg.formation != null && !tg.formation.isEmpty()) {
                body.append("<b>Bildung &amp; Besonderheiten:</b><br>").append(tg.formation).append("<br><br>");
            }
            if (tg.table != null && !tg.table.isEmpty()) {
                body.append("<b>Konjugationstabelle (").append(tg.tenseName).append("):</b><br>");
                for (Map.Entry<String, String> entry : tg.table.entrySet()) {
                    body.append("• <b>").append(entry.getValue()).append("</b><br>");
                }
            }
        } else {
            body.append("Keine vertieften Grammatikdaten für dieses Verb oder diese Zeitform verfügbar.");
        }

        String trans = (translationDe != null && !translationDe.isEmpty()) ? translationDe : (vg != null ? vg.translationDe : "");
        String title = (infinitive != null ? infinitive : "") +
                (!trans.isEmpty() ? " (" + trans + ")" : "") +
                (tg != null && tg.tenseName != null ? " - " + tg.tenseName : "");

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(Html.fromHtml(body.toString(), Html.FROM_HTML_MODE_LEGACY))
                .setPositiveButton("Schließen", null)
                .setNeutralButton("🔊 Vorlesen", (d, which) -> {
                    if (speaker != null && tg != null && tg.table != null) {
                        StringBuilder speechText = new StringBuilder();
                        for (String formText : tg.table.values()) {
                            speechText.append(formText).append(". ");
                        }
                        speaker.speak(speechText.toString());
                    }
                })
                .show();
    }
}
