package com.cydeo.entity;

import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
@Setter
@Getter
@Entity
@Table(name="projects")
@NoArgsConstructor
@Where(clause="is_deleted=false")
public class Project extends BaseEntity{



      private String projectCode;
      private String projectName;

      @Column(columnDefinition = "DATE")
      private LocalDate startDate;

      @Column(columnDefinition = "DATE")
      private LocalDate endDate;

      @Enumerated(EnumType.STRING)
      private Status projectStatus;

      @Column(columnDefinition = "text")
      private String projectDetail;

      @ManyToOne(fetch=FetchType.LAZY)
      @JoinColumn(name="manager_id")
      private User assignedManager;



}
