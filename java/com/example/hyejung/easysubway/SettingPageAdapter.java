package com.example.hyejung.easysubway;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SettingPageAdapter extends PagerAdapter {

        LayoutInflater inflater;

    public SettingPageAdapter(LayoutInflater inflater) {
            // TODO Auto-generated constructor stub
            this.inflater=inflater;
            }

    @Override
    public int getCount() {
            // TODO Auto-generated method stub
            return 3;
            }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub

            View view=null;

            view= inflater.inflate(R.layout.viewpager_childview, null);
            ImageView img= (ImageView)view.findViewById(R.id.img_viewpager_childimage);
            img.setImageResource(R.drawable.overlay01+position);
            container.addView(view);
            return view;
            }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
            }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
            // TODO Auto-generated method stub
            return v==obj;
            }

//        @Override
//        public void notifyDataSetChanged() {
//   //         setImageStep();
//            super.notifyDataSetChanged();
//
//        }
//
//        @Override
//        public int getItemPosition(Object item) {
//            return POSITION_NONE;
//        }

}

