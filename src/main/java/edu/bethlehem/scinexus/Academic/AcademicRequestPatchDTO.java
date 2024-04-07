package edu.bethlehem.scinexus.Academic;

import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.UserRequestDTO;
import edu.bethlehem.scinexus.User.UserRequestPatchDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class AcademicRequestPatchDTO extends UserRequestPatchDTO {

    @NotBlank(message = "Education Shouldn't be Empty")
    private String education;

    @Enumerated(EnumType.STRING)
    private Position position;

    @NotBlank(message = "badge Shouldn't be Empty")
    private String badge;
}
