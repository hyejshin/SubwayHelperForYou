package com.example.hyejung.easysubway.popupviews.stationdetailview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.hyejung.easysubway.cellRow.CellStationDetailHeaderRow;
import com.example.hyejung.easysubway.cellRow.CellStationDetailRow01;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import java.util.ArrayList;
import com.example.hyejung.easysubway.R;

public class StationDetailSubView02 extends RelativeLayout {
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
            Bundle bundle = (Bundle) StationDetailSubView02.this.arrayList.get(position);
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

    public StationDetailSubView02(Context context) {
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
        ArrayList<Bundle> arr = this.appManager.getArrStationAddOnInfo(this.iStationCode);
        if (arr.size() > 0) {
            setStationData((Bundle) arr.get(0));
        }
        this.listData.notifyDataSetChanged();
        ((ListView) findViewById(R.id.listView)).setSelection(0);
    }

    private void setStationData(Bundle bundle) {
        if (bundle != null) {
            Bundle object;
            String strData = bundle.getString("telNumber");
            if (strData != null) {
                object = new Bundle();
                object.putInt("cellType", 2);
                object.putString("title", "\uc804\ud654\ubc88\ud638");
                object.putString("desc", strData);
                this.arrayList.add(object);
            }
            strData = bundle.getString("address");
            if (strData != null) {
                object = new Bundle();
                object.putInt("cellType", 2);
                object.putString("title", "\uc8fc\uc18c");
                object.putString("desc", strData);
                this.arrayList.add(object);
            }
            strData = bundle.getString("toilet");
            if (strData != null) {
                object = new Bundle();
                object.putInt("cellType", 2);
                object.putString("title", "\ud654\uc7a5\uc2e4");
                object.putString("desc", strData);
                this.arrayList.add(object);
            }
            strData = bundle.getString("exitDoor");
            if (strData != null) {
                object = new Bundle();
                object.putInt("cellType", 2);
                object.putString("title", "\ub0b4\ub9ac\ub294\ubb38");
                object.putString("desc", strData);
                this.arrayList.add(object);
            }
            strData = bundle.getString("crossing");
            if (strData != null) {
                object = new Bundle();
                object.putInt("cellType", 2);
                object.putString("title", "\ubc18\ub300\ud3b8 \uc5f0\uacb0");
                object.putString("desc", strData);
                this.arrayList.add(object);
            }
            strData = bundle.getString("platform");
            if (strData != null) {
                object = new Bundle();
                object.putInt("cellType", 2);
                object.putString("title", "\ud50c\ub7ab\ud3fc");
                object.putString("desc", strData);
                this.arrayList.add(object);
            }
            strData = bundle.getString("meetingPlace");
            if (strData != null) {
                object = new Bundle();
                object.putInt("cellType", 2);
                object.putString("title", "\ub9cc\ub0a8\uc758 \uc7a5\uc18c");
                object.putString("desc", strData);
                this.arrayList.add(object);
            }
            strData = bundle.getString("amenities");
            if (strData != null) {
                object = new Bundle();
                object.putInt("cellType", 2);
                object.putString("title", "\ud3b8\uc758\uc7a5\uc18c");
                object.putString("desc", strData);
                this.arrayList.add(object);
            }
        }
    }
}
