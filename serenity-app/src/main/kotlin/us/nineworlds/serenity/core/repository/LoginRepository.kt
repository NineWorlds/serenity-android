package us.nineworlds.serenity.core.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import toothpick.InjectConstructor
import us.nineworlds.serenity.common.repository.Result
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser

@InjectConstructor
class LoginRepository(private val client: SerenityClient) {

    suspend fun loadAllUsers(): Result<List<SerenityUser>> = withContext(Dispatchers.IO) {
        val users = client.allAvailableUsers()
        Result.Success<List<SerenityUser>>(users)
    }

    suspend fun authenticateUser(user: SerenityUser, password: String? = null): Result<SerenityUser> = withContext(Dispatchers.IO) {
        val authenticatedUser = client.authenticateUser(user, password)
        Result.Success<SerenityUser>(authenticatedUser)
    }
}
