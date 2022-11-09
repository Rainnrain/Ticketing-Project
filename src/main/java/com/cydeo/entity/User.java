package com.cydeo.entity;

import com.cydeo.enums.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="users")
@NoArgsConstructor
//@Where(clause="is_deleted=false") // created custom queries to avoid errors when dealing with projects associated with deleted users.
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String userName;
    private String passWord;
    private boolean enabled;
    private String phone;
    @ManyToOne
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;


}
