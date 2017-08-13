package sk.scolopax.reservrant.data.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import sk.scolopax.reservrant.R;
import sk.scolopax.reservrant.data.Table;

/**
 * Created by Matej Sluka on 09/08/2017.
 */

public class TablesAdapter extends BaseAdapter {

    private Cursor cursor;
    private Context context;
    private LayoutInflater layoutinflater;

    public TablesAdapter(Context context) {
        this.context = context;
        layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refreshCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if ( null == cursor) return 0;
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        if ( null == cursor) return null;
        cursor.moveToPosition(position);
        return new Table(cursor);
    }

    @Override
    public long getItemId(int i) {
        Table table = (Table) getItem(i);
        return table.idTable;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.grid_item_table, parent, false);
            listViewHolder.txtIdTable = (TextView)convertView.findViewById(R.id.txt_idtable);
            listViewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.fl_table);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }

        listViewHolder.txtIdTable.setText(String.valueOf(getItemId(i)));
        Table table = (Table)getItem(i);
        listViewHolder.linearLayout.setBackground(table.isAvailable ? context.getDrawable(R.drawable.table_available) : context.getDrawable(R.drawable.table_unavailable));
        return convertView;
    }

    class ViewHolder {
        TextView txtIdTable;
        LinearLayout linearLayout;
    }
}
