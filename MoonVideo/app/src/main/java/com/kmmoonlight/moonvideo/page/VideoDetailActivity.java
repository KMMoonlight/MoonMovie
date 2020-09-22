package com.kmmoonlight.moonvideo.page;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rxhttp.RxHttp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.kmmoonlight.moonvideo.databinding.ActivityVideoDetailBinding;
import com.kmmoonlight.moonvideo.page.adapter.VideoDetailAdapter;
import com.kmmoonlight.moonvideo.repos.PlayListRepo;
import com.kmmoonlight.moonvideo.utils.HtmlParser;

import java.util.ArrayList;
import java.util.List;

public class VideoDetailActivity extends AppCompatActivity {

    private ActivityVideoDetailBinding binding;
    private String targetUrl = "";
    private List<PlayListRepo> playListRepoList;
    private VideoDetailAdapter videoDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        targetUrl = intent.getStringExtra("target_url");

        playListRepoList = new ArrayList<>();
        videoDetailAdapter = new VideoDetailAdapter(this, playListRepoList);
        binding.lvVideo.setAdapter(videoDetailAdapter);


        if (!TextUtils.isEmpty(targetUrl)) {
            QueryVideoDetail();
        }else {
            binding.pbLoading.setVisibility(View.GONE);
        }


        binding.lvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent startIntent = new Intent(VideoDetailActivity.this, PlayActivity.class);
                startIntent.putExtra("target_url", playListRepoList.get(i).getVideoUrl());
                startActivity(startIntent);
            }
        });
    }


    @SuppressLint("CheckResult")
    private void QueryVideoDetail() {
        RxHttp.get(targetUrl)
                .asString()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    binding.pbLoading.setVisibility(View.GONE);
                    playListRepoList.addAll(HtmlParser.getPlayList(s));
                    videoDetailAdapter.notifyDataSetChanged();
                }, e -> {
                    Toast.makeText(VideoDetailActivity.this, "获取数据出错", Toast.LENGTH_SHORT).show();
                    binding.pbLoading.setVisibility(View.GONE);
                });
    }
}