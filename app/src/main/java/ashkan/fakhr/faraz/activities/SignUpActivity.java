package ashkan.fakhr.faraz.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.rey.material.widget.ProgressView;

import ashkan.fakhr.faraz.R;
import ashkan.fakhr.faraz.models.RegisterResponseModel;
import ashkan.fakhr.faraz.models.UserModel;
import ashkan.fakhr.faraz.utilities.Constants;
import ashkan.fakhr.faraz.utilities.Interfaces;
import ashkan.fakhr.faraz.utilities.NetworkRequests;
import ashkan.fakhr.faraz.utilities.Snippets;

/**
 * Created by Ashkan on 12/9/2016.
 */
public class SignUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Snippets.setupUI(this, findViewById(R.id.root));
        TextView textView = (TextView) findViewById(R.id.toolbarTitle);
        textView.setText(R.string.sign_up_activity_title);
        findViewById(R.id.nameEditText).requestFocus();
        Snippets.setFontForActivity(findViewById(R.id.root));

        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpClick();
            }
        });
    }

    public void signUpClick() {
        final UserModel userModel = new UserModel();

        TextView textView = (TextView) findViewById(R.id.nameEditText);
        if (textView.getText().length() > 2) {
            userModel.setName(textView.getText().toString());
        } else {
            showError(getString(R.string.user_name_at_least_4));
            return;
        }

        textView = (TextView) findViewById(R.id.emailEditText);
        if (textView.getText().length() > 3) {
            userModel.setEmail(textView.getText().toString());
        } else {
            showError(getString(R.string.email_at_least_4));
            return;
        }

        textView = (TextView) findViewById(R.id.passwordEditText);
        if (textView.getText().length() > 3) {
            userModel.setPassword1(textView.getText().toString());
        } else {
            showError(getString(R.string.password_at_least_4));
            return;
        }

        textView = (TextView) findViewById(R.id.passwordReEditText);
        if (textView.getText().length() > 3) {
            userModel.setPassword2(textView.getText().toString());
        } else {
            showError(getString(R.string.password_at_least_4));
            return;
        }
        textView = (TextView) findViewById(R.id.mobileEditText);
        if (textView.getText().length() > 3) {
            userModel.setPhone_number(textView.getText().toString());
        } else {
            showError(getString(R.string.mobile_number_at_least_4));
            return;
        }

        String postJsonData = JSON.toJSONString(userModel);
        Snippets.hideKeyboard(this);
        ((ProgressView) findViewById(R.id.registerButtonProgress)).start();
        NetworkRequests.postRequest(Constants.SIGN_UP_URL, new Interfaces.NetworkListeners() {
            @Override
            public void onResponse(String response, String tag) {
                ((ProgressView) findViewById(R.id.registerButtonProgress)).stop();
                RegisterResponseModel registerResponseModel = null;
                try {
                    registerResponseModel = JSON.parseObject(response, RegisterResponseModel.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (registerResponseModel != null && registerResponseModel.isStatus()){
                    Toast.makeText(SignUpActivity.this, "ID: " + registerResponseModel.getUser_id(), Toast.LENGTH_LONG).show();
                    Toast.makeText(SignUpActivity.this, "کد تایید شماره تلفن برای شما ارسال خواهد شد", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, ValidationCodeActivity.class);
                    intent.putExtra(Constants.ID, registerResponseModel.getUser_id());
                    intent.putExtra(Constants.EMAIL, userModel.getEmail());
                    intent.putExtra(Constants.PASSWORD, userModel.getPassword1());
                    startActivity(intent);
                }
            }

            @Override
            public void onError(VolleyError error, String tag) {
                ((ProgressView) findViewById(R.id.registerButtonProgress)).stop();

            }

            @Override
            public void onOffline(String tag) {
                ((ProgressView) findViewById(R.id.registerButtonProgress)).stop();

            }
        }, "", postJsonData);

    }

    private void showError(String message) {
        Snackbar.make(findViewById(R.id.root), message, Snackbar.LENGTH_LONG).show();
    }
}
