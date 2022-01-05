package com.work.sklad.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.work.sklad.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SkladDao {
    @Query("select * from Product")
    suspend fun getArrival(): List<Product>

    @Query("select * from Expenditure")
    suspend fun getExpenditure(): List<Expenditure>

    @Query("select * from flow_composition")
    suspend fun getFlowComposition(): List<FlowComposition>

    @Query("select * from inventory_balance")
    suspend fun getInventoryBalance(): List<InventoryBalance>

    @Query("select * from arrive_invoice_composition")
    fun getInvoiceComposition(): Flow<List<ArriveInvoiceComposition>>

    @Query("select * from Authorization where username = :username and password = :password")
    fun searchUser(username: String, password: String): Flow<List<Authorization>>

    @Insert
    suspend fun addUser(authorization: Authorization)

    @Insert
    suspend fun insert(product: Product)

}