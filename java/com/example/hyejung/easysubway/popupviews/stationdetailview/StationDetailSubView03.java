package com.example.hyejung.easysubway.popupviews.stationdetailview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.example.hyejung.easysubway.R;
import com.example.hyejung.easysubway.cellRow.CellStationDetailHeaderRow;
import com.example.hyejung.easysubway.cellRow.CellStationDetailRow01;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import java.util.ArrayList;
import java.util.Iterator;

public class StationDetailSubView03 extends RelativeLayout {
    AppDataManager appManager = null;
    ArrayList<Bundle> arrayList = null;
    int iStationCode = 0;
    ArrayAdapter<Bundle> listData = null;
    Context mContext = null;

    public class arrayListAdapter extends ArrayAdapter<Bundle> {
        Context context;

        public arrayListAdapter(Context context, int textViewResourceId, ArrayList<Bundle> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View cell = convertView;
            Bundle bundle = (Bundle) StationDetailSubView03.this.arrayList.get(position);
            if (bundle.getInt("cellType") == 1) {
                CellStationDetailHeaderRow rowView;
                if (cell == null || cell.getId() != R.layout.cell_station_detail_header_row) {
                    cell = LayoutInflater.from(this.context).inflate(R.layout.cell_station_detail_header_row, null);
                    rowView = new CellStationDetailHeaderRow(cell);
                    cell.setTag(rowView);
                    cell.setId(R.layout.cell_station_detail_header_row);
                } else {
                    rowView = (CellStationDetailHeaderRow) cell.getTag();
                }
                rowView.setTitle(bundle.getString("title"));
            } else {
                CellStationDetailRow01 rowView2;
                if (cell == null || cell.getId() != R.layout.cell_station_detail_row01) {
                    cell = LayoutInflater.from(this.context).inflate(R.layout.cell_station_detail_row01, null);
                    rowView2 = new CellStationDetailRow01(cell);
                    cell.setTag(rowView2);
                    cell.setId(R.layout.cell_station_detail_row01);
                } else {
                    rowView2 = (CellStationDetailRow01) cell.getTag();
                }
                rowView2.setCellData(bundle);
            }
            return cell;
        }
    }

    public StationDetailSubView03(Context context) {
        super(context);
        this.mContext = context;
        this.appManager = AppDataManager.shared();
        addView(LayoutInflater.from(context).inflate(R.layout.subview_station_detail_view02, null));
        initListView();
    }

    private void initListView() {
        this.arrayList = new ArrayList();
        this.listData = new arrayListAdapter(this.mContext, 0, this.arrayList);
        ((ListView) findViewById(R.id.listView)).setAdapter(this.listData);
    }

    public void setStationCode(int code) {
        if (this.iStationCode != code) {
            this.iStationCode = code;
            getStationData();
        }
    }

    private void getStationData() {
        this.arrayList.clear();
        ArrayList<Bundle> arr = this.appManager.getArrStationExitInfo(this.iStationCode);
        if (arr.size() > 0) {
            Bundle object = new Bundle();
            object.putInt("cellType", 1);
            object.putString("title", "\ucd9c \uad6c \uc815 \ubcf4");
            this.arrayList.add(object);
            Iterator it = arr.iterator();
            while (it.hasNext()) {
                Bundle bundle = (Bundle) it.next();
                object = new Bundle();
                object.putInt("cellType", 2);
                object.putString("title", bundle.getString("gateNumber") + "\ubc88\ucd9c\uad6c");
                object.putString("desc", bundle.getString("gateInfo"));
                this.arrayList.add(object);
            }
        }
        this.listData.notifyDataSetChanged();
        ((ListView) findViewById(R.id.listView)).setSelection(0);
    }
}
