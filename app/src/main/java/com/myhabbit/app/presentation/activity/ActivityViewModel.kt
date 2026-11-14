package com.myhabbit.app.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myhabbit.app.core.result.AppResult
import com.myhabbit.app.domain.model.Habit
import com.myhabbit.app.domain.model.HabitCategory
import com.myhabbit.app.domain.usecase.habit.CreateHabitUseCase
import com.myhabbit.app.domain.usecase.habit.DeleteHabitUseCase
import com.myhabbit.app.domain.usecase.habit.GetActiveHabitsUseCase
import com.myhabbit.app.domain.usecase.habit.GetHabitByIdUseCase
import com.myhabbit.app.domain.usecase.habit.UpdateHabitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ActivityFormUiState(
    val isLoading: Boolean = false,
    val title: String = "",
    val category: HabitCategory? = null,
    val description: String = "",
    val titleError: String? = null,
    val descriptionError: String? = null,
    val categoryError: String? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

data class ActivityListUiState(
    val isLoading: Boolean = false,
    val habits: List<Habit> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val createHabitUseCase: CreateHabitUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val getActiveHabitsUseCase: GetActiveHabitsUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(ActivityFormUiState())
    val formState: StateFlow<ActivityFormUiState> = _formState.asStateFlow()

    val listState: StateFlow<ActivityListUiState> = getActiveHabitsUseCase()
        .map { habits -> ActivityListUiState(habits = habits) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ActivityListUiState(isLoading = true)
        )

    fun loadHabit(habitId: String) {
        if (_formState.value.isLoading) return
        _formState.value = _formState.value.copy(isLoading = true)
        viewModelScope.launch {
            val habit = getHabitByIdUseCase(habitId)
            _formState.value = ActivityFormUiState(
                isLoading = false,
                title = habit?.title.orEmpty(),
                category = habit?.category,
                description = habit?.description.orEmpty()
            )
        }
    }

    fun onTitleChange(value: String) {
        _formState.value = _formState.value.copy(title = value, titleError = null)
    }

    fun onCategoryChange(value: HabitCategory) {
        _formState.value = _formState.value.copy(category = value, categoryError = null)
    }

    fun onDescriptionChange(value: String) {
        _formState.value = _formState.value.copy(description = value, descriptionError = null)
    }

    fun saveHabit(habitId: String?) {
        val state = _formState.value

        val titleError = com.myhabbit.app.domain.validation.ActivityValidator.validateTitle(state.title)
        val descriptionError =
            com.myhabbit.app.domain.validation.ActivityValidator.validateDescription(state.description)
        val categoryError = if (state.category == null) "Kategori wajib dipilih" else null

        if (titleError != null || descriptionError != null || categoryError != null) {
            _formState.value = state.copy(
                titleError = titleError,
                descriptionError = descriptionError,
                categoryError = categoryError
            )
            return
        }

        val category = state.category ?: return
        _formState.value = state.copy(isSaving = true, error = null)

        viewModelScope.launch {
            val result = if (habitId == null) {
                createHabitUseCase(state.title, category, state.description.takeIf { it.isNotBlank() })
            } else {
                updateHabitUseCase(habitId, state.title, category, state.description.takeIf { it.isNotBlank() })
            }
            when (result) {
                is AppResult.Success -> {
                    _formState.value = _formState.value.copy(isSaving = false, saveSuccess = true)
                }
                is AppResult.Error -> {
                    _formState.value = _formState.value.copy(isSaving = false, error = result.message)
                }
            }
        }
    }

    fun deleteHabit(habitId: String) {
        viewModelScope.launch {
            when (val result = deleteHabitUseCase(habitId)) {
                is AppResult.Error -> {
                    _formState.value = _formState.value.copy(error = result.message)
                }
                else -> Unit
            }
        }
    }

    fun resetForm() {
        _formState.value = ActivityFormUiState()
    }
}
