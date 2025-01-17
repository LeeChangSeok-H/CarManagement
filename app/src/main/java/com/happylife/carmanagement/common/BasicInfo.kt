package com.happylife.carmanagement.common

class BasicInfo {
    val db_ourStore = "ourStore"
    val db_customerCar = "customerCar"
    val db_carInfo = "carInfo"
    val db_password = "password"
    val db_pw_value = "pw_value"
    val db_pw_admin = "pw_admin"
    val db_token ="token"
    val db_appToken = "appToken"
    val db_tokenValue = "value"
    val db_date = "date"
    val db_time = "time"
    val db_carNumber = "carNumber"
    val db_customerPhoneNumber = "customerPhoneNumber"
    val db_serverKey = "serverKey"

    val SHAREDPREFERENCES_NAME = "carManagement"
    val SHAREDPREFERENCES_KEY_ISPASSWORDCONFIRM = "isPasswordConfirm"
    val SHAREDPREFERENCES_KEY_ISFAILCOUNT = "isFailCount"
    val SHAREDPREFERENCES_KEY_ISNAMECONFIRM = "isNameConfirm"
    val SHAREDPREFERENCES_KEY_SERVERKEY = "SERVERKEY"

    val datePattern = "yyyy - MM - dd"
    val timePattern = "HH : mm"

    val INTENT_PAGERPOSITION = "pagerPosition"
    val INTENT_CARITEM = "carItem"
    val INTENT_CARID = "carId"
    val INTENT_OPEN_WORKERNAME = "openWorkerName"
    val INTENT_PASSWORD = "PASSWORD"

    val OPENWORKERNAME_PASSWORD = 100
    val OPENWORKERNAME_SETTING = 200

    val ACTION_ADDCAR = 0
    val ACTION_MODIFYCAR = 1
    val ACTION_DELETECAR = 2



}