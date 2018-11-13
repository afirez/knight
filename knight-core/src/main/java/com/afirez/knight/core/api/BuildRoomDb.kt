package com.afirez.knight.core.api

import android.arch.persistence.room.RoomDatabase
import android.content.Context

interface BuildRoomDb<DB: RoomDatabase> {
    fun buildRoomDb(context: Context, dbClass: Class<DB>, builder: RoomDatabase.Builder<DB>)
}

