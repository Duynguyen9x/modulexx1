package info.androidhive.navigationdrawer.model;

import android.graphics.drawable.Drawable;

/**
 * Created by 8470p on 12/17/2016.
 */
public class WordInfo {
    private Drawable icon;
    private Drawable expandIcon;
    private String english;
    private String vietnamese;

    public String getEnglsih() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getExpandIcon() {
        return expandIcon;
    }

    public void setExpandIcon(Drawable icon) {
        this.expandIcon = icon;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
        this.vietnamese = vietnamese;
    }

}
