package sk.scolopax.reservrant.data.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.scolopax.reservrant.R;
import sk.scolopax.reservrant.data.Customer;
import sk.scolopax.reservrant.data.dbs.DatabaseContract;

/**
 * Created by Matej Sluka on 08/08/2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {


    private static final int VIEW_TYPE_CUSTOMER = 0;

    private Cursor cursor;
    final private CustomerAdapterOnClickHandler clickHandler;


    public CustomerAdapter(CustomerAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    public Customer getItem(int position)
    {
        if ( null == cursor ) return null;
        cursor.moveToPosition(position);
        return new Customer(cursor);
    }

    public interface CustomerAdapterOnClickHandler {
        void onClick(Long id, Customer customer);
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if ( parent instanceof RecyclerView ) {
            int layoutId = -1;
            switch (viewType) {
                case VIEW_TYPE_CUSTOMER: {
                    layoutId = R.layout.list_item_customer;
                    break;
                }
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            view.setFocusable(true);
            return new CustomerViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        cursor.moveToPosition(position);

        String idCustomer = cursor.getString(DatabaseContract.TableCustomers.COL_IDX_ID);
        String firstName = cursor.getString(DatabaseContract.TableCustomers.COL_IDX_NAME_FIRST);
        String lastName = cursor.getString(DatabaseContract.TableCustomers.COL_IDX_NAME_LAST);

      //  holder.txtIdCustomer.setText(idCustomer);
        holder.txtNameFirst.setText(firstName + " " + lastName);
       // holder.txtNameLast.setText(lastName);
    }

    @Override
    public int getItemCount() {
        if ( null == cursor ) return 0;
        return cursor.getCount();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView txtNameFirst; //txtIdCustomer,,txtNameLast;

        public CustomerViewHolder(View view) {
            super(view);
           // txtIdCustomer = (TextView) view.findViewById(R.id.txt_idcustomer);
            txtNameFirst = (TextView) view.findViewById(R.id.txt_firstname);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            cursor.moveToPosition(adapterPosition);
            int idColumn = cursor.getColumnIndex(DatabaseContract.TableCustomers.COL_ID);
            clickHandler.onClick(cursor.getLong(idColumn),getItem(adapterPosition));
        }
    }

    public void refreshCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }
}
