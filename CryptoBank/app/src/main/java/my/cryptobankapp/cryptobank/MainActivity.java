package my.cryptobankapp.cryptobank;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import com.google.android.exoplayer2.*;


public class MainActivity extends AppCompatActivity implements ExoPlayer.EventListener {


    private String vidAddress = "http://yuanpay.online/crptbankpl/video.mp4.m3u8";
    private RelativeLayout activity_main;
    private WebView webView;

    private SimpleExoPlayer player;
    private PlayerView playerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);

        activity_main = findViewById(R.id.main);

        playerView = findViewById(R.id.my_video);
        play();

        FirebaseMessaging.getInstance().subscribeToTopic("noregistration");

        final CountryCodePicker ccp = (CountryCodePicker)  findViewById(R.id.ccp);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://ip-api.com/json/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ccp.setCountryForNameCode(jsonObject.get("countryCode").toString());
                        } catch (JSONException ignored) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void play() {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView = findViewById(R.id.my_video);
        playerView.setPlayer(player);
        Uri uri = Uri.parse(vidAddress);
        DataSource.Factory dataSourceFactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(this, "app-name"));
// Create a HLS media source pointing to a playlist uri.
        HlsMediaSource hlsMediaSource =
                new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);


        player.prepare(hlsMediaSource);
        player.setPlayWhenReady(true);
        playerView.setPlayer(player);
    }



    public String sendUserDataToCRM(String first_name, String last_name, String email, String phone, String country) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String params = String.format("first_name=%s&last_name=%s&email=%s&phone=%s&country=%s&site=andcrptdev&landing=5fda23e4977b3b0e642acd58", first_name, last_name, email, phone, country);
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://getyourapi.site/api/leads/google")
                .method("POST", body)
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        JSONObject Jobject = new JSONObject(jsonData);
        JSONObject Jobject1 = new JSONObject(Jobject.getString("data"));
        try{
            return Jobject1.getString("redirectUrl");
        }
        catch (Exception e){
            return "null";
        }

    }


    public void newScreenFunc(View view) {
        RelativeLayout videoplayer = findViewById(R.id.vidoplayer);
        activity_main.setBackground(ContextCompat.getDrawable(this, R.drawable.back_scr2));

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = 335;
        layoutParams.leftMargin = 20;
        layoutParams.rightMargin = 20;

        videoplayer.setLayoutParams(layoutParams);
        player.pause();


        RelativeLayout first = findViewById(R.id.firstScreen);
        first.setVisibility(View.INVISIBLE);

        RelativeLayout block_scr2 = findViewById(R.id.block_scr2);
        block_scr2.setVisibility(View.VISIBLE);

        ImageView text2_scr2 = findViewById(R.id.text2_scr2);
        text2_scr2.setVisibility(View.VISIBLE);

        Button button_scr_2 = findViewById(R.id.button_scr_2);
        button_scr_2.setVisibility(View.VISIBLE);

        RelativeLayout second_screen = findViewById(R.id.secondScreen);
        second_screen.setVisibility(View.VISIBLE);

        EditText input1 = findViewById(R.id.input1);
        EditText input2 = findViewById(R.id.input2);
        EditText input3 = findViewById(R.id.input3);
        EditText input4 = findViewById(R.id.input4);

        input1.setVisibility(View.VISIBLE);
        input2.setVisibility(View.VISIBLE);
        input3.setVisibility(View.VISIBLE);
        input4.setVisibility(View.VISIBLE);

        CountryCodePicker ccp = (CountryCodePicker)  findViewById(R.id.ccp);
        ccp.setVisibility(View.VISIBLE);
    }

    public void regFinalFunc(View view) throws IOException, JSONException {

        if(check()){
        activity_main.setBackground(ContextCompat.getDrawable(this, R.drawable.third_screen));
            RelativeLayout block_scr2 = findViewById(R.id.block_scr2);
            block_scr2.setVisibility(View.INVISIBLE);

            ImageView text2_scr2 = findViewById(R.id.text2_scr2);
            text2_scr2.setVisibility(View.INVISIBLE);

            Button button_scr_2 = findViewById(R.id.button_scr_2);
            button_scr_2.setVisibility(View.INVISIBLE);

            RelativeLayout second_screen = findViewById(R.id.secondScreen);
            second_screen.setVisibility(View.INVISIBLE);
            playerView.setVisibility(View.INVISIBLE);

            final EditText input1 = findViewById(R.id.input1);
            final EditText input2 = findViewById(R.id.input2);
            final EditText input3 = findViewById(R.id.input3);
            final EditText input4 = findViewById(R.id.input4);

            input1.setVisibility(View.INVISIBLE);
            input2.setVisibility(View.INVISIBLE);
            input3.setVisibility(View.INVISIBLE);
            input4.setVisibility(View.INVISIBLE);

            final CountryCodePicker ccp = (CountryCodePicker)  findViewById(R.id.ccp);
            ccp.setVisibility(View.INVISIBLE);

            Thread thread2 = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        String responce = sendUserDataToCRM(input1.getText().toString().trim(),input2.getText().toString().trim(),
                                input3.getText().toString().trim(),
                                ccp.getSelectedCountryCode().toString()+input4.getText().toString().trim(), ccp.getSelectedCountryNameCode());

                        if(!responce.equals("null")){
                            goToVebView(responce);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread2.start();
            FirebaseMessaging.getInstance().unsubscribeFromTopic("noregistration");
        }

    }

    public boolean check() throws IOException, JSONException {
        final boolean[] ch = {true};
        EditText input1 = findViewById(R.id.input1);
        EditText input2 = findViewById(R.id.input2);
        EditText input3 = findViewById(R.id.input3);
        final EditText input4 = findViewById(R.id.input4);

        if(input1.getText().toString().isEmpty()){
            input1.setError("Wpisz imię");
            ch[0] =  false;
        }
        if(input2.getText().toString().isEmpty()){
            input2.setError("Wpisz nazwisko");
            ch[0] =  false;
        }
        if(input3.getText().toString().isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(input3.getText().toString()).matches()){
            input3.setError("Wpisz swój e-mail");
            ch[0] =  false;
        }
        if(input4.getText().toString().isEmpty()){
            input4.setError("Wpisz swój numer telefonu");
            ch[0] =  false;
        }

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    CountryCodePicker ccp = (CountryCodePicker)  findViewById(R.id.ccp);
                    if(!validatePhone(ccp.getSelectedCountryCodeWithPlus().toString()+input4.getText().toString().trim())){
                        input4.setError("Numer telefonu nie został zweryfikowany");
                        ch[0] = false;
                        Log.i("Request", "ch: "+ch[0]);
                    }
                } catch (Exception e) {
                    Log.i("Request", "Error: "+e.getMessage());
                }
            }
        });

        thread.start();
        Log.i("Request", "ck1 "+ch[0]);

        return ch[0];
    }

    public boolean validatePhone(String phone) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String params = String.format("phone=%s", phone);
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),  params);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://getyourapi.site/api/phone/validate")
                .method("POST", body)
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        JSONObject Jobject = new JSONObject(jsonData);
        JSONObject Jobject1 = new JSONObject(Jobject.getString("data"));
        Log.i("Request", "Responce: "+Jobject + params);
        return Jobject1.getString("valid").equals("true");
    }

    public void goToVebView(String url){
        webView.setWebViewClient(new MyWebViewClient());
        webView.setVisibility(View.VISIBLE);
        // включаем поддержку JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        // указываем страницу загрузки
        webView.loadUrl(url);
    }

    private static class MyWebViewClient extends WebViewClient {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        // Для старых устройств
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}