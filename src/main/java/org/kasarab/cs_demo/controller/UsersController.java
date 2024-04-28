package org.kasarab.cs_demo.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kasarab.cs_demo.domain.UserDTO;
import org.kasarab.cs_demo.domain.UserResponse;
import org.kasarab.cs_demo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UsersController {

    private static final Logger log = LogManager.getLogger();

    private final UserServiceImpl userService;

    @Autowired
    public UsersController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        log.info("Get all users");
        List<UserDTO> userList = userService.findAll();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/all/pagination")
    public ResponseEntity<UserResponse> getAllUsersPagination(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        log.info("Getting all users with mapper, page number: {}, page size: {}", pageNo, pageSize);
        return new ResponseEntity<>(userService.getAllUsersPagination(pageNo, pageSize), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDTO user) {
        log.info("Add user: {}", user);
        userService.create(user);
        log.info("User Successfully Created");
        return new ResponseEntity<>("User Successfully Created", HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        log.info("Get user: {}", userId);
        UserDTO user = userService.findById(userId);
        log.info("User found: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody @Valid UserDTO user) {
        log.info("Update user: {}", user);
        userService.update(userId, user);
        log.info("User Successfully Updated");
        return new ResponseEntity<>("User Successfully Updated", HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        log.info("Delete user: {}", userId);
        userService.delete(userId);
        log.info("User Successfully Deleted");
        return new ResponseEntity<>("User Successfully Deleted from Database", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAllUsersByBirthdate(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateFrom,
                                                       @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateTo) {
        log.info("Search all users by birthdate dateFrom: {}", dateFrom);
        log.info("Search all users by birthdate dateTo: {}", dateTo);
        return new ResponseEntity<>(userService.findUsersByBirthday(dateFrom, dateTo), HttpStatus.OK);
    }
}
