package de.katharina.verbesfrancais.data;
import androidx.room.*;
@Entity(tableName="example_sentences",indices={@Index(value={"importKey"},unique=true)})
public class ExampleSentence {
 @PrimaryKey(autoGenerate=true) public int id;
 @ColumnInfo(name="importKey") public String importKey;
 @ColumnInfo(name="verbId") public int verbId;
 @ColumnInfo(name="sentenceFr") public String sentenceFr;
 @ColumnInfo(name="sentenceDe") public String sentenceDe;
 public String tense; public String context; public String level;
}
