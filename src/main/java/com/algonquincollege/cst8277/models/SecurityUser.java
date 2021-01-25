/***************************************************************************f******************u************zz*******y**
 * File: SecurityUser.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.models;

import static com.algonquincollege.cst8277.models.SecurityUser.SECURITY_USER_BY_NAME_QUERY;
import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
//import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.algonquincollege.cst8277.rest.SecurityRoleSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * User class used for (JSR-375) Java EE Security authorization/authentication
 */
@Entity(name="SecurityUser")
@Table(name="SECURITY_USER")
@Access(AccessType.PROPERTY)
@NamedQuery(name= SECURITY_USER_BY_NAME_QUERY, query = "select su from SecurityUser su where su.username =:param1")
public class SecurityUser implements Serializable, Principal {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    /**
     * declare String USER_FOR_OWNING_CUST_QUERY
     */
    public static final String USER_FOR_OWNING_CUST_QUERY =
        "userForOwningCust";
    /**
     * declare String SECURITY_USER_BY_NAME_QUERY
     */
    public static final String SECURITY_USER_BY_NAME_QUERY =
        "userByName";
    
    /**
     * declare int id
     */
    protected int id;
    /**
     * declare String username
     */
    protected String username;
    /**
     * declare String pwHash
     */
    protected String pwHash;
    /**
     * declare Set<SecurityRole> roles
     */
    protected Set<SecurityRole> roles = new HashSet<>();
    /**
     * declare SecurityRole
     */
    protected CustomerPojo cust;
    /**
     * contrustor
     */
    public SecurityUser() {
        super();
    }
    /**
     * get method for id
     * @return int
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
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
     * get method for String
     * @return username
     */
    @Column(name="username")
    public String getUsername() {
        return username;
    }
    /**
     * set method for username
     * @param username String
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * get method for PwHash
     * @return String
     */
    @JsonIgnore
    public String getPwHash() {
        return pwHash;
    }
    /**
     * set method for PwHash
     * @param pwHash String
     */
    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }
    /**
     * get method for roles
     * @return set
     */
    @JsonInclude(Include.NON_NULL)
    @JsonSerialize(using = SecurityRoleSerializer.class)
    @ManyToMany(targetEntity = SecurityRole.class, cascade = CascadeType.PERSIST)
    @JoinTable(name="SECURITY_USER_SECURITY_ROLE",
      joinColumns=@JoinColumn(name="USER_ID", referencedColumnName="USER_ID"),
      inverseJoinColumns=@JoinColumn(name="ROLE_ID", referencedColumnName="ROLE_ID"))
    public Set<SecurityRole> getRoles() {
        return roles;
    }
    /**
     * ser method for roles
     * @param roles set
     */
    public void setRoles(Set<SecurityRole> roles) {
        this.roles = roles;
    }
    /**
     * get method for customers
     * @return customerPojo
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="CUSTOMER_ID", referencedColumnName="ID")
    public CustomerPojo getCustomer() {
        return cust;
    }
    
    /**
     * set customers
     * @param cust costomerPojo
     */
    public void setCustomer(CustomerPojo cust) {
        this.cust = cust;
    }
    /**
     * get method or username
     */
    //Principal
    @JsonIgnore
    @Override
    public String getName() {
        return getUsername();
    }
    /**
     * override method for hashcode
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
        SecurityUser other = (SecurityUser)obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

}