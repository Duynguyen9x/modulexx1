package com.add.toeic.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import com.add.toeic.R;
import com.add.toeic.activity.DetailWordActivity;
import com.add.toeic.adapter.WordRemindAdapter;
import com.add.toeic.listeners.OnFragmentInteractionListener;
import com.add.toeic.listeners.OnRemindClickListener;
import com.add.toeic.model.Word;
import com.add.toeic.provider.AppProvider;
import com.add.toeic.temp.WordContract;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordRemindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordRemindFragment extends Fragment implements OnRemindClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Word> mArrList = null;
    private WordRemindAdapter mWordRemindAdapter = null;
    private ListView mListView = null;
    private Context mContext;
    private View view;
    private Button mBtnDel;
    private Button mBtnRunAuto;
    private Button mBtnDelAll;
    private Button mBtnDone;

    private LayoutInflater inflater;
    private LinearLayout mLn_delete_runAuto, mLn_DeleteAll_Done;
    private ImageButton imgBtnRemindDelete;

    private OnFragmentInteractionListener mListener;
    private int mCntRemind;

    public WordRemindFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordRemindFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordRemindFragment newInstance(String param1, String param2) {
        WordRemindFragment fragment = new WordRemindFragment();
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
    public void onResume() {
        super.onResume();
        mContext.getContentResolver().registerContentObserver(WordContract.Word.CONTENT_URI_REMIND, true, contentObserver);
    }

    private ContentObserver contentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            mArrList.clear();
            mArrList.addAll(AppProvider.getAllWords(true));
            mWordRemindAdapter.notifyDataSetChanged();
            mCntRemind = mArrList.size();

            if (mCntRemind == 0) {
                mLn_delete_runAuto.setVisibility(View.GONE);
                mLn_DeleteAll_Done.setVisibility(View.GONE);
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater.inflate(R.layout.empty, (ViewGroup) view);
            } else {
                if (mLn_DeleteAll_Done.getVisibility() != View.VISIBLE)
                    mLn_delete_runAuto.setVisibility(View.VISIBLE);
                ((ViewGroup) view).removeView(view.findViewById(R.id.rl_empty));
            }
            Log.i("duy.pq", "contentObserver = ");
        }

        @Override
        public boolean deliverSelfNotifications() {
            // return true avoid onChange() method is called multiple times
            return true;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.getContentResolver().unregisterContentObserver(contentObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("duynq", "WordRemindFragment : onCreateView");
        view = inflater.inflate(R.layout.fragment_remind, container, false);
        initRemindList();
        return view;
    }

    public void initRemindList() {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(mContext);

        mLn_delete_runAuto = (LinearLayout) view.findViewById(R.id.delete_run_button_layout);
        mLn_DeleteAll_Done = (LinearLayout) view.findViewById(R.id.deleteall_done_button_layout);

        mBtnDel = (Button) view.findViewById(R.id.delete_btn);
        mBtnRunAuto = (Button) view.findViewById(R.id.run_auto_btn);
        mBtnDelAll = (Button) view.findViewById((R.id.delete_all_btn));
        mBtnDone = (Button) view.findViewById((R.id.done_btn));

        mListView = (ListView) view.findViewById(R.id.list_remind);
        mArrList = new ArrayList<>();
        mWordRemindAdapter = new WordRemindAdapter(mContext, R.layout.word_item_layout_for_remind, mArrList, false);
        mWordRemindAdapter.setOnRemindClickListener(this);
        mListView.setAdapter(mWordRemindAdapter);

        initLoadData();

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWordRemindAdapter.setDelete(true);
                mLn_delete_runAuto.setVisibility(View.INVISIBLE);
                mLn_delete_runAuto.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_left));
                mLn_DeleteAll_Done.setVisibility(View.VISIBLE);
                mLn_DeleteAll_Done.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right));
            }
        });
        mBtnRunAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forceRunAuto = new Intent(mContext, DetailWordActivity.class);
                forceRunAuto.putExtra("run_from_group_or_reminded", 2);
                mContext.startActivity(forceRunAuto);
                //TODO
            }
        });
        mBtnDelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRemindWordDialog(true);
            }
        });
        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWordRemindAdapter.setDelete(false);
                mLn_DeleteAll_Done.setVisibility(View.INVISIBLE);
                mLn_DeleteAll_Done.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_right));
                mLn_delete_runAuto.setVisibility(View.VISIBLE);
                mLn_delete_runAuto.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left));
            }
        });
        mListView.invalidate();
    }

    public void initLoadData() {
        AsyncTask<Void, Void, List<Word>> loadDataTask = new AsyncTask<Void, Void, List<Word>>() {
//            private ProgressDialog progress = null;

            @Override
            protected void onPreExecute() {
//                progress = ProgressDialog.show(getContext(), null, "Loading application info...");
                super.onPreExecute();
            }

            @Override
            protected List<Word> doInBackground(Void... params) {
                return AppProvider.getAllWords(true);
            }

            @Override
            protected void onPostExecute(List<Word> listWords) {
                try {
//                    progress.dismiss();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                mCntRemind = listWords.size();
                mWordRemindAdapter.addAll(listWords);
                mListView.setAdapter(mWordRemindAdapter);
                if (mCntRemind == 0) {
                    mLn_delete_runAuto.setVisibility(View.GONE);
                    mLn_DeleteAll_Done.setVisibility(View.GONE);
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    inflater.inflate(R.layout.empty, (ViewGroup) view);
                } else {
                    mLn_delete_runAuto.setVisibility(View.VISIBLE);
                    ((ViewGroup) view).removeView(view.findViewById(R.id.rl_empty));
                }
                super.onPostExecute(listWords);
            }
        };

        loadDataTask.execute();
    }

    @Override
    public void onDestroyView() {
        Log.i("duynq", "WordRemindFragment : onDestroyView");
        super.onDestroyView();
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void update() {
        Log.d("anhdt111", "WordRemindFragment update");
        mWordRemindAdapter.notifyDataSetChanged();
    }

    private void deleteRemindWordDialog(final boolean isDeleteAll) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Delete all reminded words!");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isDeleteAll) {
                            AppProvider.deleteAll(mContext, true);
                        } else {
                            // TODO
                        }
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawableResource(R.drawable.all_circle_white_bg_child);
        alert.show();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = null;
        String[] projections = null;
        // muon lay full word thi de null, muon lay reminder thi de nhu duoi
        String selection = null;
        // String selection = WordContract.Word.WORD_STATUS + " = " + 0;

        String[] argu = null;
        String orderBy = null;
        uri = WordContract.Word.CONTENT_URI_ALL;
        Log.d("anhdt", " load cursor loader with id " + id);
        CursorLoader cursorLoader = new CursorLoader(mContext, uri, projections, selection, argu, orderBy);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        initLoadData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
