package sk.scolopax.reservrant.ui;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sk.scolopax.reservrant.R;
import sk.scolopax.reservrant.data.ReserveTableTask;
import sk.scolopax.reservrant.data.dbs.DatabaseContract;



/**
 * Created by Matej Sluka on 13/08/2017.
 */

public class TableDetailDialog extends Dialog {

    private long idTable, idCustomer;
    private Context context;

    public TableDetailDialog(@NonNull Context context, long idTable, long idCusomer) {
        super(context);
        this.idTable = idTable;
        this.idCustomer = idCusomer;
        this.context = context;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_table_detail);

        Button btnCancel = (Button) findViewById(R.id.btn_dialog_cancel);
        Button btnReserve = (Button) findViewById(R.id.btn_dialog_reserve);
        TextView txtTableId = (TextView) findViewById(R.id.txt_dialog_id_table);
        TextView txtBody = (TextView) findViewById(R.id.txt_dialog_info_body);

        txtTableId.setText(context.getString(R.string.table_id) + " " + String.valueOf(idTable));

        Cursor cursor = context.getContentResolver().query(
                DatabaseContract.TableTables.CONTENT_URI,
                DatabaseContract.TableTables.getProjection(),
                DatabaseContract.TableTables._ID + " = ? ",
                new String[] {String.valueOf(idTable)},
                null
        );

        if (cursor !=null) {
            cursor.moveToFirst();
            String idCustomerOnTable = cursor.getString(DatabaseContract.TableTables.COL_IDX_ID_CUSTOMER);
            Boolean available = cursor.getInt(DatabaseContract.TableTables.COL_IDX_AVAILABLE) == 1 ? true : false;

            String reservedCustomerText;
            if (idCustomerOnTable == null)
            {
                // after app init, reserved tables have not customer
                reservedCustomerText = context.getString(R.string.table_reserved);
            }
            else
            {
                // reserved tables via app have customer
                reservedCustomerText = context.getString(R.string.table_reserved_for) +  getCustomerName(Long.valueOf(idCustomerOnTable));
            }

            txtBody.setText( available ? context.getString(R.string.table_available) :reservedCustomerText);
            btnCancel.setVisibility(available ? View.GONE : View.VISIBLE);
            btnReserve.setVisibility(available ? View.VISIBLE : View.GONE);

        }


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long params[] = {idTable, idCustomer,MakeReservation.RESERVATION_CANCEL};
                new MakeReservation(context).execute(params);
                TableDetailDialog.this.dismiss();
            }
        });

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Long params[] = {idTable, idCustomer,MakeReservation.RESERVATION_PLACE};
             new MakeReservation(context).execute(params);
             TableDetailDialog.this.dismiss();
            }
        });

    }

    private String getCustomerName(long idCustomer)
    {
        String name = "";

        Cursor cursor = context.getContentResolver().query(
                DatabaseContract.TableCustomers.CONTENT_URI,
                DatabaseContract.TableCustomers.getProjection(),
                DatabaseContract.TableCustomers._ID + " = ? ",
                new String[] {String.valueOf(idCustomer)},
                null
        );

        if (cursor !=null) {
            cursor.moveToFirst();
            name = cursor.getString(DatabaseContract.TableCustomers.COL_IDX_NAME_FIRST)
                    + " " + cursor.getString(DatabaseContract.TableCustomers.COL_IDX_NAME_LAST);
        }
        return name;
    }

    /* Reservation task on worker thread */
    private class MakeReservation extends ReserveTableTask
    {

        public MakeReservation(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(Long result) {
        }
    }

}
