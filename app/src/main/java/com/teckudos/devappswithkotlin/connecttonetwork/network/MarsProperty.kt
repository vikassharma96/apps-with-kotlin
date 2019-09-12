package com.teckudos.devappswithkotlin.connecttonetwork.network

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// if our data class has property name that matches property in our json response moshi fills values
/*data class MarsProperty(
    val id: String,
    @Json(name = "img_src") val imgSrcUrl: String,
    val type: String,
    val price: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(imgSrcUrl)
        parcel.writeString(type)
        parcel.writeDouble(price)
    }

    override fun describeContents(): Int {
        return 0 // used by android to share active file descriptor between processes
    }

    companion object CREATOR : Parcelable.Creator<MarsProperty> {
        override fun createFromParcel(parcel: Parcel): MarsProperty {
            return MarsProperty(parcel)
        }

        override fun newArray(size: Int): Array<MarsProperty?> {
            return arrayOfNulls(size)
        }
    }
}*/

// Parcel and Parcelables - Parceling is a way of sharing objects between different processes by flattening
// an object into streams of data called parcel. Complex object can be stored into the parcel and then
// recreated from the parcel by implementing the parcelable interface and they become parcelable objects.
// each value is written in sequence in parcel object recreated by reading data from parcel in the same order
// which was written to populate data in a new object.
// A bundle is parcelable object that contain key value store of parcelable objects

// as we are using plugin kotlin-android-extensions -- we can simply use @Parcelize

@Parcelize
data class MarsProperty(
    val id: String,
    @Json(name = "img_src") val imgSrcUrl: String,
    val type: String,
    val price: Double
) : Parcelable

