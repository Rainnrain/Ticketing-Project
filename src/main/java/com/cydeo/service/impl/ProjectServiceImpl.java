package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
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
        if(dto.getProjectStatus()==null) {
            dto.setProjectStatus(Status.OPEN);
        }
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
}
