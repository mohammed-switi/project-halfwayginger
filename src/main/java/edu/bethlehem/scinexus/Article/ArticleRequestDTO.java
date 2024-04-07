package edu.bethlehem.scinexus.Article;

import edu.bethlehem.scinexus.Journal.JournalRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public class ArticleRequestDTO extends JournalRequestDTO {
    @NotBlank(message = "The Article Subject Shouldn't Be Empty")
    @NotNull(message = "The Article title Shouldn't Be Null")
    private String subject;

    @NotBlank(message = "The Article title Shouldn't Be Empty")
    @NotNull(message = "The Article title Shouldn't Be Null")
    private String title;
}
