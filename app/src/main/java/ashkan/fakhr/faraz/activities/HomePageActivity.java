package ashkan.fakhr.faraz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import ashkan.fakhr.faraz.R;
import ashkan.fakhr.faraz.utilities.Snippets;

/**
 * Created by ${Ashkan} on ${12/9/2016}.
 */

public class HomePageActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Snippets.setupUI(this, findViewById(R.id.root));

//        findViewById(R.id.activationButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

}
