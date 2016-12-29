package com.add.toeic.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.add.toeic.model.HeaderWord;
import com.add.toeic.model.Word;

public class WordUtils {

    public static final int KIND_NOND = 1;
    public static final int KIND_VERD = 2;
    public static final int KIND_ADJ = 3;
    public static final int KIND_ADV = 4;
    public static final int KIND_OTHER = 5;

    public static final int NUMBER_WORD = 600;

    public static String readName(String line) {
        try {
            return (String) line.subSequence(0, line.indexOf("/") - 1);
        } catch (Exception e) {
            return "";
        }
    }

    public static String readNameKey(String line) {
        return line.replace("Từ loại: (v): ", "").replace("Từ loại: (n): ", "")
                .replace("Từ loại: (adj): ", "")
                .replace("Từ loại: (adv): ", "")
                .replace("Từ loại: (n,v): ", "")
                .replace("Từ loại: (v,n): ", "")
                .replace("Từ loại: (v, n): ", "")
                .replace("Từ loại: (n, v): ", "");
    }

    public static int readkind(String line) {
        if (line.contains("Từ loại: (n):"))
            return 1;
        if (line.contains("Từ loại: (v):"))
            return 2;
        if (line.contains("Từ loại: (adj):"))
            return 3;
        if (line.contains("Từ loại: (adv):"))
            return 4;

        return 0;
    }

    public static String readSound(String line) {
        try {
            return (String) line.substring(line.indexOf("/"),
                    line.length());
        } catch (Exception e) {
            return "";
        }
    }

    public static String readExpand(String line) {
        return line.replace("Giải thích: ", "");
    }

    public static String readExample(String line) {
        return line.replace("Ví dụ: ", "");
    }

    public static ArrayList<Word> readAllData(Context context) throws FileNotFoundException {
        AssetManager am = context.getResources().getAssets();
        ArrayList<Word> arr = null;
        try {
            InputStreamReader ip = new InputStreamReader(context.getResources().getAssets().open("gradle.txt"));
            arr = readDataFromLocalFinal(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public static ArrayList<HeaderWord> readAllHeaderWord(Context context) throws FileNotFoundException {
        AssetManager am = context.getResources().getAssets();
        ArrayList<HeaderWord> arr = null;
        try {
            InputStreamReader ip = new InputStreamReader(context.getResources().getAssets().open("hanh.ttx"));
            arr = readDataHederWordFromLocal(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public static ArrayList<Word> readDataFromLocalFinal(InputStreamReader isr) {
        try {
            //InputStreamReader isr = new InputStreamReader(fi, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String s;
            // Đọc từng dòng văn bản!

            int t = 0;
            int count = 1;
            ArrayList<Word> arr = new ArrayList<Word>();
            String l1 = null, l2 = null, l3 = null, l4 = null, l5 = null;

            String line = br.readLine();
            while (line != null) {

                // if (true) {
                // System.out.println(t + "==" + count + " " + line);
                //
                // }

                if (t % 6 == 0) {
                    l1 = line;
                }
                if (t % 6 == 1) {
                    l2 = line;
                }
                if (t % 6 == 2) {
                    l3 = line;
                }
                if (t % 6 == 3) {
                    l4 = line;
                }
                if (t % 6 == 4) {
                    l5 = line;
                }

                line = br.readLine();
                t++;
                if (t % 6 == 0) {

                    // l1 background /'bækgraund/
                    // l2 Giải thích: education, experience
                    // l3 Từ loại: (n): kinh nghiệm
                    // l4 Ví dụ: Your background
                    // l5 Kiến thức của anh
                    // add word
                    Word new_word = new Word();
                    new_word.setId(count);
                    new_word.setName(readName(l1));
                    new_word.setSound(readSound(l1));

                    new_word.setExpand(readExpand(l2));

                    new_word.setKind_word(readkind(l3));
                    new_word.setName_key(readNameKey(l3));

                    new_word.setExample(readExample(l4));
                    new_word.setExample_key(l5);
                    arr.add(new_word);

                    count++;
                }
                if (count > NUMBER_WORD)
                    break;
            }

//            for (int i = 0; i < arr.size(); i++) {
//                Log.i("duy.pq", "i=" + i + " : " + arr.get(i).toString());
//            }

            return arr;
        } catch (IOException e) {
            System.out.println("Đã có lỗi xảy ra!");
        }
        return null;
    }

    public static ArrayList<HeaderWord> readDataHederWordFromLocal(InputStreamReader isr) {
        try {
            //InputStreamReader isr = new InputStreamReader(fi, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String s;
            // Đọc từng dòng văn bản!

            int t = 1;
            int count = 1;
            ArrayList<HeaderWord> arr = new ArrayList<HeaderWord>();
            String l1 = null, l2 = null;

            String line = br.readLine();
            while (line != null) {

                // if (true) {
                // System.out.println(t + "==" + count + " " + line);
                //
                // }

                if (t % 2 == 1) {
                    l1 = line;
                } else {
                    l2 = line;
                    HeaderWord he = new HeaderWord();
                    he.setHeader_eng(l1);
                    he.setHeader_vi(l2);
                    he.setNum(count);
                    arr.add(he);
                    count++;
                }
                line = br.readLine();
                t++;
            }
//            for (int i = 0; i < arr.size(); i++) {
//                Log.i("duy.pq", "i=" + i + " : " + arr.get(i).toString());
//
//            }
            return arr;
        } catch (IOException e) {
            System.out.println("Đã có lỗi xảy ra!");
        }
        return null;
    }

}
