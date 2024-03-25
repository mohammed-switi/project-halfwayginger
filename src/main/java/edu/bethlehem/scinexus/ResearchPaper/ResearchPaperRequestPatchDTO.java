package edu.bethlehem.scinexus.ResearchPaper;

import edu.bethlehem.scinexus.Journal.JournalPatchDTO;
import edu.bethlehem.scinexus.Journal.JournalPutDTO;
import edu.bethlehem.scinexus.Journal.Visibility;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Data
public class ResearchPaperRequestPatchDTO extends JournalPatchDTO {

    @Enumerated(EnumType.STRING)
    private ResearchLanguage language;

    @NotBlank(message = "The Journal Title Shouldn't Be Empty")
    private String title;

    @NotBlank(message = "The Journal Subject Shouldn't Be Empty")
    private String subject;

    @NotBlank(message = "The Journal Description Shouldn't Be Empty")
    private String description;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    // @Min(value = 1, message = "Id can't be less than One")
    // private long publisherId;
}