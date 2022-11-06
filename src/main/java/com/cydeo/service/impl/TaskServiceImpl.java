package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final ProjectRepository projectRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectRepository = projectRepository;
    }


    @Override
    public List<TaskDTO> listAllTasks() {
        return taskRepository.findAll().stream().map(taskMapper::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return taskMapper.convertToDTO(task.get());
        }
        return null;
    }

    @Override
    public void save(TaskDTO taskDTO) {

        taskDTO.setTaskStatus(Status.OPEN);
        taskDTO.setAssignedDate(LocalDate.now());
        Task task = taskMapper.convertToEntity(taskDTO);
        taskRepository.save(task);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isPresent()) {
            foundTask.get().setIsDeleted(true);
        }

        taskRepository.save(foundTask.get());
    }

    @Override
    public TaskDTO update(TaskDTO taskDTO) {
        Optional<Task> task = taskRepository.findById(taskDTO.getId());
        Task updatedTask = taskMapper.convertToEntity(taskDTO);

        if(task.isPresent() ) {
            updatedTask.setAssignedDate(task.get().getAssignedDate());

            if (taskDTO.getTaskStatus() == null) {
                updatedTask.setTaskStatus(task.get().getTaskStatus());
            }
        }
        taskRepository.save(updatedTask);
        return taskMapper.convertToDTO(updatedTask);
    }

    @Override
    public List<TaskDTO> findCompletedTasks() {
        return taskRepository.findByTaskStatus(Status.COMPLETE).stream().map(
                taskMapper::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public List<TaskDTO> findUncompletedTasks() {
        return taskRepository.findByTaskStatusNot(Status.COMPLETE).stream().map(
                taskMapper::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public int totalCompletedTask(String projectCode) {

     return  taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
       return taskRepository.totalNonCompletedTasks(projectCode);
    }
}