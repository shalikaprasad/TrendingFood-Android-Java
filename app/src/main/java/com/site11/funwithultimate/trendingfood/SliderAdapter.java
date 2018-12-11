package com.site11.funwithultimate.trendingfood;

import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slideimages = {
            R.drawable.farmersc,
            R.drawable.sellsc,
            R.drawable.managesc
    };
    public String[] slide_heading = {
          "ගොවියන්",
          "වෙළඳකරුවන්",
          "පාරිභෝගිකයන්"
    };

    public String[] slide_doc = {

            "ගොවියන්ගේ අස්වනු සදහා සාධරණ මිලක් ලබා දී, සාධාරණ වෙළඳකරුවන් පහසුවෙන් සොයාගැනීමටත්, ඇණවුම " +
                "වලට අනුව වගා කිරීමට හැකි වීමත්, ඔබගේ වගා ගැටලු සඳහා ඉක්මන් විසදුම් ලබා ගැනීමටත්," +
                "සහා ඔබ අවට සිටිනා වෙළඳකරුවන් පහසුවෙන් සොයාගැනීමටත් මෙය උපකාරී වේ. ",


            "වෙළඳකරුවන්ගේ අස්වනු සදහා සාධරණ මිලක් ලබා දී,පාරිභෝගිකයන් පහසුවෙන් සොයාගැනීමටත්, ඇණවුම් " +
                    "වලට අනුව අස්වනු මීලදී ගැනීමට හැකි වීමත්, ඔබගේ වෙළඳ ගැටලු සඳහා ඉක්මන් විසදුම් ලබා ගැනීමටත්, "+
                    "සහා ඔබ අවට සිටිනා පාරිභෝගිකයන් පහසුවෙන් සොයාගැනීමටත් මෙය උපකාරී වේ. ",

            "පාරිභෝගිකයන් අස්වනු සදහා සාධරණ මිලක් ලබා දී, සාධාරණ වෙළඳකරුවන් පහසුවෙන් සොයාගැනීමටත්, ඇණවුම් " +
                    "කිරීමට හැකි වීමත්, ඔබගේ ගැටලු සඳහා ඉක්මන් විසදුම් ලබා ගැනීමටත්, "+
                    "සහා ඔබ අවට සිටිනා වෙළඳකරුවන් පහසුවෙන් සොයාගැනීමටත් මෙය උපකාරී වේ. "
    };
    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.side_layout,container,false);

        ImageView slideImage = (ImageView) view.findViewById(R.id.headingimg);
        TextView slideText= (TextView) view.findViewById(R.id.headingtxt);
        TextView slidedoc = (TextView) view.findViewById(R.id.paratxt);

        slideImage.setImageResource(slideimages[position]);
        slideText.setText(slide_heading[position]);
        slidedoc.setText(slide_doc[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
