package edu.bethlehem.scinexus.Post;

import edu.bethlehem.scinexus.Journal.Visibility;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestPatchDTO {

    @NotBlank(message = "The Journal Description Shouldn't Be Empty")
    private String description;

    @NotBlank(message = "The Journal Content Shouldn't Be Empty")
    private String content;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Min(value = 1, message = "Id can't be less than One")
    private long publisherId;
}
