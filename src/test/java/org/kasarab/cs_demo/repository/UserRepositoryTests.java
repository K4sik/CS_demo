package org.kasarab.cs_demo.repository;

//import org.junit.jupiter.api.Assertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kasarab.cs_demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindById() {
        //Arrange
        User user = User.builder()
                .id(1L)
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();

        //Act
        userRepository.save(user);

        //Assert
        Assertions.assertThat(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    public void testFindByFirstname() {
        //Arrange
        User user = User.builder()
                .id(1L)
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();

        //Act
        userRepository.save(user);

        //Assert
        Assertions.assertThat(userRepository.findByFirstname("Andriy").isPresent());
    }

    @Test
    public void testFindByLastname() {
        //Arrange
        User user = User.builder()
                .id(1L)
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();

        //Act
        userRepository.save(user);

        //Assert
        Assertions.assertThat(userRepository.findByLastname("Ostapenko").isPresent());
    }

    @Test
    public void testGetAllUsers() {
        //Arrange
        User user = User.builder()
                .id(1L)
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();

        User user2 = User.builder()
                .id(2L)
                .firstname("Ivan")
                .lastname("Marciv")
                .birthdate(LocalDate.of(2001, 4, 8))
                .email("ivan@marciv.com")
                .address("Kyiv")
                .phoneNumber("+380677654321")
                .build();

        //Act
        userRepository.save(user);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        //Assert
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isEqualTo(12);          // change to number 2
    }

    @Test
    public void testExistsByEmail() {
        //Arrange
        User user = User.builder()
                .id(1L)
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();

        //Act
        userRepository.save(user);

        //Assert
        Assertions.assertThat(userRepository.existsByEmail(user.getEmail())).isEqualTo(true);
    }

    @Test
    public void testFindByBirthdateBetweenRange() {
        //Arrange
        User user = User.builder()
                .id(1L)
                .firstname("Andriy")
                .lastname("Ostapenko")
                .birthdate(LocalDate.of(2000, 8, 19))
                .email("andriy@ostapenko.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();

        User user2 = User.builder()
                .id(2L)
                .firstname("Ivan")
                .lastname("Marciv")
                .birthdate(LocalDate.of(2005, 4, 8))
                .email("ivan@marciv.com")
                .address("Kyiv")
                .phoneNumber("+380677654321")
                .build();

        User user3 = User.builder()
                .id(3L)
                .firstname("Olena")
                .lastname("Vasb")
                .birthdate(LocalDate.of(1998, 12, 15))
                .email("olena@vasb.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();

        User user4 = User.builder()
                .id(4L)
                .firstname("Petro")
                .lastname("Myhasb")
                .birthdate(LocalDate.of(1996, 1, 22))
                .email("petro@myhasb.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();

        User user5 = User.builder()
                .id(5L)
                .firstname("Nina")
                .lastname("Kych")
                .birthdate(LocalDate.of(2001, 4, 29))
                .email("nina@kych.com")
                .address("Lviv")
                .phoneNumber("+380987654321")
                .build();

        //Act
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        List<User> userList = userRepository.findByBirthdateBetween(
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2002, 1, 1)
        );

        //Assert
        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isEqualTo(2);
    }


}
