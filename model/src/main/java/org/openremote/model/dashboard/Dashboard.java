package org.openremote.model.dashboard;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static org.openremote.model.Constants.PERSISTENCE_JSON_VALUE_TYPE;
import static org.openremote.model.Constants.PERSISTENCE_UNIQUE_ID_GENERATOR;

@Entity
@Table(name = "DASHBOARD")
public class Dashboard {

    @Id
    @Column(name = "ID", length = 22, columnDefinition = "char(22)")
    @GeneratedValue(generator = PERSISTENCE_UNIQUE_ID_GENERATOR)
    protected String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_ON", updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @org.hibernate.annotations.CreationTimestamp
    protected Date createdOn;

    @NotBlank(message = "{Dashboard.realm.NotBlank}")
    @Size(min = 1, max = 255, message = "{Asset.realm.Size}")
    @Column(name = "REALM", nullable = false, updatable = false)
    protected String realm;

    @Version
    @Column(name = "VERSION", nullable = false)
    protected long version;

    @Column(name = "OWNER_ID", nullable = false)
    protected String ownerId;

    @NotBlank(message = "{Dashboard.displayName.NotBlank}")
    @Column(name = "DISPLAY_NAME", nullable = false)
    protected String displayName;

    @NotNull(message = "{Dashboard.template.NotNull}")
    @Column(name = "TEMPLATE", columnDefinition = PERSISTENCE_JSON_VALUE_TYPE, nullable = false)
    @org.hibernate.annotations.Type(type = PERSISTENCE_JSON_VALUE_TYPE)
    @Valid
    protected DashboardTemplate template;


    /* ----------------------------- */

    public Dashboard() {}


    public void setId(String id) {
        this.id = id;
    }
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    public void setRealm(String realm) {
        this.realm = realm;
    }
    public void setVersion(long version) {
        this.version = version;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public void setTemplate(@Valid DashboardTemplate template) {
        this.template = template;
    }


    public String getId() { return this.id; }
    public Date getCreatedOn() { return this.createdOn; }
    public String getRealm() { return this.realm; }
    public long getVersion() { return this.version; }
    public String getOwnerId() { return this.ownerId; }
    public String getDisplayName() { return this.displayName; }
    public DashboardTemplate getTemplate() { return this.template; }


    // TODO: still not proud of the validity check here, probably needs to move
    /*public boolean checkValidity() {
        if(realm != null && !realm.isEmpty() && ownerId != null && !ownerId.isEmpty()) {
            // TODO: check for realm;
            // TODO: check for ownerId;
            if(template.checkValidity()) {
                return true;
            }
        }
        return false;
    }*/
}