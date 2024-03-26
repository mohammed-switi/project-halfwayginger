package edu.bethlehem.scinexus.Academic;

import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.UserRequestDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicRequestDTO extends UserRequestDTO {

    @NotNull(message = "Education Shouldn't be Null")
    @NotBlank(message = "Education Shouldn't be Empty")
    private String education;

    @Enumerated(EnumType.STRING)
    private Position position;

    @NotBlank(message = "badge Shouldn't be Empty")
    private String badge;
}
