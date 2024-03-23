package edu.bethlehem.scinexus.Opinion;


import edu.bethlehem.scinexus.Journal.Journal;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OpinionDTO {

    @NotNull(message = "The Opinion Content Shouldn't Be Null")
    @NotBlank(message = "The Opinion Content can't Be Empty")
    private String content;

    @Min(value = 1,message = "Id can't be less than One")
    private Long journalId;


}
