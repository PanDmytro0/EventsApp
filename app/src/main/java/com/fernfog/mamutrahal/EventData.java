package com.fernfog.mamutrahal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class EventData implements Parcelable {
    String name;
    String buy;
    String date;
    String desccc;
    String image;
    String location;
    String price;
    String time;
    int type;

    public EventData(String name, String buy, String date, String desccc, String image, String location, String price, String time, int type) {
        this.name = name;
        this.buy = buy;
        this.date = date;
        this.desccc = desccc;
        this.image = image;
        this.location = location;
        this.price = price;
        this.time = time;
        this.type = type;
    }

    protected EventData(Parcel in) {
        name = in.readString();
        buy = in.readString();
        date = in.readString();
        desccc = in.readString();
        image = in.readString();
        location = in.readString();
        price = in.readString();
        time = in.readString();
        type = in.readInt();
    }

    public static final Creator<EventData> CREATOR = new Creator<EventData>() {
        @Override
        public EventData createFromParcel(Parcel in) {
            return new EventData(in);
        }

        @Override
        public EventData[] newArray(int size) {
            return new EventData[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getBuy() {
        return buy;
    }

    public String getDate() {
        return date;
    }

    public String getDesccc() {
        return desccc;
    }

    public String getImage() {
        return image;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(buy);
        parcel.writeString(date);
        parcel.writeString(desccc);
        parcel.writeString(image);
        parcel.writeString(location);
        parcel.writeString(price);
        parcel.writeString(time);
        parcel.writeInt(type);
    }
}
