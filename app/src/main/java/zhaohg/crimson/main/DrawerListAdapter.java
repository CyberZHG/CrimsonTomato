package zhaohg.crimson.main;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import zhaohg.crimson.R;

public class DrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DrawerItem> drawerItems;

    public DrawerListAdapter(Context context, ArrayList<DrawerItem> drawerItems) {
        this.context = context;
        this.drawerItems = drawerItems;
    }

    @Override
    public int getCount() {
        return this.drawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return this.drawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_list_item, null);
        }
        ImageView icon = (ImageView) convertView.findViewById(R.id.drawer_list_item_icon);
        TextView text = (TextView) convertView.findViewById(R.id.drawer_list_item_text);
        icon.setImageResource(drawerItems.get(position).getIcon());
        text.setText(drawerItems.get(position).getText());
        return convertView;
    }

}
