package com.example.mypar.gift.Adapter;

        import android.app.Activity;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.TextView;

        import com.example.mypar.gift.R;

        import java.util.ArrayList;
        import java.util.Arrays;

public class custom_spinner_adapter extends BaseAdapter{

    private Context context;
    private int layoutResourceId;
    private ArrayList<String> List = new ArrayList<>();

    public custom_spinner_adapter(Context context, int layoutResourceId, String[] list){
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.List.addAll(Arrays.asList(list));
    }

    @Override
    public int getCount() {
        return (List == null) ? 0: List.size();
    }

    @Override
    public Object getItem(int position) {
        return List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        TextView text = convertView.findViewById(R.id.spinner_textview);
        if(List.get(position) != null){
            text.setText(List.get(position));
        }

        return convertView;
    }
}
