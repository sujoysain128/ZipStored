package com.zipstored.com


import java.util.*

object Print {

    fun makePrint(message: String) {
        println(message)
    }

    fun makePrint(mHasMap: Map<String, String>) {
        makePrint("hasMapValue===>" + Arrays.asList(mHasMap))
    }

    /*fun makePrintRequestBody(mHasMap: Map<String, RequestBody>) {
        makePrint("hasMapValue===>" + Arrays.asList(mHasMap))
    }*/

    fun makePrint(message: Int) {
        println(message.toString())
    }

    fun makePrint(message: Double) {
        println(message.toString())
    }
}
