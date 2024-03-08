package edu.bethlehem.scinexus.Academic;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import edu.bethlehem.scinexus.User.User;

@Data
@Entity
public class Academic extends User {
    private @Id @GeneratedValue Long id;
    private String badge;
    private String education;
    private User organizationId;
    private Position position;

    public Academic(String badge, String education, User organizationId, Position position) {

        super();
        this.badge = badge;
        this.education = education;
        this.organizationId = organizationId;
        this.position = position;
    }

    public Academic() {
    }
}