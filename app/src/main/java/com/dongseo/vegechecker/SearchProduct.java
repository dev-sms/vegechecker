package com.dongseo.vegechecker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class SearchProduct extends AppCompatActivity {

    String barcodeData;
    String prdlst_nm;
    String prdlstNm;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog = new LoadingDialog(SearchProduct.this);
                        loadingDialog.show();
                    }
                });
                barcodeData = getBarcodeData();
                Log.d("scantest", barcodeData);
                Intent intent = new Intent();
                intent.putExtra("product", prdlstNm);
                intent.putExtra("prdlst_nm", prdlst_nm);
                if (barcodeData.equals("")){
                    setResult(RESULT_CANCELED);
                }
                else
                    setResult(RESULT_OK, intent);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismiss();
                        finish();
                    }
                });
            }
        }).start();
    }

    String getBarcodeData() {
        String barcodeNum;
        String serviceKey;

        barcodeNum = getIntent().getStringExtra("barcode");
        serviceKey = BuildConfig.BARCODE_API_KEY;

        StringBuffer buffer = new StringBuffer();

        String queryUrl = "http://openapi.foodsafetykorea.go.kr/api/" + serviceKey + "/I2570/xml/1/5/BRCD_NO=" + barcodeNum;

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;
            int eventType = xpp.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tag = xpp.getName();
                    if (tag.equals("PRDLST_REPORT_NO")){
                        xpp.next();
                        prdlst_nm = xpp.getText();
                        buffer.append("품목보고번호 : " + xpp.getText());
                        buffer.append("\n\n");
                    }
                    else if (tag.equals("PRDT_NM")){
                        xpp.next();
                        prdlstNm = xpp.getText();
                        buffer.append("제품명 : " + xpp.getText());
                        buffer.append("\n\n");
                    }
                }
                eventType = xpp.next();
            }
            is.close();
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
        return buffer.toString(); // 문자열 객체 반환
    }
}
