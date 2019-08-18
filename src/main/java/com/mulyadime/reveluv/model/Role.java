package com.mulyadime.reveluv.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "app_role")
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "seq_app_role")
public class Role {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_app_role")
    @Column(name = "pk_app_role")
    private Long id;
    
    @Column(name = "role_name")
    private String role;
}
