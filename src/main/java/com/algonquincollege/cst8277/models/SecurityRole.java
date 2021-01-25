/***************************************************************************f******************u************zz*******y**
 * File: SecurityRole.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.Set;


import static com.algonquincollege.cst8277.models.SecurityRole.ROLE_BY_NAME_QUERY;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * Role class used for (JSR-375) Java EE Security authorization/authentication
 */
@Entity(name="SecurityRole")
@Table(name="SECURITY_ROLE")
@Access(AccessType.PROPERTY)
@NamedQuery(name=ROLE_BY_NAME_QUERY, query="select s from SecurityRole s where s.roleName=:param1")

public class SecurityRole implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    /**
     * declare ROLE_BY_NAME_QUERY
     */
    public static final String ROLE_BY_NAME_QUERY = "roleByName";
    /**
     * declare id
     */
    protected int id;
    /**
     * declare roleName
     */
    protected String roleName;
    /**
     * declare secureityUser
     */
    protected Set<SecurityUser> users;
    /**
     * contructor
     */
    public SecurityRole() {
        super();
    }
    /**
     * get method for id
     * @return int
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ROLE_ID")
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
     * get method for role name
     * @return  String
     */
    public String getRoleName() {
        return roleName;
    }
    /**
     * set method for role name
     * @param roleName string
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    /**
     * get method for securityUser
     * @return users
     */
    @JsonInclude(Include.NON_NULL)
    @ManyToMany(mappedBy = "roles")
    public Set<SecurityUser> getUsers() {
        return users;
    }
    /**
     * set method for users
     * @param users set
     */
    public void setUsers(Set<SecurityUser> users) {
        this.users = users;
    }
    public void addUserToRole(SecurityUser user) {
        getUsers().add(user);
    }
    /**
     * override method for hashCode
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }
    /**
     * override method for equals
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SecurityRole other = (SecurityRole)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}