package com.turf.turf_booking_system.service.iml;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.turf.turf_booking_system.model.LoginRequest;
import com.turf.turf_booking_system.model.LoginResponse;
import com.turf.turf_booking_system.model.users;
import com.turf.turf_booking_system.repository.UserRepository;
import com.turf.turf_booking_system.service.userService;

@Service
public class userServiceImplement implements userService{

    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        userRepository.saveAndFlush(user);
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

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) throws Exception {
        users user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new Exception("Invalid email or password"));

            if (!loginRequest.getPassword().equals(user.getPsw())) {
                throw new Exception("Invalid email or password");
            }

        return new LoginResponse(user.getUserId(), user.getEmail(), "Login successful");
    }

}
