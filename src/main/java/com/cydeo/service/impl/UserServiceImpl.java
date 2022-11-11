package com.cydeo.service.impl;


import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
       List<User> userList=userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);
       return userList.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String userName) {
    return userMapper.convertToDto(userRepository.findByUserNameAndIsDeleted( userName,false));
    }

    @Override
    public void save(UserDTO user) {

        userRepository.save(userMapper.convertToEntity(user));
    }

//    @Override
//    public void deleteByUserName(String userName) {
//        userRepository.deleteByUserName(userName);
//    } Used for hard deletion, but I am using a soft deletion instead

    @Override
    public UserDTO update(UserDTO userDto) {

        //Update dto obj to entity
        User user= userMapper.convertToEntity(userDto);
        //Set the id and update user from Database
        user.setId(userRepository.findByUserNameAndIsDeleted(userDto.getUserName(),false).getId());
        //save updated user and return
        return userMapper.convertToDto(userRepository.save(user));
    }

    @Override
    public void delete(String username) {
        User user=userRepository.findByUserNameAndIsDeleted(username,false);
        if(checkIfUserCanBeDeleted(user)){
        user.setIsDeleted(true);
        user.setUserName(user.getUserName()+"-"+user.getId());
       userRepository.save(user);}
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {


       return userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role,false)
                .stream()
                .map(userMapper::convertToDto)
                .collect(Collectors.toList());
    }

    private boolean checkIfUserCanBeDeleted(User user){
       switch(user.getRole().getDescription()){

           case "Manager":
               List<ProjectDTO> projectDTOList=projectService
                       .listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));
               return projectDTOList.size()==0;

           case "Employee":
               List<TaskDTO> taskDTOList=taskService
                       .listAllNonCompletedByAssignedEmployee(userMapper.convertToDto(user));
               return taskDTOList.size()==0;

           default:
               return true;

       }
    }


}
