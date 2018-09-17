package com.example.bibek.myvehicle;

public class VehicleLogs {
    public int vehicleType;
    public String driverName;
    public String regoNumber;
    public String startTime;
    public String firstBreak;
    public String secondBreak;
    public String endTime;

    VehicleLogs() {
        vehicleType=-1;
        driverName=null;
        regoNumber=null;
        startTime=null;
        firstBreak=null;
        secondBreak=null;
        endTime=null;
    }

    VehicleLogs(int vehicle, String driver, String rego, String start, String first, String second, String last){
        vehicleType = vehicle;
        driverName=driver;
        regoNumber=rego;
        startTime=start;
        firstBreak=first;
        secondBreak=second;
        endTime=last;
    }
}

