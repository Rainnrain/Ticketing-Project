package com.cydeo.entity;

import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name="projects")
@NoArgsConstructor
@Where(clause="is_deleted=false") // Adds this condition to all queries
public class Project extends BaseEntity{


      @Column(unique=true)
      private String projectCode;
      private String projectName;

      @Column(columnDefinition = "DATE")
      private LocalDate startDate;

      @Column(columnDefinition = "DATE")
      private LocalDate endDate;

      @Enumerated(EnumType.STRING)
      private Status projectStatus;


      private String projectDetail;

      @ManyToOne(fetch=FetchType.LAZY)
      @JoinColumn(name="manager_id")
      private User assignedManager;


//      private int completeTaskCounts;
//      private int unfinishedTaskCounts;



}
