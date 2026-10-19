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
public final class ConjugationFormDao_Impl implements ConjugationFormDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ConjugationForm> __insertAdapterOfConjugationForm;

  public ConjugationFormDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfConjugationForm = new EntityInsertAdapter<ConjugationForm>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `conjugation_forms` (`id`,`importKey`,`verbId`,`tense`,`person`,`form`,`irregular`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ConjugationForm entity) {
        statement.bindLong(1, entity.id);
        if (entity.importKey == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.importKey);
        }
        statement.bindLong(3, entity.verbId);
        if (entity.tense == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.tense);
        }
        if (entity.person == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.person);
        }
        if (entity.form == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.form);
        }
        final int _tmp = entity.irregular ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
  }

  @Override
  public void insert(final ConjugationForm form) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfConjugationForm.insert(_connection, form);
      return null;
    });
  }

  @Override
  public void insertAll(final List<ConjugationForm> forms) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfConjugationForm.insert(_connection, forms);
      return null;
    });
  }

  @Override
  public List<ConjugationForm> getAllForms() {
    final String _sql = "SELECT * FROM conjugation_forms";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfVerbId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verbId");
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfPerson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "person");
        final int _columnIndexOfForm = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "form");
        final int _columnIndexOfIrregular = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "irregular");
        final List<ConjugationForm> _result = new ArrayList<ConjugationForm>();
        while (_stmt.step()) {
          final ConjugationForm _item;
          _item = new ConjugationForm();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _item.importKey = null;
          } else {
            _item.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _item.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfTense)) {
            _item.tense = null;
          } else {
            _item.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfPerson)) {
            _item.person = null;
          } else {
            _item.person = _stmt.getText(_columnIndexOfPerson);
          }
          if (_stmt.isNull(_columnIndexOfForm)) {
            _item.form = null;
          } else {
            _item.form = _stmt.getText(_columnIndexOfForm);
          }
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIrregular));
          _item.irregular = _tmp != 0;
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ConjugationForm> getAllForBackup() {
    final String _sql = "SELECT * FROM conjugation_forms";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfVerbId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verbId");
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfPerson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "person");
        final int _columnIndexOfForm = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "form");
        final int _columnIndexOfIrregular = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "irregular");
        final List<ConjugationForm> _result = new ArrayList<ConjugationForm>();
        while (_stmt.step()) {
          final ConjugationForm _item;
          _item = new ConjugationForm();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _item.importKey = null;
          } else {
            _item.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _item.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfTense)) {
            _item.tense = null;
          } else {
            _item.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfPerson)) {
            _item.person = null;
          } else {
            _item.person = _stmt.getText(_columnIndexOfPerson);
          }
          if (_stmt.isNull(_columnIndexOfForm)) {
            _item.form = null;
          } else {
            _item.form = _stmt.getText(_columnIndexOfForm);
          }
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIrregular));
          _item.irregular = _tmp != 0;
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ConjugationForm> getByVerb(final int verbId) {
    final String _sql = "SELECT * FROM conjugation_forms WHERE verbId=?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, verbId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfVerbId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verbId");
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfPerson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "person");
        final int _columnIndexOfForm = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "form");
        final int _columnIndexOfIrregular = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "irregular");
        final List<ConjugationForm> _result = new ArrayList<ConjugationForm>();
        while (_stmt.step()) {
          final ConjugationForm _item;
          _item = new ConjugationForm();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _item.importKey = null;
          } else {
            _item.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _item.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfTense)) {
            _item.tense = null;
          } else {
            _item.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfPerson)) {
            _item.person = null;
          } else {
            _item.person = _stmt.getText(_columnIndexOfPerson);
          }
          if (_stmt.isNull(_columnIndexOfForm)) {
            _item.form = null;
          } else {
            _item.form = _stmt.getText(_columnIndexOfForm);
          }
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIrregular));
          _item.irregular = _tmp != 0;
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public ConjugationForm findByImportKey(final String importKey) {
    final String _sql = "SELECT * FROM conjugation_forms WHERE importKey=? LIMIT 1";
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
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfPerson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "person");
        final int _columnIndexOfForm = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "form");
        final int _columnIndexOfIrregular = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "irregular");
        final ConjugationForm _result;
        if (_stmt.step()) {
          _result = new ConjugationForm();
          _result.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _result.importKey = null;
          } else {
            _result.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _result.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfTense)) {
            _result.tense = null;
          } else {
            _result.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfPerson)) {
            _result.person = null;
          } else {
            _result.person = _stmt.getText(_columnIndexOfPerson);
          }
          if (_stmt.isNull(_columnIndexOfForm)) {
            _result.form = null;
          } else {
            _result.form = _stmt.getText(_columnIndexOfForm);
          }
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIrregular));
          _result.irregular = _tmp != 0;
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
  public ConjugationForm findById(final int id) {
    final String _sql = "SELECT * FROM conjugation_forms WHERE id=? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfVerbId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verbId");
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfPerson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "person");
        final int _columnIndexOfForm = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "form");
        final int _columnIndexOfIrregular = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "irregular");
        final ConjugationForm _result;
        if (_stmt.step()) {
          _result = new ConjugationForm();
          _result.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _result.importKey = null;
          } else {
            _result.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _result.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfTense)) {
            _result.tense = null;
          } else {
            _result.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfPerson)) {
            _result.person = null;
          } else {
            _result.person = _stmt.getText(_columnIndexOfPerson);
          }
          if (_stmt.isNull(_columnIndexOfForm)) {
            _result.form = null;
          } else {
            _result.form = _stmt.getText(_columnIndexOfForm);
          }
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIrregular));
          _result.irregular = _tmp != 0;
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
    final String _sql = "SELECT COUNT(*) FROM conjugation_forms";
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
  public List<ConjugationForm> getFilteredForms(final String tense, final int verbId) {
    final String _sql = "SELECT * FROM conjugation_forms WHERE (?='' OR tense=?) AND (?=0 OR verbId=?)";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (tense == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, tense);
        }
        _argIndex = 2;
        if (tense == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, tense);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, verbId);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, verbId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfVerbId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verbId");
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfPerson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "person");
        final int _columnIndexOfForm = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "form");
        final int _columnIndexOfIrregular = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "irregular");
        final List<ConjugationForm> _result = new ArrayList<ConjugationForm>();
        while (_stmt.step()) {
          final ConjugationForm _item;
          _item = new ConjugationForm();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _item.importKey = null;
          } else {
            _item.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _item.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfTense)) {
            _item.tense = null;
          } else {
            _item.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfPerson)) {
            _item.person = null;
          } else {
            _item.person = _stmt.getText(_columnIndexOfPerson);
          }
          if (_stmt.isNull(_columnIndexOfForm)) {
            _item.form = null;
          } else {
            _item.form = _stmt.getText(_columnIndexOfForm);
          }
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIrregular));
          _item.irregular = _tmp != 0;
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ConjugationForm> getWeakestForms(final int limit) {
    final String _sql = "SELECT conjugation_forms.* FROM conjugation_forms JOIN review_progress ON review_progress.formId=conjugation_forms.id WHERE review_progress.wrongCount>0 ORDER BY review_progress.wrongCount DESC, review_progress.correctCount ASC LIMIT ?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, limit);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImportKey = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "importKey");
        final int _columnIndexOfVerbId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "verbId");
        final int _columnIndexOfTense = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tense");
        final int _columnIndexOfPerson = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "person");
        final int _columnIndexOfForm = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "form");
        final int _columnIndexOfIrregular = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "irregular");
        final List<ConjugationForm> _result = new ArrayList<ConjugationForm>();
        while (_stmt.step()) {
          final ConjugationForm _item;
          _item = new ConjugationForm();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfImportKey)) {
            _item.importKey = null;
          } else {
            _item.importKey = _stmt.getText(_columnIndexOfImportKey);
          }
          _item.verbId = (int) (_stmt.getLong(_columnIndexOfVerbId));
          if (_stmt.isNull(_columnIndexOfTense)) {
            _item.tense = null;
          } else {
            _item.tense = _stmt.getText(_columnIndexOfTense);
          }
          if (_stmt.isNull(_columnIndexOfPerson)) {
            _item.person = null;
          } else {
            _item.person = _stmt.getText(_columnIndexOfPerson);
          }
          if (_stmt.isNull(_columnIndexOfForm)) {
            _item.form = null;
          } else {
            _item.form = _stmt.getText(_columnIndexOfForm);
          }
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIrregular));
          _item.irregular = _tmp != 0;
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteAllForms() {
    final String _sql = "DELETE FROM conjugation_forms";
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
