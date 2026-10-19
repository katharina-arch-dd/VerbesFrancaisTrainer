package de.katharina.verbesfrancais.ui;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.katharina.verbesfrancais.R;
import de.katharina.verbesfrancais.data.ConjugationForm;
import de.katharina.verbesfrancais.data.Verb;

public class VerbFormAdapter extends RecyclerView.Adapter<VerbFormAdapter.ViewHolder> {
    public interface OnFormClickListener {
        void onFormClick(ConjugationForm form);
    }

    private final List<ConjugationForm> forms = new ArrayList<>();
    private final List<Verb> verbs = new ArrayList<>();
    private final OnFormClickListener listener;

    public VerbFormAdapter(OnFormClickListener l) {
        listener = l;
    }

    public void setForms(List<ConjugationForm> f, List<Verb> v) {
        forms.clear();
        if (f != null) forms.addAll(f);
        verbs.clear();
        if (v != null) verbs.addAll(v);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup p, int t) {
        return new ViewHolder(LayoutInflater.from(p.getContext()).inflate(R.layout.item_verb_form, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        ConjugationForm f = forms.get(pos);
        Verb v = findVerb(f.verbId);
        h.textVerb.setText(v == null || v.infinitive == null ? "?" : v.infinitive);
        
        String formStr = f.form != null ? f.form : "";
        String personStr = f.person != null ? f.person : "";
        String formattedInfo = tenseName(f.tense) + " - " + personStr + " -> <b>" + formStr + "</b>";
        h.textFormInfo.setText(Html.fromHtml(formattedInfo, Html.FROM_HTML_MODE_LEGACY));

        h.itemView.setOnClickListener(x -> {
            if (listener != null) listener.onFormClick(f);
        });
    }

    @Override
    public int getItemCount() {
        return forms.size();
    }

    private Verb findVerb(int id) {
        for (Verb v : verbs) {
            if (v != null && v.id == id) return v;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textVerb;
        public final TextView textFormInfo;

        public ViewHolder(@NonNull View v) {
            super(v);
            textVerb = v.findViewById(R.id.textVerb);
            textFormInfo = v.findViewById(R.id.textFormInfo);
        }
    }
}
