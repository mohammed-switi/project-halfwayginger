package edu.bethlehem.scinexus.Article;

import edu.bethlehem.scinexus.Journal.JournalRequestPatchDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ArtilceRequestPatchDTO extends JournalRequestPatchDTO {
    // @NotBlank(message = "The Article Subject Shouldn't Be Empty")
    private String subject;

    // @NotBlank(message = "The Article title Shouldn't Be Empty")
    private String title;
}
