package com.work.sklad.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.work.sklad.data.model.*
import com.work.sklad.domain.model.InvoiceComingWithWarehouseSupplier
import com.work.sklad.domain.model.ProductWithType
import com.work.sklad.domain.model.WarehouseWithProduct
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

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

    @Query("select * from supplier")
    fun getSuppliersList(): List<Supplier>

    @Query("select * from product_type")
    fun getTypes(): Flow<List<ProductType>>

    @Query("select * from product_type")
    suspend fun getProductTypes(): List<ProductType>

    @Query("select product.id, product.name, product.unit, product.price, product_type.type " +
            "from product inner join product_type on product_type.id = product.product_type_id")
    fun getProductWithType(): Flow<List<ProductWithType>>

    @Query("select warehouse.id, warehouse.name, warehouse.free_place as [freePlace], product.name as [product], product_type.type from product inner join product_type on product_type.id = product.product_type_id inner join warehouse on warehouse.product_id = product.id")
    fun getWarehouseWithProduct(): Flow<List<WarehouseWithProduct>>

    @Query("select warehouse.id, warehouse.name, warehouse.free_place as [freePlace], product.name as [product], product_type.type from product inner join product_type on product_type.id = product.product_type_id inner join warehouse on warehouse.product_id = product.id")
    suspend fun getWarehouseWithProductList(): List<WarehouseWithProduct>

    @Query("select invoice_coming.id, product.name as [product], warehouse.name as [warehouse], invoice_coming.price, invoice_coming.count, invoice_coming.manufactureDate, invoice_coming.expirationDate, supplier.company from invoice_coming inner join warehouse on invoice_coming.warehouse_id = warehouse.id inner join product on warehouse.product_id = product.id inner join supplier on invoice_coming.supplier_id = supplier.id")
    fun getInvoiceComing(): Flow<List<InvoiceComingWithWarehouseSupplier>>

    @Query("select * from product")
    fun getProducts(): Flow<List<Product>>

    @Query("select * from product")
    suspend fun getProductList(): List<Product>

    @Query("insert into User(username, password, user_type, name, surname, patronymic, phone) values (:username, :password, :userType, :name, :surname, :patronymic, :phone)")
    suspend fun addUser(username: String, password: String, userType: UserType, name: String, surname: String, patronymic: String?, phone: String)

    @Query("insert into Client(company, email, phone) values (:company, :email, :phone)")
    suspend fun addClient(company: String, email: String, phone: String)

    @Query("insert into Supplier(company, email, phone) values (:company, :email, :phone)")
    suspend fun addSupplier(company: String, email: String, phone: String)

    @Query("insert into product_type(type) values (:type)")
    suspend fun addTypes(type: String)

    @Query("insert into warehouse(name, free_place, product_id) values (:name, :freePlace, :productId)")
    suspend fun addWarehouse(name: String, freePlace: Int, productId: Int)

    @Query("insert into invoice_coming(price, count, manufactureDate, expirationDate, warehouse_id, supplier_id) values (:price, :count, :manufactureDate, :expirationDate, :warehouseId, :supplierId)")
    suspend fun addInvoiceComing(price: Double, count: Int, manufactureDate: LocalDate,
                                 expirationDate: LocalDate, warehouseId: Int, supplierId: Int)

    @Query("insert into product(name, unit, price, product_type_id) values (:name, :unit, :price, :typeId)")
    suspend fun addProduct(name: String, unit: String, price: Double, typeId: Int)

}