package com.dishakaw.assignment.model;

import javax.persistence.*;

@Entity
public class UserRole {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors, getters, and setters...
}
