package com.add.toeic.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.add.toeic.R;
import com.add.toeic.model.Word;
import com.add.toeic.services.FloatingViewServiceSimple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoadWordActivity extends AppCompatActivity {

    String TAG = "duy.pq";
    Button btn_load;
    TextView tv_kq;
    int PICKFILE_RESULT_CODE = 1001;
    Context mContext;
    ArrayList<Word> mArrWord;
    private String filename = "phuongthuy.txt";

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;
    ArrayList<String> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_load_word);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_load = (Button) findViewById(R.id.btn_load);
        tv_kq = (TextView) findViewById(R.id.tv_kq);
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(LoadWordActivity.this, FloatingViewServiceSimple.class));
            }
        });

        readData();
        //Get intent, action and MIME type
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getType();
            if (Intent.ACTION_VIEW.equals(action) && type != null && "text/plain".equals(type)) {
                mArrWord = handleViewText(intent);

                for (int t = 0; t < mArrWord.size(); t++) {
                    Log.i("duy.pq", "Itent.item=" + mArrWord.get(t).toString());
                }
            }
        }

        //listview
        mainListView = (ListView) findViewById(R.id.lv_word);

        words = new ArrayList<String>();
        for (int t = 0; t < mArrWord.size(); t++) {
            words.add(mArrWord.get(t).getName() + " : " + mArrWord.get(t).getName_key());
        }
        listAdapter = new ArrayAdapter<String>(this, R.layout.item_word, words);
        mainListView.setAdapter(listAdapter);
    }

    public void readData() {

        mArrWord = getTextFileData(filename);

        for (int t = 0; t < mArrWord.size(); t++) {
            Log.i("duy.pq", "item=" + mArrWord.get(t).toString());
        }
    }

    private ArrayList<Word> handleViewText(Intent intent) {

        ArrayList<Word> arr = new ArrayList<Word>();
        Uri uri = intent.getData();
        Log.i("duy.pq", "uri=" + uri.toString());
        BufferedReader br = null;
        try {
            String sCurrentLine;
            InputStream is = getContentResolver().openInputStream(uri);
            br = new BufferedReader(new InputStreamReader(is));

            int t = 0;
            Word word = null;
            while ((sCurrentLine = br.readLine()) != null) {
                //  Log.e(TAG, "t=" + t + "- " + sCurrentLine);

                if (t % 3 == 0) {
                    word = new Word();
                    word.setName(sCurrentLine);
                }
                if (t % 3 == 1) {
                    if (word != null) {
                        word.setName_key(sCurrentLine);
                        arr.add(word);
                    }
                }
                t++;
            }
        } catch (Exception e) {
            //nothing
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                //nothing
            }
        }
        return arr;
    }

    public static ArrayList<Word> getTextFileData(String fileName) {

        ArrayList<Word> arr = new ArrayList<Word>();

        File sdCardDir = Environment.getExternalStorageDirectory();
        File txtFile = new File(sdCardDir, fileName);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(txtFile));
            String line;
            int t = 0;
            Word word = null;
            while ((line = reader.readLine()) != null) {
                text.append(line + '\n');
                // Log.i("duy.pq", "line=" + line + " t =" + t);
                if (t % 3 == 0) {
                    word = new Word();
                    word.setName(line);
                }
                if (t % 3 == 1) {
                    if (word != null) {
                        word.setName_key(line);
                        arr.add(word);
                    }
                }
                t++;
            }
            reader.close();
        } catch (IOException e) {
            Log.e("C2c", "Error occured while reading text file!!");
        }
        return arr;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
