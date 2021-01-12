package com.demon000.odomu.dependencies

import android.content.Context
import com.demon000.odomu.api.API
import com.demon000.odomu.api.AreaAPIInterface
import com.demon000.odomu.api.UserAPIInterface
import com.demon000.odomu.repositories.UserRepository
import com.demon000.odomu.services.AreaService
import com.demon000.odomu.services.NotificationService
import com.demon000.odomu.services.UserService
import com.demon000.odomu.utils.Constants
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder

public class DependencyLocator {
    companion object {
        var context: Context? = null

        public var gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setLenient()
            .create()
        
        public var api = API(Constants.API_BASE_URL, gson)
        public var userAPI = api.retrofit.create(UserAPIInterface::class.java)
        public var areaAPI = api.retrofit.create(AreaAPIInterface::class.java)

        public var userRepository = UserRepository()

        public var userService = UserService()

        public var areaService = AreaService()

        public var notificationService = NotificationService(Constants.SOCKET_BASE_URL)

        init {
            notificationService.socket.on("connect") {
                notificationService.authenticate(userService.accessToken)
            }

            api.accessTokenObserver.observeForever { value ->
                userService.accessToken = value
                notificationService.authenticate(value)
            }

            api.refreshTokenObserver.observeForever { value ->
                userService.refreshToken = value
            }
        }
    }
}
