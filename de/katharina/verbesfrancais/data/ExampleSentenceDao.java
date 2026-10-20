package de.katharina.verbesfrancais.data;

import androidx.room.*;
import java.util.List;

@Dao
public interface ExampleSentenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExampleSentence example);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ExampleSentence> sentences);

    @Query("SELECT * FROM example_sentences")
    List<ExampleSentence> getAll();

    @Query("SELECT * FROM example_sentences")
    List<ExampleSentence> getAllForBackup();

    @Query("SELECT * FROM example_sentences WHERE verbId=:verbId ORDER BY tense ASC")
    List<ExampleSentence> getByVerb(int verbId);

    @Query("SELECT * FROM example_sentences WHERE importKey=:importKey LIMIT 1")
    ExampleSentence findByImportKey(String importKey);

    @Query("SELECT * FROM example_sentences WHERE id=:id LIMIT 1")
    ExampleSentence findById(int id);

    @Query("SELECT * FROM example_sentences WHERE tense=:tense")
    List<ExampleSentence> getByTense(String tense);

    @Query("SELECT * FROM example_sentences WHERE tense IN (:tenses)")
    List<ExampleSentence> getByTenses(List<String> tenses);

    @Query("SELECT COUNT(*) FROM example_sentences")
    int count();

    @Query("SELECT COUNT(*) FROM example_sentences WHERE tense=:tense")
    int countByTense(String tense);

    @Query("DELETE FROM example_sentences")
    void deleteAllExamples();
}
