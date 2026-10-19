package de.katharina.verbesfrancais.data;
import androidx.room.*; import java.util.List;
@Dao public interface VerbDao {
 @Insert(onConflict=OnConflictStrategy.REPLACE) long insert(Verb verb);
 @Query("SELECT * FROM verbs ORDER BY infinitive ASC") List<Verb> getAll();
 @Query("SELECT * FROM verbs ORDER BY infinitive ASC") List<Verb> getAllVerbs();
 @Query("SELECT * FROM verbs") List<Verb> getAllForBackup();
 @Query("SELECT * FROM verbs WHERE importKey=:importKey LIMIT 1") Verb findByImportKey(String importKey);
 @Query("SELECT * FROM verbs WHERE id=:id LIMIT 1") Verb findById(int id);
 @Query("SELECT COUNT(*) FROM verbs") int count();
 @Query("DELETE FROM verbs") void deleteAllVerbs();
}
