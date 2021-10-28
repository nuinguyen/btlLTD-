package com.example.mohinhbtl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mohinhbtl.database.UserDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtAddress;
    private EditText edtAge;
    private EditText edtCccd;
    private EditText edtPhone;
    private EditText edtDate;
    private EditText edtStatus;
    private Button btnUpdateUser;

    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        viniVi();

        mUser= (User) getIntent().getExtras().get("object_user");
        if(mUser != null){
            edtUsername.setText(mUser.getUsername());
            edtAddress.setText(mUser.getAddress());
            edtAge.setText(mUser.getAge());
            edtCccd.setText(mUser.getCccd());
            edtPhone.setText(mUser.getPhone());
            edtDate.setText(mUser.getDate());
            edtStatus.setText(mUser.getStatus());

        }
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate();
            }
        });
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });


    }
    private void viniVi(){
        edtUsername=findViewById(R.id.edt_username);
        edtAddress=findViewById(R.id.edt_address);
        edtAge=findViewById(R.id.edt_age);
        edtCccd=findViewById(R.id.edt_cccd);
        edtPhone=findViewById(R.id.edt_phone);
        edtDate=findViewById(R.id.edt_date);
        edtStatus=findViewById(R.id.edt_status);
        btnUpdateUser=findViewById(R.id.btn_update_user);
    }

    private void updateUser() {
        String strUsername=edtUsername.getText().toString().trim();
        String strAddress=edtAddress.getText().toString().trim();
        String strAge=edtAge.getText().toString().trim();
        String strCccd=edtCccd.getText().toString().trim();
        String strPhone=edtPhone.getText().toString().trim();
        String strDate=edtDate.getText().toString().trim();
        String strStatus=edtStatus.getText().toString().trim();

        if(TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strAddress)||TextUtils.isEmpty(strCccd) || TextUtils.isEmpty(strPhone)|| TextUtils.isEmpty(strDate)){
            return;
        }
        // Update User
        mUser.setUsername(strUsername);
        mUser.setAddress(strAddress);
        mUser.setAge(strAge);
        mUser.setCccd(strCccd);
        mUser.setPhone(strPhone);
        mUser.setDate(strDate);
        mUser.setStatus(strStatus);

        UserDatabase.getInstance(this).userDAO().updateUser(mUser);
        Toast.makeText(this,"UPDATE SUCCESSFULLY", Toast.LENGTH_SHORT).show();

        Intent intentResult=new Intent();
        setResult(Activity.RESULT_OK,intentResult);
        finish();
    }
    private void chooseDate(){
        Calendar calendar= Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                edtDate.setText(simpleDateFormat.format(calendar.getTime()));

            }
        },year,month,dayOfMonth);
        datePickerDialog.show();
    }
}