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
    fun searchUser(id: Int): Flow<List<User>>

    @Query("select * from User")
    fun getUsers(): Flow<List<User>>

    @Query("select * from client")
    fun getClients(): Flow<List<Client>>

    @Query("select * from client")
    suspend fun getClientList(): List<Client>

    @Query("select id, company, email, phone, (select count(*) from `order` where `order`.client_id = client.id) > 0 as hasDiscount from client")
    fun getClientsWithDiscount(): Flow<List<ClientDiscount>>

    @Query("select id, company, email, phone, (select count(*) from `order` where `order`.client_id = client.id) > 0 as hasDiscount from client")
    suspend fun getClientWithDiscountList(): List<ClientDiscount>

    @Query("select count(*) from `order` where `order`.client_id = :clientId")
    suspend fun clientHasDiscount(clientId: Int): Boolean

    @Query("select * from supplier")
    fun getSuppliers(): Flow<List<Supplier>>

    @Query("select * from supplier")
    fun getSuppliersList(): List<Supplier>

    @Query("select * from product_type")
    fun getTypes(): Flow<List<ProductType>>

    @Query("select * from product_type")
    suspend fun getProductTypes(): List<ProductType>

    @Query("select product.id, product.name, product.unit, product.product_type_id as typeId, product_type.type, (select sum(invoice_coming.count) from invoice_coming inner join warehouse on invoice_coming.warehouse_id = warehouse.id where warehouse.product_id = product.id) as came, (select sum(`order`.count) from `order` inner join warehouse on `order`.warehouse_id = warehouse.id where warehouse.product_id = product.id) as `left` from product inner join product_type on product_type.id = product.product_type_id")
    fun getProductWithType(): Flow<List<ProductWithType>>

    @Query("select warehouse.id, warehouse.name, warehouse.free_place as place, (select sum(invoice_coming.count) from invoice_coming where invoice_coming.warehouse_id = warehouse.id) as [invoiceIn], (select sum(`order`.count) from `order` where `order`.warehouse_id = warehouse.id) as invoiceOut, product.id as productId, product.name as [product], product_type.type, product.unit from product inner join product_type on product_type.id = product.product_type_id inner join warehouse on warehouse.product_id = product.id")
    fun getWarehouseWithProduct(): Flow<List<WarehouseWithProduct>>

    @Query("select warehouse.id, warehouse.name, warehouse.free_place as place, (select sum(invoice_coming.count) from invoice_coming where invoice_coming.warehouse_id = warehouse.id) as [invoiceIn], (select sum(`order`.count) from `order` where `order`.warehouse_id = warehouse.id) as invoiceOut, product.id as productId, product.name as [product], product_type.type, product.unit from product inner join product_type on product_type.id = product.product_type_id inner join warehouse on warehouse.product_id = product.id where warehouse.id = :id")
    suspend fun getWarehouse(id: Int): WarehouseWithProduct

    @Query("select warehouse.id, warehouse.name, warehouse.free_place as place, (select sum(invoice_coming.count) from invoice_coming where invoice_coming.warehouse_id = warehouse.id) as [invoiceIn], (select sum(`order`.count) from `order` where `order`.warehouse_id = warehouse.id) as invoiceOut, product.id as productId, product.name as [product], product_type.type, product.unit from product inner join product_type on product_type.id = product.product_type_id inner join warehouse on warehouse.product_id = product.id")
    suspend fun getWarehouseWithProductList(): List<WarehouseWithProduct>

    @Query("select invoice_coming.id, product.name as [product], warehouse.id as warehouseId, warehouse.name as [warehouse], invoice_coming.price, invoice_coming.count, invoice_coming.manufactureDate, invoice_coming.expirationDate, supplier.id as supplierId, supplier.company, product.unit from invoice_coming inner join warehouse on invoice_coming.warehouse_id = warehouse.id inner join product on warehouse.product_id = product.id inner join supplier on invoice_coming.supplier_id = supplier.id")
    fun getInvoiceComing(): Flow<List<InvoiceComingWithWarehouseSupplier>>

    @Query("select invoice.id, product.name as [product], warehouse.id as warehouseId, warehouse.name as [warehouse], invoice.order_id as orderId, `order`.price, `order`.count, invoice.manufactureDate, invoice.expirationDate, product.unit from invoice left join `order` on invoice.order_id=`order`.id  left join warehouse on `order`.warehouse_id = warehouse.id left join product on warehouse.product_id = product.id")
    fun getInvoice(): Flow<List<InvoiceWithWarehouse>>

    @Query("select invoice.id, product.name as [product], warehouse.id as warehouseId, warehouse.name as [warehouse], invoice.order_id as orderId, `order`.price, `order`.count, invoice.manufactureDate, invoice.expirationDate, product.unit from invoice left join `order` on invoice.order_id=`order`.id  left join warehouse on `order`.warehouse_id = warehouse.id left join product on warehouse.product_id = product.id where invoice.id = :id")
    suspend fun getInvoice(id: Int): List<InvoiceWithWarehouse>

    @Query("select invoice.id, product.name as [product], warehouse.id as warehouseId, warehouse.name as [warehouse], invoice.order_id as orderId, `order`.price, `order`.count, invoice.manufactureDate, invoice.expirationDate, product.unit from invoice inner join `order` on invoice.order_id=`order`.id  inner join warehouse on `order`.warehouse_id = warehouse.id inner join product on warehouse.product_id = product.id where `order`.id = :id")
    suspend fun getInvoiceByOrder(id: Int): List<InvoiceWithWarehouse>

    @Query("select `order`.id, `order`.date, client.id as clientId, client.company as client, user.id as userId, user.username as user,  `order`.price, `order`.count, `order`.warehouse_id as warehouseId, warehouse.name as warehouse, product.name as product, `order`.isCompleted, `order`.isCreated from `order` LEFT JOIN invoice on invoice.order_id = `order`.id inner join client on `order`.client_id = client.id inner join user on user.id = `order`.user_id inner join warehouse on `order`.warehouse_id = warehouse.id inner join product on product.id = warehouse.product_id where invoice.order_id is NULL")
    suspend fun getFreeOrders(): List<OrderWithInvoiceUserClient>

    @Query("select `order`.id, `order`.date, client.id as clientId, client.company as client, user.id as userId, user.username as user,  `order`.price, `order`.count, `order`.warehouse_id as warehouseId, warehouse.name as warehouse, product.name as product, `order`.isCompleted, `order`.isCreated from `order` LEFT JOIN invoice on invoice.order_id = `order`.id inner join client on `order`.client_id = client.id inner join user on user.id = `order`.user_id inner join warehouse on `order`.warehouse_id = warehouse.id inner join product on product.id = warehouse.product_id")
    fun getOrders(): Flow<List<OrderWithInvoiceUserClient>>

    @Query("select `order`.id, `order`.date, client.id as clientId, client.company as client, user.id as userId, user.username as user,  `order`.price, `order`.count, `order`.warehouse_id as warehouseId, warehouse.name as warehouse, product.name as product, `order`.isCompleted, `order`.isCreated from `order` LEFT JOIN invoice on invoice.order_id = `order`.id inner join client on `order`.client_id = client.id inner join user on user.id = `order`.user_id inner join warehouse on `order`.warehouse_id = warehouse.id inner join product on product.id = warehouse.product_id where `order`.id = :orderId")
    suspend fun getOrder(orderId: Int): List<OrderWithInvoiceUserClient>

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

    @Query("insert into invoice(manufactureDate, expirationDate, order_id) values (:manufactureDate, :expirationDate, :orderId)")
    suspend fun addInvoice(manufactureDate: LocalDate, expirationDate: LocalDate, orderId: Int)

    @Query("insert into `order`(date, client_id, user_id, warehouse_id, isCompleted, isCreated, price, count) values (:date, :clientId, :userId, :warehouseId, :isCompleted, :isCreated, :price, :count)")
    suspend fun addOrder(date: LocalDate, clientId: Int, userId: Int, warehouseId: Int, isCompleted: Boolean, isCreated: Boolean, price: Double, count: Int)

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

    @Query("UPDATE `Order` SET price = price*0.95 where id = :id;")
    suspend fun setOrderDiscount(id: Int)

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