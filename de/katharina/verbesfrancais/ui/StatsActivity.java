package de.katharina.verbesfrancais.ui;
import android.content.Intent; import android.os.Bundle; import android.widget.*; import androidx.appcompat.app.AppCompatActivity;
import java.util.List; import java.util.Locale;
import de.katharina.verbesfrancais.R; import de.katharina.verbesfrancais.data.*;
public class StatsActivity extends AppCompatActivity {
 private AppDatabase db; private TextView textOverview,textTenseStats;
 @Override protected void onCreate(Bundle s){super.onCreate(s);setContentView(R.layout.activity_stats);
  db=AppDatabase.getInstance(this);textOverview=findViewById(R.id.textOverview);textTenseStats=findViewById(R.id.textTenseStats);
  Button weakest=findViewById(R.id.buttonWeakestForms);weakest.setOnClickListener(v->openWeakestForms());loadStats();}
 private void loadStats(){new Thread(()->{int total=db.reviewProgressDao().countAll(),learned=db.reviewProgressDao().countLearned(),due=db.reviewProgressDao().countDue(System.currentTimeMillis()),problems=db.reviewProgressDao().countProblems();
  StatsResult stats=new StatsResult(total,learned,due,problems);List<TenseStats> tenseStats=db.reviewProgressDao().getStatsByTense();
  runOnUiThread(()->showStats(stats,tenseStats));}).start();}
 private void showStats(StatsResult s,List<TenseStats> ts){textOverview.setText("Verbformen insgesamt: "+s.totalForms+"\nMindestens einmal richtig: "+s.learnedForms+"\nHeute faellig: "+s.dueForms+"\nFormen mit Fehlern: "+s.problemForms);
  StringBuilder b=new StringBuilder("Fehlerrate nach Zeit:\n\n"); if(ts.isEmpty())b.append("Noch keine Uebungen gespeichert.");
  else for(TenseStats t:ts){int percent=t.total==0?0:(t.wrong*100)/t.total;b.append(tenseName(t.tense)).append(": ").append(t.wrong).append(" Fehler in ").append(t.total).append(" Formen (").append(String.format(Locale.GERMAN,"%d",percent)).append(" %)\n");}
  textTenseStats.setText(b.toString());}
 private void openWeakestForms(){new Thread(()->{List<ConjugationForm> weakest=db.conjugationFormDao().getWeakestForms(20);
  runOnUiThread(()->{if(weakest.isEmpty()){Toast.makeText(this,"Noch keine schwachen Formen vorhanden.",Toast.LENGTH_LONG).show();return;}
   Toast.makeText(this,weakest.size()+" schwaechste Formen ausgewaehlt.",Toast.LENGTH_SHORT).show();startActivity(new Intent(this,ErrorPracticeActivity.class));});}).start();}
 private String tenseName(String t){switch(t){case "present":return "Présent";case "passe_compose":return "Passé composé";case "imparfait":return "Imparfait";case "futur_simple":return "Futur simple";case "conditionnel_present":return "Conditionnel présent";case "subjonctif_present":return "Subjonctif présent";default:return t;}}
}
