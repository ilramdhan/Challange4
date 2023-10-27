package com.dicoding.challange4.utils

import org.json.JSONException
import org.json.JSONObject

object SimplifiedMessage {

    fun get(stringMessage: String): HashMap<String, String> {
        val message = HashMap<String, String>()
        val jsonObject = JSONObject(stringMessage)

        try {
            val jsonMessages = jsonObject.getJSONObject("message")
            jsonMessages.keys().forEach {
                message[it] = jsonMessages.getString(it)
            }
        } catch (e: JSONException) {
            message["message"] = jsonObject.getString("message")
        }

        return message
    }
}
