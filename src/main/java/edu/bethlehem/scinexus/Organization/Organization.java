package edu.bethlehem.scinexus.Organization;

import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table
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