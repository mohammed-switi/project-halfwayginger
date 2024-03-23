package edu.bethlehem.scinexus.Opinion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class OpinionPatchDTO {



    private String content;

    @Min(value = 1,message = "Id can't be less than One")
    private Long journalId;


}
