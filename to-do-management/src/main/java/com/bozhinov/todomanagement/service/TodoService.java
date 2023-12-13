package com.bozhinov.todomanagement.service;

import com.bozhinov.todomanagement.dto.TodoDto;
import com.bozhinov.todomanagement.entity.TodoEntity;

import java.util.List;

public interface TodoService {

    TodoDto addTodo(TodoDto todoDto);
    TodoDto findTodoById(Long id);
    List<TodoDto> getAllTodos();
    TodoDto updateTodo(TodoDto todoDto, Long id);
    void deleteTodo(Long id);
    TodoDto todoCompleted(Long id);
    TodoDto inCompleteTodo(Long id);
}
