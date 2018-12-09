package com.simplistic.simplistic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;

    }

    //Arrays
    public String[] slide_titles = {"WELCOME", "EDIT YOUR TASKS"};

    public String[] slide_descriptions = {"Simplistic is a simple to-do list application that allows you to create/delete/search for a task as well as sort based on priority.",
                                            "Click on a task item in the home page to edit. \nHold task item down to delete."};

    @Override
    public int getCount() {
        return slide_titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        TextView slideTitleView = view.findViewById(R.id.textViewTitle);
        TextView slideDescriptionView = view.findViewById(R.id.textViewDescription);

        slideTitleView.setText(slide_titles[position]);
        slideDescriptionView.setText(slide_descriptions[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
