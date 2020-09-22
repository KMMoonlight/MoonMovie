package com.kmmoonlight.moonvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rxhttp.RxHttp;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.kmmoonlight.moonvideo.databinding.ActivityMainBinding;
import com.kmmoonlight.moonvideo.page.SearchActivity;
import com.kmmoonlight.moonvideo.page.adapter.VideoRvAdapter;
import com.kmmoonlight.moonvideo.repos.VideoRepo;
import com.kmmoonlight.moonvideo.utils.HtmlParser;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private VideoRvAdapter videoAdapter;
    private VideoRvAdapter movieAdapter;
    private VideoRvAdapter showAdapter;
    private VideoRvAdapter cartoonAdapter;

    private List<VideoRepo> videoRepoList;
    private List<VideoRepo> movieRepoList;
    private List<VideoRepo> showRepoList;
    private List<VideoRepo> cartoonRepoList;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();

        getHomePageData();
    }

    private void initView() {
        binding.layoutVideo.tvCategory.setText("电视剧");
        binding.layoutMovie.tvCategory.setText("电影");
        binding.layoutShow.tvCategory.setText("综艺");
        binding.layoutCartoon.tvCategory.setText("动漫");

        videoRepoList = new ArrayList<>();
        movieRepoList = new ArrayList<>();
        showRepoList = new ArrayList<>();
        cartoonRepoList = new ArrayList<>();

        videoAdapter = new VideoRvAdapter(videoRepoList, this);
        movieAdapter = new VideoRvAdapter(movieRepoList, this);
        showAdapter = new VideoRvAdapter(showRepoList, this);
        cartoonAdapter = new VideoRvAdapter(cartoonRepoList, this);

        binding.layoutVideo.rvVideo.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.layoutMovie.rvVideo.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.layoutShow.rvVideo.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.layoutCartoon.rvVideo.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));


        binding.layoutVideo.rvVideo.setAdapter(videoAdapter);
        binding.layoutMovie.rvVideo.setAdapter(movieAdapter);
        binding.layoutShow.rvVideo.setAdapter(showAdapter);
        binding.layoutCartoon.rvVideo.setAdapter(cartoonAdapter);

        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void getHomePageData() {
        RxHttp.get("")
                .asString()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    binding.pbLoading.setVisibility(View.GONE);
                    videoRepoList.addAll(HtmlParser.getHomePageVideoList(s));
                    movieRepoList.addAll(HtmlParser.getHomePageMovieList(s));
                    showRepoList.addAll(HtmlParser.getHomePageShowList(s));
                    cartoonRepoList.addAll(HtmlParser.getHomePageCartoonList(s));

                    videoAdapter.notifyDataSetChanged();
                    movieAdapter.notifyDataSetChanged();
                    showAdapter.notifyDataSetChanged();
                    cartoonAdapter.notifyDataSetChanged();

                }, throwable -> {
                    binding.pbLoading.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Error Query For Home Data!", Toast.LENGTH_SHORT).show();
                });
    }

}