package com.add.toeic.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Laptop TCC on 12/19/2016.
 */
public class HeaderWord {
    public int num;
    public Drawable drawable;
    public String header_eng;
    public String header_vi;
    public float lear;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getHeader_eng() {
        return header_eng;
    }

    public void setHeader_eng(String header_eng) {
        this.header_eng = header_eng;
    }

    public String getHeader_vi() {
        return header_vi;
    }

    public void setHeader_vi(String header_vi) {
        this.header_vi = header_vi;
    }

    public float getLear() {
        return lear;
    }

    public void setLear(float lear) {
        this.lear = lear;
    }

    @Override
    public String toString() {
        return "HeaderWord{" +
                "num=" + num +
                ", drawable=" + drawable +
                ", header_eng='" + header_eng + '\'' +
                ", header_vi='" + header_vi + '\'' +
                ", lear=" + lear +
                '}';
    }
}
