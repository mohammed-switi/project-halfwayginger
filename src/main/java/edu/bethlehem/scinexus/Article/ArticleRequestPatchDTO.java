package edu.bethlehem.scinexus.Article;

import edu.bethlehem.scinexus.Journal.JournalRequestPatchDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public class ArticleRequestPatchDTO extends JournalRequestPatchDTO {
    // @NotBlank(message = "The Article Subject Shouldn't Be Empty")
    private String subject;

    // @NotBlank(message = "The Article title Shouldn't Be Empty")
    private String title;
}
