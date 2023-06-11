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

public class GetRawMaterialActivity extends AppCompatActivity {

    String materialData;
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
                        loadingDialog = new LoadingDialog(GetRawMaterialActivity.this);
                        loadingDialog.show();
                    }
                });
                materialData = getMaterialData();
                Log.d("scantest", materialData);
                Intent intent = new Intent();
                intent.putExtra("material", materialData);
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

    String getMaterialData() {
        String prdlst_nm;
        String serviceKey;

        prdlst_nm = getIntent().getStringExtra("product");
        prdlst_nm = filterSearch(prdlst_nm);
        serviceKey = BuildConfig.HACCP_API_KEY;

        StringBuffer buffer = new StringBuffer();

        String queryUrl = "http://apis.data.go.kr/B553748/CertImgListService/getCertImgListService?ServiceKey=" + serviceKey + "&prdlstNm=" + prdlst_nm + "&numOfRows=1";

        Log.d("scantest", queryUrl);
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
                    if (tag.equals("rawmtrl")){
                        xpp.next();
                        buffer.append("원재료 : " + xpp.getText());
                    }
                    else if (tag.equals("allergy")){
                        xpp.next();
                        buffer.append("알러지 : " + xpp.getText());
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

    private String filterSearch(String prdlstnm){
        String[] temp = prdlstnm.split(" ");
        return temp[1];
    }
}
