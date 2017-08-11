package edu.cnm.bootcamp.natedaag.winning.helpers;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.cnm.bootcamp.natedaag.winning.R;
import edu.cnm.bootcamp.natedaag.winning.entities.Pick;
import edu.cnm.bootcamp.natedaag.winning.entities.PickValue;

/**
 * Created by natedaag on 8/9/17.
 */


public class PickAdapter extends ArrayAdapter<Pick> {

// pick_layout

    @LayoutRes int layoutId;

    public PickAdapter(Context context, @LayoutRes int layoutId, List<Pick> items) {
        super(context, layoutId, items);
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(layoutId, null);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Pick pick = getItem(position);
        TextView view = null;
        if(pick.getPicked() != null) {
            view = (TextView) layout.findViewById(R.id.pick_date);
            view.setText(format.format(pick.getPicked()));
        }
        ArrayList<PickValue> values = new ArrayList<>((pick.getNewValues() == null) ? pick.getValues() : pick.getNewValues());
        Collections.sort(values);
        for (int i = 0; i < values.size(); i++) {
            view = (TextView) layout.getChildAt(i + 1);
            view.setText(Integer.toString(values.get(i).getValue()));
        }
        return layout;
    }
}
