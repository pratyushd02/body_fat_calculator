package com.example.bodyfatcal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import com.example.bodyfatcal.databinding.ActivityAdpageBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    float h;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView textView;
    EditText editText;
    EditText height;
    EditText weight;
    EditText age;
    Toast toast;
    Button button;

    private static final String TOAST_TEXT = " res/values/strings.xml";
    private static final String TAG = "";

    private static final int START_LEVEL = 1;
    private int mLevel;
    private Button mNextLevelButton;
    private InterstitialAd mInterstitialAd;
    private TextView mLevelTextView;
    private ActivityAdpageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // Load the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        loadInterstitialAd();

        // Create the next level button, which tries to show an interstitial when clicked.
        mNextLevelButton = binding.nextLevelButton;
        mNextLevelButton.setEnabled(false);
        mNextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitial();
            }
        });

        // Create the text view to show the level number.
        mLevelTextView = binding.level;
        mLevel = START_LEVEL;

        // Toasts the test ad message on the screen. Remove this after defining your own ad unit ID.

        setContentView(R.layout.activity_main);

        radioGroup = findViewById(R.id.radioGroup);
        textView  = findViewById(R.id.output);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        age = (EditText) findViewById(R.id.age);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.interstitial_ad_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mNextLevelButton.setEnabled(true);

                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Log.d(TAG, "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;
                                        Log.d(TAG, "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d(TAG, "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                        mNextLevelButton.setEnabled(true);
                    }
                });
    }

    private void showInterstitial() {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            goToNextLevel();
        }
    }

    private void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
        mLevelTextView.setText("Level " + (++mLevel));
        if (mInterstitialAd == null) {
            loadInterstitialAd();
        }
    }

    public void calculate(View v){
        showInterstitial();

        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);



        if(selectedId==-1){
            Toast.makeText(MainActivity.this,"Please Select Gender", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(height.getText().toString())){
            Toast.makeText(MainActivity.this,"Please Enter Height in cms", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(weight.getText().toString())){
            Toast.makeText(MainActivity.this,"Please Enter Weight in kg", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(age.getText().toString())){
            Toast.makeText(MainActivity.this,"Please Enter Your Age", Toast.LENGTH_SHORT).show();
        }
        else{
            Float h = Float.valueOf(height.getText().toString());
            Float w = Float.valueOf(weight.getText().toString());
            Integer a = Integer.valueOf(age.getText().toString());
            double pounds = w * 2.20462;
            double inches = h * 0.393701;
            double bmi = (pounds/(inches*inches)) * 703;
            String gender = (String) radioButton.getText();
            if(gender.equals("Female")){
                double bfi = (1.20 * bmi) + (0.23 * a) - 5.4;
                String bfp = String.format("%.2f",bfi);
                String state = "";
                if(a<=40){
                    if(bfi<21){
                        state="UnderFat";
                    }
                    else if(bfi<33){
                        state="Healthy";
                    }
                    else if(bfi<39){
                        state="Overweight";
                    }
                    else{
                        state="Obese";
                    }
                }
                else if(a<=60){
                    if(bfi<23){
                        state="UnderFat";
                    }
                    else if(bfi<35){
                        state="Healthy";
                    }
                    else if(bfi<40){
                        state="Overweight";
                    }
                    else{
                        state="Obese";
                    }

                }
                else{
                    if(bfi<24){
                        state="UnderFat";
                    }
                    else if(bfi<36){
                        state="Healthy";
                    }
                    else if(bfi<42){
                        state="Overweight";
                    }
                    else{
                        state="Obese";
                    }
                }

                textView.setText("\n \n Your Body Fat Percentage is "+bfp+"% \n \n"+"You fall under "+state+" Category");
            }
            else{
                double bfi = (1.20 * bmi) + (0.23 * a) - 16.2;
                String bfp = String.format("%.2f",bfi);
                String state = "";
                if(a<=40){
                    if(bfi<8){
                        state="UnderFat";
                    }
                    else if(bfi<19){
                        state="Healthy";
                    }
                    else if(bfi<25){
                        state="Overweight";
                    }
                    else{
                        state="Obese";
                    }
                }
                else if(a<=60){
                    if(bfi<11){
                        state="UnderFat";
                    }
                    else if(bfi<22){
                        state="Healthy";
                    }
                    else if(bfi<27){
                        state="Overweight";
                    }
                    else{
                        state="Obese";
                    }

                }
                else{
                    if(bfi<13){
                        state="UnderFat";
                    }
                    else if(bfi<25){
                        state="Healthy";
                    }
                    else if(bfi<30){
                        state="Overweight";
                    }
                    else{
                        state="Obese";
                    }
                }

                textView.setText("\n \n Your Body Fat Percentage is "+bfp+"% \n \n"+"You fall under "+state+" Category");
            }
        }

    }

}