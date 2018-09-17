package com.example.bibek.myvehicle;

/**
 * Created by Pratik on 5/16/2017.
 */

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button profileSave= (Button) getActivity().findViewById(R.id.btnProfileSave);
        profileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editName= (EditText) getActivity().findViewById(R.id.usernameValue);
                String un = editName.getText().toString();
                MainActivity.username = un;
                EditText p1= (EditText) getActivity().findViewById(R.id.password1Value);
                String strP1 = p1.getText().toString();
                EditText p2= (EditText) getActivity().findViewById(R.id.password2Value);
                String strP2 = p2.getText().toString();
                if (strP1.compareTo(strP2)==0 && strP1!="" && un != ""){
                    MainActivity.password = strP2;
                    FragmentManager fragmentManager=getFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    Home_fragment hf=new Home_fragment();
                    fragmentTransaction.replace(R.id.carPlace, hf);
                    fragmentTransaction.commit();
                }
                else{
                    DisplayToast("Passwords must be the same or null. Username cannot be null.");
                }
            }
        });

        Button profileCancel= (Button) getActivity().findViewById(R.id.btnProfileCancel);
        profileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                Home_fragment hf=new Home_fragment();
                fragmentTransaction.replace(R.id.carPlace, hf);
                fragmentTransaction.commit();
            }
        });
    }

    private void DisplayToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}

