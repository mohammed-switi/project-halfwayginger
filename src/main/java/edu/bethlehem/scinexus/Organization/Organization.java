package edu.bethlehem.scinexus.Organization;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Organization {
    private @Id @GeneratedValue Long id;
    private String type;
    private Boolean verified;

    public Organization(String type, Boolean verified) {
        this.type = type;
        this.verified = verified;
    }

    public Organization() {
    }
}