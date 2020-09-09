package com.example.system;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class RegisterActivity extends AppCompatActivity {

    private TextView title;
    private EditText accounts;
    private EditText password;
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/com/cn21/account/public";
        //数据库名
        final String DATABASE_FILENAME = "systeminfo.db";
        String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
        File dir = new File(DATABASE_PATH);
        if (!dir.exists()){
            dir.mkdirs();
        }
        final SQLiteDatabase SY = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);

        accounts = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        login = (Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入的数据
                String strUsername = accounts.getText().toString();
                String strPassword = password.getText().toString();
                if(TextUtils.isEmpty(accounts.getText()) || TextUtils.isEmpty(password.getText())){
                    Toast.makeText(RegisterActivity.this,"用户名或密码为空！",Toast.LENGTH_SHORT).show();
                }else{
                    SY.execSQL("insert into User(accounts, password) values(?,?)", new  String[]{strUsername, strPassword});
                    //Intent返回到登陆界面
                    Intent intent = new Intent(RegisterActivity.this,Login.class);
                    startActivity(intent);
                }
            }
        });

        register = (Button) findViewById(R.id.button2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent返回到登录界面
                Intent intent = new Intent(RegisterActivity.this,Login.class);
                startActivity(intent);
            }
        });
    }
}


