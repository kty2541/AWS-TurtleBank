package com.app.shieldbank;
// 송금 Activity

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class QR_sendMoney extends AppCompatActivity {

    Button send;
    TextView tt;
    private long now;
    private Date date;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected static String hPassword(String accountPW) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(accountPW.getBytes());

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Define the fee amount
    final int fee = 1000; // Example fee amount

    // Enter the correct url for your api service site
    final int initialTimeoutMs = 2000; // 초기 타임아웃 값 (2초)
    final int maxNumRetries = 0; // 최대 재시도 횟수
    final float backoffMultiplier = 1f; // 재시도 간격의 배수

    RetryPolicy policy = new DefaultRetryPolicy(initialTimeoutMs, maxNumRetries, backoffMultiplier);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_sendmoney);
        tt = findViewById(R.id.edact2);
        Intent i = getIntent();
        String accountNumber = i.getStringExtra("account_number");
        tt.setText(accountNumber);
        tt.setFocusable(false);  // 포커스 받지 않도록 설정
        tt.setClickable(false);  // 클릭 불가능하도록 설정
        tt.setCursorVisible(false);  // 커서 숨기기 (있는 경우)

        send = findViewById(R.id.sendbutton);
        send.setOnClickListener(v -> sendMoney());
    }

    public void sendMoney() {
        SharedPreferences sharedPreferences = getSharedPreferences("jwt", Context.MODE_PRIVATE);
        final String retrievedToken = sharedPreferences.getString("accesstoken", null);
        SharedPreferences sharedPreferences1 = getSharedPreferences("apiurl", Context.MODE_PRIVATE);
        final String url = sharedPreferences1.getString("apiurl", null);
        String endpoint = "/api/balance/transfer";
        final String finalUrl = url + endpoint;

        EditText ed = findViewById(R.id.edact);     // 송금계좌
        EditText ed2 = findViewById(R.id.edact2);    // 수취계좌
        EditText ed3 = findViewById(R.id.edamt);     // 이체금액
        EditText ed4 = findViewById(R.id.accountPW); // 계좌비번

        int from_account = 0;
        int to_account = 0;
        int amount = 0;

        String accountPW = ed4.getText().toString().trim();
        String hAccountPW = hPassword(accountPW);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure the time zone is consistent
        now = System.currentTimeMillis();
        date = new Date(now);
        String sendtime = sdf.format(date);

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject requestData = new JSONObject();
        JSONObject requestDataEncrypted = new JSONObject();
        try {
            // Fetch values
            if (!ed.getText().toString().isEmpty() && !ed2.getText().toString().isEmpty() && !ed3.getText().toString().isEmpty() && !ed4.getText().toString().isEmpty()) {
                from_account = Integer.parseInt(ed.getText().toString());
                to_account = Integer.parseInt(ed2.getText().toString());
                amount = Integer.parseInt(ed3.getText().toString());

                // Check if the amount is valid
                if (amount <= 0) {
                    Toast.makeText(getApplicationContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add the fee to the amount
                int totalAmount = amount + fee;

                // Input your API parameters
                requestData.put("from_account", from_account);  // 송금계좌 varchar
                requestData.put("to_account", to_account);      // 수취계좌 varchar
                requestData.put("amount", amount);              // 이체금액 int
                requestData.put("total_amount", totalAmount);   // Total amount including fee
                requestData.put("sendtime", sendtime);          // 전송시간 datetime
                requestData.put("accountPW", hAccountPW);       // 계좌비번
                requestData.put("fee", fee);                    // 수수료

                // Log the request data
                Log.d("RequestData", requestData.toString());
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                return;
            }

            // Encrypt data before sending
            String encryptedData = EncryptDecrypt.encrypt(requestData.toString());
            Log.d("EncryptedRequest", encryptedData); // Log the encrypted data
            requestDataEncrypted.put("enc_data", encryptedData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, finalUrl, requestDataEncrypted,
                response -> {
                    Log.d("SendResponse", response.toString());
                    try {
                        JSONObject decryptedResponse = new JSONObject(EncryptDecrypt.decrypt(response.get("enc_data").toString()));
                        Log.d("DecryptedResponse", decryptedResponse.toString());

                        if (decryptedResponse.getJSONObject("status").getInt("code") != 200) {
                            Toast.makeText(getApplicationContext(), "Error: " + decryptedResponse.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getApplicationContext(), "Success: " + decryptedResponse.getJSONObject("data").getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.d("ResponseError", String.valueOf(e));
                        e.printStackTrace();
                    }

                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }, error -> {
            Log.d("NetworkError", String.valueOf(error));
            Toast.makeText(getApplicationContext(), "Something went wrong[Send]", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + retrievedToken);
                return headers;
            }

            @Override
            public RetryPolicy getRetryPolicy() {
                return policy;
            }
        };

        requestQueue.add(jsonObjectRequest);
        requestQueue.getCache().clear();
    }
}
