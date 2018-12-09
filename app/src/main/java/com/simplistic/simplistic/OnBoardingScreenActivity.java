package com.simplistic.simplistic;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class OnBoardingScreenActivity extends AppCompatActivity {
    private ViewPager vPager;
    private SliderAdapter sliderAdapter;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);

        vPager = findViewById(R.id.ViewPager);
        sliderAdapter = new SliderAdapter(this);
        vPager.setAdapter(sliderAdapter);

        buttonNext = findViewById(R.id.buttonNext);
        vPager.addOnPageChangeListener(pagerListener);
    }

    ViewPager.OnPageChangeListener pagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            if(i == 0) {
                buttonNext.setEnabled(true);
                buttonNext.setText("NEXT");
            }
            else if (i == 1) {
                buttonNext.setEnabled(true);
                buttonNext.setText("FINISH");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void onClickButtonNext(View view) {
        if(buttonNext.getText().equals("FINISH")){
            Intent intent = new Intent(this, com.simplistic.simplistic.MainActivity.class);
            startActivity(intent);
        }
        else if (buttonNext.getText().equals("NEXT"))
            vPager.setCurrentItem(vPager.getCurrentItem() + 1);
    }
}
