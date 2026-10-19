package de.katharina.verbesfrancais.data;
import android.content.Context; import androidx.room.*;
@Database(entities={Verb.class,ConjugationForm.class,ExampleSentence.class,ReviewProgress.class},version=1,exportSchema=false)
public abstract class AppDatabase extends RoomDatabase {
 private static volatile AppDatabase INSTANCE;
 public abstract VerbDao verbDao(); public abstract ConjugationFormDao conjugationFormDao();
 public abstract ExampleSentenceDao exampleSentenceDao(); public abstract ReviewProgressDao reviewProgressDao();
 public static AppDatabase getInstance(Context c){ if(INSTANCE==null){ synchronized(AppDatabase.class){ if(INSTANCE==null){ INSTANCE=Room.databaseBuilder(c.getApplicationContext(),AppDatabase.class,"verbes_francais.db").build(); } } } return INSTANCE; }
}
