package com.example.sharaki.movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Sharaki on 9/14/2016.
 */
public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<MovieBean.ResultsBean> movies;
    ArrayList<String> posters;
    String imageUrl;
    int flag = 0; // 0 if coming from mainactivity and 1 if favorites
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, ArrayList<MovieBean.ResultsBean> movies) { // mainActivity
        this.context = applicationContext;
        this.movies = movies;
        flag = 0;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public CustomAdapter(Context context, ArrayList<String> posters, ArrayList<String> name) { // favorites
        this.context = context;
        this.posters = posters;
        flag = 1;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        if(flag==0){
            return movies.size();        }
        else{
            return posters.size();
        }

    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflter.inflate(R.layout.single_item, null);
        }

        if (flag == 0){
            imageUrl =movies.get(i).getPoster_path();
        }
        else
            imageUrl = posters.get(i);

        ImageView poster = (ImageView) view.findViewById(R.id.icon);
        Picasso.with(context).load("https://image.tmdb.org/t/p/w342"+imageUrl).into(poster);
        return view;
    }
}
