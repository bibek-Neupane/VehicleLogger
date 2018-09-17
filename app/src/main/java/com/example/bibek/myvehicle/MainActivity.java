package com.example.bibek.myvehicle;



import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    static ArrayList<VehicleLogs> entries;
    int currentPage;
    static int hour, min, sec, day, month, yr;
    static final String[] pageNames =
            {"Car", "5t Truck", "10t Truck", "Tipper", "Articulated", "Home"};
    static final int READ_BLOCK_SIZE = 100;
    static String username;
    static String password;



    public MainActivity() {
        super();
        entries = new ArrayList<VehicleLogs>();
        currentPage = 5;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Fragment frag = new Home_fragment();
        //Communicate the page title to the fragment
        Bundle args = new Bundle();
        args.putInt("vehicle", currentPage);
        frag.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.carPlace, frag);
        ft.commit();

        DBAdapter db = new DBAdapter(this);
        try {
            String destPath = "/data/data/" + getPackageName() +
                    "/databases";
            File f = new File(destPath);
            if (!f.exists()) {   // create dir and then copy db
                f.mkdirs();
                f.createNewFile();
                //---copy the db from the assets folder into
                // the databases folder---
                CopyDB(getBaseContext().getAssets().open("logs"),
                        new FileOutputStream(destPath + "/logs"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //---get all Entries in DB and put into mon_dl .. fri_dl ---
        db.open();
        Cursor c = db.getAllEntries();
        if (c.moveToFirst()) {
            do {
                setDaysEntries(c);
            } while (c.moveToNext());
        }
        db.close();
    }

    public void setDaysEntries(Cursor c) {
        String str = new String(c.getString(1));
        String str1 = new String(c.getString(2));
        String str2 = new String(c.getString(3));
        String str3 = new String(c.getString(4));
        String str4 = new String(c.getString(5));
        String str5 = new String(c.getString(6));
        String str6 = new String(c.getString(7));

        VehicleLogs dl = null;
        int i = Integer.parseInt(str);
        dl = new VehicleLogs(i, c.getString(2),
                c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7));// 0 car, 1 5t truck 2 etc
        entries.add(dl);
    }

    public void CopyDB(InputStream inputStream,
                       OutputStream outputStream) throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            FragmentManager fragmentManager=getFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            ProfileFragment hf=new ProfileFragment();
            fragmentTransaction.replace(R.id.carPlace, hf);
            fragmentTransaction.commit();
            return true;
        }
        else if (id == R.id.action_save){
            DBAdapter db = new DBAdapter(this);
            db.open();
            db.removeAll(); // remove existing entries
            Iterator<VehicleLogs> itr= entries.iterator();
            insertEntry(itr, db);
            db.close();
            return true;
        }
        else if (id == R.id.action_send) {
            final Context ctx = this;
            new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle("would you like to send an email?.")
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String[] to = {"balsys@cqu.edu.au"};
                                    String[] cc = {"balsys@cqu.edu.au"};

                                }


                                private void sendEmail(String[] emailAddresses, String[] carbonCopies,
                                                       String subject, String message) {
                                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                    emailIntent.setData(Uri.parse("mailto:"));
                                    String[] to = emailAddresses;
                                    String[] cc = carbonCopies;
                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                                    emailIntent.putExtra(Intent.EXTRA_CC, cc);
                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                                    emailIntent.putExtra(Intent.EXTRA_TEXT, message);
                                    emailIntent.setType("message/rfc822");
                                    startActivity(Intent.createChooser(emailIntent, "Email"));

                                    DBAdapter db = new DBAdapter(ctx);
                                    db.open();
                                    db.removeAll(); // remove existing entries
                                    db.close();
                                    entries.clear();

                                }
                            }

                    )
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // do nothing
                                }
                            }
                    ).create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void insertEntry(Iterator<VehicleLogs> itr, DBAdapter db) {
        long id;
        VehicleLogs dl;
        // now add entries
        while (itr.hasNext()) {
            dl = itr.next();
            id = db.insertEntry(dl.vehicleType, dl.driverName, dl.regoNumber, dl.startTime, dl.firstBreak, dl.secondBreak, dl.endTime);
        }
    }


    private void showCurrentPage() {
        //Cause the friday and home pages to loop
        if (currentPage > 5) currentPage = 0;
        if (currentPage < 0) currentPage = 5;

        //HomeFragment
        if (currentPage == 5) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.carPlace, (Fragment) new Home_fragment()).commit();
        }

        //DayFragment
        else {
            Fragment frag = new Vehicle_fragment();

            //Communicate the page title to the fragment
            Bundle args = new Bundle();
            args.putInt("vehicle", currentPage);
            frag.setArguments(args);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.carPlace, frag).commit();
        }
    }

    public void clickNext(View view) {
        currentPage++;
        showCurrentPage();
    }

    public void clickPrev(View view) {
        currentPage--;
        showCurrentPage();
    }

    public void clickHome(View view) {
        currentPage = 5;
        showCurrentPage();
    }

    public void clickVehicle(View view) {
        //Retrieve day index from view's tag
        currentPage = Integer.valueOf((String) view.getTag());
        showCurrentPage();
    }

    public void clickVehicleList(View view) {
        showVehicleList();
    }

    public void showVehicleList() {
        Fragment frag = new VehicleList();

        //Communicate the day to the fragment
        Bundle args = new Bundle();
        args.putInt("vehicle", currentPage);
        frag.setArguments(args);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.carPlace, frag).commit();
    }

    public void vehicleListReturnClick(View view) {
        showCurrentPage();
    }

    public void clickProfile(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //   ft.replace(android.R.id.content, (Fragment) new Profile_fragment()).commit();
    }

    @Override public void onBackPressed(){
        final Context ctx = this;
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("Save entries to DB first?")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,	int whichButton)
                            {
                                DBAdapter db = new DBAdapter(ctx);
                                db.open();
                                db.removeAll(); // remove existing entries
                                Iterator<VehicleLogs> itr= entries.iterator();
                                insertEntry(itr, db);
                                db.close();
                                finish();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,	int whichButton){
                        finish();
                    }
                }).create().show();
    }


}
