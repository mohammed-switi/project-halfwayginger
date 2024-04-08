package edu.bethlehem.scinexus.ResearchPaper;

import edu.bethlehem.scinexus.Journal.JournalRequestDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResearchPaperRequestDTO extends JournalRequestDTO {

    @Enumerated(EnumType.STRING)
    @NotNull
    private ResearchLanguage language;

    @NotBlank(message = "The Journal Title Shouldn't Be Empty")
    private String title;

    @NotBlank(message = "The Journal Subject Shouldn't Be Empty")
    private String subject;

    @NotBlank(message = "The Journal Description Shouldn't Be Empty")
    private String description;

}