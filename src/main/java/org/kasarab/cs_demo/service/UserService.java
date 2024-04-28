package org.kasarab.cs_demo.service;


import org.kasarab.cs_demo.domain.UserDTO;
import org.kasarab.cs_demo.domain.UserResponse;
import org.kasarab.cs_demo.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    User save(User user);

    UserDTO create(UserDTO user);

    UserDTO update(Long userId, UserDTO user);

    void delete(Long userId);

    UserDTO findById(Long id);

    UserDTO findByFirstname(String firstname);

    UserDTO findByLastname(String lastname);

    boolean existsByEmail(String email);

    List<UserDTO> findAll();

    List<UserDTO> findUsersByBirthday(LocalDate dateFrom, LocalDate dateTo);

    boolean checkUserAge(LocalDate date);

    UserResponse getAllUsersPagination(int pageNo, int pageSize);
}
