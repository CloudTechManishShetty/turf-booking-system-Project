package com.turf.turf_booking_system.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turf.turf_booking_system.model.users;
import com.turf.turf_booking_system.service.userService;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/users")
public class UserController {
    
    // @Autowired
    // private UserRepository userRepository;

    // // Get all users
    // @GetMapping
    // public List<users> getAllUsers() {
    //     return userRepository.findAll();
    // }
    @Autowired
    userService userservice;
    public UserController(userService uService){
        this.userservice=uService;
    }

    //for Specific User based on ID.
    @GetMapping("{user_Id}")
    public users getUserDetails(@PathVariable("user_Id") Long user_Id) {
        return userservice.getUser(user_Id);
    }

    //for All users in Databse.
    @GetMapping
    public List<users> getAllUsers() {
        return userservice.getAllUser();
    }

    //To create new Users
    @PostMapping
    public String createUser(@RequestBody users user) {
        userservice.createUser(user);
        return "Sucessfull Create a User";
    }
    
    //To Update Users.
    @PutMapping
    public String updateUser(@RequestBody users user) {
        userservice.updateUser(user);
        return "Sucessfully Updated the User.";
    }

    //To Delete a User
    @DeleteMapping("{user_Id}")
    public String deleteUser(@PathVariable("user_Id") Long user_Id) {
        userservice.delete(user_Id);
        return "Sucessfully Deleted a User with User-ID: "+user_Id;
    }
        
}
