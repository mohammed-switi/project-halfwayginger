package edu.bethlehem.scinexus.Organization;

import edu.bethlehem.scinexus.User.OrganizationType;
import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.UserRequestDTO;
import edu.bethlehem.scinexus.User.UserRequestPatchDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRequestPatchDTO extends UserRequestPatchDTO {

    @Enumerated(EnumType.STRING)
    // @NotBlank(message = "The Organization Type Must Be Determined")
    // @NotNull(message = "The Organization Type Must Be Determined")
    private OrganizationType type;

}
