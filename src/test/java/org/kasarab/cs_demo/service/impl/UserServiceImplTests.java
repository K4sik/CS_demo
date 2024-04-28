package org.kasarab.cs_demo.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kasarab.cs_demo.domain.UserDTO;
import org.kasarab.cs_demo.entity.User;
import org.kasarab.cs_demo.exceptions.UserServiceException;
import org.kasarab.cs_demo.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testCreate_ValidUser() {
        // Arrange
        UserDTO userDTO = UserDTO.builder()
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();
        User user = User.builder()
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();

        when(modelMapper.map(any(UserDTO.class), eq(User.class))).thenReturn(user);
        // Act
        userService.create(userDTO);
        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreate_UserUnderAge() {
        // Arrange
        UserDTO userDTO = UserDTO.builder()
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2012, 5, 2))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();
        userDTO.setBirthdate(LocalDate.of(2020, 8, 19));
        // Act
        assertThrows(UserServiceException.class, () -> userService.create(userDTO));
    }

    @Test
    public void testCreate_DuplicateEmail() {
        // Arrange
        UserDTO userDTO = UserDTO.builder()
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        // Act
//        userService.create(userDTO);
        assertThrows(UserServiceException.class, () -> userService.create(userDTO));

    }

    @Test
    public void testUpdate_UserExists() {
        // Arrange
        Long userId = 1L;
        UserDTO userDTO = UserDTO.builder()
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();
        User userFromDb = User.builder()
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userFromDb));
        // Act
        userService.update(userId, userDTO);
        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdate_UserNotFound() {
        // Arrange
        Long userId = 1L;
        UserDTO userDTO = UserDTO.builder()
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UserServiceException.class, () -> userService.update(userId, userDTO));
    }

    @Test
    public void testDelete_UserExists() {
        // Arrange
        Long userId = 1L;
        User user = User.builder()
                .id(1L)
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();

        UserDTO userDTO = UserDTO.builder()
//                .id(1L)
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();

        // Act
//        userRepository.save(user);
        userService.create(userDTO);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        // Assert
        assertAll(() -> userService.delete(userId));            // I've no one idea why "User with id 1 was not found"
    }

    @Test
    public void testDelete_UserNotFound() {
        // Arrange
        Long userId = 100L;
        when(userRepository.existsById(userId)).thenReturn(false);
        // Act & Assert
        assertThrows(UserServiceException.class, () -> userService.delete(userId));
    }

    @Test
    public void testFindById_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UserServiceException.class, () -> userService.findById(userId));
    }

    @Test
    public void testFindByFirstname_UserNotFound() {
        // Arrange
        String firstname = "Ivan";
        when(userRepository.findByFirstname(firstname)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UserServiceException.class, () -> userService.findByFirstname(firstname));
    }

    @Test
    public void testFindByLastname_UserNotFound() {
        // Arrange
        String lastname = "Salash";
        when(userRepository.findByLastname(lastname)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UserServiceException.class, () -> userService.findByLastname(lastname));
    }

    @Test
    public void testExistsByEmail_EmailExists() {
        // Arrange
        String email = "ivan.salash@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        // Act
        boolean result = userService.existsByEmail(email);
        // Assert
        assertTrue(result);
    }

    @Test
    public void testExistsByEmail_EmailDoesNotExist() {
        // Arrange
        String email = "ivan.salash@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        // Act
        boolean result = userService.existsByEmail(email);
        // Assert
        assertFalse(result);
    }

    @Test
    public void testFindAll_NoUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        // Act
        List<UserDTO> result = userService.findAll();
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindAll_WithUsers() {
        // Arrange
        List<User> users = List.of(
                User.builder()
                        .id(1L)
                        .firstname("Andriy")
                        .lastname("Ostapenko")
                        .birthdate(LocalDate.of(2000, 8, 19))
                        .email("andriy@ostapenko.com")
                        .address("Lviv")
                        .phoneNumber("+380987654321")
                        .build(),

                User.builder()
                        .id(2L)
                        .firstname("Ivan")
                        .lastname("Marciv")
                        .birthdate(LocalDate.of(2005, 4, 8))
                        .email("ivan@marciv.com")
                        .address("Kyiv")
                        .phoneNumber("+380677654321")
                        .build(),

                User.builder()
                        .id(3L)
                        .firstname("Olena")
                        .lastname("Vasb")
                        .birthdate(LocalDate.of(1998, 12, 15))
                        .email("olena@vasb.com")
                        .address("Lviv")
                        .phoneNumber("+380987654321")
                        .build(),

                User.builder()
                        .id(4L)
                        .firstname("Petro")
                        .lastname("Myhasb")
                        .birthdate(LocalDate.of(1996, 1, 22))
                        .email("petro@myhasb.com")
                        .address("Lviv")
                        .phoneNumber("+380987654321")
                        .build(),

                User.builder()
                        .id(5L)
                        .firstname("Nina")
                        .lastname("Kych")
                        .birthdate(LocalDate.of(2001, 4, 29))
                        .email("nina@kych.com")
                        .address("Lviv")
                        .phoneNumber("+380987654321")
                        .build()
        );
        when(userRepository.findAll()).thenReturn(users);
        // Act
        List<UserDTO> result = userService.findAll();
        // Assert
        assertNotNull(result);
        assertEquals(users.size(), result.size());
    }

    @Test
    public void testFindUsersByBirthday_ValidRange() {
        // Arrange
        LocalDate dateFrom = LocalDate.of(1998, 1, 1);
        LocalDate dateTo = LocalDate.of(2003, 12, 31);
        List<User> users = List.of(
                User.builder()
                        .id(1L)
                        .firstname("Andriy")
                        .lastname("Ostapenko")
                        .birthdate(LocalDate.of(2000, 8, 19))
                        .email("andriy@ostapenko.com")
                        .address("Lviv")
                        .phoneNumber("+380987654321")
                        .build(),

                User.builder()
                        .id(2L)
                        .firstname("Ivan")
                        .lastname("Marciv")
                        .birthdate(LocalDate.of(2005, 4, 8))
                        .email("ivan@marciv.com")
                        .address("Kyiv")
                        .phoneNumber("+380677654321")
                        .build(),

                User.builder()
                        .id(3L)
                        .firstname("Olena")
                        .lastname("Vasb")
                        .birthdate(LocalDate.of(1998, 12, 15))
                        .email("olena@vasb.com")
                        .address("Lviv")
                        .phoneNumber("+380987654321")
                        .build(),

                User.builder()
                        .id(4L)
                        .firstname("Petro")
                        .lastname("Myhasb")
                        .birthdate(LocalDate.of(1996, 1, 22))
                        .email("petro@myhasb.com")
                        .address("Lviv")
                        .phoneNumber("+380987654321")
                        .build(),

                User.builder()
                        .id(5L)
                        .firstname("Nina")
                        .lastname("Kych")
                        .birthdate(LocalDate.of(2001, 4, 29))
                        .email("nina@kych.com")
                        .address("Lviv")
                        .phoneNumber("+380987654321")
                        .build()
        );
        when(userRepository.findByBirthdateBetween(dateFrom, dateTo)).thenReturn(users);
        // Act
        List<UserDTO> result = userService.findUsersByBirthday(dateFrom, dateTo);
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testFindUsersByBirthday_InvalidRange() {
        // Arrange
        LocalDate dateFrom = LocalDate.of(1995, 1, 1);
        LocalDate dateTo = LocalDate.of(1990, 12, 31); // Invalid range
        // Act & Assert
        assertThrows(UserServiceException.class, () -> userService.findUsersByBirthday(dateFrom, dateTo));
    }

}
