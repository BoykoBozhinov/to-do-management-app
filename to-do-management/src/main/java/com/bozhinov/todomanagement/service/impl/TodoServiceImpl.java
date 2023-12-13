package com.bozhinov.todomanagement.service.impl;

import com.bozhinov.todomanagement.dto.TodoDto;
import com.bozhinov.todomanagement.entity.TodoEntity;
import com.bozhinov.todomanagement.exception.ResourceNotFoundException;
import com.bozhinov.todomanagement.repository.TodoRepository;
import com.bozhinov.todomanagement.service.TodoService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import static com.bozhinov.todomanagement.constants.Constants.NOT_FOUND_EXCEPTION_MESSAGE;

@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    public TodoServiceImpl(TodoRepository todoRepository, ModelMapper modelMapper) {
        this.todoRepository = todoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TodoDto addTodo(TodoDto todoDto) {

        TodoEntity todo = modelMapper.map(todoDto, TodoEntity.class);
        TodoEntity savedTodo = todoRepository.save(todo);

        return modelMapper.map(savedTodo, TodoDto.class);
    }

    @Override
    public TodoDto findTodoById(Long id) {
        TodoEntity todo = todoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(NOT_FOUND_EXCEPTION_MESSAGE, id)));
        return modelMapper.map(todo, TodoDto.class);
    }

    @Override
    public List<TodoDto> getAllTodos() {
       List<TodoEntity> allTodos = todoRepository.findAll();
       return allTodos.stream().map((todo) -> modelMapper.map(todo, TodoDto.class))
               .collect(Collectors.toList());
    }
    @Override
    public TodoDto updateTodo(TodoDto todoDto, Long id) {

        TodoEntity todo = todoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(NOT_FOUND_EXCEPTION_MESSAGE, id)));

        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());
        todo.setCompleted(todoDto.isCompleted());

        TodoEntity updatedDto = todoRepository.save(todo);

        return modelMapper.map(updatedDto, TodoDto.class);
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(NOT_FOUND_EXCEPTION_MESSAGE, id)));
        todoRepository.deleteById(id);
    }

    @Override
    public TodoDto todoCompleted(Long id) {
        TodoEntity todo = todoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(NOT_FOUND_EXCEPTION_MESSAGE, id)));
        todo.setCompleted(Boolean.TRUE);
        TodoEntity updatedTodo = todoRepository.save(todo);

        return modelMapper.map(updatedTodo, TodoDto.class);
    }

    @Override
    public TodoDto inCompleteTodo(Long id) {
        TodoEntity todo = todoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(NOT_FOUND_EXCEPTION_MESSAGE, id)));

        todo.setCompleted(Boolean.FALSE);
        TodoEntity inCompleteTodo = todoRepository.save(todo);

        return modelMapper.map(inCompleteTodo, TodoDto.class);
    }
}
