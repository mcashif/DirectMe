package com.mks.apple.directme;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ClientListAdaptor extends BaseAdapter implements OnClickListener {
    private Context context;

    private List<ClientDetail> listClientDetails;

    public ClientListAdaptor(Context context, List<ClientDetail> listPhonebook) {
        this.context = context;
        this.listClientDetails = listPhonebook;
    }

    public int getCount() {
        return listClientDetails.size();
    }

    public Object getItem(int position) {
        return listClientDetails.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ClientDetail entry = listClientDetails.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_rowlayout, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.clintname);
        tvName.setText(entry.getName());

        TextView tvPhone = (TextView) convertView.findViewById(R.id.clintinst);
        tvPhone.setText(entry.getInstructions());

        TextView tvAddress = (TextView) convertView.findViewById(R.id.clintaddress);
        tvAddress.setText(entry.getAddress());

        // Set the onClick Listener on this button
        Button btnRemove = (Button) convertView.findViewById(R.id.btnRemove);
        btnRemove.setFocusableInTouchMode(false);
        btnRemove.setFocusable(false);
        btnRemove.setOnClickListener(this);
        // Set the entry, so that you can capture which item was clicked and
        // then remove it
        // As an alternative, you can use the id/position of the item to capture
        // the item
        // that was clicked.
        btnRemove.setTag(entry);

        // btnRemove.setId(position);


        return convertView;
    }

    @Override
    public void onClick(View view) {

        final View vw=view;

        new AlertDialog.Builder(context)
                .setTitle("Delete entry")
                .setMessage("Are you sure you have delivered and remove this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ClientDetail entry = (ClientDetail) vw.getTag();
                        listClientDetails.remove(entry);
                        // listPhonebook.remove(view.getId());
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void showDialog(ClientDetail entry) {
        // Create and show your dialog
        // Depending on the Dialogs button clicks delete it or do nothing
    }

}
