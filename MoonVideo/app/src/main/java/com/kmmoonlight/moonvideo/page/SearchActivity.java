package com.kmmoonlight.moonvideo.page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rxhttp.RxHttp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.kmmoonlight.moonvideo.databinding.ActivitySearchBinding;
import com.kmmoonlight.moonvideo.page.adapter.VideoRvAdapter;
import com.kmmoonlight.moonvideo.repos.VideoRepo;
import com.kmmoonlight.moonvideo.utils.HtmlParser;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;

    private List<VideoRepo> videoRepoList;

    private VideoRvAdapter videoRvAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        videoRepoList = new ArrayList<>();
        videoRvAdapter = new VideoRvAdapter(videoRepoList, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        binding.rvSearch.setLayoutManager(layoutManager);
        binding.rvSearch.setAdapter(videoRvAdapter);

        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputMethodManager = (InputMethodManager) SearchActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //点击搜索
                String searchKeyWord = binding.etSearch.getText().toString();
                if (!TextUtils.isEmpty(searchKeyWord)) {
                    QuerySearch(searchKeyWord);
                }
            }
        });

    }


    @SuppressLint("CheckResult")
    private void QuerySearch(String keyword) {

        binding.pbLoading.setVisibility(View.VISIBLE);
        RxHttp.postForm("/index.php?m=vod-search")
                .add("wd", keyword)
                .add("submit", "")
                .asString()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    binding.pbLoading.setVisibility(View.GONE);
                    videoRepoList.clear();
                    videoRepoList.addAll(HtmlParser.getSearchResult(s));
                    videoRvAdapter.notifyDataSetChanged();

                    if (videoRepoList.size() != 0) {
                        binding.tvNoData.setVisibility(View.GONE);
                    }else {
                        binding.tvNoData.setVisibility(View.VISIBLE);
                    }

                }, e-> {
                    binding.pbLoading.setVisibility(View.GONE);
                    Toast.makeText(SearchActivity.this, "搜索接口出错", Toast.LENGTH_SHORT).show();
                });
    }



}