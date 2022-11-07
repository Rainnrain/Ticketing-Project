package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;


import java.util.List;

public interface TaskService {

   List<TaskDTO> listAllTasks();

   TaskDTO findById(Long id);
   void save(TaskDTO task);

   void deleteById(Long id);

   TaskDTO update(TaskDTO taskDTO);

   List<TaskDTO> listsAllTasksByStatusIsNot(Status status);

   List<TaskDTO> listAllTasksByStatus(Status status);
   int totalCompletedTask(String projectCode);
   int totalNonCompletedTask(String projectCode);
   void deleteByProject(ProjectDTO projectDTO);
   void completeByProject(ProjectDTO projectDTO);
   List<TaskDTO> listAllNonCompletedByAssignedEmployee(UserDTO AssignedEmployee);
}
