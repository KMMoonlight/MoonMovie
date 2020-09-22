package com.kmmoonlight.moonvideo.page.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kmmoonlight.moonvideo.R;
import com.kmmoonlight.moonvideo.repos.PlayListRepo;

import java.util.List;

public class VideoDetailAdapter extends BaseAdapter {

    private Context context;
    private List<PlayListRepo> playListRepoList;
    private LayoutInflater layoutInflater;

    public VideoDetailAdapter(Context context, List<PlayListRepo> playListRepoList) {
        this.context = context;
        this.playListRepoList = playListRepoList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return playListRepoList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        VideoDetailViewHolder videoDetailViewHolder;

        if (view == null) {
            videoDetailViewHolder = new VideoDetailViewHolder();

            view = layoutInflater.inflate(R.layout.video_detail_item_layout, null);

            videoDetailViewHolder.tv_video_title = view.findViewById(R.id.tv_video_title);
            view.setTag(videoDetailViewHolder);
        }else {
            videoDetailViewHolder = (VideoDetailViewHolder) view.getTag();
        }

        videoDetailViewHolder.tv_video_title.setText(playListRepoList.get(i).getVideoName());

        return view;
    }

    static class VideoDetailViewHolder {

        TextView tv_video_title;

    }
}
