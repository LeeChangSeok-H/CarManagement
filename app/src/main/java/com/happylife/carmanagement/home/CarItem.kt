package com.happylife.carmanagement.home

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class CarItem (
    val date : String? = "",
    val time : String? = "",
    val companyName : String? = "",
    val customerPhoneNumber : String? = "",
    val carNumber : String? = "",
    val carType : String? = "",
    val drivenDistance : String? = "",
    val workList : String? = "" ,
    val etc : String? = "") : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(companyName)
        parcel.writeString(customerPhoneNumber)
        parcel.writeString(carNumber)
        parcel.writeString(carType)
        parcel.writeString(drivenDistance)
        parcel.writeString(workList)
        parcel.writeString(etc)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CarItem> {
        override fun createFromParcel(parcel: Parcel): CarItem {
            return CarItem(parcel)
        }

        override fun newArray(size: Int): Array<CarItem?> {
            return arrayOfNulls(size)
        }
    }
}