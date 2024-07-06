package com.haggai.forex;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private ProgressBar loadingProggresBar;
    private SwipeRefreshLayout swipeRefreshLayout1;
    private TextView aedTextView, btcTextView, cadTextView, cnyTextView, clpTextView, egpTextView, eurTextView, idrTextView, madTextView, sarTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.swipeRefreshLayout1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        swipeRefreshLayout1 = findViewById(R.id.swipeRefreshLayout1);
        aedTextView = findViewById(R.id.aedTextView);
        btcTextView = findViewById(R.id.btcTextView);
        cadTextView = findViewById(R.id.cadTextView);
        cnyTextView = findViewById(R.id.cnyTextView);
        clpTextView = findViewById(R.id.clpTextView);
        egpTextView = findViewById(R.id.egpTextView);
        eurTextView = findViewById(R.id.eurTextView);
        idrTextView = findViewById(R.id.idrTextView);
        madTextView = findViewById(R.id.madTextView);
        sarTextView = findViewById(R.id.sarTextView);
        loadingProggresBar = findViewById(R.id.loadingProgresBar);

        initSwipeRefreshLayout();
        initForex();

    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout1.setOnRefreshListener(()-> {
            initForex();

            swipeRefreshLayout1.setRefreshing(false);
        });
    }

    public String formatNumber(double number, String format){
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(number);
    }

    private void initForex() {
        loadingProggresBar.setVisibility(TextView.VISIBLE);

        String url = "https://openexchangerates.org/api/latest.json?app_id=9e3ca9dbf3ec40f19ea8d327cace6aad";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                RootModel rootModel = gson.fromJson(new String(responseBody), RootModel.class);
                RatesModel ratesModel = rootModel.getRatesModel();

                double aed  = ratesModel.getIDR() / ratesModel.getAED();
                double btc  = ratesModel.getIDR() / ratesModel.getBTC();
                double cad  = ratesModel.getIDR() / ratesModel.getCAD();
                double cny  = ratesModel.getIDR() / ratesModel.getCNY();
                double clp  = ratesModel.getIDR() / ratesModel.getCLP();
                double egp  = ratesModel.getIDR() / ratesModel.getEGP();
                double eur  = ratesModel.getIDR() / ratesModel.getEUR();
                double mad  = ratesModel.getIDR() / ratesModel.getMAD();
                double sar  = ratesModel.getIDR() / ratesModel.getSAR();
                double idr  = ratesModel.getIDR();

                aedTextView.setText(formatNumber(aed, "###,##0.##"));
                btcTextView.setText(formatNumber(btc, "###,##0.##"));
                cadTextView.setText(formatNumber(cad, "###,##0.##"));
                cnyTextView.setText(formatNumber(cny, "###,##0.##"));
                clpTextView.setText(formatNumber(clp, "###,##0.##"));
                egpTextView.setText(formatNumber(egp, "###,##0.##"));
                eurTextView.setText(formatNumber(eur, "###,##0.##"));
                madTextView.setText(formatNumber(mad, "###,##0.##"));
                sarTextView.setText(formatNumber(sar, "###,##0.##"));
                idrTextView.setText(formatNumber(idr, "###,##0.##"));

                loadingProggresBar.setVisibility(TextView.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}