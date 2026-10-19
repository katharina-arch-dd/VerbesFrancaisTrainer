package de.katharina.verbesfrancais.data;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
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
public final class ReviewProgressDao_Impl implements ReviewProgressDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<ReviewProgress> __insertAdapterOfReviewProgress;

  private final EntityDeleteOrUpdateAdapter<ReviewProgress> __updateAdapterOfReviewProgress;

  public ReviewProgressDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfReviewProgress = new EntityInsertAdapter<ReviewProgress>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `review_progress` (`id`,`formId`,`boxLevel`,`nextReviewAt`,`correctCount`,`wrongCount`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ReviewProgress entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.formId);
        statement.bindLong(3, entity.boxLevel);
        statement.bindLong(4, entity.nextReviewAt);
        statement.bindLong(5, entity.correctCount);
        statement.bindLong(6, entity.wrongCount);
      }
    };
    this.__updateAdapterOfReviewProgress = new EntityDeleteOrUpdateAdapter<ReviewProgress>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `review_progress` SET `id` = ?,`formId` = ?,`boxLevel` = ?,`nextReviewAt` = ?,`correctCount` = ?,`wrongCount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final ReviewProgress entity) {
        statement.bindLong(1, entity.id);
        statement.bindLong(2, entity.formId);
        statement.bindLong(3, entity.boxLevel);
        statement.bindLong(4, entity.nextReviewAt);
        statement.bindLong(5, entity.correctCount);
        statement.bindLong(6, entity.wrongCount);
        statement.bindLong(7, entity.id);
      }
    };
  }

  @Override
  public void insert(final ReviewProgress progress) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfReviewProgress.insert(_connection, progress);
      return null;
    });
  }

  @Override
  public void update(final ReviewProgress progress) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfReviewProgress.handle(_connection, progress);
      return null;
    });
  }

  @Override
  public ReviewProgress getByFormId(final int formId) {
    final String _sql = "SELECT * FROM review_progress WHERE formId=? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, formId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfFormId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "formId");
        final int _columnIndexOfBoxLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxLevel");
        final int _columnIndexOfNextReviewAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "nextReviewAt");
        final int _columnIndexOfCorrectCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "correctCount");
        final int _columnIndexOfWrongCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "wrongCount");
        final ReviewProgress _result;
        if (_stmt.step()) {
          _result = new ReviewProgress();
          _result.id = (int) (_stmt.getLong(_columnIndexOfId));
          _result.formId = (int) (_stmt.getLong(_columnIndexOfFormId));
          _result.boxLevel = (int) (_stmt.getLong(_columnIndexOfBoxLevel));
          _result.nextReviewAt = _stmt.getLong(_columnIndexOfNextReviewAt);
          _result.correctCount = (int) (_stmt.getLong(_columnIndexOfCorrectCount));
          _result.wrongCount = (int) (_stmt.getLong(_columnIndexOfWrongCount));
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
  public List<ReviewProgress> getDue(final long now) {
    final String _sql = "SELECT * FROM review_progress WHERE nextReviewAt<=? ORDER BY nextReviewAt ASC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, now);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfFormId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "formId");
        final int _columnIndexOfBoxLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxLevel");
        final int _columnIndexOfNextReviewAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "nextReviewAt");
        final int _columnIndexOfCorrectCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "correctCount");
        final int _columnIndexOfWrongCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "wrongCount");
        final List<ReviewProgress> _result = new ArrayList<ReviewProgress>();
        while (_stmt.step()) {
          final ReviewProgress _item;
          _item = new ReviewProgress();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          _item.formId = (int) (_stmt.getLong(_columnIndexOfFormId));
          _item.boxLevel = (int) (_stmt.getLong(_columnIndexOfBoxLevel));
          _item.nextReviewAt = _stmt.getLong(_columnIndexOfNextReviewAt);
          _item.correctCount = (int) (_stmt.getLong(_columnIndexOfCorrectCount));
          _item.wrongCount = (int) (_stmt.getLong(_columnIndexOfWrongCount));
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ReviewProgress> getProblemForms() {
    final String _sql = "SELECT * FROM review_progress WHERE wrongCount>0 ORDER BY wrongCount DESC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfFormId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "formId");
        final int _columnIndexOfBoxLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxLevel");
        final int _columnIndexOfNextReviewAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "nextReviewAt");
        final int _columnIndexOfCorrectCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "correctCount");
        final int _columnIndexOfWrongCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "wrongCount");
        final List<ReviewProgress> _result = new ArrayList<ReviewProgress>();
        while (_stmt.step()) {
          final ReviewProgress _item;
          _item = new ReviewProgress();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          _item.formId = (int) (_stmt.getLong(_columnIndexOfFormId));
          _item.boxLevel = (int) (_stmt.getLong(_columnIndexOfBoxLevel));
          _item.nextReviewAt = _stmt.getLong(_columnIndexOfNextReviewAt);
          _item.correctCount = (int) (_stmt.getLong(_columnIndexOfCorrectCount));
          _item.wrongCount = (int) (_stmt.getLong(_columnIndexOfWrongCount));
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ReviewProgress> getAllForBackup() {
    final String _sql = "SELECT * FROM review_progress";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfFormId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "formId");
        final int _columnIndexOfBoxLevel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "boxLevel");
        final int _columnIndexOfNextReviewAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "nextReviewAt");
        final int _columnIndexOfCorrectCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "correctCount");
        final int _columnIndexOfWrongCount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "wrongCount");
        final List<ReviewProgress> _result = new ArrayList<ReviewProgress>();
        while (_stmt.step()) {
          final ReviewProgress _item;
          _item = new ReviewProgress();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          _item.formId = (int) (_stmt.getLong(_columnIndexOfFormId));
          _item.boxLevel = (int) (_stmt.getLong(_columnIndexOfBoxLevel));
          _item.nextReviewAt = _stmt.getLong(_columnIndexOfNextReviewAt);
          _item.correctCount = (int) (_stmt.getLong(_columnIndexOfCorrectCount));
          _item.wrongCount = (int) (_stmt.getLong(_columnIndexOfWrongCount));
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public int countAll() {
    final String _sql = "SELECT COUNT(*) FROM review_progress";
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
  public int countLearned() {
    final String _sql = "SELECT COUNT(*) FROM review_progress WHERE correctCount>0";
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
  public int countDue(final long now) {
    final String _sql = "SELECT COUNT(*) FROM review_progress WHERE nextReviewAt<=?";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, now);
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
  public int countProblems() {
    final String _sql = "SELECT COUNT(*) FROM review_progress WHERE wrongCount>0";
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
  public List<TenseStats> getStatsByTense() {
    final String _sql = "SELECT conjugation_forms.tense AS tense, COUNT(*) AS total, SUM(review_progress.wrongCount) AS wrong FROM review_progress JOIN conjugation_forms ON conjugation_forms.id=review_progress.formId GROUP BY conjugation_forms.tense ORDER BY wrong DESC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfTense = 0;
        final int _columnIndexOfTotal = 1;
        final int _columnIndexOfWrong = 2;
        final List<TenseStats> _result = new ArrayList<TenseStats>();
        while (_stmt.step()) {
          final TenseStats _item;
          final String _tmpTense;
          if (_stmt.isNull(_columnIndexOfTense)) {
            _tmpTense = null;
          } else {
            _tmpTense = _stmt.getText(_columnIndexOfTense);
          }
          final int _tmpTotal;
          _tmpTotal = (int) (_stmt.getLong(_columnIndexOfTotal));
          final int _tmpWrong;
          _tmpWrong = (int) (_stmt.getLong(_columnIndexOfWrong));
          _item = new TenseStats(_tmpTense,_tmpTotal,_tmpWrong);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteAllProgress() {
    final String _sql = "DELETE FROM review_progress";
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
