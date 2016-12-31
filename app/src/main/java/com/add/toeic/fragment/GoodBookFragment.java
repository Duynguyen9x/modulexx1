package com.add.toeic.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.add.toeic.R;
import com.add.toeic.adapter.BookTheoryAdapter;
import com.add.toeic.listeners.OnFragmentInteractionListener;
import com.add.toeic.model.BookTheory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GoodBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoodBookFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PARAM_KIND = "param_kind";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParamKind;
    private String mParam2;

    private ArrayList<BookTheory> mArrList = null;
    private BookTheoryAdapter mViewAdapter = null;
    private ListView mListView = null;
    private Context mContext;
    private View view;

    private OnFragmentInteractionListener mListener;
    private int mKind;

    public GoodBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoodBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoodBookFragment newInstance(String param1, String param2) {
        GoodBookFragment fragment = new GoodBookFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_KIND, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamKind = getArguments().getString(PARAM_KIND);
            mParam2 = getArguments().getString(ARG_PARAM2);

            if (mParamKind.equals("1")) {
                mKind = 1;
            }
            if (mParamKind.equals("2")) {
                mKind = 2;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_good_book, container, false);
        initViewList();

        return view;
    }

    public void initViewList() {
        mListView = (ListView) view.findViewById(R.id.list_book);
        mArrList = new ArrayList<>();

        mViewAdapter = new BookTheoryAdapter(getContext(), R.layout.booktheory_item_layout, mArrList);
        mListView.setAdapter(mViewAdapter);

        initLoadData();
    }

    public void initLoadData() {
        AsyncTask<Void, Void, List<BookTheory>> loadBitmapTask = new AsyncTask<Void, Void, List<BookTheory>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<BookTheory> doInBackground(Void... params) {
                return getListBooks();
            }

            @Override
            protected void onPostExecute(List<BookTheory> listBooks) {

                mViewAdapter.addAll(listBooks);
                mListView.setAdapter(mViewAdapter);
                super.onPostExecute(listBooks);
            }
        };

        loadBitmapTask.execute();
    }

    public List<BookTheory> getListBooks() {
        if (mKind == 1) {
            List<BookTheory> listBook = new ArrayList<BookTheory>();
            ArrayList<BookTheory> arr = new ArrayList<>();

            BookTheory book1 = new BookTheory();
            book1.setNumber("1. ");
            book1.setContent("Sach hay 1");
            listBook.add(book1);

            BookTheory book2 = new BookTheory();
            book2.setNumber("2. ");
            book2.setContent("Sach hay 2");
            listBook.add(book2);

            BookTheory book3 = new BookTheory();
            book3.setNumber("3. ");
            book3.setContent("Sach hay 3");
            listBook.add(book3);

            return listBook;
        }

        if (mKind == 2) {
            List<BookTheory> listBook = new ArrayList<BookTheory>();
            ArrayList<BookTheory> arr = new ArrayList<>();

            BookTheory book1 = new BookTheory();
            book1.setNumber("1. ");
            book1.setContent("Sach deo hay 1");
            listBook.add(book1);

            BookTheory book2 = new BookTheory();
            book2.setNumber("2. ");
            book2.setContent("Sach deo hay 2");
            listBook.add(book2);

            BookTheory book3 = new BookTheory();
            book3.setNumber("3. ");
            book3.setContent("Sach deo hay 3");
            listBook.add(book3);

            return listBook;
        }
        return null;
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
}
