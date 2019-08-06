package com.liquorworldkotlin.RetrofitServiceClass

object mServiceList {

    //const val Base_URL = "http://runmobileapps.com/sober_connect/api/"
    const val Base_URL = "https://runmobileapps.com/zipstored/api/"


    const val master_slots = Base_URL + "master-slots"
    const val master_amenities = Base_URL + "amenities"

    const val register = Base_URL + "registration"
    const val login = Base_URL + "login"
    const val logout = Base_URL + "logout"
    const val forgot_password = Base_URL + "forgot-password"

    //storage
    const val storage_providers_list = Base_URL + "storage-providers-list"
    const val location_list = Base_URL + "location-list"
    const val storage_providers_details = Base_URL + "storage-providers-details"

}
