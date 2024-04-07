package edu.bethlehem.scinexus.UserLinks;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLinksKey {

    @Column(name = "linksTo")
    Long linksTo;

    @Column(name = "linksFrom")
    Long linksFrom;

}
