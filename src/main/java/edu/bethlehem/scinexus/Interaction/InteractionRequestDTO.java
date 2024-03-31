package edu.bethlehem.scinexus.Interaction;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InteractionRequestDTO {

    @JdbcTypeCode(SqlTypes.JSON)
    @Enumerated(EnumType.STRING)
    @NotNull
    private InteractionType type;
}
