package com.work.sklad.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.work.sklad.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SkladDao {

    @Query("select * from User where username = :username and password = :password")
    fun searchUser(username: String, password: String): Flow<List<User>>

    @Query("select * from User where id = :id")
    fun searchUser(id: Int): Flow<List<User>>

    @Query("select * from client")
    fun getClients(): Flow<List<Client>>

    @Query("insert into User(username, password, user_type, name, surname, patronymic, phone) values (:username, :password, :userType, :name, :surname, :patronymic, :phone)")
    suspend fun addUser(username: String, password: String, userType: UserType, name: String, surname: String, patronymic: String?, phone: String)

    @Query("insert into Client(company, email, phone) values (:company, :email, :phone)")
    suspend fun addClient(company: String, email: String, phone: String)

    @Insert
    suspend fun insert(product: Product)

}