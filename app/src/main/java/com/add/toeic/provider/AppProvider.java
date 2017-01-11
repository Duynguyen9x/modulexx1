package com.add.toeic.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.add.toeic.Constants.DBInfo;
import com.add.toeic.database.DBHelper;
import com.add.toeic.model.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppProvider extends ContentProvider {

    static final String AUTHORITY = "com.add.toeic";
    static final String URL = "content://" + AUTHORITY;
    static final Uri CONTENT_URI = Uri.parse(URL).buildUpon().appendPath(DBInfo.TABLE_WORD_REMIND).build();

    private static HashMap<String, String> values;

    static final int WORDS = 1;
    static final int WORD_ID = 2;

    private static UriMatcher matcher;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, DBInfo.TABLE_WORD_REMIND, 1);
        matcher.addURI(AUTHORITY, DBInfo.TABLE_WORD_REMIND + "/*", 2);
    }

    private static SQLiteDatabase sqlDB;

    @Override
    public boolean onCreate() {
        DBHelper dbHelper = new DBHelper(getContext());
        sqlDB = dbHelper.getWritableDatabase();
        return (sqlDB == null) ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DBInfo.TABLE_WORD_REMIND);

        switch (matcher.match(uri)) {
            case WORDS:
                qb.setProjectionMap(values);
                break;
            case WORD_ID:
                qb.appendWhere(DBInfo.COLUMN_WORD_ID + " = " + uri.getPathSegments().get(1));
                break;
            default:
        }
        Cursor cursor = qb.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = sqlDB.insert(DBInfo.TABLE_WORD_REMIND, null, values);
        if (rowID > 0) {
            Uri mUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            this.getContext().getContentResolver().notifyChange(mUri, null);
            return mUri;
        }
        throw new SQLException("fail to add " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cnt = 0;
        switch (matcher.match(uri)) {
            case WORDS:
                cnt = sqlDB.delete(DBInfo.TABLE_WORD_REMIND, selection, selectionArgs);
                break;
            case WORD_ID:
                String id = uri.getPathSegments().get(1);
//                cnt = sqlDB.delete(DBInfo.TABLE_WORD_REMIND, DBInfo.COLUMN_WORD_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                cnt = sqlDB.delete(DBInfo.TABLE_WORD_REMIND, selection, selectionArgs);
                break;
            default:
        }
        this.getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public static void addToRemind(Word word, Context context) {
        if (checkHasWord(word, context))
            return;
        ContentValues values = new ContentValues();
        values.put(DBInfo.COLUMN_WORD_NAME, word.getName());
        values.put(DBInfo.COLUMN_WORD_SOUND, word.getSound());
        values.put(DBInfo.COLUMN_WORD_NAME_KEY, word.getName_key());
        values.put(DBInfo.COLUMN_WORD_EXAMPLE, word.getExample_key());
        values.put(DBInfo.COLUMN_WORD_EXAMPLE_KEY, word.getExample_key());
        values.put(DBInfo.COLUMN_WORD_KIND, word.getKind_word());
        context.getContentResolver().insert(DBInfo.CONTENT_URI, values);
    }

    public static void removeToRemind(Word word, Context context) {
        if (!checkHasWord(word, context)) {
            return;
        } else {
            context.getContentResolver().delete(ContentUris.withAppendedId(DBInfo.CONTENT_URI, word.getId()), DBInfo.COLUMN_WORD_NAME + " = ?", new String[] {String.valueOf(word.getName())});
        }
    }

    public static ArrayList<Word> getAllWords(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        return dbHelper.getAllWords();
    }

    public static boolean checkHasWord(Word word, Context context) {
        String wordname = word.getName();
        String selection = DBInfo.COLUMN_WORD_NAME + "=?";
        String[] arg = {wordname};

        DBHelper dbHelper = new DBHelper(context);
        sqlDB = dbHelper.getWritableDatabase();

        Cursor cursor = sqlDB.query(DBInfo.TABLE_WORD_REMIND, null, selection, arg, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                cursor.close();
                return false;
            } else {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }
}
