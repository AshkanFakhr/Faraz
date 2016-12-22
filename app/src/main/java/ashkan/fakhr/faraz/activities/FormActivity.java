package ashkan.fakhr.faraz.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.List;

import ashkan.fakhr.faraz.R;
import ashkan.fakhr.faraz.adapters.ChoiceSpinnerAdapter;
import ashkan.fakhr.faraz.adapters.TopicSpinnerAdapter;
import ashkan.fakhr.faraz.models.FormModel;
import ashkan.fakhr.faraz.models.TopicModel;
import ashkan.fakhr.faraz.utilities.Constants;
import ashkan.fakhr.faraz.utilities.Snippets;
import ashkan.fakhr.faraz.utilities.Spinner;

public class FormActivity extends AppCompatActivity {

    List<TopicModel> topicList;
    LinearLayout formLinLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = (TextView) findViewById(R.id.toolbarTitle);
        textView.setText(R.string.form_activity_title);
        setContentView(R.layout.activity_form);
        Snippets.setupUI(this, findViewById(R.id.root));
        formLinLay = (LinearLayout) findViewById(R.id.dynamicFormLinLay);
        topicList = JSON.parseArray(getIntent().getExtras().getString(Constants.DATA), TopicModel.class);
        TopicSpinnerAdapter topicSpinnerAdapter = new TopicSpinnerAdapter(this, topicList);
        final Spinner spinner = (Spinner) findViewById(R.id.topicSpinner);
        spinner.setAdapter(topicSpinnerAdapter);
        spinner.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        spinner.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner parent, View view, int position, long id) {
                spinner.setSelection(position);
                onTopicSelected(topicList.get(position));
                return false;
            }
        });
        onTopicSelected(topicList.get(0));
    }

    private void onTopicSelected(TopicModel topicModel) {
        formLinLay.removeAllViews();
        for (FormModel formModel : topicModel.getForm()) {
            switch (formModel.getType()) {
                case "text":
                    createTextRow(formModel);
                    break;
                case "choices":
                    createChoicesRow(formModel);
                    break;
                case "bool":
                    createCheckBox(formModel);
                    break;
            }
        }

        Snippets.setupUI(this, findViewById(R.id.root));
        ViewAnimator.animate(findViewById(R.id.formScrollView)).alpha(0, 1).duration(600).start();
    }

    private void createCheckBox(FormModel formModel) {
        View row = LayoutInflater.from(this).inflate(R.layout.item_bool, formLinLay, false);
        ((TextView) row.findViewById(R.id.title)).setText(formModel.getLabel());
//        ((CheckBox) row.findViewById(R.id.checkbox)).setText("ب");
        formLinLay.addView(row);

    }

    private void createChoicesRow(FormModel formModel) {

        View row = LayoutInflater.from(this).inflate(R.layout.item_options_spinner, formLinLay, false);
        final Spinner spinner = (Spinner) row.findViewById(R.id.spinner);
        ChoiceSpinnerAdapter choiceSpinnerAdapter = new ChoiceSpinnerAdapter(this, formModel.getConfiguration().getChoices());
        spinner.setAdapter(choiceSpinnerAdapter);
        spinner.getLabelView().setText(formModel.getLabel());
        spinner.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner parent, View view, int position, long id) {
                spinner.setSelection(position);
                return false;
            }
        });
        formLinLay.addView(row);

    }

    private void createTextRow(FormModel formModel) {
        View row = LayoutInflater.from(this).inflate(R.layout.item_text, formLinLay, false);
        ((TextView) row.findViewById(R.id.title)).setText(formModel.getLabel());
        formLinLay.addView(row);
    }
}