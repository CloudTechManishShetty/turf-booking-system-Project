package com.turf.turf_booking_system.service.iml;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.turf.turf_booking_system.model.users;
import com.turf.turf_booking_system.repository.UserRepository;
import com.turf.turf_booking_system.service.userService;

@Service
public class userServiceImplement implements userService{

    UserRepository userRepository;

    public userServiceImplement(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public String createUser(users user) {
        userRepository.save(user);
        return "User Created Successfully";
    }

    @Override
    public String updateUser(users user) {
        userRepository.save(user);
        return "Succesfull Updated the User";
    }

    @Override
    public String delete(Long userId) {
        userRepository.deleteById(userId);
        return "User with "+userId+" Deleted Sucessfully";
    }

    @Override
    public users getUser(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public List<users> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public List<users> getUsers(@Param("name") String name) {
        return userRepository.findByName(name);
    }

}
