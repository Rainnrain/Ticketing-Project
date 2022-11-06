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

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserMapper userMapper, TaskService taskService, UserService userService) {
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
        projectRepository.save(project);
    }

    @Override
    public void complete(String projectCode) {
        Project project= projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
    }

//    @Override
//    public List<ProjectDTO> ListOfProjectsByManager(UserDTO manager) {
//
//       List <ProjectDTO> projectsByManager= projectRepository.findAllByAssignedManager(userMapper.convertToEntity(manager)).stream()
//               .map(projectMapper::convertToDto).collect(Collectors.toList());
//
//       projectsByManager.stream().map(projectDTO -> {
//
//         int complete= taskRepository.findByTaskStatus(Status.COMPLETE).size();
//          int inProgress= taskRepository.findByTaskStatusNot(Status.COMPLETE).size();
//
//           projectDTO.setUnfinishedTaskCounts(inProgress);
//           projectDTO.setCompleteTaskCounts(complete);
//           return projectDTO;
//       }).collect(Collectors.toList());
//
//      return projectsByManager;
//
//               }

//    @Override
//    public List<ProjectDTO> listAllProjectDetails() {
//
//        UserDTO userDTO=userService.findByUserName("harold@manager.com");
//        User manager= userMapper.convertToEntity(userDTO);
//
//        List <ProjectDTO> projectsByManager= projectRepository.findAllByAssignedManager(manager).stream()
//                .map(projectMapper::convertToDto).collect(Collectors.toList());
//
//        projectsByManager.stream().map(projectDTO -> {
//
//            int complete= taskRepository.findByTaskStatus(Status.COMPLETE).size();
//            int inProgress= taskRepository.findByTaskStatusNot(Status.COMPLETE).size();
//
//            projectDTO.setUnfinishedTaskCounts(inProgress);
//            projectDTO.setCompleteTaskCounts(complete);
//            return projectDTO;
//        }).collect(Collectors.toList());
//
//        return projectsByManager;
//    }

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
}



