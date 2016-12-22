package ashkan.fakhr.faraz.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Snippets.setupUI(this, findViewById(R.id.root));
        TextView textView = (TextView) findViewById(R.id.toolbarTitle);
        textView.setText(R.string.login_activity_title);
        findViewById(R.id.emailEditText).requestFocus();
        Snippets.setFontForActivity(findViewById(R.id.root));

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginClick();
            }
        });
    }

    public void loginClick() {
        UserModel userModel = new UserModel();

        TextView textView = (TextView) findViewById(R.id.emailEditText);
        if (textView.getText().length() > 2) {
            userModel.setEmail(textView.getText().toString());
        } else {
            showError(getString(R.string.user_name_at_least_4));
            return;
        }

        textView = (TextView) findViewById(R.id.passwordEditText);
        if (textView.getText().length() > 3) {
            userModel.setPassword(textView.getText().toString());
        } else {
            showError(getString(R.string.user_name_at_least_4));
            return;
        }

        String postJsonData = JSON.toJSONString(userModel);
        Snippets.hideKeyboard(this);
        ((ProgressView) findViewById(R.id.loginButtonProgress)).start();
        NetworkRequests.postRequest(Constants.LOGIN_URL, new Interfaces.NetworkListeners() {
            @Override
            public void onResponse(String response, String tag) {
                ((ProgressView) findViewById(R.id.loginButtonProgress)).stop();
                JSONObject jsonObject = JSON.parseObject(response);
                Snippets.setSP(Constants.TOKEN, jsonObject.getString("token"));
                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                startActivity(intent);

            }

            @Override
            public void onError(VolleyError error, String tag) {
                ((ProgressView) findViewById(R.id.loginButtonProgress)).stop();

            }

            @Override
            public void onOffline(String tag) {
                ((ProgressView) findViewById(R.id.loginButtonProgress)).stop();

            }
        }, "", postJsonData);
    }

    private void showError(String message) {
        Snackbar.make(findViewById(R.id.root), message, Snackbar.LENGTH_LONG).show();
    }

}
