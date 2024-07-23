package com.app.shieldbank;
// 화면 전환

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        System.exit(0);
                    }
                }).create().show();
    }


    public void logout(View view){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("jwt", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isloggedin", false);
        editor.apply();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
    public void addBeneficiary(View v){
        startActivity(new Intent(getApplicationContext(), AddBeneficiary.class));
    } // 계좌 추가
    public void viewMyBeneficiaries(View V){
        startActivity(new Intent(getApplicationContext(), ViewBeneficiary.class));
    } // 계좌 확인
    public void viewbalance(View V){
        startActivity(new Intent(getApplicationContext(), ViewBalance.class));
    } // 자산 확인
    public void getPendingBeneficiaries(View V){
        startActivity(new Intent(getApplicationContext(), PendingBeneficiary.class));
    } // 이체
    public void viewTransactions(View V){
        startActivity(new Intent(getApplicationContext(), GetTransactions.class));
    } // 이체 내역
    public void viewBeneficiaryAdmin(View V){
        startActivity(new Intent(getApplicationContext(), ViewBeneficiaryAdmin.class));
    } // 이체 승인
    public void resetPassword(View v){
        startActivity(new Intent(getApplicationContext(), ResetPassword.class));
    } // 비밀번호 변경
    public void myprofile(View V){
        startActivity(new Intent(getApplicationContext(), Myprofile.class));
    } // 나의 정보
    public void viewQuestionAndAnswer(View v) {
        startActivity(new Intent(getApplicationContext(), QnAListView.class));
    } // QnA 게시판
    public void adminAnnouncement(View v) {
        startActivity(new Intent(getApplicationContext(), NoticeListView.class));
    } // 공지사항
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        TextView t1=findViewById(R.id.dasht);
//        if(RootUtil.isDeviceRooted()) {
//            Toast.makeText(getApplicationContext(), "Phone is Rooted", Toast.LENGTH_SHORT).show();
//            finish();
//        }


    }


}