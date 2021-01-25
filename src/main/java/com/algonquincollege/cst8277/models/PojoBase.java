/***************************************************************************f******************u************zz*******y**
 * File: PojoBase.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Abstract class that is base of (class) hierarchy for all c.a.cst8277.models @Entity classes
 */
//@MappedSuperclass
@MappedSuperclass
@Access(AccessType.PROPERTY) // NOTE: by using this annotations, any annotation on a field is ignored without warning
@EntityListeners({PojoListener.class}) // a liitle bit difference from professor
public abstract class PojoBase implements Serializable {
    /**
     * declare serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * declare id
     */
    protected int id;
    /**
     * declare localdatetime created
     */
    protected LocalDateTime created;
    /**
     * declare localdatetime updated
     */
    protected LocalDateTime updated;
    /**
     * declare int version
     */
    protected int version;
    /**
     * get method for id
     * @return id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }
    /**
     * set method for id
     * @param id int
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * get localdatetimr created
     * @return created
     */
    @Column (name ="CREATED")
    public LocalDateTime getCreatedDate() {
        return created;
    }
    /**
     * set localdatetime created
     * @param created localdatetime
     */
    public void setCreatedDate(LocalDateTime created) {
        this.created = created;
    }
    /**
     * get localdatetime updated
     * @return updated
     */
    @Column (name="UPDATED")
    public LocalDateTime getUpdatedDate() {
        return updated;
    }
    /**
     * set localdatetime updated
     * @param updated localdatetime
     */
    public void setUpdatedDate(LocalDateTime updated) {
        this.updated = updated;
    }
    /**
     * get method for version
     * @return version
     */
    @Version
    public int getVersion() {
        return version;
    }
    /**
     * set version
     * @param version int
     */
    public void setVersion(int version) {
        this.version = version;
    }

    // It is a good idea for hashCode() to use the @Id property
    // since it maps to table's PK and that is how Db determine's identity
    // (same for equals()
    /**
     * override method hashcode
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }
    /**
     * override method equals
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PojoBase)) {
            return false;
        }
        PojoBase other = (PojoBase)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}