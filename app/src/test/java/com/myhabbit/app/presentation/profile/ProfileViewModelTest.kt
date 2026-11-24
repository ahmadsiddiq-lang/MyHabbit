package com.myhabbit.app.presentation.profile

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
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authRepository: AuthRepository
    private val currentUserFlow = MutableStateFlow<User?>(null)

    private val testUser = User(
        id = "gid",
        googleId = "gid",
        name = "Ahmad",
        email = "ahmad@example.com",
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

    @Test
    fun uiState_initiallySignedOut() = runTest(testDispatcher) {
        val viewModel = ProfileViewModel(authRepository)

        val state = viewModel.uiState.first { !it.isLoading }

        assertNull(state.user)
    }

    @Test
    fun uiState_emitsCurrentUser() = runTest(testDispatcher) {
        currentUserFlow.value = testUser
        val viewModel = ProfileViewModel(authRepository)

        val state = viewModel.uiState.first { it.user != null }

        assertEquals("Ahmad", state.user?.name)
        assertEquals("ahmad@example.com", state.user?.email)
        assertFalse(state.isLoading)
    }

    @Test
    fun signOut_clearsUser() = runTest(testDispatcher) {
        currentUserFlow.value = testUser
        Mockito.`when`(authRepository.signOut()).thenAnswer {
            currentUserFlow.value = null
            Unit
        }
        val viewModel = ProfileViewModel(authRepository)
        viewModel.uiState.first { it.user != null }

        viewModel.signOut()

        val state = viewModel.uiState.first { it.user == null }
        assertNull(state.user)
    }
}
