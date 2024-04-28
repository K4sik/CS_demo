package org.kasarab.cs_demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Valid
    @Size(min = 5, max = 50, message = "Firstname must contain from 5 to 50 characters")
    @NotBlank(message = "Firstname cannot be empty")
    private String firstname;

    @Valid
    @Size(min = 5, max = 50, message = "Lastname must contain from 5 to 50 characters")
    @NotBlank(message = "Lastname cannot be empty")
    private String lastname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthdate;

    @Valid
    @Size(min = 5, max = 50, message = "Email must contain from 5 to 50 characters")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    private String address;

    private String phoneNumber;
}
