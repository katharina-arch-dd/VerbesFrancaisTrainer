package de.katharina.verbesfrancais.data;
import androidx.room.*;
@Entity(tableName="verbs",indices={@Index(value={"importKey"},unique=true)})
public class Verb {
 @PrimaryKey(autoGenerate=true) public int id;
 @ColumnInfo(name="importKey") public String importKey;
 public String infinitive;
 @ColumnInfo(name="translationDe") public String translationDe;
 public String group; public String auxiliary; public boolean reflexive;
}
