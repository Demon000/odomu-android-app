package com.demon000.odomu.dependencies

import android.content.Context
import com.demon000.odomu.R
import com.demon000.odomu.api.API
import com.demon000.odomu.api.AreaAPIInterface
import com.demon000.odomu.api.UserAPIInterface
import com.demon000.odomu.repositories.UserRepository
import com.demon000.odomu.services.AreaService
import com.demon000.odomu.services.UserService
import com.demon000.odomu.utils.Constants

public class DependencyLocator {
    companion object {
        var context: Context? = null
        
        public var api = API(Constants.API_BASE_URL)
        public var userAPI = api.retrofit.create(UserAPIInterface::class.java)
        public var areaAPI = api.retrofit.create(AreaAPIInterface::class.java)

        public var userRepository = UserRepository()

        public var userService = UserService()

        public var areaService = AreaService()

        init {
            api.accessTokenObserver.observeForever { value ->
                userService.accessToken = value
            }

            api.refreshTokenObserver.observeForever { value ->
                userService.refreshToken = value
            }
        }
    }
}
