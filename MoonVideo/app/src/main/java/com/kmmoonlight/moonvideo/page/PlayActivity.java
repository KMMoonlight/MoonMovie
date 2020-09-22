package com.kmmoonlight.moonvideo.page;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rxhttp.RxHttp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.kmmoonlight.moonvideo.databinding.ActivityPlayBinding;
import com.kmmoonlight.moonvideo.utils.FullScreenUtils;

import java.net.URLDecoder;

public class PlayActivity extends AppCompatActivity {

    private ActivityPlayBinding binding;

    private String targetUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreenUtils.fullScreen(this);
        binding = ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        targetUrl = intent.getStringExtra("target_url");


        if (!TextUtils.isEmpty(targetUrl)) {
            targetUrl = toURLDecoded(targetUrl);
            webInit();
            if (targetUrl.endsWith(".m3u8")) {
                doPlay();
            }else {

                if (targetUrl.endsWith("#2m3u8")) {
                    targetUrl = targetUrl.split("#2m3")[0];
                }
                binding.webView.loadUrl(targetUrl);
            }
        }
    }


    private void doPlay() {



        String docHtml = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=utf-8 />\n" +
                "    <title>Play</title>\n" +
                "\n" +
                "    <link href=\"https://cdn.bootcss.com/video.js/6.3.3/video-js.min.css\" rel=\"stylesheet\">\n" +
                "    <script src=\"https://cdn.bootcss.com/video.js/6.3.3/video.min.js\"></script>\n" +
                "    <script src=\"https://cdn.bootcss.com/videojs-contrib-hls/5.11.0/videojs-contrib-hls.js\"></script>\n" +
                "\n" +
                "    <style>\n" +
                "        .main {\n" +
                "            padding: 0;\n" +
                "            margin: 0;\n" +
                "            position: absolute;\n" +
                "            left: 0;\n" +
                "            top: 0;\n" +
                "            bottom: 0;\n" +
                "            right: 0;\n" +
                "            background: red;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "    <div class=\"main\">\n" +
                "        <video id=\"my_video_1\" class=\"video-js vjs-default-skin\" controls preload=\"auto\"\n" +
                "            style=\"width: 100%; height:100%; object-fit: fill\" data-setup='{}'>\n" +
                "            <source src=\"" + targetUrl + "\" type=\"application/x-mpegURL\">\n" +
                "        </video>\n" +
                "    </div>\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>";


        binding.webView.loadData(docHtml, "text/html", "utf-8");
    }

    private void webInit() {
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //缩放但是不显示缩放按钮
        webSettings.setSupportZoom(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setBlockNetworkImage(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);


        binding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                if (url.startsWith("http") || url.startsWith("https")) {
                    view.loadUrl(url);
                    return false;
                }
                return true;
            }

        });

        binding.webView.setWebChromeClient(new WebChromeClient(){

            private View myView = null;

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                ViewGroup parent = (ViewGroup) binding.webView.getParent();
                parent.removeView(binding.webView);
                binding.containerFull.addView(view);
                binding.containerFull.setVisibility(View.VISIBLE);
                myView = view;
                setFullScreen();
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();

                if (myView != null) {
                    binding.containerFull.removeAllViews();
                    binding.webViewWrapper.addView(binding.webView);
                    binding.containerFull.setVisibility(View.GONE);
                    quitFullScreen();
                    myView = null;
                }
            }
        });
    }


    @Override
    public void onBackPressed() {

        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    private String toURLDecoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLDecoder.decode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            localException.printStackTrace();
        }

        return "";

    }

    /**
     * 设置全屏
     */
    private void setFullScreen() {
        // 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 退出全屏
     */
    private void quitFullScreen() {
        // 声明当前屏幕状态的参数并获取
        final WindowManager.LayoutParams attrs = this.getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setAttributes(attrs);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

}