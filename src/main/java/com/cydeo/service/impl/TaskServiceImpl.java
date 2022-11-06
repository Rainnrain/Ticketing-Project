package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.TaskRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        List<TaskDTO> taskDTOList= taskRepository.findAll().stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
       return  taskDTOList;
    }

    @Override
    public TaskDTO findById(Long id) {

        return taskMapper.convertToDTO(taskRepository.findById(id).get());
    }

    @Override
    public void save(TaskDTO taskDTO) {
        if(taskDTO.getTaskStatus()==null) {
            taskDTO.setTaskStatus(Status.OPEN);
        }
        if(taskDTO.getAssignedDate()==null){
            taskDTO.setAssignedDate(LocalDate.now());
        }
       Task task=taskMapper.convertToEntity(taskDTO);


        taskRepository.save(task);

    }

    @Override
    public void deleteById(Long id) {
        Task task=taskRepository.findById(id).orElseThrow();
        task.setIsDeleted(true);
        taskRepository.save(task);
    }
}
