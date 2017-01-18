package com.add.toeic.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.add.toeic.model.Word;
import com.add.toeic.temp.WordContract;
import com.add.toeic.utils.WordUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class AppProvider extends ContentProvider {

    private static final String DATABASE_NAME = "addtoeic.db";
    private static final int DATABASE_VERSION = 1;

    private static HashMap<String, String> values;
    private static final int WORDS_ALL = 1;
    private static final int WORD_ID_ALL = 2;
    private static final int WORDS_REMIND = 3;
    private static final int WORD_ID_REMIND = 4;
    private static SQLiteDatabase sqlDB;
    private static DBHelper dbHelper;
    private static UriMatcher matcher;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(WordContract.AUTHORITY, WordContract.Word.TB_WORD_ALL, WORDS_ALL);
        matcher.addURI(WordContract.AUTHORITY, WordContract.Word.TB_WORD_ALL + "/*", WORD_ID_ALL);
        matcher.addURI(WordContract.AUTHORITY, WordContract.Word.TB_WORD_REMIND, WORDS_REMIND);
        matcher.addURI(WordContract.AUTHORITY, WordContract.Word.TB_WORD_REMIND + "/*", WORD_ID_REMIND);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        sqlDB = dbHelper.getWritableDatabase();
        return (sqlDB != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (matcher.match(uri)) {
            case WORDS_REMIND:
                qb.setTables(WordContract.Word.TB_WORD_REMIND);
                qb.setProjectionMap(values);
                break;
            case WORD_ID_REMIND:
                qb.setTables(WordContract.Word.TB_WORD_REMIND);
                qb.appendWhere(WordContract.Word.ID + " = " + uri.getPathSegments().get(1));
                break;
            case WORDS_ALL:
                qb.setTables(WordContract.Word.TB_WORD_ALL);
                qb.setProjectionMap(values);
                break;
            case WORD_ID_ALL:
                qb.setTables(WordContract.Word.TB_WORD_ALL);
                qb.appendWhere(WordContract.Word.ID + " = " + uri.getPathSegments().get(1));
                break;
            default:
        }
        Cursor cursor = qb.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (matcher.match(uri)) {
            case WORDS_ALL:
                long rowID = sqlDB.insert(WordContract.Word.TB_WORD_ALL, null, values);
                if (rowID > 0) {
                    Uri mUri = WordContract.Word.buildUri(WordContract.Word.CONTENT_URI_ALL, rowID);
                    return mUri;
                }
                break;
            case WORDS_REMIND:
                long rowID_remind = sqlDB.insert(WordContract.Word.TB_WORD_REMIND, null, values);
                if (rowID_remind > 0) {
                    Uri mUri = WordContract.Word.buildUri(WordContract.Word.CONTENT_URI_REMIND, rowID_remind);
                    return mUri;
                }
                break;
            default:
        }
        throw new SQLException("fail to add " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //TODO
        int cnt = 0;
        switch (matcher.match(uri)) {
            case WORDS_REMIND:
                cnt = sqlDB.delete(WordContract.Word.TB_WORD_REMIND, selection, selectionArgs);
                break;
            case WORD_ID_REMIND:
//                String id = uri.getPathSegments().get(1);
//                cnt = sqlDB.delete(DBInfo.TABLE_WORD_REMIND, DBInfo.COLUMN_WORD_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                cnt = sqlDB.delete(WordContract.Word.TB_WORD_REMIND, selection, selectionArgs);
                break;
            default:
        }
        // not call notifyChange here, it will called onChange multi times
//        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public static void addWord(Word word, Context context, boolean isRemind) {
        if (checkHasWord(word, true))
            return;
        ContentValues values = new ContentValues();
        values.put(WordContract.Word.ID, word.getId());
        values.put(WordContract.Word.NAME, word.getName());
        values.put(WordContract.Word.NAME_KEY, word.getName_key());
        values.put(WordContract.Word.SOUND, word.getSound());
        values.put(WordContract.Word.EXAMPLE, word.getExample());
        values.put(WordContract.Word.EXAMPLE_KEY, word.getExample_key());
        values.put(WordContract.Word.EXPAND, word.getExpand());
        values.put(WordContract.Word.KIND, word.getKind());
        values.put(WordContract.Word.REMEMBER, word.getRemember());

        sqlDB.insert((isRemind ? WordContract.Word.TB_WORD_REMIND : WordContract.Word.TB_WORD_ALL), null, values);
        context.getContentResolver().notifyChange((isRemind ? WordContract.Word.CONTENT_URI_REMIND : WordContract.Word.CONTENT_URI_ALL), null);
    }

    public static void addMultiWord(ArrayList<Word> words, boolean isRemind, Context context) {
        if (sqlDB != null)
            sqlDB = dbHelper.getWritableDatabase();
        int count = 0;
        sqlDB.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Word word : words) {
                if (checkHasWord(word, true)) {
                    continue;
                }
                values.put(WordContract.Word.ID, word.getId());
                values.put(WordContract.Word.NAME, word.getName());
                values.put(WordContract.Word.NAME_KEY, word.getName_key());
                values.put(WordContract.Word.SOUND, word.getSound());
                values.put(WordContract.Word.EXAMPLE, word.getExample());
                values.put(WordContract.Word.EXAMPLE_KEY, word.getExample_key());
                values.put(WordContract.Word.EXPAND, word.getExpand());
                values.put(WordContract.Word.KIND, word.getKind());
                values.put(WordContract.Word.REMEMBER, word.getRemember());
                long id = sqlDB.insert((isRemind ? WordContract.Word.TB_WORD_REMIND : WordContract.Word.TB_WORD_ALL), null, values);
                if (id != -1)
                    count++;
            }
            sqlDB.setTransactionSuccessful();
        } finally {
            sqlDB.endTransaction();
        }
        if (count > 0) {
            context.getContentResolver().notifyChange((isRemind ? WordContract.Word.CONTENT_URI_REMIND : WordContract.Word.CONTENT_URI_ALL), null);
        }
    }

    public static void removeWord(Word word, Context context, boolean isRemind) {
        if (checkHasWord(word, true)) {
            sqlDB.delete((isRemind ? WordContract.Word.TB_WORD_REMIND : WordContract.Word.TB_WORD_ALL), WordContract.Word.NAME + " = ?", new String[]{String.valueOf(word.getName())});
            context.getContentResolver().notifyChange((isRemind ? WordContract.Word.CONTENT_URI_REMIND : WordContract.Word.CONTENT_URI_ALL), null);
        }
    }

    public static void removeMultiWord(ArrayList<Word> words, Context context, boolean isRemind) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        for (Word word : words) {
            if (!checkHasWord(word, true)) {
                continue;
            }
            ContentProviderOperation operation = ContentProviderOperation
                    .newDelete(isRemind ? WordContract.Word.CONTENT_URI_REMIND : WordContract.Word.CONTENT_URI_ALL)
                    .withSelection(WordContract.Word.NAME + " = ?", new String[]{word.getName()})
                    .build();
            operations.add(operation);
        }
        try {
            if (operations.size() > 0) {
                context.getContentResolver().applyBatch(WordContract.AUTHORITY, operations);
                context.getContentResolver().notifyChange(isRemind ? WordContract.Word.CONTENT_URI_REMIND : WordContract.Word.CONTENT_URI_ALL, null);
            }
        } catch (RemoteException e) {
        } catch (OperationApplicationException e) {
        }
    }

    public static void deleteAll(Context context, boolean isRemind) {
        sqlDB.delete((isRemind ? WordContract.Word.TB_WORD_REMIND : WordContract.Word.TB_WORD_ALL), null, null);
        context.getContentResolver().notifyChange((isRemind ? WordContract.Word.CONTENT_URI_REMIND : WordContract.Word.CONTENT_URI_ALL), null);
    }

    public static boolean checkHasWord(Word word, boolean isRemind) {
        String wordname = word.getName();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = WordContract.Word.NAME + "=?";
        String[] arg = {wordname};

        Cursor cursor = db.query((isRemind ? WordContract.Word.TB_WORD_REMIND : WordContract.Word.TB_WORD_ALL), null, selection, arg, null, null, null);
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

    public static boolean tb_All_is_Empty() {
        String count = "SELECT count(*) FROM " + WordContract.Word.TB_WORD_ALL;
        Cursor c = sqlDB.rawQuery(count, null);
        c.moveToFirst();
        return (c.getInt(0) <= 0);
    }

    public static void populateTB_ALL(Context context) {
        ArrayList<Word> arr = null;
        try {
            arr = WordUtils.readAllData(context);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        addMultiWord(arr, false, context);
    }

    public static Word getWordById_tb_All(int idWord) {
        Word word = new Word();
        Cursor cursor = sqlDB.rawQuery("SELECT  * FROM " + WordContract.Word.TB_WORD_ALL + " WHERE " + WordContract.Word.ID + " = " + idWord, null);
        if (cursor.moveToFirst()) {
            word.setId(cursor.getInt(0));
            word.setName(cursor.getString(1));
            word.setName_key(cursor.getString(2));
            word.setSound(cursor.getString(3));
            word.setExample(cursor.getString(4));
            word.setExample_key(cursor.getString(5));
            word.setExpand(cursor.getString(6));
            word.setKind(cursor.getInt(7));
            word.setRemember(cursor.getInt(8));
        }
        return word;
    }

    public static ArrayList<Word> getAllWords(boolean isRemind) {
        Log.i("anhdt", "MyDatabaseHelper.getAllNotes ... " + isRemind);

        ArrayList<Word> wordList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + (isRemind ? WordContract.Word.TB_WORD_REMIND : WordContract.Word.TB_WORD_ALL);
        sqlDB = dbHelper.getWritableDatabase();
        Cursor cursor = sqlDB.rawQuery(selectQuery, null);

        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setId(cursor.getInt(0));
                word.setName(cursor.getString(1));
                word.setName_key(cursor.getString(2));
                word.setSound(cursor.getString(3));
                word.setExample(cursor.getString(4));
                word.setExample_key(cursor.getString(5));
                word.setExpand(cursor.getString(6));
                word.setKind(cursor.getInt(7));
                word.setRemember(cursor.getInt(8));
                // Thêm vào danh sách.
                wordList.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return wordList;
    }

    public static ArrayList<Word> getAllWords_without_remembered(boolean isRemind) {
        Log.i("anhdt", "MyDatabaseHelper.getAllWords_without_remembered ... " + isRemind);

        ArrayList<Word> wordList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + (isRemind ? WordContract.Word.TB_WORD_REMIND : WordContract.Word.TB_WORD_ALL) + " WHERE " + WordContract.Word.REMEMBER + " = ?";
        String[] args = {"0"};
        sqlDB = dbHelper.getWritableDatabase();
        Cursor cursor = sqlDB.rawQuery(selectQuery, args);

        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setId(cursor.getInt(0));
                word.setName(cursor.getString(1));
                word.setName_key(cursor.getString(2));
                word.setSound(cursor.getString(3));
                word.setExample(cursor.getString(4));
                word.setExample_key(cursor.getString(5));
                word.setExpand(cursor.getString(6));
                word.setKind(cursor.getInt(7));
                word.setRemember(cursor.getInt(8));
                // Thêm vào danh sách.
                wordList.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return wordList;
    }

    public static void toggleRemember(Word word, Context context, boolean isRemind) {
        Log.i("anhdt", "toggleRemember ... ");
        ContentValues values = new ContentValues();
        int isRemember = word.getRemember();
        if(isRemember == 0){
            isRemember = 1;
        } else if(isRemember == 1){
            isRemember = 0;
        }
        values.put(WordContract.Word.REMEMBER, isRemember);
        sqlDB.update(WordContract.Word.TB_WORD_ALL, values, WordContract.Word.NAME + " = " + "'" + word.getName() +"'", null);
        context.getContentResolver().notifyChange((isRemind ? WordContract.Word.CONTENT_URI_REMIND : WordContract.Word.CONTENT_URI_ALL), null);
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("anhdt", "MyDatabaseHelper.onCreate ... ");
            db.execSQL(createDB(false));
            db.execSQL(createDB(true));
        }

        private String createDB(boolean isRemind) {
            StringBuilder bd = new StringBuilder();

            bd.append("CREATE TABLE ");
            bd.append((isRemind ? WordContract.Word.TB_WORD_REMIND : WordContract.Word.TB_WORD_ALL));
            bd.append(" ( ");
            bd.append(WordContract.Word.ID);
            bd.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
            bd.append(WordContract.Word.NAME);
            bd.append(" TEXT NOT NULL, ");
            bd.append(WordContract.Word.NAME_KEY);
            bd.append(" TEXT, ");
            bd.append(WordContract.Word.SOUND);
            bd.append(" TEXT, ");
            bd.append(WordContract.Word.EXAMPLE);
            bd.append(" TEXT, ");
            bd.append(WordContract.Word.EXAMPLE_KEY);
            bd.append(" TEXT, ");
            bd.append(WordContract.Word.EXPAND);
            bd.append(" TEXT, ");
            bd.append(WordContract.Word.KIND);
            bd.append(" INTEGER, ");
            bd.append(WordContract.Word.REMEMBER);
            bd.append(" INTEGER ); ");

            return bd.toString();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("anhdt", "MyDatabaseHelper.onUpgrade ... ");
            // Hủy (drop) bảng cũ nếu nó đã tồn tại.
            db.execSQL("DROP TABLE IF EXISTS " + WordContract.Word.TB_WORD_ALL);
            db.execSQL("DROP TABLE IF EXISTS " + WordContract.Word.TB_WORD_REMIND);
            // Và tạo lại.
            onCreate(db);
        }
    }
}
