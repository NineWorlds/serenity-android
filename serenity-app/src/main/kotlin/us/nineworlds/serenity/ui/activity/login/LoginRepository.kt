package us.nineworlds.serenity.ui.activity.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import toothpick.InjectConstructor
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser

@InjectConstructor
class LoginRepository(private val client: SerenityClient) {

    sealed class Result<out R> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val exception: Exception) : Result<Nothing>()
    }

    suspend fun loadAllUsers(): Result<List<SerenityUser>> = withContext(Dispatchers.IO) {
        val users = client.allAvailableUsers()
        Result.Success<List<SerenityUser>>(users)
    }

    suspend fun authenticateUser(user: SerenityUser): Result<SerenityUser> = withContext(Dispatchers.IO) {
        val authenticatedUser = client.authenticateUser(user)
        Result.Success<SerenityUser>(authenticatedUser)
    }
}