package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, @Lazy ProjectMapper projectMapper, @Lazy UserService userService, UserMapper userMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
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
                updatedTask.setTaskStatus(
                        taskDTO.getTaskStatus()==null?task.get().getTaskStatus():taskDTO.getTaskStatus());
            }

        taskRepository.save(updatedTask);
        return taskMapper.convertToDTO(updatedTask);
    }

    @Override
    public List<TaskDTO> listsAllTasksByStatusIsNot(Status status) {
        UserDTO loggedInUser= userService.findByUserName("john@employee.com");
        List<Task> tasks=  taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status,userMapper.convertToEntity(loggedInUser));

        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {
        UserDTO loggedInUser= userService.findByUserName("john@employee.com");
        List<Task> tasks=  taskRepository.findAllByTaskStatusAndAssignedEmployee(status,userMapper.convertToEntity(loggedInUser));

        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }


    @Override
    public int totalCompletedTask(String projectCode) {

     return  taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {

        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO projectDTO) {
        Project project= projectMapper.convertToEntity(projectDTO);
        List<Task> tasks= taskRepository.findAllByProject(project);
        tasks.forEach(task->deleteById(task.getId()));
    }

    @Override
    public void completeByProject(ProjectDTO projectDTO) {
        Project project= projectMapper.convertToEntity(projectDTO);
        List<Task> tasks= taskRepository.findAllByProject(project);
        tasks.stream().map(taskMapper::convertToDTO)
                .forEach(taskDTO->{
                    taskDTO.setTaskStatus(Status.COMPLETE);
                    update(taskDTO);
                });

    }

    @Override
    public List<TaskDTO> listAllNonCompletedByAssignedEmployee(UserDTO assignedEmployee) {

        List<Task> taskList= taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(Status.COMPLETE, userMapper
                        .convertToEntity(assignedEmployee));
        return taskList.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }
}