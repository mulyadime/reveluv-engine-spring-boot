package com.mulyadime.reveluv.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "app_user")
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "seq_app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "seq_app_user")
    @Column(name = "pk_app_user")
    private Long id;
    
    private String userid;
    
    private String name;
    
    private String password;
    
    @Column(name = "is_active")
    private Boolean active;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "app_user_role"
    , joinColumns = @JoinColumn(name = "fk_app_user")
    , inverseJoinColumns = @JoinColumn(name = "fk_app_role"))
    private Set<Role> roles;

}