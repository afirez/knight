package com.afirez.knight.core.api

import androidx.room.RoomDatabase
import android.content.Context

interface BuildRoomDb<DB: RoomDatabase> {
    fun buildRoomDb(context: Context, dbClass: Class<DB>, builder: RoomDatabase.Builder<DB>)
}

