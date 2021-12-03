package com.work.sklad.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.work.sklad.data.model.*
import io.reactivex.rxjava3.core.Single

@Dao
interface SkladDao {
    @Query("select * from Product")
    fun getArrival(): Single<List<Product>>

    @Query("select * from Expenditure")
    fun getExpenditure(): Single<List<Expenditure>>

    @Query("select * from flow_composition")
    fun getFlowComposition(): Single<List<FlowComposition>>

    @Query("select * from flow_types")
    fun getFlowTypes(): Single<List<FlowTypes>>

    @Query("select * from inventory_balance")
    fun getInventoryBalance(): Single<List<InventoryBalance>>

    @Query("select * from invoice_composition")
    fun getInvoiceComposition(): Single<List<InvoiceComposition>>

    @Insert
    fun insert(product: Product)

}