package org.kasarab.cs_demo.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kasarab.cs_demo.domain.UserDTO;
import org.kasarab.cs_demo.domain.UserResponse;
import org.kasarab.cs_demo.entity.User;
import org.kasarab.cs_demo.exceptions.UserServiceException;
import org.kasarab.cs_demo.repository.UserRepository;
import org.kasarab.cs_demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.kasarab.cs_demo.constant.ErrorMessages.*;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger();

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Value("${user.age}")
    private int userAge;

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public User save(User user) {
        log.info("Saving user: {}", user);
        return userRepository.save(user);
    }

    @Override
    public UserDTO create(UserDTO user) {
        log.info("Creating user: {}", user);
        if (!checkUserAge(user.getBirthdate())) {
            log.error("User birthdate is incorrect");
            log.error(String.format(USER_IS_NOT_ALLOWED_BY_AGE_MSG, userAge));
            throw new UserServiceException(String.format(USER_IS_NOT_ALLOWED_BY_AGE_MSG, userAge), HttpStatus.BAD_REQUEST);
        }

        if (existsByEmail(user.getEmail())) {
            log.error("User already exists with email: {}", user.getEmail());
            log.error(String.format(USER_WITH_EMAIL_EXISTS_MSG, user.getEmail()));
            throw new UserServiceException(String.format(USER_WITH_EMAIL_EXISTS_MSG, user.getEmail()), HttpStatus.BAD_REQUEST);
        }

        if (!isValidEmail(user.getEmail())) {
            log.error("Invalid email: {}", user.getEmail());
            log.error(EMAIL_IS_NOT_VALID_MSG);
            throw new UserServiceException(EMAIL_IS_NOT_VALID_MSG, HttpStatus.BAD_REQUEST);
        }

        save(modelMapper.map(user, User.class));
        log.info("User Successfully created: {}", user);
        return user;
    }

    @Override
    public UserDTO update(Long userId, UserDTO user) {
        log.info("Updating user: {}", user);

        User userFromDb = userRepository.findById(userId).orElseThrow(() ->
                new UserServiceException(String.format(USER_WITH_ID_NOT_FOUND_MSG, userId), HttpStatus.NOT_FOUND));

        userFromDb.setFirstname(user.getFirstname());
        userFromDb.setLastname(user.getLastname());
        userFromDb.setBirthdate(user.getBirthdate());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setAddress(user.getAddress());
        userFromDb.setPhoneNumber(user.getPhoneNumber());

        save(userFromDb);
        log.info("User Successfully updated: {}", user);
        return user;
    }

    @Override
    public void delete(Long userId) {
        log.info("Deleting user: {}", userId);
        if (!userRepository.existsById(userId)) {
            log.error("User does not exist with id: {}", userId);
            log.error(String.format(USER_WITH_ID_NOT_FOUND_MSG, userId));
            throw new UserServiceException(String.format(USER_WITH_ID_NOT_FOUND_MSG, userId), HttpStatus.NOT_FOUND);
        }

        userRepository.deleteById(userId);
        log.info("User Successfully deleted: {}", userId);
    }

    @Override
    public UserDTO findById(Long userId) {
        log.info("Finding user: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserServiceException(String.format(USER_WITH_ID_NOT_FOUND_MSG, userId), HttpStatus.NOT_FOUND));

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO findByFirstname(String firstname) {
        log.info("Finding user: {}", firstname);
        User user = userRepository.findByFirstname(firstname).orElseThrow(() ->
                new UserServiceException(String.format(USER_NOT_FOUND_MSG, firstname), HttpStatus.NOT_FOUND));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO findByLastname(String lastname) {
        log.info("Finding user: {}", lastname);
        User user = userRepository.findByLastname(lastname).orElseThrow(() ->
                new UserServiceException(String.format(USER_NOT_FOUND_MSG, lastname), HttpStatus.NOT_FOUND));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("Checking if user exists with email: {}", email);
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserDTO> findAll() {
        log.info("Finding all users");
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOList = userList
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        log.info("Found {} users", userDTOList.size());
        return userDTOList;
    }

    @Override
    public List<UserDTO> findUsersByBirthday(LocalDate dateFrom, LocalDate dateTo) {
        log.info("Finding users by birthday");
        if (dateFrom.isAfter(dateTo)) {
            log.error(DATE_MSG);
            throw new UserServiceException(DATE_MSG, HttpStatus.BAD_REQUEST);
        }
        List<User> userList = userRepository.findByBirthdateBetween(dateFrom, dateTo);
        List<UserDTO> userDTOList = userList
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        log.info("Found {} users", userDTOList.size());
        return userDTOList;
    }

    @Override
    public boolean checkUserAge(LocalDate date) {
        log.info("Checking if user age is valid");
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(date, currentDate);
        int age = period.getYears();
        return age >= userAge;
    }

    @Override
    public UserResponse getAllUsersPagination(int pageNo, int pageSize) {

        PageRequest pageable = PageRequest.of(pageNo, pageSize);
        Page<User> users = userRepository.findAll(pageable);
        List<User> userList = users.getContent();
        List<UserDTO> userDTOList = userList
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        return new UserResponse(
                userDTOList,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isLast()
        );
    }

    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
