package com.perkedel.htlauncher.enumerations

import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.perkedel.htlauncher.R

enum class EditWhich(@StringRes val select:Int, @StringRes val label:Int):Parcelable {
    // https://androidhub.wordpress.com/2011/08/03/android-intents-for-passing-data-between-activities-part-3/
    // https://stackoverflow.com/a/8765766/9079640
    Home(select = R.string.home_screen_file, label = R.string.home_screen_file_label) {
        override fun describeContents(): Int {
            return 0;
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(select)
//            dest.writeString(Resources.getSystem().getString(label))
            dest.writeString("Home")
        }

//        public final val CREATOR by Parcelable.Creator<EditWhich>(){
//            public val createFromParcel(Parcel source){
//
//        }
//        }
    },
    Pages(select = R.string.pages_folder, label = R.string.pages_folder_label) {
        override fun describeContents(): Int {
            return 0;
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(select)
//            dest.writeString(Resources.getSystem().getString(label))
            dest.writeString("Pages")
        }
    },
    Items(select = R.string.items_folder, label = R.string.items_folder_label) {
        override fun describeContents(): Int {
            return 0;
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(select)
//            dest.writeString(Resources.getSystem().getString(label))
            dest.writeString("Items")
        }
    },
    Themes(select = R.string.themes_folder, label = R.string.themes_folder_label) {
        override fun describeContents(): Int {
            return 0;
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(select)
//            dest.writeString(Resources.getSystem().getString(label))
            dest.writeString("Themes")
        }
    },
    Medias(select = R.string.medias_folder, label = R.string.medias_folder_label) {
        override fun describeContents(): Int {
            return 0;
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(select)
//            dest.writeString(Resources.getSystem().getString(label))
            dest.writeString("Medias")
        }
    },
    Shortcuts(select = R.string.shortcuts_folder, label = R.string.shortcuts_folder_label) {
        override fun describeContents(): Int {
            return 0;
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(select)
//            dest.writeString(Resources.getSystem().getString(label))
            dest.writeString("Shortcuts")
        }
    },
    Misc(select = R.string.misc_folder, label = R.string.misc_folder_label) {
        override fun describeContents(): Int {
            return 0;
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeInt(select)
//            dest.writeString(Resources.getSystem().getString(label))
            dest.writeString("Misc")
        }
    },
}