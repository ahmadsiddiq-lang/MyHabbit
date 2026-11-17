package com.myhabbit.app.presentation.login

import android.content.Context
import com.myhabbit.app.domain.model.AuthResult
import com.myhabbit.app.domain.model.User
import com.myhabbit.app.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authRepository: AuthRepository
    private val activityContext = Mockito.mock(Context::class.java)
    private val currentUserFlow = MutableStateFlow<User?>(null)
    private val testUser = User(
        id = "g1",
        googleId = "g1",
        name = "Ahmad",
        email = "ahmad@gmail.com",
        photoUrl = null
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = Mockito.mock(AuthRepository::class.java)
        Mockito.`when`(authRepository.observeCurrentUser()).thenReturn(currentUserFlow)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = LoginViewModel(authRepository)

    @Test
    fun initialState_userNull_andAuthCheckedAfterFirstEmission() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        val state = viewModel.uiState.first { it.authChecked }

        assertNull(state.user)
        assertFalse(state.isSigningIn)
        assertTrue(state.authChecked)
    }

    @Test
    fun signIn_success_userEmitted() = runTest(testDispatcher) {
        Mockito.`when`(authRepository.signIn(activityContext)).thenAnswer {
            currentUserFlow.value = testUser
            AuthResult.Success(testUser)
        }
        val viewModel = createViewModel()
        viewModel.uiState.first { it.authChecked }

        viewModel.signIn(activityContext)

        val state = viewModel.uiState.first { it.user != null }
        assertEquals("Ahmad", state.user?.name)
        assertFalse(state.isSigningIn)
        assertNull(state.error)
    }

    @Test
    fun signIn_error_setsErrorAndUserStaysNull() = runTest(testDispatcher) {
        Mockito.`when`(authRepository.signIn(activityContext)).thenAnswer {
            AuthResult.Error("Login dibatalkan")
        }
        val viewModel = createViewModel()
        viewModel.uiState.first { it.authChecked }

        viewModel.signIn(activityContext)

        val state = viewModel.uiState.first { it.error != null }
        assertEquals("Login dibatalkan", state.error)
        assertNull(state.user)
        assertFalse(state.isSigningIn)
    }

    @Test
    fun clearError_removesErrorMessage() = runTest(testDispatcher) {
        Mockito.`when`(authRepository.signIn(activityContext)).thenAnswer {
            AuthResult.Error("Login dibatalkan")
        }
        val viewModel = createViewModel()
        viewModel.uiState.first { it.authChecked }

        viewModel.signIn(activityContext)
        viewModel.uiState.first { it.error != null }

        viewModel.clearError()

        val state = viewModel.uiState.first { it.error == null }
        assertNull(state.error)
    }
}
