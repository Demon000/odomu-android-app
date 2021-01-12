package com.demon000.odomu.services

import io.socket.client.IO
import io.socket.client.Socket

enum class SocketEvent(val x: String) {
    CONNECT("connect"),
    AUTHENTICATE("authenticate"),
    AREA_ADDED("area-added"),
    AREA_UPDATED("area-updated"),
    AREA_DELETED("area-deleted"),
}

class NotificationService(baseURL: String) {
    val socket: Socket = IO.socket(baseURL)

    init {
        socket.connect()
    }

    fun authenticate(accessToken: String?) {
        if (!socket.connected() || accessToken == null) {
            return
        }

        socket.emit("authenticate", accessToken)
    }
}
