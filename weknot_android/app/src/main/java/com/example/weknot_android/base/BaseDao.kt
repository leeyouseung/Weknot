package com.example.weknot_android.base

import androidx.room.*

import io.reactivex.Completable

@Dao
interface BaseDao<ET> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: ET): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: Array<out ET>): Completable

    @Update
    fun update(entity: ET): Completable

    @Delete
    fun delete(entity: ET): Completable

}
