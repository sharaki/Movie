package com.example.sharaki.movie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharaki.movie.MovieBean.ResultsBean;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LinearLayout linearLayout2;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ResultsBean curMovie;

    @Override
    public void onStart() {
//        getActivity().setTitle(curMovie.getTitle());
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curMovie = (ResultsBean) getArguments().getSerializable("movie");
            //Toast.makeText(getContext(), (int) curMovie.getVote_average(), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        if(curMovie!=null){

            ImageView img = (ImageView) rootView.findViewById(R.id.img);
            Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w342"+curMovie.getPoster_path()).into(img);

            TextView tv = (TextView) rootView.findViewById(R.id.date);
            tv.setText(curMovie.getRelease_date());

            TextView tv1 = (TextView) rootView.findViewById(R.id.overview);
            tv1.setText(curMovie.getOverview());

            String rating =Double.toString(curMovie.getVote_average());
            TextView tv2 = (TextView) rootView.findViewById(R.id.vote);
            tv2.setText(rating+"/10");
            linearLayout = (LinearLayout) rootView.findViewById(R.id.l1);
            linearLayout2 = (LinearLayout) rootView.findViewById(R.id.l2);

            getTrailer();
            getReviews();

            rootView.findViewById(R.id.fav).setOnClickListener(this);

        }
        return rootView;
    }
    LinearLayout linearLayout;
    TrailerBean trailerBean;
    List<TrailerBean.ResultsBean> trailers;
    public void getTrailer(){

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = "http://api.themoviedb.org/3/movie/"+curMovie.getId()+"/videos?api_key=3e06132ef05647f94d1c11e01f800ac0";
        asyncHttpClient.get(getContext(), url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Gson gson = new Gson();
                trailerBean=gson.fromJson(new String(responseBody),TrailerBean.class);
                trailers = trailerBean.getResults();
                String a=trailers.get(0).getKey();
                if (trailers.isEmpty()) {
                    Toast.makeText(getContext(), "No Trailers!", Toast.LENGTH_LONG).show();
                    return;
                }
                int i = 0;
                for (final TrailerBean.ResultsBean result : trailers){
                    Button button = new Button(getContext());
                    button.setId(i);
                    button.setText(result.getName());
                    button.setLayoutParams(lp);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +
                                    result.getKey())));
                        }
                    });
                    i++;
                    linearLayout.addView(button);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void getReviews(){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = "http://api.themoviedb.org/3/movie/"+curMovie.getId()+"/reviews?api_key=3e06132ef05647f94d1c11e01f800ac0";
        asyncHttpClient.get(getContext(), url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Gson gson = new Gson();
                ReviewsBean reviewsBean = gson.fromJson(new String(responseBody), ReviewsBean.class);
                List<ReviewsBean.ResultsBean> reviews = reviewsBean.getResults();

                if (reviews.isEmpty()) {
                    Toast.makeText(getContext(), "No Reviews!", Toast.LENGTH_LONG).show();
                    return;
                }
                int i = 0;
                for (final ReviewsBean.ResultsBean result : reviews){
                    TextView author = new TextView(getContext());
                    author.setId(i+1);
                    author.setTypeface(Typeface.DEFAULT_BOLD);

                    author.setText(result.getAuthor());
                    author.setLayoutParams(lp);
                    linearLayout2.addView(author);

                    TextView content = new TextView(getContext());
                    content.setId(i+10);
                    content.setText(result.getContent());
                    content.setLayoutParams(lp);
                    linearLayout2.addView(content);
                    i++;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

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
        Realm realm ;
    public void addToFav(){

        realm=Realm.getDefaultInstance();
        final int id=curMovie.getId();

        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Db db=realm.createObject(Db.class,id);
                    db.name=curMovie.getOriginal_title();
                    db.poster=curMovie.getPoster_path();
                    db.id=id;
                    db.rate=curMovie.getVote_average();
                    db.overview=curMovie.getOverview();
                    db.date=curMovie.getRelease_date();
                }

            });
            Toast.makeText(getContext(),"Added to Favorites",Toast.LENGTH_LONG).show();
        } catch (RealmPrimaryKeyConstraintException exception){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Db> results = realm.where(Db.class).equalTo("id",id).findAll();
                    results.deleteAllFromRealm();
                }
            });
            Toast.makeText(getContext(),"deleted to Favorites",Toast.LENGTH_LONG).show();

        }

    };

    @Override
    public void onClick(View view) {
        addToFav();
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
