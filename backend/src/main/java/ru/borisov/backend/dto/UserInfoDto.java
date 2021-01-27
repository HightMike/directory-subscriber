package ru.borisov.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {

    @Size(min = 1, max = 10)
    private String firstName;

    @Size(min = 1, max = 20)
    private String lastName;

    @Pattern(
            regexp = "^\\d{3}-?\\d{3}-?\\d{2}-?\\d{2}$",
            message = "Invalid phone format!"
    )
    private String workPhone;

    @Pattern(
            regexp = "^\\d{3}-?\\d{3}-?\\d{2}-?\\d{2}$",
            message = "Invalid phone format!"
    )
    private String mobile;

    @Pattern(
            regexp = "^([\\w\\.\\-]+)@([\\w\\-]+)$",
            message = "Invalid email format!"
    )
    private String email;



}
