package com.kmmoonlight.moonvideo.utils;

import android.util.Log;

import com.kmmoonlight.moonvideo.repos.PlayListRepo;
import com.kmmoonlight.moonvideo.repos.VideoRepo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class HtmlParser {


    private static Element parseDiv(String content,int index) {

        Document doc = Jsoup.parse(content);

        Elements elementsByClass = doc.getElementsByClass("index-area");

        switch (index) {

            case 1:
                return elementsByClass.get(2);

            case 2:
                return elementsByClass.get(4);


            case 3:
                return elementsByClass.get(5);

            case 4:
            default:
                return elementsByClass.get(6);
        }
    }

    private static List<VideoRepo> parseVideo(Element element) {
        List<VideoRepo> videoRepos = new ArrayList<>();
        Elements qcontainer = element.getElementsByClass("qcontainer");

        for (Element container : qcontainer) {
            Element element1 = container.getElementsByClass("face front").get(0);
            Element element2 = element1.getElementsByClass("link-hover").get(0);
            String href = element2.attr("href");
            String title = element2.attr("title");
            String img = element2.getElementsByTag("img").get(0).attr("data-original");
            VideoRepo videoRepo = new VideoRepo();
            videoRepo.setVideoUrl(href);
            videoRepo.setVideoName(title);
            videoRepo.setVideoImage(img);
            videoRepos.add(videoRepo);
        }
        return videoRepos;
    }

    //解析首页的电影
    public static List<VideoRepo> getHomePageMovieList(String htmlContent) {
        Element movieElement = parseDiv(htmlContent, 2);
        return parseVideo(movieElement);
    }

    //解析首页电视剧
    public static List<VideoRepo> getHomePageVideoList(String htmlContent) {
        Element videoElement = parseDiv(htmlContent, 1);
        return parseVideo(videoElement);
    }

    //解析首页动漫
    public static List<VideoRepo> getHomePageCartoonList(String htmlContent) {
        Element cartoonElement = parseDiv(htmlContent, 4);
        return parseVideo(cartoonElement);
    }

    //解析首页综艺
    public static List<VideoRepo> getHomePageShowList(String htmlContent) {
        Element showElement = parseDiv(htmlContent, 3);
        return parseVideo(showElement);
    }


    //解析剧集列表
    public static List<PlayListRepo> getPlayList(String htmlContent) {

        List<PlayListRepo> playListRepoList = new ArrayList<>();

        Document doc = Jsoup.parse(htmlContent);

        Element element = doc.getElementsByClass("playlist").get(0);

        Elements videos = element.getElementsByTag("a");

        for (Element item : videos) {
            String videoTitle = item.text();
            String videoUrl = item.attr("href");

            PlayListRepo playListRepo = new PlayListRepo();
            playListRepo.setVideoName(videoTitle);
            playListRepo.setVideoUrl(videoUrl);
            playListRepoList.add(playListRepo);
        }


        //获取播放地址
        Element main = doc.getElementsByClass("main").get(0);
        Element script = main.getElementsByTag("script").get(0);
        String s = script.html().split("mac_url=unescape\\('")[1].split("\\);")[0];

        String[] split = s.split("%24");

        List<String> addressList = new ArrayList<>();
        for (String it : split) {
            if (it.contains("http")) {
                addressList.add(it.split("m3u8")[0] + "m3u8");
            }
        }

        for (int i = 0; i < playListRepoList.size(); i++) {
            playListRepoList.get(i).setVideoUrl(addressList.get(i));
        }

        return playListRepoList;
    }


    //解析播放地址
    public static List<VideoRepo> getSearchResult(String htmlContent) {

        List<VideoRepo> videoRepoList = new ArrayList<>();

        Document doc = Jsoup.parse(htmlContent);

        Element element = doc.getElementsByClass("index-area").get(0);

        Elements resultElements = element.getElementsByClass("link-hover");

        if (resultElements.size() != 0) {

            for (Element result: resultElements) {
                String href = result.attr("href");
                String title = result.attr("title");
                String img = result.getElementsByTag("img").get(0).attr("data-original");
                VideoRepo videoRepo = new VideoRepo();
                videoRepo.setVideoImage(img);
                videoRepo.setVideoUrl(href);
                videoRepo.setVideoName(title);
                videoRepoList.add(videoRepo);
            }

        }

        return videoRepoList;
    }

}
