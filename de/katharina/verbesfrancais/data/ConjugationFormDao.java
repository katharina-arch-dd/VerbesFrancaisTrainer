package de.katharina.verbesfrancais.data;

import androidx.room.*;
import java.util.List;

@Dao
public interface ConjugationFormDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConjugationForm form);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ConjugationForm> forms);

    @Query("SELECT * FROM conjugation_forms")
    List<ConjugationForm> getAllForms();

    @Query("SELECT * FROM conjugation_forms")
    List<ConjugationForm> getAllForBackup();

    @Query("SELECT * FROM conjugation_forms WHERE verbId=:verbId")
    List<ConjugationForm> getByVerb(int verbId);

    @Query("SELECT * FROM conjugation_forms WHERE importKey=:importKey LIMIT 1")
    ConjugationForm findByImportKey(String importKey);

    @Query("SELECT * FROM conjugation_forms WHERE id=:id LIMIT 1")
    ConjugationForm findById(int id);

    @Query("SELECT COUNT(*) FROM conjugation_forms")
    int count();

    @Query("SELECT * FROM conjugation_forms WHERE (:tense='' OR tense=:tense) AND (:verbId=0 OR verbId=:verbId)")
    List<ConjugationForm> getFilteredForms(String tense, int verbId);

    @Query("SELECT * FROM conjugation_forms WHERE tense IN (:tenses)")
    List<ConjugationForm> getFormsByTenses(List<String> tenses);

    @Query("SELECT conjugation_forms.* FROM conjugation_forms JOIN review_progress ON review_progress.formId=conjugation_forms.id WHERE review_progress.wrongCount>0 ORDER BY review_progress.wrongCount DESC, review_progress.correctCount ASC LIMIT :limit")
    List<ConjugationForm> getWeakestForms(int limit);

    @Query("DELETE FROM conjugation_forms")
    void deleteAllForms();
}
