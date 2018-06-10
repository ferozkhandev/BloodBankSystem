package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class Need_Blood extends AppCompatActivity {
    private Button btn_next;
    private int counter = 0;
    private String bloodGroup, city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need__blood);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_next = findViewById(R.id.btn_next);


        final Spinner citizenship = findViewById(R.id.country_names);
        String[] countries = getResources().getStringArray(R.array.countries);
        Arrays.sort(countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        citizenship.setAdapter(adapter);
        citizenship.setSelection(counter);

        citizenship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (citizenship.getSelectedItem().toString().equals("Pakistan"))
                {
                    final Spinner city = (Spinner)findViewById(R.id.city_names);
                    String[] cities = getResources().getStringArray(R.array.cities);
                    Arrays.sort(cities);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item,cities);
                    city.setAdapter(adapter);
                    city.setSelection(0);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Sorry, this app is not supported in your region yet.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Spinner spinner = findViewById(R.id.bloodgroup_spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.blood_group, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try
                {
                    bloodGroup = spinner.getSelectedItem().toString();
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),"Error: "+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setAdapter(adapter1);
        spinner.setSelection(0);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Need_Blood.this,Donors_List.class);
                intent.putExtra("bloodgroup",bloodGroup);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
