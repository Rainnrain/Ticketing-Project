package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;
    private final TaskService taskService;

    private final UserService userService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserMapper userMapper, @Lazy TaskService taskService, @Lazy UserService userService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userMapper = userMapper;
        this.taskService = taskService;
        this.userService = userService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        return projectMapper.convertToDto(projectRepository.findByProjectCode(code));

    }

    @Override
    public List<ProjectDTO> listAllProjects() {
      return  projectRepository.findAll().stream().map(projectMapper::convertToDto)
              .collect(Collectors.toList());

    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
        projectRepository.save(projectMapper.convertToEntity(dto));
    }

    @Override
    public void update(ProjectDTO dto) {

      Project convertedProject= projectMapper.convertToEntity(dto);
      Project project=projectRepository.findByProjectCode(dto.getProjectCode());
      convertedProject.setProjectStatus(project.getProjectStatus());
      convertedProject.setId(project.getId());
      projectRepository.save(convertedProject);

    }

    @Override
    public void deleteById(String projectCode) {

       Project project= projectRepository.findByProjectCode(projectCode);
        project.setIsDeleted(true);
        project.setProjectCode(project.getProjectCode()+"-"+project.getId());
        projectRepository.save(project);
        taskService.deleteByProject(projectMapper.convertToDto(project)); // deletes all tasks associated with a gievn project
    }

    @Override
    public void complete(String projectCode) {
        Project project= projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
        taskService.completeByProject(projectMapper.convertToDto(project)); // marks all tasks as completed associated with a given project
    }



    @Override
    public List<ProjectDTO> listAllProjectDetails() {

        UserDTO currentUserDTO = userService.findByUserName("harold@manager.com");
        User user = userMapper.convertToEntity(currentUserDTO);

        List<Project> list = projectRepository.findAllByAssignedManager(user);

        return list.stream().map(project -> {

                    ProjectDTO obj = projectMapper.convertToDto(project);

                    obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
                    obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));

                    return obj;
                }

        ).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager) {

      List<Project> projectList= projectRepository
                .findAllByProjectStatusIsNotAndAndAssignedManager(Status.COMPLETE, userMapper
                        .convertToEntity(assignedManager));
      return projectList.stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }
}



