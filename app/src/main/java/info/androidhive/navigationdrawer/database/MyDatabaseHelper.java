package info.androidhive.navigationdrawer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import info.androidhive.navigationdrawer.model.Word;

/**
 * Created by sev_user on 12/26/2016.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "Toeic.SQLite";


    // Phiên bản
    private static final int DATABASE_VERSION = 1;


    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "Word_Manager";


    // Tên bảng: Word.
    private static final String TABLE_WORD = "word";

    private static final String COLUMN_WORD_ID = "word_id";
    private static final String COLUMN_WORD_NAME = "word_name";
    private static final String COLUMN_WORD_NAME_KEY = "word_name_key";
    private static final String COLUMN_WORD_SOUND = "word_sound";

    private static final String COLUMN_WORD_EXAMPLE = "word_example";
    private static final String COLUMN_WORD_EXAMPLE_KEY = "word_example_key";

    private static final String COLUMN_WORD_KIND = "word_kind";


    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script tạo bảng.
        String script = "CREATE TABLE " + TABLE_WORD + "("
                + COLUMN_WORD_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_WORD_NAME + " TEXT,"
                + COLUMN_WORD_SOUND + " TEXT,"
                + COLUMN_WORD_NAME_KEY + " TEXT,"
                + COLUMN_WORD_EXAMPLE + " TEXT,"
                + COLUMN_WORD_EXAMPLE_KEY + " TEXT,"
                + COLUMN_WORD_KIND + " INTEGER"
                + ")";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORD);


        // Và tạo lại.
        onCreate(db);
    }

    // Nếu trong bảng Note chưa có dữ liệu,
    // Trèn vào mặc định 2 bản ghi.
    public void createDefaultNotesIfNeed() {
        int count = this.getWordsCount();
        if (count == 0) {

            Word w1 = new Word();
            w1.setName("Go");
            w1.setName_key("di");
            w1.setSound("gâu");
            w1.setExample("I go to school");
            w1.setExample_key("Toi di hoc");
            w1.setKind_word(1);

            this.addWord(w1);

            Word w2 = new Word();
            w2.setName("School");
            w2.setName_key("truong");
            w2.setSound("gâu");
            w2.setExample("I go to school");
            w2.setExample_key("Toi di hoc");
            w2.setKind_word(2);
            this.addWord(w2);


            Word w3 = new Word();
            w3.setName("fUCK");
            w3.setName_key("truong");
            w3.setSound("gâu");
            w3.setExample("I go to school");
            w3.setExample_key("Toi di hoc");
            w3.setKind_word(4);
            this.addWord(w3);


            Word w4 = new Word();
            w4.setName("Spennis");
            w4.setName_key("truong");
            w4.setSound("gâu");
            w4.setExample("I go to school");
            w4.setExample_key("Toi di hoc");
            w4.setKind_word(0);
            this.addWord(w4);
        }
    }


    public void addWord(Word word) {

        if (checkHasWord(word)) {
            Log.i(TAG, "MyDatabaseHelper.addWord: have word" + word.getName());
            return;
        }
        Log.i(TAG, "MyDatabaseHelper.addWord ... " + word.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD_NAME, word.getName());
        values.put(COLUMN_WORD_SOUND, word.getSound());
        values.put(COLUMN_WORD_NAME_KEY, word.getName_key());
        values.put(COLUMN_WORD_EXAMPLE, word.getExample_key());
        values.put(COLUMN_WORD_EXAMPLE_KEY, word.getExample_key());
        values.put(COLUMN_WORD_KIND, word.getKind_word());

        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_WORD, null, values);

        // Đóng kết nối database.
        db.close();
    }

    public void addWordList(ArrayList<Word> arr) {
        for (int i = 0; i < arr.size(); i++) {
            addWord(arr.get(i));
        }
    }


//    public Word getWord(int id) {
//        Log.i(TAG, "MyDatabaseHelper.getNote ... " + id);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_WORD, new String[]{COLUMN_WORD_ID,
//                        COLUMN_WORD_NAME, COLUMN_WORD_SOUND, COLUMN_WORD_NAME_KEY}, COLUMN_WORD_ID + "=?",
//                new String[]{String.valueOf(id)}, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//        cursor.close();
//
//        Word word = new Word();
//        word.setId(Integer.parseInt(cursor.getString(0)));
//        word.setName(cursor.getString(1));
//        word.setSound(cursor.getString(2));
//        word.setName_key(cursor.getString(3));
//        word.setExample(cursor.getString(4));
//        word.setExample_key(cursor.getString(5));
//        word.setKind_word(Integer.parseInt(cursor.getString(6)));
//
//        return word;
//    }

    public boolean checkHasWord(Word word) {
        String wordname = word.getName();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_WORD_NAME + "=?";
        String[] arg = {wordname};

        Cursor cursor = db.query(TABLE_WORD, null, selection, arg, null, null, null);
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

    public ArrayList<Word> getAllWords() {
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... ");

        ArrayList<Word> wordList = new ArrayList<Word>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WORD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setId(Integer.parseInt(cursor.getString(0)));
                word.setName(cursor.getString(1));
                word.setSound(cursor.getString(2));
                word.setName_key(cursor.getString(3));
                word.setExample(cursor.getString(4));
                word.setExample_key(cursor.getString(5));
                word.setKind_word(Integer.parseInt(cursor.getString(6)));
                // Thêm vào danh sách.
                wordList.add(word);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return note list
        return wordList;
    }

    public int getWordsCount() {
        Log.i(TAG, "MyDatabaseHelper.getNotesCount ... ");

        String countQuery = "SELECT  * FROM " + TABLE_WORD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }


    public void deleteWord(Word word) {
        Log.i(TAG, "MyDatabaseHelper.delete ... " + word.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORD, COLUMN_WORD_NAME + " = ?",
                new String[]{word.getName()});
        db.close();
    }

    public void deleteAll() {
        Log.i(TAG, "MyDatabaseHelper.delete all ... ");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORD, null, null);
        db.close();
    }
}