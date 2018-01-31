package demo.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;
import java.util.Set;

/**
 * Created by sannelson on 1/31/2018.
 */

public class PayPalWebView extends AppCompatActivity{
    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        String url = getIntent().getStringExtra("URL");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        webview = (WebView) findViewById(R.id.webViewPayPal);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("URL URL", url);
                if(url.contains("demo.sample.com")){
                    Uri _url = Uri.parse(url);
                    Intent intent = new Intent(PayPalWebView.this, ProcessComplete.class);
                    intent.putExtra("ID", _url.getQueryParameter("id"));
                    intent.putExtra("STATUS", _url.getQueryParameter("status"));
                    intent.putExtra("PAYERID",_url.getQueryParameter("payerId"));

                    startActivity(intent);
                    PayPalWebView.this.finish();
                }
                return false;
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(url);
    }

}
