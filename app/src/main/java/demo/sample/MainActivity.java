package demo.sample;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Properties;

import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnCart = (Button) findViewById(R.id.cartbutton);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    JSONObject productJson = new JSONObject();
                    productJson.put("description","This is camera");
                    productJson.put("shipping","5");
                    productJson.put("tax","2");
                    productJson.put("shipping_discount","-3");
                    productJson.put("total","17");
                    productJson.put("currency","USD");
                    productJson.put("intent","sale");
                    productJson.put("subtotal","10.00");
                    productJson.put("name","Camera");
                    productJson.put("price","10.00");
                    productJson.put("quantity","1");
                    productJson.put("handling_fee","1");
                    productJson.put("insurance","2");
                    productJson.put("customFlag","false");
                    //custom flag --- not a part of paypal payload
                    productJson.put("webview","true");
                    showLoader(true); // set loader visible

                    // make api call here ....

                    AsyncHttpClient client = new AsyncHttpClient();
                    StringEntity requestData = new StringEntity(productJson.toString());
                    Properties properties = new Properties();
                    AssetManager assetManager = getApplicationContext().getAssets();
                    InputStream inputStream = assetManager.open("app.properties");
                    properties.load(inputStream);
                    String url = properties.get("createPayments").toString();
                    client.post(getApplicationContext(), url, requestData, "application/json", new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            Toast.makeText(getApplicationContext(), "Got response from server" , Toast.LENGTH_SHORT).show();

                            try{
                                String url = Utility.getUrlFromJSONArray(response);
                                if(!url.equals(null)) {
                                    showLoader(false);
                                        Intent intent = new Intent(MainActivity.this, PayPalWebView.class);
                                        intent.putExtra("URL", url);
                                        startActivity(intent);
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showLoader(Boolean setVisibilty) {
        ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
        int colorCodeDark = Color.parseColor("#253B80");
        progress.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));
        if(setVisibilty) {
            progress.setVisibility(View.VISIBLE);
        }else {
            progress.setVisibility(View.INVISIBLE);
        }
    }


}
