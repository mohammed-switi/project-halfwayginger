package edu.bethlehem.scinexus.Academic;

import edu.bethlehem.scinexus.User.Position;

import edu.bethlehem.scinexus.User.UserRequestPatchDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AcademicRequestPatchDTO extends UserRequestPatchDTO {

    @NotBlank(message = "Education Shouldn't be Empty")
    private String education;

    @Enumerated(EnumType.STRING)
    private Position position;

    @NotBlank(message = "badge Shouldn't be Empty")
    private String badge;
}
