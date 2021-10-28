package com.example.mohinhbtl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mohinhbtl.database.UserDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtAddress;
    private EditText edtCccd;
    private EditText edtStatus;
    private EditText edtAge;
    private EditText edtPhone;
    private EditText edtDate;
    private Button btnAddUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initVi();

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate();
            }
        });

    }

    private void initVi(){
        edtUsername=findViewById(R.id.edt_username);
        edtAddress=findViewById(R.id.edt_address);
        edtAge=findViewById(R.id.edt_age);
        edtCccd=findViewById(R.id.edt_cccd);
        edtPhone=findViewById(R.id.edt_phone);
        edtDate=findViewById(R.id.edt_date);
        edtStatus=findViewById(R.id.edt_status);
        btnAddUser=findViewById(R.id.btn_add_user);

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

    private void addUser() {
        String strUsername=edtUsername.getText().toString().trim();
        String strAddress=edtAddress.getText().toString().trim();
        String strAge=edtAge.getText().toString().trim();
        String strCccd=edtCccd.getText().toString().trim();
        String strPhone=edtPhone.getText().toString().trim();
        String strDate=edtDate.getText().toString().trim();
        String strStatus=edtStatus.getText().toString().trim();

        if(TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strAddress)){
            return;
        }


        User user =new User(strUsername,strAge,strAddress,strCccd,strPhone,strDate,strStatus);

        if(isUserExist(user)){
            Toast.makeText(this,"User exist",Toast.LENGTH_SHORT).show();
            hideSoftKeyboard();
            return;
        }

        UserDatabase.getInstance(this).userDAO().insert(user);
        Toast.makeText(this,"ADD USER",Toast.LENGTH_SHORT).show();

        edtUsername.setText("");
        edtAddress.setText("");
        edtAge.setText("");
        edtCccd.setText("");
        edtPhone.setText("");
        edtDate.setText("");
        edtStatus.setText("");

        hideSoftKeyboard();


    }
    public void hideSoftKeyboard(){
        try{
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private boolean isUserExist( User user){
        List<User> list =UserDatabase.getInstance(this).userDAO().checkUser(user.getUsername());
        return list !=null && !list.isEmpty();
    }



}