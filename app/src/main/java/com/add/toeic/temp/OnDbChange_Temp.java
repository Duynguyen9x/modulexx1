package com.add.toeic.temp;

import com.add.toeic.model.Word;

import java.util.List;

/**
 * Created by DTA on 1/7/2017.
 */

public interface OnDbChange_Temp {
    void onContentChanged(boolean isAdd, List<Word> words, int groupPos, int childPos);
}
