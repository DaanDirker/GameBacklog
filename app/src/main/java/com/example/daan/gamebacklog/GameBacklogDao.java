package com.example.daan.gamebacklog;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GameBacklogDao {

    @Query("SELECT * FROM gamebacklog")
    public List<GameBacklog> getAllGameBacklog();

    @Insert
    public void insertGameBacklog(GameBacklog gameBacklog);

    @Delete
    public void removeGameBacklog(GameBacklog gameBacklog);

    @Update
    public void updateGameBacklog(GameBacklog gameBacklog);
}
