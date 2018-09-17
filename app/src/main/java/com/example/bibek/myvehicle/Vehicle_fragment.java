package com.example.bibek.myvehicle;



import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.util.Calendar;

public class Vehicle_fragment extends Fragment  {
    static int hour, min, sec, day, month, yr;
    private TrackGPS gps;
    double latitude;
    double longitude;
    public Vehicle_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vehicle_fragment, container, false);
        int vehicle = getArguments().getInt("vehicle");
        //Modify label
        TextView vehicleLabel = (TextView) view.findViewById(R.id.car);
        vehicleLabel.setText(MainActivity.pageNames[vehicle]);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Button save = (Button) getActivity().findViewById(R.id.btnSaveEntry);
        final Button startB = (Button) getActivity().findViewById(R.id.btnStartTime);
        final Button firstB = (Button) getActivity().findViewById(R.id.btnFirstBreak);
        final Button secondB = (Button) getActivity().findViewById(R.id.btnSecondBreak);
        final Button endB = (Button) getActivity().findViewById(R.id.btnEndTime);
        final TextView startTm, firstTm, secondTm, endTm, nameTm, regoTm;
        nameTm = (TextView) getActivity().findViewById(R.id.driverName);
        regoTm = (TextView) getActivity().findViewById(R.id.regoNumber);
        startTm = (TextView) getActivity().findViewById(R.id.startTime);
        firstTm = (TextView) getActivity().findViewById(R.id.firstBreak);
        secondTm = (TextView) getActivity().findViewById(R.id.secondBreak);
        endTm = (TextView) getActivity().findViewById(R.id.endTime);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startTime = startTm.getText().toString();
                String firstTime = firstTm.getText().toString();
                String secondTime= secondTm.getText().toString();
                String endTime= endTm.getText().toString();
                String driverName= nameTm.getText().toString();
                String regoNumber= regoTm.getText().toString();

                if (startTime.equals("") || firstTime.equals("") || secondTime.equals("") || endTime.equals("")
                        || driverName.equals("") || regoNumber.equals("")){
                    DisplayToast("Entry not saved as not all data entered. Complete all entries and try again.");
                }
                else {
                    int vehicle = getArguments().getInt("vehicle");
                    VehicleLogs dl= new VehicleLogs(vehicle, driverName, regoNumber, startTime, firstTime, secondTime, endTime);// 0 mon, 1 Tues etc
                    MainActivity.entries.add(dl);
                    DisplayToast("Entry saved.");

                }
                startB.setVisibility(View.VISIBLE);
                startTm.setText("");
                firstB.setVisibility(View.INVISIBLE);
                firstTm.setText("");
                secondB.setVisibility(View.INVISIBLE);
                secondTm.setText("");
                endB.setVisibility(View.INVISIBLE);
                endTm.setText("");
                nameTm.setText("");
                regoTm.setText("");
            }
        });

        startB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTime = "Start: ";
                sTime = sTime + getTimeString();
                startTm.setText(sTime);
                startB.setVisibility(View.INVISIBLE);
                firstB.setVisibility(View.VISIBLE);
            }
        });

        firstB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTime = "1st break: ";
                sTime = sTime + getTimeString();
                firstTm.setText(sTime);
                firstB.setVisibility(View.INVISIBLE);
                secondB.setVisibility(View.VISIBLE);
            }
        });

        secondB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTime = "2nd break: ";
                sTime = sTime + getTimeString();
                secondTm.setText(sTime);
                secondB.setVisibility(View.INVISIBLE);
                endB.setVisibility(View.VISIBLE);
            }
        });

        endB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTime = "End: ";
                sTime = sTime + getTimeString();
                endTm.setText(sTime);
                endB.setVisibility(View.INVISIBLE);
            }
        });
    }

    private String getTimeString(){
        Calendar date = Calendar.getInstance();
        yr = date.get(Calendar.YEAR);
        month = date.get(Calendar.MONTH);
        day = date.get(Calendar.DAY_OF_MONTH);
        hour = date.get(Calendar.HOUR_OF_DAY);
        min = date.get(Calendar.MINUTE);
        sec = date.get(Calendar.SECOND);
        String sTime = Integer.toString(day) + "/" + Integer.toString(month) + "/" +
                Integer.toString(yr) + " " + Integer.toString(hour) + ":" + Integer.toString(min);
        return sTime;
    }

    private void DisplayToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    public void onDestroy() {
        super.onDestroy();
        gps.stopUsingGPS();
    }

}

