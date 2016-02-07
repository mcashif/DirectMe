package com.mks.apple.directme;


import android.os.Parcel;
import android.os.Parcelable;

public class ClientDetail implements Parcelable {
    private String name;
    private String address;
    private String instruction;
    private String lat;
    private String log;

    // Constructor for the Phonebook class
    public ClientDetail(String name, String adress, String instruction, String lat, String log) {
        super();
        this.name = name;
        this.address = adress;
        this.instruction = instruction;
        this.lat = lat;
        this.log = log;
    }

    // Getter and setter methods for all the fields.
    // Though you would not be using the setters for this example,
    // it might be useful later.
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String adres) {
        this.address = adres;
    }
    public String getInstructions() {
        return instruction;
    }
    public void setInstructions(String ins) {
        this.instruction = ins;
    }

    public String getLatitude() {
        return lat;
    }
    public void setLatitude(String lt) {
        this.lat = lt;
    }
    public String getLongitude() {
        return log;
    }
    public void setLongitude(String lg) {
        this.log = lg;
    }


    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        // TODO Auto-generated method stub
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(instruction);
        dest.writeString(lat);
        dest.writeString(log);
    }

    public ClientDetail(Parcel in) {
        name = in.readString();
        address = in.readString();
        instruction = in.readString();
        lat = in.readString();
        log = in.readString();

    }

    public static final Parcelable.Creator<ClientDetail> CREATOR = new Parcelable.Creator<ClientDetail>() {
        public ClientDetail createFromParcel(Parcel in) {
            return new ClientDetail(in);
        }

        public ClientDetail[] newArray(int size) {
            return new ClientDetail[size];
        }
    };
}
