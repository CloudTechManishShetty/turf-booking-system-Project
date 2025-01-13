    package com.turf.turf_booking_system.controller;
    import java.util.List;

    import org.springframework.beans.factory.annotation.Autowired;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.BadCredentialsException;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.bind.annotation.DeleteMapping;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import com.turf.turf_booking_system.Security.utillis.JwtUtil;
    import com.turf.turf_booking_system.dto.LoginRequest;
    import com.turf.turf_booking_system.dto.LoginResponse;
    import com.turf.turf_booking_system.model.users;
    import com.turf.turf_booking_system.service.userService;


    import org.springframework.web.bind.annotation.PutMapping;
    import org.springframework.web.bind.annotation.RequestParam;



    @RestController
    @RequestMapping("api/users")
    public class UserController {
        

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        userService userservice;
        public UserController(userService uService){
            this.userservice=uService;
        }

        //for Specific User based on ID.
        @PreAuthorize("hasRole('user') or hasRole('admin') or hasRole('super_admin')")
        @GetMapping("{user_Id}")
        public users getUserDetails(@PathVariable("user_Id") Long user_Id) {
            return userservice.getUser(user_Id);
        }

        //for specific user based on Name.
        @PreAuthorize("hasRole('admin') or hasRole('super_admin')")
        @GetMapping("/search")
        public ResponseEntity<List<users>> searchUsers(@RequestParam String name) {
            List<users> users = userservice.getUsers(name);
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(users);
        }

        //for All users in Databse.
        @PreAuthorize("hasRole('super_admin')")
        @GetMapping
        public List<users> getAllUsers() {
            return userservice.getAllUser();
        }

        //To create new Users
        @PostMapping(value = "/reg/signup", consumes = "application/json")
        public ResponseEntity<String> createUser(@RequestBody users user) {
            userservice.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
        }
        
        //changes made here
        @PostMapping("/login")
        public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
            // Log the incoming request
            System.out.println("Login request received for email: " + loginRequest.getEmail());
            
            try {
                // Authenticate the user
                Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                    )
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
    
                // Get the authenticated user's email
                String email = authentication.getName();
    
                // Fetch the user from the database
                users user = userservice.findByEmail(email);
    
                // Generate JWT token
                String jwtToken = jwtUtil.generateToken(user.getUserId());
    
                // Create and return the LoginResponse
                LoginResponse loginResponse = new LoginResponse(
                    user.getUserId(),
                    user.getEmail(),
                    "Login successful",
                    user.getRole(),
                    jwtToken
                );
    
                return ResponseEntity.ok(loginResponse);
    
            } catch (BadCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password");
            } catch (Exception e) {
                e.printStackTrace(); // Log the exception for debugging
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An error occurred during login");
            }
        }

        //To Update Users.
        @PreAuthorize("hasRole('user') or hasRole('super_admin')")
        @PutMapping
        public String updateUser(@RequestBody users user) {
            userservice.updateUser(user);
            return "Sucessfully Updated the User.";
        }

        //To Delete a User
        @PreAuthorize("hasRole('super_admin')")
        @DeleteMapping("{user_Id}")
        public String deleteUser(@PathVariable("user_Id") Long user_Id) {
            userservice.delete(user_Id);
            return "Sucessfully Deleted a User with User-ID: "+user_Id;
        }    
    }
