package de.katharina.verbesfrancais.data;
import androidx.room.*; import java.util.List;
@Dao public interface ReviewProgressDao {
 @Insert(onConflict=OnConflictStrategy.REPLACE) void insert(ReviewProgress progress);
 @Update void update(ReviewProgress progress);
 @Query("SELECT * FROM review_progress WHERE formId=:formId LIMIT 1") ReviewProgress getByFormId(int formId);
 @Query("SELECT * FROM review_progress WHERE nextReviewAt<=:now ORDER BY nextReviewAt ASC") List<ReviewProgress> getDue(long now);
 @Query("SELECT * FROM review_progress WHERE wrongCount>0 ORDER BY wrongCount DESC") List<ReviewProgress> getProblemForms();
 @Query("SELECT * FROM review_progress") List<ReviewProgress> getAllForBackup();
 @Query("SELECT COUNT(*) FROM review_progress") int countAll();
 @Query("SELECT COUNT(*) FROM review_progress WHERE correctCount>0") int countLearned();
 @Query("SELECT COUNT(*) FROM review_progress WHERE nextReviewAt<=:now") int countDue(long now);
 @Query("SELECT COUNT(*) FROM review_progress WHERE wrongCount>0") int countProblems();
 @Query("SELECT conjugation_forms.tense AS tense, COUNT(*) AS total, SUM(review_progress.wrongCount) AS wrong FROM review_progress JOIN conjugation_forms ON conjugation_forms.id=review_progress.formId GROUP BY conjugation_forms.tense ORDER BY wrong DESC") List<TenseStats> getStatsByTense();
 @Query("DELETE FROM review_progress") void deleteAllProgress();
}
