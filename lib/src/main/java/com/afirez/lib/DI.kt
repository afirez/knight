package com.afirez.lib

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val lib = Kodein.Module(":lib") {
    bind<User>(tag = "user") with singleton { User() }
}

//class DI {
//    companion object {
//        val lib = Kodein.Module(":lib") {
//            bind<User>(tag = "user") with singleton { User() }
//        }
//    }
//}