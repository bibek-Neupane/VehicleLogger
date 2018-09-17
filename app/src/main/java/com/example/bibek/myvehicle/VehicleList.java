package com.example.bibek.myvehicle;

/**
 * Created by Pratik on 5/16/2017.
 */
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link ListFragment} subclass.
 */
public class VehicleList extends ListFragment {
    static ArrayList<String> strRes = new ArrayList<String>();

    public VehicleList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_vehicle_list, container, false);
        //Modify labels
        Button back = (Button) v.findViewById(R.id.btnVehicleListReturn);
        int vehicle = getArguments().getInt("vehicle");
        back.setText("Return to " + MainActivity.pageNames[vehicle]);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int i=0;
        VehicleLogs dl;
        int vehicle = getArguments().getInt("vehicle");

        Iterator<VehicleLogs> itr = MainActivity.entries.iterator();
        while(itr.hasNext()){
            dl=itr.next();
            if (vehicle== dl.vehicleType) {
                strRes.add(dl.driverName + " " + dl.regoNumber+ " " + dl.startTime+" "+ dl.firstBreak+"  "+dl.secondBreak+" "+ dl.endTime);
            }
        }
        String[] strResult=new String[strRes.size()];
        Iterator<String> itrStr = strRes.iterator();
        while(itrStr.hasNext()){
            strResult[i++]=itrStr.next();
        }
        strRes.clear();
        if (strResult==null)
            strResult[0]="First data to display";
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, strResult));

    }

    public void onListItemClick(ListView parent, View v,
                                int position, long id)
    {
//        Toast.makeText(getActivity(),
//            "You have selected " + strResult[position],
        //           Toast.LENGTH_SHORT).show();
    }
}
