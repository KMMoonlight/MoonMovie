package com.kmmoonlight.moonvideo.page.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmmoonlight.moonvideo.R;
import com.kmmoonlight.moonvideo.page.VideoDetailActivity;
import com.kmmoonlight.moonvideo.repos.VideoRepo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoRvAdapter extends RecyclerView.Adapter<VideoRvAdapter.VideoViewHolder>{

    public List<VideoRepo> videoRepoList;
    public LayoutInflater layoutInflater;
    public Context context;

    public VideoRvAdapter(List<VideoRepo> videoRepoList, Context context) {
        this.videoRepoList = videoRepoList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.video_item_layout, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.tv_video_title.setText(videoRepoList.get(position).getVideoName());
        Glide.with(context)
                .load(videoRepoList.get(position).getVideoImage())
                .fitCenter()
                .into(holder.iv_video);

        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到详情页
                Intent intent = new Intent(context, VideoDetailActivity.class);
                intent.putExtra("target_url", videoRepoList.get(position).getVideoUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoRepoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView tv_video_title;
        ImageView iv_video;
        RelativeLayout rl_parent;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_video_title = itemView.findViewById(R.id.tv_video_title);
            iv_video = itemView.findViewById(R.id.iv_video);
            rl_parent = itemView.findViewById(R.id.rl_parent);
        }

    }



}
