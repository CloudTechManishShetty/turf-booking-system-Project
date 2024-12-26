package com.turf.turf_booking_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class users {
    @Id
    private Long user_Id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;
    private boolean isApproved;

    //Constructor
    public users(Long id,String name,String email,String phone,String password,String role,boolean apprStatus){
        this.user_Id=id;
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.password=password;
        this.role=role;
        this.isApproved=apprStatus;
    }

    //Empty Constructor
    public users(){}

    // Getters and Setters

    public Long getUserId() {
        return user_Id;
    }

    public void setUserId(Long userId) {
        this.user_Id = userId;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
        
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPsw() {
        return password;
    }

    public void setPsw(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}