package com.work.sklad.data.model

enum class UserType {
    SalesManager, WarehouseMan, Picker, WarehouseManager;

    override fun toString(): String {
        return when(this) {
            SalesManager -> "Менеджер по продажам"
            WarehouseMan -> "Работник склада"
            Picker -> "Сборщик"
            WarehouseManager -> "Директор склада"
        }
    }
}