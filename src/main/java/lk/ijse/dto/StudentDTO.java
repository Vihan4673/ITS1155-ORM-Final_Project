package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDTO {
    private String studentId;
    private String name;
    private String address;
    private Long tel;
    private Date registrationDate;
}
