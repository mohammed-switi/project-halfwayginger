package edu.bethlehem.scinexus.ResearchPaper;

import edu.bethlehem.scinexus.Journal.JournalPutDTO;
import edu.bethlehem.scinexus.Journal.Visibility;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
public class ResearchPaperRequestDTO extends JournalPutDTO {

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