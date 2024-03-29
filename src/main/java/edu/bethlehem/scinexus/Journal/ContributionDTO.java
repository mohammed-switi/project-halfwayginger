package edu.bethlehem.scinexus.Journal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class ContributionDTO {



    @NotNull(message = "UserId must not be null")
    @Positive(message = "UserId must be positive")
    private Long userId;

    @NotNull(message = "JournalId must not be null")
    @Positive(message = "JournalId must be positive")
    private Long journalId;
}
