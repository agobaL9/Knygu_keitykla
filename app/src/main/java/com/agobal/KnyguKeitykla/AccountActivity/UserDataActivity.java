package com.agobal.KnyguKeitykla.AccountActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.agobal.KnyguKeitykla.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserDataActivity extends Activity{

    private static final String TAG = "UserDataActivity";


    private EditText inputName;
    private EditText inputLastName;
    private Boolean isDataCorrect = false;

    private Spinner spinnerCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_user_data);

        inputName = findViewById(R.id.inputName);
        inputLastName = findViewById(R.id.inputLastName);
        Button btnNext = findViewById(R.id.btnNext);
        spinnerCity = findViewById(R.id.spinCity);

        btnNext.setOnClickListener(view -> {

                    String FirstName = inputName.getText().toString();
                    String LastName = inputLastName.getText().toString();
                    String CityName = spinnerCity.getSelectedItem().toString();

                    checkUserData(FirstName, LastName);
                    if(isDataCorrect)
                    {
                        storeUserData(FirstName, LastName, CityName);
                        // Launch main activity
                        Intent intent = new Intent(UserDataActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        selectCity();
    }

    private void selectCity() {


        FirebaseDatabase  database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference("City");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> areas = new ArrayList<>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("cityName").getValue(String.class);
                    areas.add(areaName);
                }

                Spinner areaSpinner = findViewById(R.id.spinCity);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<>(UserDataActivity.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkUserData(String FirstName, String LastName) {

        if(TextUtils.isEmpty(FirstName)) {
            inputName.setError("Užpildykite visus laukus!");
            return;
        }

        if(TextUtils.isEmpty(LastName)) {
            inputLastName.setError("Užpildykite visus laukus!");
            return;
        }


        if(!FirstName.matches("[a-zA-Z.? ]*") || !LastName.matches("[a-zA-Z.? ]*"))
        {
            Toast.makeText(getApplicationContext(),
                    "Netinka specialūs simboliai!", Toast.LENGTH_LONG).show();
            isDataCorrect =false;
        }
        else
            isDataCorrect=true;

    }

    private void storeUserData(String FirstName, String LastName, String CityName) {
        FirebaseDatabase  database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference("Users");

        Map<String, Object> hopperUpdates = new HashMap<>();

        hopperUpdates.put(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/firstName", FirstName);
        hopperUpdates.put(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/lastName", LastName);
        hopperUpdates.put(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/cityName", CityName);
        mDatabaseRef.updateChildren(hopperUpdates);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),
                "Turite užpildyti duomenis!", Toast.LENGTH_LONG).show();
    }
}

