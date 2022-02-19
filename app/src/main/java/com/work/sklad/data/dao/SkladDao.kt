package com.work.sklad.data.dao

import androidx.room.*
import com.work.sklad.data.model.*
import com.work.sklad.domain.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface SkladDao {

    @Query("select * from User where username = :username and password = :password")
    suspend fun searchUser(username: String, password: String):List<User>

    @Query("select * from User where username = :username")
    suspend fun searchUser(username: String):List<User>

    @Query("select * from User where id = :id")
    suspend fun searchUser(id: Int): List<User>

    @Query("select * from client")
    fun getClients(): Flow<List<Client>>

    @Query("select * from client")
    fun getClientList(): List<Client>

    @Query("select * from supplier")
    fun getSuppliers(): Flow<List<Supplier>>

    @Query("select * from supplier")
    fun getSuppliersList(): List<Supplier>

    @Query("select * from product_type")
    fun getTypes(): Flow<List<ProductType>>

    @Query("select * from product_type")
    suspend fun getProductTypes(): List<ProductType>

    @Query("select product.id, product.name, product.unit, product.product_type_id as typeId, product_type.type, (select sum(invoice_coming.count) from invoice_coming inner join warehouse on invoice_coming.warehouse_id = warehouse.id where warehouse.product_id = product.id) as came, (select sum(invoice.count) from invoice inner join warehouse on invoice.warehouse_id = warehouse.id where warehouse.product_id = product.id) as `left` from product inner join product_type on product_type.id = product.product_type_id")
    fun getProductWithType(): Flow<List<ProductWithType>>

    @Query("select warehouse.id, warehouse.name, warehouse.free_place as place, (select sum(invoice_coming.count) from invoice_coming where invoice_coming.warehouse_id = warehouse.id) as [invoiceIn], (select sum(invoice.count) from invoice where invoice.warehouse_id = warehouse.id) as invoiceOut, product.id as productId, product.name as [product], product_type.type from product inner join product_type on product_type.id = product.product_type_id inner join warehouse on warehouse.product_id = product.id")
    fun getWarehouseWithProduct(): Flow<List<WarehouseWithProduct>>

    @Query("select warehouse.id, warehouse.name, warehouse.free_place as place, (select sum(invoice_coming.count) from invoice_coming where invoice_coming.warehouse_id = warehouse.id) as [invoiceIn], (select sum(invoice.count) from invoice where invoice.warehouse_id = warehouse.id) as invoiceOut, product.id as productId, product.name as [product], product_type.type from product inner join product_type on product_type.id = product.product_type_id inner join warehouse on warehouse.product_id = product.id")
    suspend fun getWarehouseWithProductList(): List<WarehouseWithProduct>

    @Query("select invoice_coming.id, product.name as [product], warehouse.id as warehouseId, warehouse.name as [warehouse], invoice_coming.price, invoice_coming.count, invoice_coming.manufactureDate, invoice_coming.expirationDate, supplier.id as supplierId, supplier.company from invoice_coming inner join warehouse on invoice_coming.warehouse_id = warehouse.id inner join product on warehouse.product_id = product.id inner join supplier on invoice_coming.supplier_id = supplier.id")
    fun getInvoiceComing(): Flow<List<InvoiceComingWithWarehouseSupplier>>

    @Query("select invoice.id, product.name as [product], warehouse.id as warehouseId, warehouse.name as [warehouse], invoice.price, invoice.count, invoice.manufactureDate, invoice.expirationDate from invoice inner join warehouse on invoice.warehouse_id = warehouse.id inner join product on warehouse.product_id = product.id")
    fun getInvoice(): Flow<List<InvoiceWithWarehouse>>

    @Query("select invoice.id, product.name as [product], warehouse.id as warehouseId, warehouse.name as [warehouse], invoice.price, invoice.count, invoice.manufactureDate, invoice.expirationDate from invoice inner join warehouse on invoice.warehouse_id = warehouse.id inner join product on warehouse.product_id = product.id where invoice.id = :id")
    suspend fun getInvoice(id: Int): List<InvoiceWithWarehouse>

    @Query("select invoice.id, product.name as [product], warehouse.id as warehouseId, warehouse.name as [warehouse], invoice.price, invoice.count, invoice.manufactureDate, invoice.expirationDate from invoice inner join warehouse on invoice.warehouse_id = warehouse.id inner join product on warehouse.product_id = product.id LEFT OUTER JOIN `order` on `order`.invoice_id = invoice.id where `order`.invoice_id is NULL")
    suspend fun getFreeInvoices(): List<InvoiceWithWarehouse>

    @Query("select `order`.id, `order`.date, client.id as clientId, client.company as client, user.id as userId, user.username as user, invoice.id as invoiceId, invoice.price, warehouse.name as warehouse, product.name as product, `order`.isCompleted from `order` inner join invoice on invoice.id = `order`.invoice_id inner join client on `order`.client_id = client.id inner join user on user.id = `order`.user_id inner join warehouse on invoice.warehouse_id = warehouse.id inner join product on product.id = warehouse.product_id")
    fun getOrders(): Flow<List<OrderWithInvoiceUserClient>>

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

    @Query("insert into invoice(price, count, manufactureDate, expirationDate, warehouse_id) values (:price, :count, :manufactureDate, :expirationDate, :warehouseId)")
    suspend fun addInvoice(price: Double, count: Int, manufactureDate: LocalDate,
                                 expirationDate: LocalDate, warehouseId: Int)

    @Query("insert into `order`(date, client_id, user_id, invoice_id, isCompleted) values (:date, :clientId, :userId, :invoiceId, :isCompleted)")
    suspend fun addOrder(date: LocalDate, clientId: Int, userId: Int, invoiceId: Int, isCompleted: Boolean)

    @Query("insert into product(name, unit, product_type_id) values (:name, :unit, :typeId)")
    suspend fun addProduct(name: String, unit: String, typeId: Int)

    @Delete
    suspend fun deleteSupplier(supplier: Supplier)

    @Update
    suspend fun updateSupplier(supplier: Supplier)

    @Delete
    suspend fun delete(item: Client)

    @Update
    suspend fun update(item: Client)

    @Delete
    suspend fun delete(item: Invoice)

    @Update
    suspend fun update(item: Invoice)

    @Delete
    suspend fun delete(item: InvoiceComing)

    @Update
    suspend fun update(item: InvoiceComing)

    @Delete
    suspend fun delete(item: Order)

    @Update
    suspend fun update(item: Order)

    @Delete
    suspend fun delete(item: Product)

    @Update
    suspend fun update(item: Product)

    @Delete
    suspend fun delete(item: ProductType)

    @Update
    suspend fun update(item: ProductType)

    @Delete
    suspend fun delete(item: User)

    @Update
    suspend fun update(item: User)

    @Delete
    suspend fun delete(item: Warehouse)

    @Update
    suspend fun update(item: Warehouse)

}