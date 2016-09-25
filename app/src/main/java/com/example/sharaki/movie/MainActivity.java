package com.example.sharaki.movie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements MovieFragment.OnFragmentInteractionListener,
        DetailsFragment.OnFragmentInteractionListener, AdapterView.OnItemClickListener{

    private boolean flag = false;

    @Override
    protected void onResume() {
        super.onResume();
        RealmConfiguration config = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String filter = prefs.getString("filter", "popular");
        MovieFragment movieFragment = new MovieFragment();
        Bundle bundle = new Bundle();
        bundle.putString("filter", filter);
        if (findViewById(R.id.tablet) != null) {
            Toast.makeText(this, "tablet mode", Toast.LENGTH_LONG).show();
            bundle.putBoolean("tablet", true);
            movieFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.master_movies, movieFragment).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.master_details, new DetailsFragment()).commit();
        } else {
            bundle.putBoolean("tablet", false);
            movieFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, movieFragment).commit();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag = savedInstanceState == null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.mainactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings){
            startActivity(new Intent(this, SettingsActivity.class));
        }
        else {
            startActivity(new Intent(this, Favourite.class));

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}


