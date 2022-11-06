package com.cydeo.repository;


import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {


List<Task> findByTaskStatus(Status status);
    List<Task> findByTaskStatusNot(Status status);

//    @Query(value = " Select COUNT(*) " +
//            "FROM tasks t join projects p on t.project_id=p.id " +
//            "where p.project_code=?1 AND t.task_status ='COMPLETE', nativeQuery = true)
//    int totalCompletedTasks(String projectCode);
//
//    @Query("select count (t) FROM Task t where t.project.projectCode=?1 " +
//            "AND t.taskStatus <> 'COMPLETE' ")
//    int totalNonCompletedTasks(@Param("ProjectCode")String projectCode);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectCode = " +
            "?1 AND t.taskStatus <> 'COMPLETE'")
    int totalNonCompletedTasks(String projectCode);

    @Query(value = "SELECT COUNT(*) " +
            "FROM tasks t JOIN projects p on t.project_id=p.id " +
            "WHERE p.project_code=?1 AND t.task_status='COMPLETE'",nativeQuery = true)
    int totalCompletedTasks(String projectCode);



}
