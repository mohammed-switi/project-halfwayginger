package edu.bethlehem.scinexus.Journal;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalPutDTO {

    @NotNull(message = "The Journal Content Shouldn't Be Null")
    @NotBlank(message = "The Journal Content Shouldn't Be Empty")
    private String content;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Visibility visibility;
}
