package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;

import java.util.List;

public interface ProjectService {

    ProjectDTO getByProjectCode(String code);
    List<ProjectDTO> listAllProjects();
    void save(ProjectDTO dto);
    void update(ProjectDTO dto);
    void deleteById(String ProjectCode);
    void complete(String ProjectCode);
    List<ProjectDTO> listAllProjectDetails();
    List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager);


}
