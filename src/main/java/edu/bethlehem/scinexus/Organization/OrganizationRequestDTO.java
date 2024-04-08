package edu.bethlehem.scinexus.Organization;

import edu.bethlehem.scinexus.User.OrganizationType;
import edu.bethlehem.scinexus.User.UserRequestDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizationRequestDTO extends UserRequestDTO {

    @Enumerated(EnumType.STRING)
    // @NotBlank(message = "The Organization Type Must Be Determined")
    // @NotNull(message = "The Organization Type Must Be Determined")
    private OrganizationType type;

}
