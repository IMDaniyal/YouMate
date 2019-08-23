package com.example.youmate.adapter;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {videomodel.class},version = 1)
    public abstract class MyDatabase extends RoomDatabase
{

        private static MyDatabase INSTANCE;

        public abstract videoDao videoDao();


        public static MyDatabase getAppDatabase(Context context) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "user-database2").build();
            }
            return INSTANCE;
        }
        public static void destroyInstance() {
            INSTANCE = null;
        }
    }


