package com.example.mohinhbtl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohinhbtl.database.UserDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AllActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE =10 ;
    private RecyclerView rcvUser;
    private UserAdapter userAdapter;
    private TextView tvDeleteAll;
    private TextView tvCount;
    private EditText edtSearch;
    private EditText edtSearchDate;
    private List<User> mListUser;
    private SearchView searchView;
    private String strSearch,strKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);

        initVi();

        userAdapter=new UserAdapter(new UserAdapter.IClickItemUser() {
            @Override
            public void updateUser(User user) {
                clickUpdateUser(user);
            }

            @Override
            public void deleteUser(User user) {
                clickDeleteUser(user);
            }
        });
        mListUser=new ArrayList<>();
        userAdapter.setData(mListUser);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rcvUser.setLayoutManager(linearLayoutManager);

        rcvUser.setAdapter(userAdapter);
        tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteAllUser();
            }
        });

        edtSearchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AllActivity.this,strSearch,Toast.LENGTH_SHORT).show();
                    handleSearchDate();
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH){
                    // search
                    handleSearchUser();
                }
                return false;
            }
        });
//        edtSearchDate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if(i== EditorInfo.IME_ACTION_SEARCH){
//                    handleSearchDate();
//                }
//                return false;
//            }
//        });
//        registerForContextMenu(menuac);


        loadData();

    }

    private void initVi(){

        rcvUser=findViewById(R.id.rcv_user);
        tvDeleteAll=findViewById(R.id.tv_delete_all);
        tvCount=findViewById(R.id.tv_count);
        edtSearch=findViewById(R.id.edt_search);
        edtSearchDate=findViewById(R.id.edt_search_date);

    }
//    private void chooseDate(){
//        Calendar calendar= Calendar.getInstance();
//        int dayOfMonth = calendar.get(Calendar.DATE);
//        int month = calendar.get(Calendar.MONTH);
//        int year = calendar.get(Calendar.YEAR);
//        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                calendar.set(i,i1,i2);
//                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
//                edtSearchDate.setText(simpleDateFormat.format(calendar.getTime()));
//
//            }
//        },year,month,dayOfMonth);
//        datePickerDialog.show();
//    }
    private void loadData(){
        mListUser= UserDatabase.getInstance(this).userDAO().getListUser();
        userAdapter.setData(mListUser);
        CountUser();
    }
    public void hideSoftKeyboard(){
        try{
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }
    private void clickUpdateUser(User user){
        Intent intent=new Intent(AllActivity.this,UpdateActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("object_user",user);
        intent.putExtras(bundle);
        startActivityForResult(intent,MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MY_REQUEST_CODE && resultCode==Activity.RESULT_OK){
            loadData();
        }
    }
    private void clickDeleteUser(User user){
        new AlertDialog.Builder(this)
                .setTitle("Xóa bệnh nhân")
                .setMessage("Bạn có chắc không?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete user
                        UserDatabase.getInstance(AllActivity.this).userDAO().deleteUser(user);
                        Toast.makeText(AllActivity.this,"DELETE USER ",Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .setNegativeButton("No",null)
                .show();

    }

    private void clickDeleteAllUser(){
        new AlertDialog.Builder(this)
                .setTitle("Xóa tất cả danh sách bệnh nhân")
                .setMessage("Bạn có chắc không?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete user
                        UserDatabase.getInstance(AllActivity.this).userDAO().deleteAllUser();
                        Toast.makeText(AllActivity.this,"DELETE ALL USER ",Toast.LENGTH_SHORT).show();

                        loadData();
                    }
                })
                .setNegativeButton("No",null)
                .show();

    }
    private void handleSearchUser(){
        strKeyword=edtSearch.getText().toString().trim();
        mListUser=new ArrayList<>();
        mListUser=UserDatabase.getInstance(this).userDAO().searchUser(strKeyword);
        userAdapter.setData(mListUser);
        tvCount.setText(String.valueOf(UserDatabase.getInstance(this).userDAO().countUser(strKeyword)));
        hideSoftKeyboard();
    }
    private void handleSearchDate(){
        String strKeyword=edtSearchDate.getText().toString().trim();
        mListUser=new ArrayList<>();
        mListUser=UserDatabase.getInstance(this).userDAO().searchDate(strKeyword);
        userAdapter.setData(mListUser);
        tvCount.setText(String.valueOf(UserDatabase.getInstance(this).userDAO().countDate(strKeyword)));
        hideSoftKeyboard();
    }
//    private void CountUser(){
//        String strKeyword=edtSearchDate.getText().toString().trim();
//        if(strKeyword.isEmpty() || strKeyword==null){
//            tvCount.setText(String.valueOf(UserDatabase.getInstance(this).userDAO().countUserAll()));
//        }else{
//            tvCount.setText(String.valueOf(UserDatabase.getInstance(this).userDAO().countUser(strKeyword)));
//        }
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userAdapter.getFilter().filter(query);
                strSearch=query.toString();
                CountUser();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.getFilter().filter(newText);
                strSearch=newText.toString();
                CountUser();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
    private void CountUser(){

//        if(strSearch!=null){
//            tvCount.setText(String.valueOf(UserDatabase.getInstance(this).userDAO().countDate(strSearch)));
//            return;
//        }
//        if(strKeyword!=null){
//            return;
//        }
        tvCount.setText(String.valueOf(UserDatabase.getInstance(this).userDAO().countUserAll()));
    }
//    private void CountUser(){
////        String strKeyword=edtSearchDate.getText().toString().trim();
////        if(strSearch.isEmpty() || strSearch==null){
//            tvCount.setText(String.valueOf(UserDatabase.getInstance(this).userDAO().countUserAll()));
////        }else{
////            tvCount.setText(String.valueOf(UserDatabase.getInstance(this).userDAO().countUser(strKeyword)));
////        }
//        if(strSearch!=null){
//    tvCount.setText(String.valueOf(UserDatabase.getInstance(this).userDAO().countUser(strSearch)));
//        }
//
//    }


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        getMenuInflater().inflate(R.menu.context_menu,menu);
//        super.onCreateContextMenu(menu, v, menuInfo);
//
//    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//                int pos=info.position;
        int id=id=item.getItemId();
        switch (id){
            case 121:
                Toast.makeText(AllActivity.this,"Show",Toast.LENGTH_LONG).show();
                break;
            case 122:
                Toast.makeText(AllActivity.this,"Edit",Toast.LENGTH_LONG).show();
                break;

        }
        return super.onContextItemSelected(item);
    }


//    @Override
//    public void onBackPressed() {
//        if(!searchView.isIconified()){
//            searchView.setIconified(true);
//            return;
//        }
//        super.onBackPressed();
//    }
}