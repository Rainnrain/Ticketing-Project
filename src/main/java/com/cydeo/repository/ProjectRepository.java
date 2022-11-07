package com.cydeo.repository;

import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByProjectCode(String ProjectCode);
    List<Project> findAllByAssignedManager(User manager);

    List<Project> findAllByProjectStatusIsNotAndAndAssignedManager(Status status, User user);

}
