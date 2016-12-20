package ashkan.fakhr.faraz.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import ashkan.fakhr.faraz.R;
import ashkan.fakhr.faraz.models.ChoiceModel;

/**
 * Created by Amin on 09/08/2016.
 * <p>
 * This class will be used for
 */

public class ChoiceSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<ChoiceModel> modelList;

    public ChoiceSpinnerAdapter(Context context, List<ChoiceModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.row_spinner, parent, false);

        ((TextView) v.findViewById(R.id.title)).setText(modelList.get(position).getId());


        return v;
    }
}
