package com.example.system;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import RoomDBHelper.RoomDBHelper;
import Room.Room;
import TableContanst.TableContanst;

public class ChangeRoomActivity extends Activity implements OnClickListener {
    private static final String TAG = "AddRoomActivity";
    private final static int DATE_DIALOG = 1;
    private static final int DATE_PICKER_ID = 1;
    private TextView idText;
    private EditText nameText;
    private EditText ageText;
    private EditText phoneText;
    private EditText dataText;
    private RadioGroup group;
    private RadioButton button1;
    private RadioButton button2;
    private RadioGroup group2;
    private RadioButton box1;
    private RadioButton box2;
    private RadioButton box3;
    private RadioButton box4;
    private Button restoreButton;
    private Button resetButton;
    private boolean isAdd = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_room);
        idText = (TextView) findViewById(R.id.tv_stu_id);
        nameText = (EditText) findViewById(R.id.et_name);
        ageText = (EditText) findViewById(R.id.et_age);
        button1 = (RadioButton) findViewById(R.id.rb_sex_female);
        button2 = (RadioButton) findViewById(R.id.rb_sex_male);
        phoneText = (EditText) findViewById(R.id.et_phone);
        dataText = (EditText) findViewById(R.id.et_traindate);
        group = (RadioGroup) findViewById(R.id.rg_sex);
        group2 = (RadioGroup) findViewById(R.id.rg_likes);

        restoreButton = (Button) findViewById(R.id.btn_save);
        resetButton = (Button) findViewById(R.id.btn_clear);
        box1 = (RadioButton)findViewById(R.id.box1);
        box2 = (RadioButton)findViewById(R.id.box2);
        box3 = (RadioButton)findViewById(R.id.box3);
        box4 = (RadioButton)findViewById(R.id.box4);
        restoreButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        dataText.setOnClickListener(this);
        checkIsAddRoom();
    }

    // 检查此时Activity是否用于添加房客信息
    private void checkIsAddRoom() {
        Intent intent = getIntent();
        Serializable serial = intent.getSerializableExtra(TableContanst.ROOM_TABLE);
        if (serial == null) {
            isAdd = true;
            dataText.setText(getCurrentDate());
        } else {
            isAdd = false;
            showEditUI();
        }
    }

    //显示房客信息更新的UI104
    private void showEditUI() {
        // 先将Room携带的数据还原到room的每一个属性中去
        String name = nameText.getText().toString();
        String age = ageText.getText().toString();
        String sex = ((RadioButton) findViewById(group.getCheckedRadioButtonId())).getText().toString();
        String tel =  phoneText.getText().toString();
        String data = getCurrentDate();
        String id = ((RadioButton) findViewById(group2.getCheckedRadioButtonId())).getText().toString();

        if (sex.toString().equals("男")) {
            button2.setChecked(true);
        } else if (sex.toString().equals("女")) {
            button1.setChecked(true);
        }
        if (id.toString().equals("标准间")) {
            box1.setChecked(true);
        } else if (id.toString().equals("单人间")) {
            box2.setChecked(true);
        }else if  (id.toString().equals("三人间")){
            box3.setChecked(true);
        }else if  (id.toString().equals("大床房")){
            box4.setChecked(true);
        }

        // 还原数据
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
        int length = RoomListActivity.raw2;
        for (int i = 1;i<length;i++){
            info.moveToNext();
        }
        nameText.setText(info.getString(info.getColumnIndex("Name")));
        ageText.setText(info.getString(info.getColumnIndex("Age")));
        phoneText.setText(info.getString(info.getColumnIndex("Tel")));
        dataText.setText(info.getString(info.getColumnIndex("Data")));
        setTitle("房间信息更新");
        restoreButton.setText("更新");
    }

    public void onClick(View v) {
        // 收集数据
        if (v == restoreButton) {
            Boolean state = checkUIInput();
            if(state==true){
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(ChangeRoomActivity.this);
                alertdialogbuilder.setMessage("保存成功!");
                alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteRoomInformation();
                        upData();
                    }
                });
                AlertDialog alertdialog1 = alertdialogbuilder.create();
                alertdialog1.show();
            }
        } else if (v == resetButton) {
            clearUIData();
        } else if (v == dataText) {
            showDialog(DATE_PICKER_ID);
        }
    }

    public void deleteRoomInformation() {
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
        int length = RoomListActivity.raw2;
        for (int i = 1;i<length;i++){
            info.moveToNext();
        }
        String name = info.getString(info.getColumnIndex("Name"));
        String deleteSql = "delete from GuestInfo where Name = ?";
        SY.execSQL(deleteSql, new String[]{name});
    }

    private  void upData(){
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
        String name = nameText.getText().toString();
        String age = ageText.getText().toString();
        String sex = ((RadioButton) findViewById(group.getCheckedRadioButtonId())).getText().toString();
        String tel =  phoneText.getText().toString();
        String data = getCurrentDate();
        String id = ((RadioButton) findViewById(group2.getCheckedRadioButtonId())).getText().toString();
        String insertSql = "insert into GuestInfo(Name, Sex,Age,Tel,Data,ID) values(?,?,?,?,?,?)";
        SY.execSQL(insertSql, new  String[]{name, sex,age,tel,data,id,});

        Intent intent = new Intent(ChangeRoomActivity.this,RoomListActivity.class);
        startActivity(intent);
    }

    //       清空界面的数据176
    private void clearUIData() {
        nameText.setText("");
        ageText.setText("");
        phoneText.setText("");
        dataText.setText("");
        group.clearCheck();
        group2.clearCheck();
    }

    //      * 得到当前的日期
    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date());
    }

    //验证用户是否按要求输入了数据
    private boolean checkUIInput() { // name, age, sex
        String name = nameText.getText().toString();
        String age = ageText.getText().toString();
        String tel =  phoneText.getText().toString();
        int id = group.getCheckedRadioButtonId();
        int id2 = group2.getCheckedRadioButtonId();
        String message = null;
        View invadView = null;
        if (name.trim().length() == 0) {
            message = "请输入姓名！";
            invadView = nameText;
        } else if (age.trim().length() == 0) {
            message = "请输入年龄！";
            invadView = ageText;
        } else if (id == -1) {
            message = "请选择性别！";
        } else if(tel.trim().length() == 0){
            message = "请输入电话！";
            invadView = phoneText;
        }else if (id2 == -1) {
            message = "请选择房间类型";
        }
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            if (invadView != null)
                invadView.requestFocus();
            return false;
        }         return true;
    }

    //时间的监听与事件
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dataText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }
    };
}
