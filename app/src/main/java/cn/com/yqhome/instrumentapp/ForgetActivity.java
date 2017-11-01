package cn.com.yqhome.instrumentapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;

/**
 * Created by depengli on 2017/10/25.
 */

public class ForgetActivity extends AppCompatActivity {

    EditText _mobileText;
    EditText _codeText;
    EditText _passwordText;
    EditText _reEnterPasswordText;
    Button _forgetButton;
    Button _codeButton;
    TextView _loginLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        _mobileText = (EditText) findViewById(R.id.input_mobile_forget);
        _codeText = (EditText) findViewById(R.id.input_checkCode_forget);
        _passwordText = (EditText) findViewById(R.id.input_password_forget);
        _reEnterPasswordText = (EditText)findViewById(R.id.input_reEnterPassword_forget);
        _forgetButton = (Button)findViewById(R.id.btn_forget);
        _codeButton = (Button)findViewById(R.id.button_validCode);
        _loginLink = (TextView) findViewById(R.id.link_to_login);

        _forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forget();
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

//        _codeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                 send code
//            }
//        });
    }

    public void forget(){
        if (!validate()) {
            onSignupFailed();
            return;
        }

        _forgetButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ForgetActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("修改密码...");
        progressDialog.show();

        String verifycode = _codeText.getText().toString();
        String mobile = _mobileText.getText().toString();
        final String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (!password.equals(reEnterPassword)){
            progressDialog.dismiss();
            _reEnterPasswordText.setError("您输入的密码不匹配");
            return;
        }

        // TODO: Implement your own signup logic here.

        RequestParams params = new RequestParams();
        params.put("username",mobile);
        params.put("password",BaseUtils.md5(password));
        params.put("phone",mobile);
        params.put("verfyCode",verifycode);

        WebUtils.Forget(params,new CallbackListener(){
            @Override
            public void forgetCallback(JSONObject callObject) {
                progressDialog.dismiss();
                try {
                    if (callObject.getBoolean("success")){
                        final String errMessage = callObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                        onSignupSuccess();
                    }else{
                        final String errMessage = callObject.getString("message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                        onSignupFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onSignupSuccess() {
        _forgetButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_LONG).show();
        _forgetButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String code = _codeText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (code.isEmpty() ) {
            _codeText.setError("请输入正确的验证码");
            valid = false;
        } else {
            _codeText.setError(null);
        }

        if (mobile.isEmpty() ) {
            _mobileText.setError("请输入手机号码");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("请输入密码，必须是4位到10位的字符");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("您输入的密码不匹配");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

}
