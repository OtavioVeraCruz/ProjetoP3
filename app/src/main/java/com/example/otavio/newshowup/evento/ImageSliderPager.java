package com.example.otavio.newshowup.evento;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.otavio.newshowup.R;
import com.example.otavio.newshowup.utils.LoadImg;

import java.util.ArrayList;

public class ImageSliderPager extends PagerAdapter
{
    private Context context;
    private ArrayList<String>fotos;

    ImageSliderPager(@NonNull Context context, ArrayList<String> fotos) {
        this.context=context;
        this.fotos=fotos;
    }

    @Override
    public int getCount() {
        return fotos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View v= layoutInflater.inflate(R.layout.custom_image,container,false);
        ImageView imageView=v.findViewById(R.id.img_slider);

        LoadImg.loadSimpleImage(fotos.get(position),imageView,context);
        ViewPager viewPager=(ViewPager)container;
        viewPager.addView(v,0);

        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp=(ViewPager)container;
        View v=(View)object;
        vp.removeView(v);
    }
}
