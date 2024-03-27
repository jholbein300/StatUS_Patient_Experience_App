package com.example.status_patient_home

import org.junit.Test

class LoginUnitTest {
    @Test
    fun testLogin() {
        val user = ConnectionClass.tempLogin("y.wasin@hospital.org","pass123!@")
            if (user != null) {
                printUserData(user)
            }

    }

    private fun printUserData(user: User) {
        println("User Id: ${user.id}, Email: ${user.emailAddress}")
        println("Name: ${user.firstName} ${user.lastName}")


    }
}