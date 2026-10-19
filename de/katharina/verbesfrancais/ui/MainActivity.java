package de.katharina.verbesfrancais.ui;
import android.content.Intent; import android.net.Uri; import android.os.Bundle;
import android.widget.*; import androidx.activity.result.ActivityResultLauncher; import androidx.activity.result.contract.ActivityResultContracts; import androidx.appcompat.app.AppCompatActivity;
import de.katharina.verbesfrancais.R; import de.katharina.verbesfrancais.backup.BackupManager;
import de.katharina.verbesfrancais.data.AppDatabase; import de.katharina.verbesfrancais.importer.JsonVerbImporter;
import de.katharina.verbesfrancais.settings.DailyGoalManager;
public class MainActivity extends AppCompatActivity {
 private AppDatabase db; private DailyGoalManager goals;
 private TextView textDailyOverview; private EditText editDailyGoal;
 private final ActivityResultLauncher<Intent> jsonPicker=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),r->{if(r.getResultCode()==RESULT_OK&&r.getData()!=null&&r.getData().getData()!=null)importJson(r.getData().getData());});
 private final ActivityResultLauncher<Intent> exportPicker=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),r->{if(r.getResultCode()==RESULT_OK&&r.getData()!=null&&r.getData().getData()!=null)exportBackup(r.getData().getData());});
 private final ActivityResultLauncher<Intent> importBackupPicker=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),r->{if(r.getResultCode()==RESULT_OK&&r.getData()!=null&&r.getData().getData()!=null)restoreBackup(r.getData().getData());});
 @Override protected void onCreate(Bundle s){super.onCreate(s);setContentView(R.layout.activity_main);
  db=AppDatabase.getInstance(this); goals=new DailyGoalManager(this);
  textDailyOverview=findViewById(R.id.textDailyOverview); editDailyGoal=findViewById(R.id.editDailyGoal);
  Button saveGoal=findViewById(R.id.buttonSaveGoal),importJson=findViewById(R.id.buttonImportJson),practice=findViewById(R.id.buttonPractice),
  list=findViewById(R.id.buttonVerbList),context=findViewById(R.id.buttonContextPractice),errors=findViewById(R.id.buttonErrorPractice),
  stats=findViewById(R.id.buttonStats),exportB=findViewById(R.id.buttonExportBackup),importB=findViewById(R.id.buttonImportBackup);
  showDailyOverview();
  saveGoal.setOnClickListener(v->saveDailyGoal()); importJson.setOnClickListener(v->openJsonPicker());
  practice.setOnClickListener(v->startActivity(new Intent(this,PracticeActivity.class)));
  list.setOnClickListener(v->startActivity(new Intent(this,VerbListActivity.class)));
  context.setOnClickListener(v->startActivity(new Intent(this,ContextPracticeActivity.class)));
  errors.setOnClickListener(v->startActivity(new Intent(this,ErrorPracticeActivity.class)));
  stats.setOnClickListener(v->startActivity(new Intent(this,StatsActivity.class)));
  exportB.setOnClickListener(v->openExportPicker()); importB.setOnClickListener(v->openImportPicker());
 }
 private void showDailyOverview(){textDailyOverview.setText("Heute: "+goals.getTodayCount()+"/"+goals.getDailyGoal()+" Formen\nStreak: "+goals.getStreak()+" Tage");}
 private void saveDailyGoal(){String v=editDailyGoal.getText().toString(); if(v.isEmpty()){Toast.makeText(this,"Bitte eine Zahl eingeben.",Toast.LENGTH_SHORT).show();return;}
  try{goals.setDailyGoal(Integer.parseInt(v));showDailyOverview();Toast.makeText(this,"Tagesziel gespeichert.",Toast.LENGTH_SHORT).show();}catch(NumberFormatException e){Toast.makeText(this,"Bitte eine gueltige Zahl eingeben.",Toast.LENGTH_SHORT).show();}}
 private void openJsonPicker(){Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.addCategory(Intent.CATEGORY_OPENABLE);i.setType("application/json");jsonPicker.launch(i);}
 private void importJson(Uri uri){new Thread(()->{try{JsonVerbImporter im=new JsonVerbImporter(this,db);JsonVerbImporter.ImportResult r=im.importFromUri(uri);
  runOnUiThread(()->Toast.makeText(this,r.verbs+" Verben, "+r.forms+" Formen und "+r.examples+" Beispielsaetze importiert.",Toast.LENGTH_LONG).show());}
  catch(Exception e){runOnUiThread(()->Toast.makeText(this,"Import fehlgeschlagen: "+e.getMessage(),Toast.LENGTH_LONG).show());}}).start();}
 private void openExportPicker(){Intent i=new Intent(Intent.ACTION_CREATE_DOCUMENT);i.addCategory(Intent.CATEGORY_OPENABLE);i.setType("application/json");i.putExtra(Intent.EXTRA_TITLE,"verbes_francais_backup.json");exportPicker.launch(i);}
 private void openImportPicker(){Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.addCategory(Intent.CATEGORY_OPENABLE);i.setType("application/json");importBackupPicker.launch(i);}
 private void exportBackup(Uri uri){new Thread(()->{try{new BackupManager(this,db).exportToUri(uri);runOnUiThread(()->Toast.makeText(this,"Backup erfolgreich exportiert.",Toast.LENGTH_LONG).show());}catch(Exception e){runOnUiThread(()->Toast.makeText(this,"Export fehlgeschlagen: "+e.getMessage(),Toast.LENGTH_LONG).show());}}).start();}
 private void restoreBackup(Uri uri){new Thread(()->{try{BackupManager.RestoreResult r=new BackupManager(this,db).restoreFromUri(uri);
  runOnUiThread(()->Toast.makeText(this,r.verbs+" Verben, "+r.forms+" Formen, "+r.examples+" Saetze und "+r.progress+" Fortschritte wiederhergestellt.",Toast.LENGTH_LONG).show());}
  catch(Exception e){runOnUiThread(()->Toast.makeText(this,"Wiederherstellung fehlgeschlagen: "+e.getMessage(),Toast.LENGTH_LONG).show());}}).start();}
}
