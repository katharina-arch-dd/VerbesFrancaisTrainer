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
public final class VerbDao_Impl implements VerbDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Verb> __insertAdapterOfVerb;

  public VerbDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfVerb = new EntityInsertAdapter<Verb>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `verbs` (`id`,`importKey`,`infinitive`,`translationDe`,`group`,`auxiliary`,`reflexive`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Verb entity) {
        statement.bindLong(1, entity.id);
        if (entity.importKey == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.importKey);
        }
        if (entity.infinitive == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.infinitive);
        }
        if (entity.translationDe == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.translationDe);
        }
        if (entity.group == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.group);
        }
        if (entity.auxiliary == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.auxiliary);
        }
        final int _tmp = entity.reflexive ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
  }

  @Override
  public long insert(final Verb verb) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfVerb.insertAndReturnId(_connection, verb);
    });
  }

  @Override
  public List<Verb> getAll() {
    final String _sql = "SELECT * FROM verbs ORDER BY infinitive ASC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfInfinitive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "infinitive");
        final int _columnIndexOfTranslationDe = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "translationDe");
        final int _columnIndexOfGroup = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "group");
        final int _columnIndexOfAuxiliary = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "auxiliary");
        final int _columnIndexOfReflexive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "reflexive");
        final List<Verb> _result = new ArrayList<Verb>();
        while (_stmt.step()) {
          final Verb _item;
          _item = new Verb();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _item.importKey = null;
          } else {
            _item.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          if (_stmt.isNull(_columnIndexOfInfinitive)) {
            _item.infinitive = null;
          } else {
            _item.infinitive = _stmt.getText(_columnIndexOfInfinitive);
          }
          if (_stmt.isNull(_columnIndexOfTranslationDe)) {
            _item.translationDe = null;
          } else {
            _item.translationDe = _stmt.getText(_columnIndexOfTranslationDe);
          }
          if (_stmt.isNull(_columnIndexOfGroup)) {
            _item.group = null;
          } else {
            _item.group = _stmt.getText(_columnIndexOfGroup);
          }
          if (_stmt.isNull(_columnIndexOfAuxiliary)) {
            _item.auxiliary = null;
          } else {
            _item.auxiliary = _stmt.getText(_columnIndexOfAuxiliary);
          }
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfReflexive));
          _item.reflexive = _tmp != 0;
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Verb> getAllVerbs() {
    final String _sql = "SELECT * FROM verbs ORDER BY infinitive ASC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfInfinitive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "infinitive");
        final int _columnIndexOfTranslationDe = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "translationDe");
        final int _columnIndexOfGroup = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "group");
        final int _columnIndexOfAuxiliary = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "auxiliary");
        final int _columnIndexOfReflexive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "reflexive");
        final List<Verb> _result = new ArrayList<Verb>();
        while (_stmt.step()) {
          final Verb _item;
          _item = new Verb();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _item.importKey = null;
          } else {
            _item.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          if (_stmt.isNull(_columnIndexOfInfinitive)) {
            _item.infinitive = null;
          } else {
            _item.infinitive = _stmt.getText(_columnIndexOfInfinitive);
          }
          if (_stmt.isNull(_columnIndexOfTranslationDe)) {
            _item.translationDe = null;
          } else {
            _item.translationDe = _stmt.getText(_columnIndexOfTranslationDe);
          }
          if (_stmt.isNull(_columnIndexOfGroup)) {
            _item.group = null;
          } else {
            _item.group = _stmt.getText(_columnIndexOfGroup);
          }
          if (_stmt.isNull(_columnIndexOfAuxiliary)) {
            _item.auxiliary = null;
          } else {
            _item.auxiliary = _stmt.getText(_columnIndexOfAuxiliary);
          }
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfReflexive));
          _item.reflexive = _tmp != 0;
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Verb> getAllForBackup() {
    final String _sql = "SELECT * FROM verbs";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfInfinitive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "infinitive");
        final int _columnIndexOfTranslationDe = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "translationDe");
        final int _columnIndexOfGroup = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "group");
        final int _columnIndexOfAuxiliary = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "auxiliary");
        final int _columnIndexOfReflexive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "reflexive");
        final List<Verb> _result = new ArrayList<Verb>();
        while (_stmt.step()) {
          final Verb _item;
          _item = new Verb();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _item.importKey = null;
          } else {
            _item.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          if (_stmt.isNull(_columnIndexOfInfinitive)) {
            _item.infinitive = null;
          } else {
            _item.infinitive = _stmt.getText(_columnIndexOfInfinitive);
          }
          if (_stmt.isNull(_columnIndexOfTranslationDe)) {
            _item.translationDe = null;
          } else {
            _item.translationDe = _stmt.getText(_columnIndexOfTranslationDe);
          }
          if (_stmt.isNull(_columnIndexOfGroup)) {
            _item.group = null;
          } else {
            _item.group = _stmt.getText(_columnIndexOfGroup);
          }
          if (_stmt.isNull(_columnIndexOfAuxiliary)) {
            _item.auxiliary = null;
          } else {
            _item.auxiliary = _stmt.getText(_columnIndexOfAuxiliary);
          }
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfReflexive));
          _item.reflexive = _tmp != 0;
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Verb findByImportKey(final String importKey) {
    final String _sql = "SELECT * FROM verbs WHERE importKey=? LIMIT 1";
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
        final int _columnIndexOfInfinitive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "infinitive");
        final int _columnIndexOfTranslationDe = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "translationDe");
        final int _columnIndexOfGroup = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "group");
        final int _columnIndexOfAuxiliary = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "auxiliary");
        final int _columnIndexOfReflexive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "reflexive");
        final Verb _result;
        if (_stmt.step()) {
          _result = new Verb();
          _result.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _result.importKey = null;
          } else {
            _result.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          if (_stmt.isNull(_columnIndexOfInfinitive)) {
            _result.infinitive = null;
          } else {
            _result.infinitive = _stmt.getText(_columnIndexOfInfinitive);
          }
          if (_stmt.isNull(_columnIndexOfTranslationDe)) {
            _result.translationDe = null;
          } else {
            _result.translationDe = _stmt.getText(_columnIndexOfTranslationDe);
          }
          if (_stmt.isNull(_columnIndexOfGroup)) {
            _result.group = null;
          } else {
            _result.group = _stmt.getText(_columnIndexOfGroup);
          }
          if (_stmt.isNull(_columnIndexOfAuxiliary)) {
            _result.auxiliary = null;
          } else {
            _result.auxiliary = _stmt.getText(_columnIndexOfAuxiliary);
          }
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfReflexive));
          _result.reflexive = _tmp != 0;
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
  public Verb findById(final int id) {
    final String _sql = "SELECT * FROM verbs WHERE id=? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfInfinitive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "infinitive");
        final int _columnIndexOfTranslationDe = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "translationDe");
        final int _columnIndexOfGroup = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "group");
        final int _columnIndexOfAuxiliary = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "auxiliary");
        final int _columnIndexOfReflexive = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "reflexive");
        final Verb _result;
        if (_stmt.step()) {
          _result = new Verb();
          _result.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _result.importKey = null;
          } else {
            _result.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          if (_stmt.isNull(_columnIndexOfInfinitive)) {
            _result.infinitive = null;
          } else {
            _result.infinitive = _stmt.getText(_columnIndexOfInfinitive);
          }
          if (_stmt.isNull(_columnIndexOfTranslationDe)) {
            _result.translationDe = null;
          } else {
            _result.translationDe = _stmt.getText(_columnIndexOfTranslationDe);
          }
          if (_stmt.isNull(_columnIndexOfGroup)) {
            _result.group = null;
          } else {
            _result.group = _stmt.getText(_columnIndexOfGroup);
          }
          if (_stmt.isNull(_columnIndexOfAuxiliary)) {
            _result.auxiliary = null;
          } else {
            _result.auxiliary = _stmt.getText(_columnIndexOfAuxiliary);
          }
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfReflexive));
          _result.reflexive = _tmp != 0;
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
  public int count() {
    final String _sql = "SELECT COUNT(*) FROM verbs";
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
  public void deleteAllVerbs() {
    final String _sql = "DELETE FROM verbs";
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
