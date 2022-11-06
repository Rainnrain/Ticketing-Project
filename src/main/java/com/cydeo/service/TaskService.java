package com.cydeo.service;

import com.cydeo.dto.TaskDTO;


import java.util.List;

public interface TaskService {

   List<TaskDTO> listAllTasks();

   TaskDTO findById(Long id);
   void save(TaskDTO task);

   void deleteById(Long id);

   void update(TaskDTO taskDTO);
}
