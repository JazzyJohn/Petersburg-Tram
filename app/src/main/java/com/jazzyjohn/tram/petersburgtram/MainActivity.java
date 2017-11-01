package com.jazzyjohn.tram.petersburgtram;

import android.app.Dialog;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Map<Integer, String> data = new Hashtable<>();

    TramColor firstColor = TramColor.Invalid ;
    TramColor secondColor = TramColor.Invalid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            loadData();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        MobileAds.initialize(this, getResources().getString(R.string.YOUR_ADMOB_APP_ID));

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("4E80F9E3B95A8DAEE0FD62FC3DB87649")
                .build();
        adView.loadAd(adRequest);

    }

    private void loadData() throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        Resources res = getResources();
        InputStream in_s= res.openRawResource(R.raw.data);

        Map<Integer, StringBuilder> builders = new Hashtable<>();

        xpp.setInput(in_s, null);
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType )            {
                case  XmlPullParser.START_TAG:
                    if(xpp.getName().equals(getString(R.string.tram)))
                    {
                        xpp.nextTag();
                        String number = xpp.nextText();
                        xpp.nextTag();
                        Integer color1 = Integer.valueOf(xpp.nextText());
                        xpp.nextTag();
                        Integer color2 = Integer.valueOf(xpp.nextText());
                        Integer code = getTramCode(color1,color2);
                        StringBuilder numbers  = builders.get(code);
                        if(numbers== null)
                        {
                            numbers = new StringBuilder(number);
                        }
                        else
                        {
                            numbers.append("\n");
                            numbers.append(number);
                        }

                        builders.put(code,numbers);
                    }
                    eventType = xpp.nextTag();
                    break;
                default:
                    eventType = xpp.next();
                    break;
            }
        }
        for ( Map.Entry<Integer, StringBuilder> entry : builders.entrySet() ) {
            data.put(entry.getKey(),entry.getValue().toString());
        }
    }


    public void onColor1Click(View view)
    {
        showColorDialog(1);
    }
    public void onColor2Click(View view)
    {
        showColorDialog(2);
    }

    void showColorDialog(final int number)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.color_dialog);


        ImageButton btWhite = dialog.findViewById(R.id.whiteBtn);
        btWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSet(number,TramColor.White);
                dialog.cancel();
            }
        });
        ImageButton btBlue = dialog.findViewById(R.id.blueBtn);
        btBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSet(number,TramColor.Blue);
                dialog.cancel();
            }
        });
        ImageButton btGreen = dialog.findViewById(R.id.greenBtn);
        btGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSet(number,TramColor.Green);
                dialog.cancel();
            }
        });
        ImageButton btYellow = dialog.findViewById(R.id.yellowBtn);
        btYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSet(number,TramColor.Yellow);
                dialog.cancel();
            }
        });
        ImageButton btRed = dialog.findViewById(R.id.redBtn);
        btRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorSet(number,TramColor.Red);
                dialog.cancel();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    private void colorSet(int number, TramColor color) {
        if(number==1)
        {
            firstColor = color;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((ImageButton)findViewById(R.id.color1)).setImageDrawable(new ColorDrawable(getResources().getColor(android.R.color.white, getTheme())));
            }
            else {
                ((ImageButton) findViewById(R.id.color1)).setImageDrawable(new ColorDrawable(getResources().getColor(TramColor.toColor(color))));
            }
        }
        else
        {
            secondColor = color;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((ImageButton)findViewById(R.id.color2)).setImageDrawable(new ColorDrawable(getResources().getColor(android.R.color.white, getTheme())));
            }
            else {
                ((ImageButton) findViewById(R.id.color2)).setImageDrawable(new ColorDrawable(getResources().getColor(TramColor.toColor(color))));
            }
        }
        checkTram();
    }
    private void checkTram()
    {
        if(firstColor != TramColor.Invalid && secondColor != TramColor.Invalid)
        {
            int tramCod= getTramCode(TramColor.toInt(firstColor), TramColor.toInt(secondColor));

            String text = data.get(tramCod);
            if(text == null)
            {
                text = getResources().getString(R.string.NoTram);
                findViewById(R.id.availabeRoutesLabel).setVisibility(View.GONE);
            }
            else
            {
                findViewById(R.id.availabeRoutesLabel).setVisibility(View.VISIBLE);
            }
            TextView routes = findViewById(R.id.availableRoute);
            routes.setText(text);
        }
    }
    private int getTramCode(int a, int b)
    {
        return a * 10 +b;
    }
}
