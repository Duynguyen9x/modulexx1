package com.add.toeic.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.add.toeic.R;
import com.add.toeic.listeners.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int DEFAULT = 0;
    public static final int BLUE_STYLE = 1;
    public static final int GREEN_STYLE = 2;
    public static final int PINK_STYLE = 3;
    public static final int ORANGE_STYLE = 4;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private View view;
    private Button btn_change;
    private View mExpandView, mCollapseView;
    private int mStyle;

    public static final String PREFS_NAME = "OUR_STYLE";
    public static final String PREFS_STYLE = "DEFAULT";

    private OnFragmentInteractionListener mListener;
    private SharedPreferences.Editor editor;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView();
        return view;
    }

    public void initView() {
         editor = mContext.getSharedPreferences(PREFS_NAME, mContext.MODE_PRIVATE).edit();

        SharedPreferences sharedPrefs = mContext.getSharedPreferences(PREFS_NAME, mContext.MODE_PRIVATE);

        mExpandView = view.findViewById(R.id.layout_expand);
        mCollapseView = view.findViewById(R.id.layout_collapse);
        btn_change = (Button) view.findViewById(R.id.btn_changeStyle);

        mStyle = sharedPrefs.getInt(PREFS_STYLE, 0);

        switch (mStyle) {
            case SettingsFragment.BLUE_STYLE:
                mCollapseView.setBackground(getResources().getDrawable(R.drawable.chatheader_blue_style, null));
                mExpandView.setBackground(getResources().getDrawable(R.drawable.chatheader_blue_style, null));
                btn_change.setBackground(getResources().getDrawable(R.drawable.chatheader_blue_style, null));
                break;
            case SettingsFragment.GREEN_STYLE:
                mCollapseView.setBackground(getResources().getDrawable(R.drawable.chatheader_lightgreen_style, null));
                mExpandView.setBackground(getResources().getDrawable(R.drawable.chatheader_lightgreen_style, null));
                btn_change.setBackground(getResources().getDrawable(R.drawable.chatheader_lightgreen_style, null));
                break;
            case SettingsFragment.PINK_STYLE:
                mCollapseView.setBackground(getResources().getDrawable(R.drawable.chatheader_pink_style, null));
                mExpandView.setBackground(getResources().getDrawable(R.drawable.chatheader_pink_style, null));
                btn_change.setBackground(getResources().getDrawable(R.drawable.chatheader_pink_style, null));
                break;
            case SettingsFragment.ORANGE_STYLE:
                mCollapseView.setBackground(getResources().getDrawable(R.drawable.chatheader_orange_style, null));
                mExpandView.setBackground(getResources().getDrawable(R.drawable.chatheader_orange_style, null));
                btn_change.setBackground(getResources().getDrawable(R.drawable.chatheader_orange_style, null));
                break;
            case SettingsFragment.DEFAULT:
                mCollapseView.setBackground(getResources().getDrawable(R.drawable.circle_chatheader_full, null));
                mExpandView.setBackground(getResources().getDrawable(R.drawable.circle_chatheader_full, null));
                btn_change.setBackground(getResources().getDrawable(R.drawable.circle_chatheader_full, null));
                break;
        }

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStyle == DEFAULT) {
                    mStyle = BLUE_STYLE;
                    editor.putInt(PREFS_STYLE, mStyle);
                    mCollapseView.setBackground(getResources().getDrawable(R.drawable.chatheader_blue_style, null));
                    mExpandView.setBackground(getResources().getDrawable(R.drawable.chatheader_blue_style, null));
                    btn_change.setBackground(getResources().getDrawable(R.drawable.chatheader_blue_style, null));
                } else if (mStyle == BLUE_STYLE) {
                    mStyle = GREEN_STYLE;
                    editor.putInt(PREFS_STYLE, mStyle);
                    mCollapseView.setBackground(getResources().getDrawable(R.drawable.chatheader_lightgreen_style, null));
                    mExpandView.setBackground(getResources().getDrawable(R.drawable.chatheader_lightgreen_style, null));
                    btn_change.setBackground(getResources().getDrawable(R.drawable.chatheader_lightgreen_style, null));
                } else if (mStyle == GREEN_STYLE) {
                    mStyle = PINK_STYLE;
                    editor.putInt(PREFS_STYLE, mStyle);
                    mCollapseView.setBackground(getResources().getDrawable(R.drawable.chatheader_pink_style, null));
                    mExpandView.setBackground(getResources().getDrawable(R.drawable.chatheader_pink_style, null));
                    btn_change.setBackground(getResources().getDrawable(R.drawable.chatheader_pink_style, null));
                } else if (mStyle == PINK_STYLE){
                    mStyle = ORANGE_STYLE;
                    editor.putInt(PREFS_STYLE, mStyle);
                    mCollapseView.setBackground(getResources().getDrawable(R.drawable.chatheader_orange_style, null));
                    mExpandView.setBackground(getResources().getDrawable(R.drawable.chatheader_orange_style, null));
                    btn_change.setBackground(getResources().getDrawable(R.drawable.chatheader_orange_style, null));
                } else {
                    mStyle = DEFAULT;
                    editor.putInt(PREFS_STYLE, mStyle);
                    mCollapseView.setBackground(getResources().getDrawable(R.drawable.circle_chatheader_full, null));
                    mExpandView.setBackground(getResources().getDrawable(R.drawable.circle_chatheader_full, null));
                    btn_change.setBackground(getResources().getDrawable(R.drawable.circle_chatheader_full, null));
                }
                editor.apply();

                Intent intent = new Intent();
                intent.setAction("com.add.toeic.CUSTOM_INTENT_STYLE");
                mContext.sendBroadcast(intent);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
