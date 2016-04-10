package com.example.hyejung.easysubway.popupviews.favoriteslistview;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;

//import com.example.hyejung.easysubway.cellRow.PathListRow02;
//import com.example.hyejung.easysubway.cellRow.PathListRow03;
import com.example.hyejung.easysubway.cellRow.SubwayStationListRow;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.PointDataManager;
import java.util.ArrayList;

public class FavoritesListActivity extends Activity implements OnClickListener {
    AppDataManager appManager = null;
    ArrayList<Bundle> arrayList = null;
    int iModeFlag = 0;
    ArrayAdapter<Bundle> listData = null;
    Button modeFlagButton = null;

    public class arrayListAdapter extends ArrayAdapter<Bundle> {
        Activity context;

        public arrayListAdapter(Activity context, int textViewResourceId, ArrayList<Bundle> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View cell = convertView;
            Bundle bundle = (Bundle) FavoritesListActivity.this.arrayList.get(position);
            if (FavoritesListActivity.this.iModeFlag == 0) {
                SubwayStationListRow rowView;
                if (cell == null || convertView.getId() != 100) {
                    cell = this.context.getLayoutInflater().inflate(R.layout.cell_subway_station_list_row, null);
                    rowView = new SubwayStationListRow(cell);
                    cell.setTag(rowView);
                    cell.setId(100);
                } else {
                    rowView = (SubwayStationListRow) cell.getTag();
                }
                rowView.setCellData(this.context, bundle);
            } 
       
            return cell;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_list_view);
        this.appManager = AppDataManager.shared();
        initTableBar();
        initListView();
        setModeFlag(this.appManager.getMySharedPreferences("FavoritesListMode", 0));
    }

    private void initTableBar() {
        LinearLayout titlebar = (LinearLayout) findViewById(R.id.titleBer);
        View barIndicator = LayoutInflater.from(this).inflate(R.layout.bar_titlebar_2button, null);
        ((TextView) barIndicator.findViewById(R.id.title)).setText("\uc990\uaca8\ucc3e\uae30");
        this.modeFlagButton = (Button) barIndicator.findViewById(R.id.navi_titlebar_2button1);
        this.modeFlagButton.setOnClickListener(this);
        Button button2 = (Button) barIndicator.findViewById(R.id.navi_titlebar_2button2);
        button2.setText("\ub2eb\uae30");
        button2.setOnClickListener(this);
        titlebar.addView(barIndicator);
    }

    private void getArrayList() {
        ArrayList<Bundle> arrList;
        if (this.arrayList == null) {
            this.arrayList = new ArrayList();
        } else {
            this.arrayList.clear();
        }
        if (this.iModeFlag == 0) {
            arrList = this.appManager.getFavoritesStation();
        } else {
            arrList = this.appManager.getFavoritesPath();
        }
        if (arrList.size() > 0) {
            this.arrayList.addAll(arrList);
        }
        arrList.clear();
        if (this.listData != null) {
            this.listData.notifyDataSetChanged();
        }
    }

    private void initListView() {
        if (this.arrayList == null) {
            this.arrayList = new ArrayList();
        } else {
            this.arrayList.clear();
        }
        this.listData = new arrayListAdapter(this, 0, this.arrayList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(this.listData);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bundle bundle = (Bundle) FavoritesListActivity.this.arrayList.get(position);
                Intent intent = FavoritesListActivity.this.getIntent();
                if (FavoritesListActivity.this.iModeFlag == 0) {
                    String stationCode = bundle.getString("stationCode");
                    intent.putExtra("resFlag", 2);
                    intent.putExtra("stationCode", stationCode);
                } else if (PointDataManager.shared().setSelectedPoint(bundle).booleanValue()) {
                    intent.putExtra("resFlag", 3);
                }
                FavoritesListActivity.this.setResult(-1, intent);
                FavoritesListActivity.this.finish();
                FavoritesListActivity.this.overridePendingTransition(0, 0);
            }
        });
        /*
         * 즐겨찾기 리스트 long 클릭시 알림 메세지 창
         */
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
        	int selectedRow = -1;
            
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                this.selectedRow = position;
                Builder alertDlg = new Builder(view.getContext());
                alertDlg.setTitle("\uc54c\ub9bc");
                alertDlg.setPositiveButton("\uc544\ub2c8\uc624", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	dialog.dismiss();
                    }
                });
                alertDlg.setNegativeButton("\uc608", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	int rowid = ((Bundle) FavoritesListActivity.this.arrayList.get(selectedRow)).getInt("rowid");
                        if (FavoritesListActivity.this.iModeFlag == 0) {
                            if (FavoritesListActivity.this.appManager.deleteFavoritesStation(rowid).booleanValue()) {
                                FavoritesListActivity.this.arrayList.remove(selectedRow);
                            }
                        } else if (FavoritesListActivity.this.appManager.deleteFavoritesPath(rowid).booleanValue()) {
                            FavoritesListActivity.this.arrayList.remove(selectedRow);
                        }
                        FavoritesListActivity.this.listData.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                alertDlg.setMessage(String.format("\uc120\ud0dd\ub41c \ud56d\ubaa9\uc744 \uc0ad\uc81c\ud558\uc2dc\uaca0\uc2b5\ub2c8\uae4c?", new Object[]{FavoritesListActivity.this.arrayList.get(position)}));
                alertDlg.show();
                return false;
            }
        });
    }

    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.navi_titlebar_2button1) {
            cmdTableBer_Button1();
        } else if (id == R.id.navi_titlebar_2button2) {
            cmdTableBer_Button2();
        }
    }

    private void setModeFlag(int flag) {
        this.iModeFlag = flag;
        if (this.iModeFlag == 0) {
            this.modeFlagButton.setText("\uc774\ub3d9\uacbd\ub85c");
        } else {
            this.modeFlagButton.setText("\uc9c0\ud558\ucca0\uc5ed");
        }
        getArrayList();
    }

    private void cmdTableBer_Button1() {
        if (this.iModeFlag == 0) {
            setModeFlag(1);
            this.appManager.setMySharedPreferences("FavoritesListMode", 1);
            return;
        }
        setModeFlag(0);
        this.appManager.setMySharedPreferences("FavoritesListMode", 0);
    }

    private void cmdTableBer_Button2() {
        finish();
    }
}
