package com.Denzo.firl.Model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE uid = :uid")
    User getUserById(String uid);

    @Query("SELECT * FROM users WHERE sex = :sex")
    List<User> getUsersBySex(String sex);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> users);

    @Update
    void update(User user);

    @Query("DELETE FROM users")
    void deleteAll();
}
