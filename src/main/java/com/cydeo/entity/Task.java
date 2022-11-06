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


    private String taskSubject;

    private String taskDetail;

    @Enumerated( EnumType.STRING)
    private Status taskStatus;

    @Column(columnDefinition = "DATE")
    private LocalDate assignedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User assignedEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id" )// not necessary since it will already be created like this
    private Project project;
}
