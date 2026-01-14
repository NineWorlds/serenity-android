package us.nineworlds.serenity.core.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import toothpick.config.Module
import us.nineworlds.serenity.test.InjectingTest

@Ignore
class SettingsRepositoryTest : InjectingTest() {

    private val mockContext: Context = mockk(relaxed = true)
    private val mockDataStore: DataStore<Preferences> = mockk(relaxed = true)
    private val mockPreferences: Preferences = mockk(relaxed = true)

    lateinit var repository: SettingsRepository

    @Before
    override fun setUp() {
        mockkStatic("us.nineworlds.serenity.core.repository.SettingsRepositoryKt")
        mockkStatic("androidx.datastore.preferences.core.PreferencesDataStoreKt")
        every { mockContext.dataStore } returns mockDataStore
        every { mockDataStore.data } returns flowOf(mockPreferences)
        
        super.setUp()
        repository = scope.getInstance(SettingsRepository::class.java)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        closeScope()
    }

    override fun installTestModules() {
        scope.installModules(TestModule())
    }

    inner class TestModule : Module() {
        init {
            bind(Context::class.java).toInstance(mockContext)
            bind(SettingsRepository::class.java).toInstance(SettingsRepository(mockContext))
        }
    }

    @Test
    fun getStringDelegatesToDataStore() {
        val key = stringPreferencesKey("test_key")
        every { mockPreferences[key] } returns "test_value"

        val result = repository.getString("test_key", "default")

        assertThat(result).isEqualTo("test_value")
    }

    @Test
    fun getStringReturnsDefaultValueWhenKeyMissing() {
        val key = stringPreferencesKey("test_key")
        every { mockPreferences[key] } returns null

        val result = repository.getString("test_key", "default")

        assertThat(result).isEqualTo("default")
    }

    @Test
    fun setStringUpdatesDataStore() = runTest {
        coEvery { mockDataStore.updateData(any()) } returns mockPreferences

        repository.setString("test_key", "test_value")

        coVerify { mockDataStore.updateData(any()) }
    }

    @Test
    fun getBooleanDelegatesToDataStore() {
        val key = booleanPreferencesKey("test_key")
        every { mockPreferences[key] } returns true

        val result = repository.getBoolean("test_key", false)

        assertThat(result).isTrue()
    }

    @Test
    fun setBooleanUpdatesDataStore() = runTest {
        coEvery { mockDataStore.updateData(any()) } returns mockPreferences

        repository.setBoolean("test_key", true)

        coVerify { mockDataStore.updateData(any()) }
    }
}
