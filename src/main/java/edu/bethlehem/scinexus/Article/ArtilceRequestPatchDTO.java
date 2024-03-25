package edu.bethlehem.scinexus.Article;

import edu.bethlehem.scinexus.Journal.JournalPatchDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ArtilceRequestPatchDTO extends JournalPatchDTO {
    @NotBlank(message = "The Article Subject Shouldn't Be Empty")
    private String subject;

    @NotBlank(message = "The Article title Shouldn't Be Empty")
    private String title;
}
