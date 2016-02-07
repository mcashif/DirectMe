package com.mks.apple.directme;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;

import android.widget.TextView;

import android.app.ListActivity;

import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DirectionListActivity extends Activity {

    List<ClientDetail> listOfClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_list);

        ListView list = (ListView) findViewById(R.id.results);
        list.setClickable(true);

        listOfClients = new ArrayList<ClientDetail>();

        listOfClients.add(new ClientDetail("Near Home", "0332-8457013", "House 42B Canal View Town", "33.5985174", "73.1288838"));
        listOfClients.add(new ClientDetail("Water Filter", "0332-8457013", "House 42B Canal View Town", "33.72066", "73.07301"));
        listOfClients.add(new ClientDetail("Home", "0332-8457013", "House 42B Canal View Town", "33.59589", "73.12867"));
        listOfClients.add(new ClientDetail("Location-4", "0332-8457013", "House 42B Canal View Town","33.50684","73.19020"));
        listOfClients.add(new ClientDetail("Location-5", "0332-8457013", "House 42B Canal View Town","33.55721","73.01030"));
        listOfClients.add(new ClientDetail("Location-6", "0332-8457013", "House 42B Canal View Town","33.59725","72.71367"));

        ClientListAdaptor adapter = new ClientListAdaptor(this, listOfClients);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
                System.out.println("sadsfsf");
                openPathMap(position);
            }
        });

        list.setAdapter(adapter);
    }

    private void openPathMap(int index) {

        Intent mapIntent = new Intent(DirectionListActivity.this, MapsActivity.class);
        mapIntent.putExtra("lat", listOfClients.get(index).getLatitude()); //Optional parameters
        mapIntent.putExtra("lon", listOfClients.get(index).getLongitude()); //Optional parameters
        mapIntent.putExtra("add", listOfClients.get(index).getAddress()); //Optional parameters
        mapIntent.putExtra("inf", listOfClients.get(index).getInstructions()); //Optional parameters
        DirectionListActivity.this.startActivity(mapIntent);

    }

    public void onBtnClickedList(View v){
        if(v.getId() == R.id.showall){

            Intent mapIntent = new Intent(DirectionListActivity.this, MapAllActivity.class);

            Bundle b=new Bundle();
            b.putParcelableArrayList("KEYCLIENT", (ArrayList<ClientDetail>) listOfClients);
            mapIntent.putExtras(b);

            DirectionListActivity.this.startActivity(mapIntent);
        }
    }




}
