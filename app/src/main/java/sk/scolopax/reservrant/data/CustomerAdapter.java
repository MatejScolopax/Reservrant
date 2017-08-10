package sk.scolopax.reservrant.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.scolopax.reservrant.R;

/**
 * Created by scolopax on 08/08/2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {


    private static final int VIEW_TYPE_CUSTOMER = 0;

    private Cursor mCursor;
    final private Context mContext;
    final private CustomerAdapterOnClickHandler mClickHandler;


    public CustomerAdapter(Context context, CustomerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public Customer getItem(int position)
    {
        if ( null == mCursor ) return null;
        mCursor.moveToPosition(position);
        return new Customer(mCursor);
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
        mCursor.moveToPosition(position);

        String idCustomer = mCursor.getString(DatabaseContract.TableCustomers.COL_IDX_ID);
        String firstName = mCursor.getString(DatabaseContract.TableCustomers.COL_IDX_NAME_FIRST);
        String lastName = mCursor.getString(DatabaseContract.TableCustomers.COL_IDX_NAME_LAST);

      //  holder.txtIdCustomer.setText(idCustomer);
        holder.txtNameFirst.setText(firstName + " " + lastName);
       // holder.txtNameLast.setText(lastName);
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView txtNameFirst; //txtIdCustomer,,txtNameLast;

        public CustomerViewHolder(View view) {
            super(view);
           // txtIdCustomer = (TextView) view.findViewById(R.id.txt_idcustomer);
            txtNameFirst = (TextView) view.findViewById(R.id.txt_firstname);
           // txtNameLast = (TextView) view.findViewById(R.id.txt_lastname);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int idColumn = mCursor.getColumnIndex(DatabaseContract.TableCustomers.COL_ID);
            mClickHandler.onClick(mCursor.getLong(idColumn),getItem(adapterPosition));
        }
    }

    public void refreshCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
