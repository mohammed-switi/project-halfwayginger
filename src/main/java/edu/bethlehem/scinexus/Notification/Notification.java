package edu.bethlehem.scinexus.Notification;

import edu.bethlehem.scinexus.Academic.Academic;
import edu.bethlehem.scinexus.Organization.Organization;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Notification {
    private @Id @GeneratedValue Long id;
    private Long notificationId;
    private String content;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "notificationsAcademic")
    private Academic academic;

    @ManyToOne
    @JoinColumn(name = "notificationsOrganization")
    private Organization organization;

    public Notification(Long notificationId, String content, Status status) {
        this.notificationId = notificationId;
        this.content = content;
        this.status = status;
    }

    public Notification() {
    }
}