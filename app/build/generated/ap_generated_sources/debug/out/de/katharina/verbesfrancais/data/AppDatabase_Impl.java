package de.katharina.verbesfrancais.data;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile VerbDao _verbDao;

  private volatile ConjugationFormDao _conjugationFormDao;

  private volatile ExampleSentenceDao _exampleSentenceDao;

  private volatile ReviewProgressDao _reviewProgressDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "0de18f5cbaed8e1ec92d35c52a9a9d00", "c2d3ad48e4d24e7f1cee34eb4f1aa673") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `verbs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `importKey` TEXT, `infinitive` TEXT, `translationDe` TEXT, `group` TEXT, `auxiliary` TEXT, `reflexive` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE UNIQUE INDEX IF NOT EXISTS `index_verbs_importKey` ON `verbs` (`importKey`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `conjugation_forms` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `importKey` TEXT, `verbId` INTEGER NOT NULL, `tense` TEXT, `person` TEXT, `form` TEXT, `irregular` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE UNIQUE INDEX IF NOT EXISTS `index_conjugation_forms_importKey` ON `conjugation_forms` (`importKey`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `example_sentences` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `importKey` TEXT, `verbId` INTEGER NOT NULL, `sentenceFr` TEXT, `sentenceDe` TEXT, `tense` TEXT, `context` TEXT, `level` TEXT)");
        SQLite.execSQL(connection, "CREATE UNIQUE INDEX IF NOT EXISTS `index_example_sentences_importKey` ON `example_sentences` (`importKey`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `review_progress` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `formId` INTEGER NOT NULL, `boxLevel` INTEGER NOT NULL, `nextReviewAt` INTEGER NOT NULL, `correctCount` INTEGER NOT NULL, `wrongCount` INTEGER NOT NULL)");
        SQLite.execSQL(connection, "CREATE UNIQUE INDEX IF NOT EXISTS `index_review_progress_formId` ON `review_progress` (`formId`)");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0de18f5cbaed8e1ec92d35c52a9a9d00')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `verbs`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `conjugation_forms`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `example_sentences`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `review_progress`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsVerbs = new HashMap<String, TableInfo.Column>(7);
        _columnsVerbs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVerbs.put("importKey", new TableInfo.Column("importKey", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVerbs.put("infinitive", new TableInfo.Column("infinitive", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVerbs.put("translationDe", new TableInfo.Column("translationDe", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVerbs.put("group", new TableInfo.Column("group", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVerbs.put("auxiliary", new TableInfo.Column("auxiliary", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVerbs.put("reflexive", new TableInfo.Column("reflexive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysVerbs = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesVerbs = new HashSet<TableInfo.Index>(1);
        _indicesVerbs.add(new TableInfo.Index("index_verbs_importKey", true, Arrays.asList("importKey"), Arrays.asList("ASC")));
        final TableInfo _infoVerbs = new TableInfo("verbs", _columnsVerbs, _foreignKeysVerbs, _indicesVerbs);
        final TableInfo _existingVerbs = TableInfo.read(connection, "verbs");
        if (!_infoVerbs.equals(_existingVerbs)) {
          return new RoomOpenDelegate.ValidationResult(false, "verbs(de.katharina.verbesfrancais.data.Verb).\n"
                  + " Expected:\n" + _infoVerbs + "\n"
                  + " Found:\n" + _existingVerbs);
        }
        final Map<String, TableInfo.Column> _columnsConjugationForms = new HashMap<String, TableInfo.Column>(7);
        _columnsConjugationForms.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConjugationForms.put("importKey", new TableInfo.Column("importKey", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConjugationForms.put("verbId", new TableInfo.Column("verbId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConjugationForms.put("tense", new TableInfo.Column("tense", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConjugationForms.put("person", new TableInfo.Column("person", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConjugationForms.put("form", new TableInfo.Column("form", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConjugationForms.put("irregular", new TableInfo.Column("irregular", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysConjugationForms = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesConjugationForms = new HashSet<TableInfo.Index>(1);
        _indicesConjugationForms.add(new TableInfo.Index("index_conjugation_forms_importKey", true, Arrays.asList("importKey"), Arrays.asList("ASC")));
        final TableInfo _infoConjugationForms = new TableInfo("conjugation_forms", _columnsConjugationForms, _foreignKeysConjugationForms, _indicesConjugationForms);
        final TableInfo _existingConjugationForms = TableInfo.read(connection, "conjugation_forms");
        if (!_infoConjugationForms.equals(_existingConjugationForms)) {
          return new RoomOpenDelegate.ValidationResult(false, "conjugation_forms(de.katharina.verbesfrancais.data.ConjugationForm).\n"
                  + " Expected:\n" + _infoConjugationForms + "\n"
                  + " Found:\n" + _existingConjugationForms);
        }
        final Map<String, TableInfo.Column> _columnsExampleSentences = new HashMap<String, TableInfo.Column>(8);
        _columnsExampleSentences.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExampleSentences.put("importKey", new TableInfo.Column("importKey", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExampleSentences.put("verbId", new TableInfo.Column("verbId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExampleSentences.put("sentenceFr", new TableInfo.Column("sentenceFr", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExampleSentences.put("sentenceDe", new TableInfo.Column("sentenceDe", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExampleSentences.put("tense", new TableInfo.Column("tense", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExampleSentences.put("context", new TableInfo.Column("context", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExampleSentences.put("level", new TableInfo.Column("level", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysExampleSentences = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesExampleSentences = new HashSet<TableInfo.Index>(1);
        _indicesExampleSentences.add(new TableInfo.Index("index_example_sentences_importKey", true, Arrays.asList("importKey"), Arrays.asList("ASC")));
        final TableInfo _infoExampleSentences = new TableInfo("example_sentences", _columnsExampleSentences, _foreignKeysExampleSentences, _indicesExampleSentences);
        final TableInfo _existingExampleSentences = TableInfo.read(connection, "example_sentences");
        if (!_infoExampleSentences.equals(_existingExampleSentences)) {
          return new RoomOpenDelegate.ValidationResult(false, "example_sentences(de.katharina.verbesfrancais.data.ExampleSentence).\n"
                  + " Expected:\n" + _infoExampleSentences + "\n"
                  + " Found:\n" + _existingExampleSentences);
        }
        final Map<String, TableInfo.Column> _columnsReviewProgress = new HashMap<String, TableInfo.Column>(6);
        _columnsReviewProgress.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewProgress.put("formId", new TableInfo.Column("formId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewProgress.put("boxLevel", new TableInfo.Column("boxLevel", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewProgress.put("nextReviewAt", new TableInfo.Column("nextReviewAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewProgress.put("correctCount", new TableInfo.Column("correctCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewProgress.put("wrongCount", new TableInfo.Column("wrongCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysReviewProgress = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesReviewProgress = new HashSet<TableInfo.Index>(1);
        _indicesReviewProgress.add(new TableInfo.Index("index_review_progress_formId", true, Arrays.asList("formId"), Arrays.asList("ASC")));
        final TableInfo _infoReviewProgress = new TableInfo("review_progress", _columnsReviewProgress, _foreignKeysReviewProgress, _indicesReviewProgress);
        final TableInfo _existingReviewProgress = TableInfo.read(connection, "review_progress");
        if (!_infoReviewProgress.equals(_existingReviewProgress)) {
          return new RoomOpenDelegate.ValidationResult(false, "review_progress(de.katharina.verbesfrancais.data.ReviewProgress).\n"
                  + " Expected:\n" + _infoReviewProgress + "\n"
                  + " Found:\n" + _existingReviewProgress);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "verbs", "conjugation_forms", "example_sentences", "review_progress");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "verbs", "conjugation_forms", "example_sentences", "review_progress");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(VerbDao.class, VerbDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ConjugationFormDao.class, ConjugationFormDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExampleSentenceDao.class, ExampleSentenceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReviewProgressDao.class, ReviewProgressDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public VerbDao verbDao() {
    if (_verbDao != null) {
      return _verbDao;
    } else {
      synchronized(this) {
        if(_verbDao == null) {
          _verbDao = new VerbDao_Impl(this);
        }
        return _verbDao;
      }
    }
  }

  @Override
  public ConjugationFormDao conjugationFormDao() {
    if (_conjugationFormDao != null) {
      return _conjugationFormDao;
    } else {
      synchronized(this) {
        if(_conjugationFormDao == null) {
          _conjugationFormDao = new ConjugationFormDao_Impl(this);
        }
        return _conjugationFormDao;
      }
    }
  }

  @Override
  public ExampleSentenceDao exampleSentenceDao() {
    if (_exampleSentenceDao != null) {
      return _exampleSentenceDao;
    } else {
      synchronized(this) {
        if(_exampleSentenceDao == null) {
          _exampleSentenceDao = new ExampleSentenceDao_Impl(this);
        }
        return _exampleSentenceDao;
      }
    }
  }

  @Override
  public ReviewProgressDao reviewProgressDao() {
    if (_reviewProgressDao != null) {
      return _reviewProgressDao;
    } else {
      synchronized(this) {
        if(_reviewProgressDao == null) {
          _reviewProgressDao = new ReviewProgressDao_Impl(this);
        }
        return _reviewProgressDao;
      }
    }
  }
}
