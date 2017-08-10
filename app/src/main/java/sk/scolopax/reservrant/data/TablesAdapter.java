package sk.scolopax.reservrant.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import sk.scolopax.reservrant.R;

/**
 * Created by scolopax on 09/08/2017.
 */

public class TablesAdapter extends BaseAdapter {

    private Cursor mCursor;
    private Context context;
    private LayoutInflater layoutinflater;

    public TablesAdapter(Context context) {
        this.context = context;
        layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refreshCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        if ( null == mCursor ) return null;
        mCursor.moveToPosition(position);
        return new Table(mCursor);
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
            listViewHolder.ll = (LinearLayout) convertView.findViewById(R.id.fl_table);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }

        listViewHolder.txtIdTable.setText(String.valueOf(getItemId(i)));
        Table t = (Table)getItem(i);
        listViewHolder.ll.setBackground(t.isAvailable ? context.getDrawable(R.drawable.table_available) : context.getDrawable(R.drawable.table_unavailable));
        return convertView;
    }

    class ViewHolder {
        TextView txtIdTable;
        LinearLayout ll;
    }
}
