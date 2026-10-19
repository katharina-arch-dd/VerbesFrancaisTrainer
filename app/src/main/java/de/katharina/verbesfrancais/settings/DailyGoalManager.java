package de.katharina.verbesfrancais.settings;
import android.content.Context; import android.content.SharedPreferences;
import java.time.LocalDate; import java.time.format.DateTimeFormatter; import java.time.temporal.ChronoUnit;
public class DailyGoalManager {
 private static final String P="verbes_francais_settings";
 private final SharedPreferences prefs;
 public DailyGoalManager(Context c){prefs=c.getSharedPreferences(P,Context.MODE_PRIVATE);}
 public int getDailyGoal(){return prefs.getInt("daily_goal",20);}
 public void setDailyGoal(int g){prefs.edit().putInt("daily_goal",Math.max(1,g)).apply();}
 public int getTodayCount(){return prefs.getInt("today_count",0);}
 public int getStreak(){return prefs.getInt("streak",0);}
 public boolean isDailyGoalReached(){return getTodayCount()>=getDailyGoal();}
 public void registerAnswer(){ String today=LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
  String last=prefs.getString("last_practice_date",""); int count=getTodayCount();
  if(!today.equals(last)) count=0; count++;
  prefs.edit().putString("last_practice_date",today).putInt("today_count",count).apply(); }
 public void updateStreak(){ String today=LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
  String last=prefs.getString("last_practice_date",""); if(today.equals(last))return;
  int streak=getStreak();
  if(last.isEmpty()) streak=1; else { LocalDate l=LocalDate.parse(last); long d=l.until(LocalDate.now(),ChronoUnit.DAYS); streak=d==1?streak+1:1; }
  prefs.edit().putString("last_practice_date",today).putInt("streak",streak).putInt("today_count",0).apply(); }
}
