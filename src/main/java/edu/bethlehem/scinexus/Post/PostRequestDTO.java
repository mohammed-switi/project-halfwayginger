package edu.bethlehem.scinexus.Post;

import edu.bethlehem.scinexus.Journal.Visibility;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {

    @NotNull(message = "The Journal Description Shouldn't Be Null")
    @NotBlank(message = "The Journal Description Shouldn't Be Empty")
    private String description;

    @NotNull(message = "The Journal Content Shouldn't Be Null")
    @NotBlank(message = "The Journal Content Shouldn't Be Empty")
    private String content;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Visibility visibility;

}
