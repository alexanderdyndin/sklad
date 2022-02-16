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

    @Query("select * from supplier")
    fun getSuppliers(): Flow<List<Supplier>>

    @Query("select * from product_type")
    fun getTypes(): Flow<List<ProductType>>

    @Query("select * from product")
    fun getProducts(): Flow<List<Product>>

    @Query("insert into User(username, password, user_type, name, surname, patronymic, phone) values (:username, :password, :userType, :name, :surname, :patronymic, :phone)")
    suspend fun addUser(username: String, password: String, userType: UserType, name: String, surname: String, patronymic: String?, phone: String)

    @Query("insert into Client(company, email, phone) values (:company, :email, :phone)")
    suspend fun addClient(company: String, email: String, phone: String)

    @Query("insert into Supplier(company, email, phone) values (:company, :email, :phone)")
    suspend fun addSupplier(company: String, email: String, phone: String)

    @Query("insert into product_type(type) values (:type)")
    suspend fun addTypes(type: String)

    @Query("insert into product(name, unit, price, product_type_id) values (:name, :unit, :price, :typeId)")
    suspend fun addProduct(name: String, unit: String, price: Double, typeId: Int)

}