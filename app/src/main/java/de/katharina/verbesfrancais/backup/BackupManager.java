package de.katharina.verbesfrancais.backup;
import android.content.Context; import android.net.Uri;
import org.json.JSONArray; import org.json.JSONObject;
import java.io.*; import java.nio.charset.StandardCharsets; import java.util.List;
import de.katharina.verbesfrancais.data.*;
public class BackupManager {
 private final Context context; private final AppDatabase db;
 public BackupManager(Context c,AppDatabase db){this.context=c;this.db=db;}
 public void exportToUri(Uri uri) throws Exception {
  JSONObject root=new JSONObject(); root.put("backupVersion","1");
  root.put("verbs",verbsToJson(db.verbDao().getAllForBackup()));
  root.put("forms",formsToJson(db.conjugationFormDao().getAllForBackup()));
  root.put("examples",examplesToJson(db.exampleSentenceDao().getAllForBackup()));
  root.put("progress",progressToJson(db.reviewProgressDao().getAllForBackup()));
  write(uri,root.toString(2));
 }
 public RestoreResult restoreFromUri(Uri uri) throws Exception {
  JSONObject root=new JSONObject(readText(uri));
  if(!root.has("verbs")||!root.has("forms")||!root.has("examples")||!root.has("progress"))
   throw new IllegalArgumentException("Kein gueltiges Backup.");
  JSONArray va=root.getJSONArray("verbs"),fa=root.getJSONArray("forms"),ea=root.getJSONArray("examples"),pa=root.getJSONArray("progress");
  db.runInTransaction(()->{ try{
   db.reviewProgressDao().deleteAllProgress(); db.exampleSentenceDao().deleteAllExamples();
   db.conjugationFormDao().deleteAllForms(); db.verbDao().deleteAllVerbs();
   for(int i=0;i<va.length();i++){JSONObject o=va.getJSONObject(i);Verb v=new Verb();v.id=o.getInt("id");v.importKey=o.getString("importKey");v.infinitive=o.getString("infinitive");v.translationDe=o.optString("translationDe","");v.group=o.optString("group","irregular");v.auxiliary=o.optString("auxiliary","avoir");v.reflexive=o.optBoolean("reflexive",false);db.verbDao().insert(v);}
   for(int i=0;i<fa.length();i++){JSONObject o=fa.getJSONObject(i);ConjugationForm f=new ConjugationForm();f.id=o.getInt("id");f.importKey=o.getString("importKey");f.verbId=o.getInt("verbId");f.tense=o.getString("tense");f.person=o.getString("person");f.form=o.getString("form");f.irregular=o.optBoolean("irregular",false);db.conjugationFormDao().insert(f);}
   for(int i=0;i<ea.length();i++){JSONObject o=ea.getJSONObject(i);ExampleSentence e=new ExampleSentence();e.id=o.getInt("id");e.importKey=o.getString("importKey");e.verbId=o.getInt("verbId");e.sentenceFr=o.getString("sentenceFr");e.sentenceDe=o.getString("sentenceDe");e.tense=o.getString("tense");e.context=o.optString("context","Alltag");e.level=o.optString("level","B1");db.exampleSentenceDao().insert(e);}
   for(int i=0;i<pa.length();i++){JSONObject o=pa.getJSONObject(i);ReviewProgress p=new ReviewProgress();p.id=o.getInt("id");p.formId=o.getInt("formId");p.boxLevel=o.getInt("boxLevel");p.nextReviewAt=o.getLong("nextReviewAt");p.correctCount=o.getInt("correctCount");p.wrongCount=o.getInt("wrongCount");db.reviewProgressDao().insert(p);}
  }catch(Exception e){throw new RuntimeException(e);} });
  RestoreResult r=new RestoreResult(); r.verbs=va.length();r.forms=fa.length();r.examples=ea.length();r.progress=pa.length(); return r;
 }
 private JSONArray verbsToJson(List<Verb> vs) throws Exception { JSONArray a=new JSONArray();
  for(Verb v:vs){JSONObject o=new JSONObject();o.put("id",v.id);o.put("importKey",v.importKey);o.put("infinitive",v.infinitive);o.put("translationDe",v.translationDe);o.put("group",v.group);o.put("auxiliary",v.auxiliary);o.put("reflexive",v.reflexive);a.put(o);} return a; }
 private JSONArray formsToJson(List<ConjugationForm> fs) throws Exception { JSONArray a=new JSONArray();
  for(ConjugationForm f:fs){JSONObject o=new JSONObject();o.put("id",f.id);o.put("importKey",f.importKey);o.put("verbId",f.verbId);o.put("tense",f.tense);o.put("person",f.person);o.put("form",f.form);o.put("irregular",f.irregular);a.put(o);} return a; }
 private JSONArray examplesToJson(List<ExampleSentence> es) throws Exception { JSONArray a=new JSONArray();
  for(ExampleSentence e:es){JSONObject o=new JSONObject();o.put("id",e.id);o.put("importKey",e.importKey);o.put("verbId",e.verbId);o.put("sentenceFr",e.sentenceFr);o.put("sentenceDe",e.sentenceDe);o.put("tense",e.tense);o.put("context",e.context);o.put("level",e.level);a.put(o);} return a; }
 private JSONArray progressToJson(List<ReviewProgress> ps) throws Exception { JSONArray a=new JSONArray();
  for(ReviewProgress p:ps){JSONObject o=new JSONObject();o.put("id",p.id);o.put("formId",p.formId);o.put("boxLevel",p.boxLevel);o.put("nextReviewAt",p.nextReviewAt);o.put("correctCount",p.correctCount);o.put("wrongCount",p.wrongCount);a.put(o);} return a; }
 private void write(Uri uri,String text) throws Exception { try(OutputStream out=context.getContentResolver().openOutputStream(uri)){ if(out==null)throw new Exception("Datei konnte nicht geoeffnet werden."); out.write(text.getBytes(StandardCharsets.UTF_8)); } }
 private String readText(Uri uri) throws Exception { StringBuilder b=new StringBuilder();
  try(InputStream in=context.getContentResolver().openInputStream(uri);BufferedReader r=new BufferedReader(new InputStreamReader(in,StandardCharsets.UTF_8))){String l;while((l=r.readLine())!=null)b.append(l).append("\n");} return b.toString(); }
 public static class RestoreResult { public int verbs,forms,examples,progress; }
}
