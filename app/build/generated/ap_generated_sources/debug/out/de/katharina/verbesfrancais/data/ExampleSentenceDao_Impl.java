package de.katharina.verbesfrancais.data;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ExampleSentenceDao_Impl implements ExampleSentenceDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ExampleSentence> __insertAdapterOfExampleSentence;

  public ExampleSentenceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExampleSentence = new EntityInsertAdapter<ExampleSentence>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `example_sentences` (`id`,`importKey`,`verbId`,`sentenceFr`,`sentenceDe`,`tense`,`context`,`level`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ExampleSentence entity) {
        statement.bindLong(1, entity.id);
        if (entity.importKey == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.importKey);
        }
        statement.bindLong(3, entity.verbId);
        if (entity.sentenceFr == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.sentenceFr);
        }
        if (entity.sentenceDe == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.sentenceDe);
        }
        if (entity.tense == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.tense);
        }
        if (entity.context == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.context);
        }
        if (entity.level == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.level);
        }
      }
    };
  }

  @Override
  public void insert(final ExampleSentence example) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExampleSentence.insert(_connection, example);
      return null;
    });
  }

  @Override
  public void insertAll(final List<ExampleSentence> sentences) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExampleSentence.insert(_connection, sentences);
      return null;
    });
  }

  @Override
  public List<ExampleSentence> getAll() {
    final String _sql = "SELECT * FROM example_sentences";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfVerbId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verbId");
        final int _columnIndexOfSentenceFr = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sentenceFr");
        final int _columnIndexOfSentenceDe = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sentenceDe");
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfContext = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "context");
        final int _columnIndexOfLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "level");
        final List<ExampleSentence> _result = new ArrayList<ExampleSentence>();
        while (_stmt.step()) {
          final ExampleSentence _item;
          _item = new ExampleSentence();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _item.importKey = null;
          } else {
            _item.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _item.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfSentenceFr)) {
            _item.sentenceFr = null;
          } else {
            _item.sentenceFr = _stmt.getText(_columnIndexOfSentenceFr);
          }
          if (_stmt.isNull(_columnIndexOfSentenceDe)) {
            _item.sentenceDe = null;
          } else {
            _item.sentenceDe = _stmt.getText(_columnIndexOfSentenceDe);
          }
          if (_stmt.isNull(_columnIndexOfTense)) {
            _item.tense = null;
          } else {
            _item.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfContext)) {
            _item.context = null;
          } else {
            _item.context = _stmt.getText(_columnIndexOfContext);
          }
          if (_stmt.isNull(_columnIndexOfLevel)) {
            _item.level = null;
          } else {
            _item.level = _stmt.getText(_columnIndexOfLevel);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ExampleSentence> getAllForBackup() {
    final String _sql = "SELECT * FROM example_sentences";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfVerbId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verbId");
        final int _columnIndexOfSentenceFr = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sentenceFr");
        final int _columnIndexOfSentenceDe = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sentenceDe");
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfContext = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "context");
        final int _columnIndexOfLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "level");
        final List<ExampleSentence> _result = new ArrayList<ExampleSentence>();
        while (_stmt.step()) {
          final ExampleSentence _item;
          _item = new ExampleSentence();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _item.importKey = null;
          } else {
            _item.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _item.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfSentenceFr)) {
            _item.sentenceFr = null;
          } else {
            _item.sentenceFr = _stmt.getText(_columnIndexOfSentenceFr);
          }
          if (_stmt.isNull(_columnIndexOfSentenceDe)) {
            _item.sentenceDe = null;
          } else {
            _item.sentenceDe = _stmt.getText(_columnIndexOfSentenceDe);
          }
          if (_stmt.isNull(_columnIndexOfTense)) {
            _item.tense = null;
          } else {
            _item.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfContext)) {
            _item.context = null;
          } else {
            _item.context = _stmt.getText(_columnIndexOfContext);
          }
          if (_stmt.isNull(_columnIndexOfLevel)) {
            _item.level = null;
          } else {
            _item.level = _stmt.getText(_columnIndexOfLevel);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ExampleSentence> getByVerb(final int verbId) {
    final String _sql = "SELECT * FROM example_sentences WHERE verbId=? ORDER BY tense ASC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, verbId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfVerbId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verbId");
        final int _columnIndexOfSentenceFr = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sentenceFr");
        final int _columnIndexOfSentenceDe = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sentenceDe");
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfContext = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "context");
        final int _columnIndexOfLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "level");
        final List<ExampleSentence> _result = new ArrayList<ExampleSentence>();
        while (_stmt.step()) {
          final ExampleSentence _item;
          _item = new ExampleSentence();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _item.importKey = null;
          } else {
            _item.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _item.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfSentenceFr)) {
            _item.sentenceFr = null;
          } else {
            _item.sentenceFr = _stmt.getText(_columnIndexOfSentenceFr);
          }
          if (_stmt.isNull(_columnIndexOfSentenceDe)) {
            _item.sentenceDe = null;
          } else {
            _item.sentenceDe = _stmt.getText(_columnIndexOfSentenceDe);
          }
          if (_stmt.isNull(_columnIndexOfTense)) {
            _item.tense = null;
          } else {
            _item.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfContext)) {
            _item.context = null;
          } else {
            _item.context = _stmt.getText(_columnIndexOfContext);
          }
          if (_stmt.isNull(_columnIndexOfLevel)) {
            _item.level = null;
          } else {
            _item.level = _stmt.getText(_columnIndexOfLevel);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public ExampleSentence findByImportKey(final String importKey) {
    final String _sql = "SELECT * FROM example_sentences WHERE importKey=? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (importKey == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, importKey);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfVerbId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verbId");
        final int _columnIndexOfSentenceFr = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sentenceFr");
        final int _columnIndexOfSentenceDe = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sentenceDe");
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfContext = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "context");
        final int _columnIndexOfLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "level");
        final ExampleSentence _result;
        if (_stmt.step()) {
          _result = new ExampleSentence();
          _result.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _result.importKey = null;
          } else {
            _result.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _result.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfSentenceFr)) {
            _result.sentenceFr = null;
          } else {
            _result.sentenceFr = _stmt.getText(_columnIndexOfSentenceFr);
          }
          if (_stmt.isNull(_columnIndexOfSentenceDe)) {
            _result.sentenceDe = null;
          } else {
            _result.sentenceDe = _stmt.getText(_columnIndexOfSentenceDe);
          }
          if (_stmt.isNull(_columnIndexOfTense)) {
            _result.tense = null;
          } else {
            _result.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfContext)) {
            _result.context = null;
          } else {
            _result.context = _stmt.getText(_columnIndexOfContext);
          }
          if (_stmt.isNull(_columnIndexOfLevel)) {
            _result.level = null;
          } else {
            _result.level = _stmt.getText(_columnIndexOfLevel);
          }
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public ExampleSentence findById(final int id) {
    final String _sql = "SELECT * FROM example_sentences WHERE id=? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfVerbId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verbId");
        final int _columnIndexOfSentenceFr = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sentenceFr");
        final int _columnIndexOfSentenceDe = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sentenceDe");
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfContext = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "context");
        final int _columnIndexOfLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "level");
        final ExampleSentence _result;
        if (_stmt.step()) {
          _result = new ExampleSentence();
          _result.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _result.importKey = null;
          } else {
            _result.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _result.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfSentenceFr)) {
            _result.sentenceFr = null;
          } else {
            _result.sentenceFr = _stmt.getText(_columnIndexOfSentenceFr);
          }
          if (_stmt.isNull(_columnIndexOfSentenceDe)) {
            _result.sentenceDe = null;
          } else {
            _result.sentenceDe = _stmt.getText(_columnIndexOfSentenceDe);
          }
          if (_stmt.isNull(_columnIndexOfTense)) {
            _result.tense = null;
          } else {
            _result.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfContext)) {
            _result.context = null;
          } else {
            _result.context = _stmt.getText(_columnIndexOfContext);
          }
          if (_stmt.isNull(_columnIndexOfLevel)) {
            _result.level = null;
          } else {
            _result.level = _stmt.getText(_columnIndexOfLevel);
          }
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ExampleSentence> getByTense(final String tense) {
    final String _sql = "SELECT * FROM example_sentences WHERE tense=?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (tense == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, tense);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfVerbId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verbId");
        final int _columnIndexOfSentenceFr = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sentenceFr");
        final int _columnIndexOfSentenceDe = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sentenceDe");
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfContext = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "context");
        final int _columnIndexOfLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "level");
        final List<ExampleSentence> _result = new ArrayList<ExampleSentence>();
        while (_stmt.step()) {
          final ExampleSentence _item;
          _item = new ExampleSentence();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _item.importKey = null;
          } else {
            _item.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _item.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfSentenceFr)) {
            _item.sentenceFr = null;
          } else {
            _item.sentenceFr = _stmt.getText(_columnIndexOfSentenceFr);
          }
          if (_stmt.isNull(_columnIndexOfSentenceDe)) {
            _item.sentenceDe = null;
          } else {
            _item.sentenceDe = _stmt.getText(_columnIndexOfSentenceDe);
          }
          if (_stmt.isNull(_columnIndexOfTense)) {
            _item.tense = null;
          } else {
            _item.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfContext)) {
            _item.context = null;
          } else {
            _item.context = _stmt.getText(_columnIndexOfContext);
          }
          if (_stmt.isNull(_columnIndexOfLevel)) {
            _item.level = null;
          } else {
            _item.level = _stmt.getText(_columnIndexOfLevel);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public int count() {
    final String _sql = "SELECT COUNT(*) FROM example_sentences";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _result;
        if (_stmt.step()) {
          _result = (int) (_stmt.getLong(0));
        } else {
          _result = 0;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public int countByTense(final String tense) {
    final String _sql = "SELECT COUNT(*) FROM example_sentences WHERE tense=?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (tense == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, tense);
        }
        final int _result;
        if (_stmt.step()) {
          _result = (int) (_stmt.getLong(0));
        } else {
          _result = 0;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteAllExamples() {
    final String _sql = "DELETE FROM example_sentences";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
