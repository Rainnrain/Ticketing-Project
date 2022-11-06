package com.cydeo.entity;


import com.cydeo.enums.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@Where(clause="is_deleted=false")
@Table(name = "tasks")

public class Task extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "projects_id" )
    private Project project;

    @ManyToOne
    @JoinColumn(name="users_id")
    private User assignedEmployee;

    private String taskSubject;

    private String taskDetail;

    @Enumerated(value = EnumType.STRING)
    private Status taskStatus;

    @Column(columnDefinition = "DATE")
    private LocalDate assignedDate;
}
