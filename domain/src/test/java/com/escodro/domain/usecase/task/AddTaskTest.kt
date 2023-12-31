package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class AddTaskTest {

    private val taskRepository = TaskRepositoryFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    @Before
    fun setup() = runTest {
        taskRepository.cleanTable()
        glanceInteractor.clean()
    }

    @Test
    fun `test if task is correctly added`() = runTest {
        val task = Task(id = 15, title = "this title", description = "this desc")
        addTaskUseCase(task)

        val result = getTaskUseCase(task.id)

        require(result != null)
        Assert.assertEquals(task, result)
    }

    @Test
    fun `test if task with empty title is not added`() = runTest {
        val task = Task(id = 15, title = " ", description = "this desc")
        addTaskUseCase(task)

        val result = getTaskUseCase(task.id)

        Assert.assertNull(result)
    }

    @Test
    fun `test if the glance was notified`() = runTest {
        val task = Task(id = 15, title = "this title", description = "this desc")
        addTaskUseCase(task)

        Assert.assertTrue(glanceInteractor.wasNotified)
    }
}
