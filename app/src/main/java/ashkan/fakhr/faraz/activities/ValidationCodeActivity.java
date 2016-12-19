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

import org.json.JSONException;
import org.json.JSONObject;

import ashkan.fakhr.faraz.R;
import ashkan.fakhr.faraz.models.RegisterResponseModel;
import ashkan.fakhr.faraz.models.UserModel;
import ashkan.fakhr.faraz.models.ValidationResponseModel;
import ashkan.fakhr.faraz.utilities.Constants;
import ashkan.fakhr.faraz.utilities.Interfaces;
import ashkan.fakhr.faraz.utilities.NetworkRequests;
import ashkan.fakhr.faraz.utilities.Snippets;

/**
 * Created by ${Ashkan} on ${12/9/2016}.
 */

public class ValidationCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        Snippets.setupUI(this, findViewById(R.id.root));

        findViewById(R.id.activationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEnterCode();
            }
        });
    }

    private void btnEnterCode() {
        String key;

        TextView textView = (TextView) findViewById(R.id.activationCode);
        if (textView.getText().length() > 3) {
            key = textView.getText().toString();
        } else {
            showError(getString(R.string.mobile_number_at_least_4));
            return;
        }
        String ACTIVATION_URL = Constants.SIGN_UP_URL + "" + getIntent().getExtras().getString(Constants.ID) + "/activation";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Snippets.hideKeyboard(this);
        ((ProgressView) findViewById(R.id.validationButtonProgress)).start();
        NetworkRequests.postRequest(ACTIVATION_URL, new Interfaces.NetworkListeners() {
            @Override
            public void onResponse(String response, String tag) {
                ((ProgressView) findViewById(R.id.validationButtonProgress)).stop();
                ValidationResponseModel validationResponseModel = null;
                try {
                    validationResponseModel = JSON.parseObject(response, ValidationResponseModel.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (validationResponseModel != null && validationResponseModel.isStatus()) {
                    Toast.makeText(ValidationCodeActivity.this, "کد فعال سازی درست است", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ValidationCodeActivity.this, HomePageActivity.class);
//                    intent.putExtra(SIGN_UP_SUCCESS_MESSAGE, "You registered successfully");
                    startActivity(intent);
                }
            }

            @Override
            public void onError(VolleyError error, String tag) {
                ((ProgressView) findViewById(R.id.validationButtonProgress)).stop();

            }

            @Override
            public void onOffline(String tag) {
                ((ProgressView) findViewById(R.id.validationButtonProgress)).stop();

            }
        }, "", jsonObject.toString());

    }

    private void showError(String message) {
        Snackbar.make(findViewById(R.id.root), message, Snackbar.LENGTH_LONG).show();
    }

}
