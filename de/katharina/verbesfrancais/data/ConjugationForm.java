package de.katharina.verbesfrancais.data;
import androidx.room.*;
@Entity(tableName="conjugation_forms",indices={@Index(value={"importKey"},unique=true)})
public class ConjugationForm {
 @PrimaryKey(autoGenerate=true) public int id;
 @ColumnInfo(name="importKey") public String importKey;
 @ColumnInfo(name="verbId") public int verbId;
 public String tense; public String person; public String form; public boolean irregular;
}
