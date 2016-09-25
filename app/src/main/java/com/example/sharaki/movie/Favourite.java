package com.example.sharaki.movie;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class Favourite extends AppCompatActivity implements AdapterView.OnItemClickListener , DetailsFragment.OnFragmentInteractionListener{
    Realm realm;
    ArrayList<String> posters = new ArrayList<>(), title = new ArrayList<>(), date = new ArrayList<>(), overView = new ArrayList<>();
    ArrayList<Integer> movieId = new ArrayList<>();
    ArrayList<Double> rating = new ArrayList<>();
    CustomAdapter customAdapter;
    GridView favoritesGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_favourite);
        favoritesGrid= (GridView) findViewById(R.id.gridfav);
    }
    @Override
    protected void onStart() {
        super.onStart();
        RealmResults<Db> favorites = realm.where(Db.class).findAll();
        for (Db result : favorites) {
            posters.add(result.poster);
            movieId.add(result.id);
            title.add(result.name);
            overView.add(result.overview);
            date.add(result.date);
            rating.add(result.rate);
            customAdapter = new CustomAdapter(this, posters, title);
            favoritesGrid.setAdapter(customAdapter);
            favoritesGrid.setOnItemClickListener(this);
        }
    }

    @Override
    protected void onStop() {
        posters.clear();
        movieId.clear();
        title.clear();
        overView.clear();
        date.clear();
        rating.clear();
        super.onStop();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle =new Bundle();
        MovieBean.ResultsBean curmovie = new MovieBean.ResultsBean();
        curmovie.setPoster_path(posters.get(position));
        curmovie.setTitle(title.get(position));
        curmovie.setVote_average(rating.get(position));
        curmovie.setOverview(overView.get(position));
        curmovie.setRelease_date(date.get(position));

        bundle.putSerializable("movie", curmovie);

        detailsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.favactivity,detailsFragment).addToBackStack("detail").commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
