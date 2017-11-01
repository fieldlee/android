package cn.com.yqhome.instrumentapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.com.yqhome.instrumentapp.Fragments.Interface.CallbackListener;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_checkCode) EditText _codeText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.button_validCode) Button _codeButton;
    @Bind(R.id.link_login) TextView _loginLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
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

        _codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 send code
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("创建账号...");
        progressDialog.show();

        String name = _nameText.getText().toString();
//        String address = _addressText.getText().toString();
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
        params.put("avator",name);
        params.put("verfyCode",verifycode);


        final Activity activity = this;
        WebUtils.Register(params,new CallbackListener(){
            @Override
            public void registerCallbase(JSONObject userObject) {
                progressDialog.dismiss();
                try {
                    if (userObject.getBoolean("success")){
                        String avator = userObject.getString("avator");
                        String username = userObject.getString("username");
                        String token = userObject.getString("token");
                        BaseUtils.saveUser(activity,avator,username,false,password,token);
                        onSignupSuccess();
                    }else{
                        final String errMessage = userObject.getString("message");
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
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
//        String address = _addressText.getText().toString();
        String code = _codeText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("请至少输入3个字符");
            valid = false;
        } else {
            _nameText.setError(null);
        }

//        if (address.isEmpty()) {
//            _addressText.setError("Enter Valid Address");
//            valid = false;
//        } else {
//            _addressText.setError(null);
//        }


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