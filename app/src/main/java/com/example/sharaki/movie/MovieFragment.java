package com.example.sharaki.movie;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment implements AdapterView.OnItemClickListener,
        DetailsFragment.OnFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String TAG = "MovieLOG";
    Context mcContext = getContext();
    private String url;
    private CustomAdapter customAdapter;
    private boolean tablet;

    public MovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieFragment newInstance(String param1, String param2) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ArrayList<MovieBean.ResultsBean> list = new ArrayList<MovieBean.ResultsBean>();
    ProgressBar progressBar;
    String filter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null){
            filter = getArguments().getString("filter");
            tablet = getArguments().getBoolean("tablet");
        }
            url= "http://api.themoviedb.org/3/movie/"+filter+"?api_key=3e06132ef05647f94d1c11e01f800ac0";
    }
    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        progressBar.setVisibility(View.VISIBLE);
        asyncHttpClient.get(getContext(), url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("gooo", "onSuccess: "+ Arrays.toString(responseBody));
                String responseStr = new String(responseBody);
                Gson gson = new Gson();
                MovieBean movieBean = gson.fromJson(responseStr, MovieBean.class);
                for (MovieBean.ResultsBean result : movieBean.getResults()){
                    list.add(result);
                }

                customAdapter = new CustomAdapter(getContext(), list);
                gridView.setAdapter(customAdapter);
                gridView.setOnItemClickListener(MovieFragment.this);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "error loading data from the server", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onResume() {
        getActivity().setTitle("Movie");

        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    public void onStart() {

        super.onStart();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        MovieBean.ResultsBean movie = (MovieBean.ResultsBean) customAdapter.getItem(position);

        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle =new Bundle();
        bundle.putSerializable("movie",movie);

        detailsFragment.setArguments(bundle);

        if (!tablet)
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, detailsFragment).addToBackStack("detail").commit();
        else
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.master_details, detailsFragment).addToBackStack("detail").commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
