package de.katharina.verbesfrancais.data;
import androidx.room.*;
@Entity(tableName="review_progress",indices={@Index(value={"formId"},unique=true)})
public class ReviewProgress {
 @PrimaryKey(autoGenerate=true) public int id;
 @ColumnInfo(name="formId") public int formId;
 @ColumnInfo(name="boxLevel") public int boxLevel=1;
 @ColumnInfo(name="nextReviewAt") public long nextReviewAt=System.currentTimeMillis();
 @ColumnInfo(name="correctCount") public int correctCount=0;
 @ColumnInfo(name="wrongCount") public int wrongCount=0;
}
