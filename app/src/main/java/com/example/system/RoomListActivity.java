package com.example.system;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import RoomDBHelper.RoomDBHelper;
import Room.Room;
import TableContanst.TableContanst;

public class RoomListActivity extends ListActivity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {

    private static final String TAG = "TestSQLite";
    private Button addRoom;
    public static int raw;
    public static int raw2;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private ListView listView;
    private List<Integer> list;
    private List<Integer> list2;
    private RelativeLayout relativeLayout;
    private Button searchButton;
    private Button selectButton;
    private Button deleteButton;
    private Button selectAllButton;
    private Button canleButton;
    private LinearLayout layout;
    private Room room;
    private Boolean isDeleteList = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate");
        list = new ArrayList<Integer>();
        list2 = new ArrayList<Integer>();
        room = new Room();
        addRoom = (Button) findViewById(R.id.btn_add_room);
        searchButton = (Button) findViewById(R.id.bn_search_id);
        selectButton = (Button) findViewById(R.id.bn_select);
        deleteButton = (Button) findViewById(R.id.bn_delete);
        selectAllButton = (Button) findViewById(R.id.bn_selectall);
        canleButton = (Button) findViewById(R.id.bn_canel);
        layout = (LinearLayout) findViewById(R.id.showLiner);
        relativeLayout=(RelativeLayout) findViewById(R.id.RelativeLayout);
        listView = getListView();

        // 为按键设置监听
        addRoom.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        selectButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        canleButton.setOnClickListener(this);
        selectAllButton.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setOnCreateContextMenuListener(this);

    }

    // 调用load()方法将数据库中的所有记录显示在当前页面
    @Override
    protected void onStart() {
        super.onStart();
        load();
    }

    public  void onClick(View v) {
        // 跳转到添加信息的界面
        if (v == addRoom) {
            startActivity(new Intent(RoomListActivity.this, AddRoomActivity.class));
        } else if (v == searchButton) {
            // 跳转到查询界面
            startActivity(new Intent(this,RoomSearch.class));
        } else if (v == selectButton) {
            // 跳转到选择界面
            isDeleteList = !isDeleteList;
            if (isDeleteList) {
                checkOrClearAllCheckboxs(true);
            } else {
                showOrHiddenCheckBoxs(false);
            }
        } else if (v == deleteButton) {
            // 删除数据
            AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(RoomListActivity.this);
            alertdialogbuilder.setMessage("确认是否删除");
            alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface arg0, int arg1) {
                    deleteRoomInformation(raw2);
                    load();
                }
            });
            alertdialogbuilder.setNegativeButton("取消",null);
            AlertDialog alertdialog1 = alertdialogbuilder.create();
            alertdialog1.show();
        } else if (v == canleButton) {
            // 点击取消，回到初始界面
            load();
            layout.setVisibility(View.GONE);
            isDeleteList = !isDeleteList;
        } else if (v == selectAllButton) {
            // 全选，如果当前全选按钮显示是全选，则在点击后变为取消全选，如果当前为取消全选，则在点击后变为全选
            selectAllMethods();
        }
    }

    // 创建菜单
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        MenuInflater inflater = new MenuInflater(this); //getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    // 对菜单中的按钮添加响应时间
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        Intent intent = new Intent();
        switch (item_id) {
            // 删除
            case R.id.delete:
                intent.setClass(this, ChangeRoomActivity.class);
                this.startActivity(intent);
                break;
            case R.id.look:
                raw = raw2;
                // 查看房间信息
                intent.setClass(this, ShowRoomActivity.class);
                this.startActivity(intent);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        raw2 = position + 1;
        //registerForContextMenu(listView);
        return false;
    }

    // 点击一条记录是触发的事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isDeleteList) {
            raw = position + 1;
            Intent intent = new Intent();
            intent.setClass(this, ShowRoomActivity.class);
            this.startActivity(intent);
        } else {
            CheckBox box = (CheckBox) view.findViewById(R.id.cb_box);
            box.setChecked(!box.isChecked());
            raw2 = position + 1;
            deleteButton.setEnabled(true);
        }
    }

    // 自定义一个加载数据库中的全部记录到当前页面的无参方法
    public void load() {
        final String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/com/cn21/account/public";
        //数据库名
        final String DATABASE_FILENAME = "hotelinfo.db";
        String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
        File dir = new File(DATABASE_PATH);
        if (!dir.exists()){
            dir.mkdirs();
        }
        final SQLiteDatabase SY = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);

        Cursor info = SY.rawQuery("select Name as _id, Sex,Age,Tel,Data,ID from GuestInfo",null);
        info.moveToFirst();
        //new CursorLoader((Context) cursor);
        adapter = new SimpleCursorAdapter(this, R.layout.room_list,
                info, new String[] {"_id", "Sex", "Age", "Tel", "Data", "ID"},
                new int[] {R.id.tv_stu_name, R.id.tv_stu_sex, R.id.tv_stu_age, R.id.tv_stu_tel, R.id.tv_stu_date,R.id.tv_stu_id},0);
        listView.setAdapter(adapter);
    }

    // 全选或者取消全选
    private void checkOrClearAllCheckboxs(boolean b) {
        int childCount = listView.getChildCount();
        Log.e(TAG, "list child size=" + childCount);
        for (int i = 0; i < childCount; i++) {
            View view = listView.getChildAt(i);
            if (view != null) {
                CheckBox box = (CheckBox) view.findViewById(R.id.cb_box);
                box.setChecked(!b);
            }
        }
        showOrHiddenCheckBoxs(true);
    }

    // 显示或者隐藏自定义菜单
    private void showOrHiddenCheckBoxs(boolean b) {
        int childCount = listView.getChildCount();
        Log.e(TAG, "list child size=" + childCount);
        for (int i = 0; i < childCount; i++) {
            View view = listView.getChildAt(i);
            if (view != null) {
                CheckBox box = (CheckBox) view.findViewById(R.id.cb_box);
                int visible = b ? View.VISIBLE : View.GONE;
                box.setVisibility(visible);
                layout.setVisibility(visible);
                deleteButton.setEnabled(false);
            }
        }
    }

    public void deleteRoomInformation(int delete_id) {
        final String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/com/cn21/account/public";
        //数据库名
        final String DATABASE_FILENAME = "hotelinfo.db";
        String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
        File dir = new File(DATABASE_PATH);
        if (!dir.exists()){
            dir.mkdirs();
        }
        final SQLiteDatabase SY = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);

        Cursor info = SY.rawQuery("select * from GuestInfo",null);
        info.moveToFirst();
        int length = delete_id;
        for (int i = 1;i<length;i++){
            info.moveToNext();
        }
        String name = info.getString(info.getColumnIndex("Name"));
        String deleteSql = "delete from GuestInfo where Name = ?";
        SY.execSQL(deleteSql, new String[]{name});
    }

    // 点击全选事件时所触发的响应
    private void selectAllMethods() {
        // 全选，如果当前全选按钮显示是全选，则在点击后变为取消全选，如果当前为取消全选，则在点击后变为全选
        if (selectAllButton.getText().toString().equals("全选")) {
            int childCount = listView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = listView.getChildAt(i);
                if (view != null) {
                    CheckBox box = (CheckBox) view.findViewById(R.id.cb_box);
                    box.setChecked(true);
                    deleteButton.setEnabled(true);
                    selectAllButton.setText("取消全选");
                }
            }
        } else if (selectAllButton.getText().toString().equals("取消全选")) {
            checkOrClearAllCheckboxs(true);
            deleteButton.setEnabled(false);
            selectAllButton.setText("全选");
        }
    }
}