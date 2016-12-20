package ashkan.fakhr.faraz.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.rey.material.widget.ProgressView;

import java.util.List;

import ashkan.fakhr.faraz.R;
import ashkan.fakhr.faraz.models.FormModel;
import ashkan.fakhr.faraz.models.TopicModel;
import ashkan.fakhr.faraz.utilities.Constants;
import ashkan.fakhr.faraz.utilities.Interfaces;
import ashkan.fakhr.faraz.utilities.NetworkRequests;
import ashkan.fakhr.faraz.utilities.Snippets;

/**
 * Created by ${Ashkan} on ${12/9/2016}.
 */

public class HomePageActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Snippets.setupUI(this, findViewById(R.id.root));

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadTopics();
            }
        });

    }

    private void downloadTopics() {
        ((ProgressView) findViewById(R.id.toolbar).findViewById(R.id.toolbarProgress)).start();
        NetworkRequests.getRequest(Constants.TOPICS_URL, new Interfaces.NetworkListeners() {
            @Override
            public void onResponse(String response, String tag) {
                List<TopicModel> formModels = null;
                try {
                    formModels = JSON.parseArray(response, TopicModel.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (formModels != null) {
                    Intent intent = new Intent(HomePageActivity.this, FormActivity.class);
                    intent.putExtra(Constants.DATA, response);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(VolleyError error, String tag) {

            }

            @Override
            public void onOffline(String tag) {

            }
        }, "");
    }

}
