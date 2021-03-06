package com.work.sklad.data.model

enum class UserType {
    SalesManager, WarehouseMan, Picker, WarehouseManager, Admin;

    override fun toString(): String {
        return when(this) {
            SalesManager -> "Менеджер по продажам"
            WarehouseMan -> "Кладовщик"
            Picker -> "Сборщик"
            WarehouseManager -> "Директор склада"
            Admin -> "Администратор"
        }
    }
}