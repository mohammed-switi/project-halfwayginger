package edu.bethlehem.scinexus.Article;

import edu.bethlehem.scinexus.Journal.JournalRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArticleRequestDTO extends JournalRequestDTO {

    private String subject;

    @NotBlank(message = "The Article title Shouldn't Be Empty")
    @NotNull(message = "The Article title Shouldn't Be Null")
    private String title;

    @NotBlank(message = "The Article title Shouldn't Be Empty")
    @NotNull(message = "The Article title Shouldn't Be Null")
    private String brief;
}
