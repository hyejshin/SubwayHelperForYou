package com.example.hyejung.easysubway;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SettingPage extends Activity {

    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);
        pager= (ViewPager)findViewById(R.id.pager);
        SettingPageAdapter adapter= new SettingPageAdapter(getLayoutInflater());
//        pager.getAdapter().notifyDataSetChanged();
        pager.setAdapter(adapter);

        Button btn_previous = (Button)findViewById(R.id.btn_previous);
        Button btn = (Button)findViewById(R.id.gotosetting);
        btn_previous.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
    }

    public void mOnClick(View v){

        int position, position2;

        position = pager.getCurrentItem();
        position2 = position;

        switch( v.getId() ){
            case R.id.btn_previous:
                pager.setCurrentItem(position-1,true);
                position2 = position -1;
                break;

            case R.id.btn_next:
                pager.setCurrentItem(position+1,true);
                position2 = position + 1;
                break;
        }

        if(position2 < 0)
            position2 = 0;
        if(position2 > 2)
            position2 = 2;

        ImageView image1 = (ImageView)findViewById(R.id.firststep);
        ImageView image2 = (ImageView)findViewById(R.id.secondstep);
        ImageView image3 = (ImageView)findViewById(R.id.thirdstep);
        Button btn_previous = (Button)findViewById(R.id.btn_previous);
        Button btn_next = (Button)findViewById(R.id.btn_next);
        Button btn = (Button)findViewById(R.id.gotosetting);

        if(position2 == 0) {
            image1.setImageResource(R.drawable.white_dot);
            image2.setImageResource(R.drawable.gray_dot);
            image3.setImageResource(R.drawable.gray_dot);
            btn_previous.setVisibility(View.INVISIBLE);
            btn_next.setVisibility(View.VISIBLE);
            btn.setVisibility(View.INVISIBLE);
        }
        else if(position2 == 1){
            image1.setImageResource(R.drawable.gray_dot);
            image2.setImageResource(R.drawable.white_dot);
            image3.setImageResource(R.drawable.gray_dot);
            btn_previous.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.VISIBLE);
            btn.setVisibility(View.INVISIBLE);
        } else {
            image1.setImageResource(R.drawable.gray_dot);
            image2.setImageResource(R.drawable.gray_dot);
            image3.setImageResource(R.drawable.white_dot);
            btn_previous.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.INVISIBLE);
            btn.setVisibility(View.VISIBLE);
        }
    }

    public void setImageStep() {
        int position = pager.getCurrentItem();
        Toast.makeText(this,"포지션은 : " + position,Toast.LENGTH_SHORT).show();
//        if(position < 0)
//            position = 0;
//        if(position > 2)
//            position = 2;

        ImageView image1 = (ImageView)findViewById(R.id.firststep);
        ImageView image2 = (ImageView)findViewById(R.id.secondstep);
        ImageView image3 = (ImageView)findViewById(R.id.thirdstep);

        if(position == 0) {
            image1.setImageResource(R.drawable.white_dot);
            image2.setImageResource(R.drawable.gray_dot);
            image3.setImageResource(R.drawable.gray_dot);
        }
        else if(position == 1){
            image1.setImageResource(R.drawable.gray_dot);
            image2.setImageResource(R.drawable.white_dot);
            image3.setImageResource(R.drawable.gray_dot);
        } else {
            image1.setImageResource(R.drawable.gray_dot);
            image2.setImageResource(R.drawable.gray_dot);
            image3.setImageResource(R.drawable.white_dot);
        }
    }

    public void moveToSetting(View v){
        Intent it = new Intent(SettingPage.this, SettingPopup.class);
        startActivity(it);
        finish();
    }

//    public class SettingPageAdapter extends PagerAdapter {
//
//        LayoutInflater inflater;
//
//        public SettingPageAdapter(LayoutInflater inflater) {
//            // TODO Auto-generated constructor stub
//            this.inflater=inflater;
//        }
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return 3;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            // TODO Auto-generated method stub
//
//            View view=null;
//
//            view= inflater.inflate(R.layout.viewpager_childview, null);
//            ImageView img= (ImageView)view.findViewById(R.id.img_viewpager_childimage);
//            img.setImageResource(R.drawable.overlay01 + position);
//            setImageStep();
//            container.addView(view);
//            return view;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            // TODO Auto-generated method stub
//            container.removeView((View) object);
//        }
//
//        @Override
//        public boolean isViewFromObject(View v, Object obj) {
//            // TODO Auto-generated method stub
//            return v==obj;
//        }
//
////        @Override
////        public void notifyDataSetChanged() {
////   //         setImageStep();
////            super.notifyDataSetChanged();
////
////        }
////
////        @Override
////        public int getItemPosition(Object item) {
////            return POSITION_NONE;
////        }
//    }
}
