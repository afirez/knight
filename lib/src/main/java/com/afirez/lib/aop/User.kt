package com.afirez.lib.aop

data class User(var name:String = "afirez") {
    override fun toString(): String {
        return "User(name='$name')"
    }
}