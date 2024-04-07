package edu.bethlehem.scinexus.Interaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionRequestDTO {

    @JdbcTypeCode(SqlTypes.JSON)
    @Enumerated(EnumType.STRING)
    @NotNull
    private InteractionType type;
}
