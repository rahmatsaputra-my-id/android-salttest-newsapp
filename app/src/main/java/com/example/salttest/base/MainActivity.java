package com.example.salttest.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.salttest.R;
import com.example.salttest.adapter.Adapter;
import com.example.salttest.models.Artikel;
import com.example.salttest.models.Berita;
import com.example.salttest.resources.Provider_client;
import com.example.salttest.resources.Provider_interface;
import com.example.salttest.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public  static final String API_KEY = "5a167798e53f44fc8e141fec905a21c1";
    public  static final String Ngra = "id";
    public  static final String Ctgry = "business";
    public  static final String q1 = "apple";
    public  static final String from1 = "2020-01-09";
    public  static final String to1 = "2020-01-09";
    public  static final String sortBy1 = "popularity";

    private RecyclerView _recyclerView;
    private RecyclerView.LayoutManager _layoutManager;
    private List<Artikel> _artikel = new ArrayList<>();
    private Adapter _adapter;
    private String TAG = MainActivity.class.getSimpleName();
    private TextView _topHeadline;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private RelativeLayout _errorLayout;
    private ImageView _errorImage;
    private TextView _errorTitle, _errorMessage;
    private Button _btnRetry;

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.openinbrowser,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.openBrowser:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://salt.id/"));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(browserIntent);
                return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.saltlogo2);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _swipeRefreshLayout= findViewById(R.id.swipeRefresh);
        _swipeRefreshLayout.setOnRefreshListener(this);
        _swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        _topHeadline = findViewById(R.id.BeritaUtama);
        _recyclerView = findViewById(R.id.rv);

        _layoutManager = new LinearLayoutManager(this);
        _recyclerView.setLayoutManager(_layoutManager);
        _recyclerView.setAdapter(_adapter);
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        _recyclerView.setNestedScrollingEnabled(false);

        onLoadingSwipeRefresh("");

        _errorLayout = findViewById(R.id.errorLayout);
        _errorImage = findViewById(R.id.errorImage);
        _errorTitle = findViewById(R.id.errorTitle);
        _errorMessage = findViewById(R.id.errorMessage);
        _btnRetry = findViewById(R.id.btnRetry);
    }

    public void LoadJson(final String keyword){

        _errorLayout.setVisibility(View.GONE);
        _swipeRefreshLayout.setRefreshing(true);

        Provider_interface _apiInterface = Provider_client.getApiClient().create(Provider_interface.class);

        String _country = Utils.getCountry();
        String _language = Utils.getLanguage();

        Call <Berita> _call;

        if(keyword.length()>0){
            _call = _apiInterface.getBeritaSearch(keyword, _language, "publishAt", API_KEY);
        }else{

//            _call = _apiInterface.getBerita(q1,from1, to1, sortBy1, API_KEY);
            _call = _apiInterface.getBerita(Ngra,Ctgry, API_KEY);
//            _call = _apiInterface.getBerita(_country, API_KEY);
        }

        _call.enqueue(new Callback<Berita>(){

            @Override
            public void onResponse(Call<Berita> _call, Response<Berita> response) {
                Log.d("RESPONNYA YANG INI BROH",response.toString());
                if(response.isSuccessful() && response.body().getArtikel() != null){

                    if(!_artikel.isEmpty()){
                        _artikel.clear();
                    }

                    _artikel = response.body().getArtikel();
                    _adapter = new Adapter(_artikel, MainActivity.this);
                    _recyclerView.setAdapter(_adapter);
                    _adapter.notifyDataSetChanged();

//                    initListener();

                    _topHeadline.setVisibility(View.VISIBLE);
                    _swipeRefreshLayout.setRefreshing(false);

                } else {
                    _topHeadline.setVisibility(View.INVISIBLE);
                    _swipeRefreshLayout.setRefreshing(false);

                    String errorCode;
                    switch (response.code()) {
                        case 404:
                            errorCode = "404 not found";
                            break;
                        case 500:
                            errorCode = "500 server broken";
                            break;
                        default:
                            errorCode = "unknown error";
                            break;
                    }

                    showErrorMessage(
                            R.drawable.ic_error_outline_black_24dp,
                            "No Result",
                            "Please Try Again!\n"+
                                    errorCode);

                }
            }

            @Override
            public void onFailure(Call<Berita> _call, Throwable t) {
                _topHeadline.setVisibility(View.INVISIBLE);
                _swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(
                        R.drawable.ic_error_outline_black_24dp,
                        "Oops..\n",
                        "Network failure, Check Your Connection");
//                                + t.toString());
            }
        });
    }

//    private void initListener(){
//        _adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                ImageView imageView = view.findViewById(R.id.gmbr);
//                Intent intent = view.findViewById(MainActivity.this, NewsDetailActivity.class);
//
//                Artikel artikel = _artikel.get(position);
//                getIntent().putExtra("url", _artikel.getUrl());
//                getIntent().putExtra("title", _artikel.getTitle());
//                getIntent().putExtra("img", _artikel.getUrlToImage());
//                getIntent().putExtra("date", _artikel.getPublishedAt());
//                getIntent().putExtra("source", _artikel.getSource.getName());
//                getIntent().putExtra("author", _artikel.getAuthor());
//
//            }
//        });
//    }

    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private void onLoadingSwipeRefresh(final String keyword){

        _swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );

    }

    private void showErrorMessage(int imageView, String title, String message){

        if (_errorLayout.getVisibility() == View.GONE) {
            _errorLayout.setVisibility(View.VISIBLE);
        }

        _errorImage.setImageResource(imageView);
        _errorTitle.setText(title);
        _errorMessage.setText(message);

        _btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingSwipeRefresh("");
            }
        });

    }

}
