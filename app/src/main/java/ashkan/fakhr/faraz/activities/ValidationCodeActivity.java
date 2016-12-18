package ashkan.fakhr.faraz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import ashkan.fakhr.faraz.R;
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

        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEnterCode();
            }
        });
    }

    private void btnEnterCode() {


    }

}
