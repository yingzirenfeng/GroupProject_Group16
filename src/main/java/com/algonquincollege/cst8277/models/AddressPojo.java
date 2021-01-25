/***************************************************************************f******************u************zz*******y**
 * File: AddressPojo.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
//import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
*
* Description: model for the Address object
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
  @JsonSubTypes({
    @Type(value = BillingAddressPojo.class, name = "B"),
    @Type(value = ShippingAddressPojo.class, name = "S")
})
//@MappedSuperclass
@Entity(name = "Address")
@Table(name = "CUST_ADDR")
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name="ADDR_ID"))
//@Inheritance(strategy = InheritanceType.JOINED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ADDR_TYPE", length=1)
public abstract class AddressPojo extends PojoBase implements Serializable {

    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    /**
     * declare street
     */
    protected String street;
    /**
     * declare city
     */
    protected String city;
    /**
     * declare country
     */
    protected String country;
    /**
     * declare postalCode
     */
    protected String postal;
    /**
     * declare state
     */
    protected String state;

    /**
     * JPA requires each @Entity class have a default constructor
     */
    public AddressPojo() {
        super();
    }
    /**
     * get method for city
     * @return
     */
    @Column(name = "CITY")
    public String getCity() {
        return city;
    }
    /**
     * set method for city
     * @param city String
     */
    public void setCity(String city) {
        this.city = city;
    }
    /**
     * get method for country
     * @return String
     */
    @Column(name = "COUNTRY")
    public String getCountry() {
        return country;
    }
    /**
     * set method for country
     * @param country String
     */
    public void setCountry(String country) {
        this.country = country;
    }
    /**
     * get methood for postalCode
     * @return postalCode
     */
    @Column(name = "POSTAL_CODE")
    public String getPostal() {
        return postal;
    }
    /**
     * set method for postalCode
     * @param postalCode
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }
    /**
     * get method for state
     * @return state
     */
    @Column(name = "STATE")
    public String getState() {
        return state;
    }
    /**
     * set method for state
     * @param state String
     */
    public void setState(String state) {
        this.state = state;
    }
    /**
     * get method for street
     * @return street
     */
    @Column(name = "STREET")
    public String getStreet() {
        return street;
    }
    
    /**
     * set method for street
     * @param street String
     */
    public void setStreet(String street) {
        this.street = street;
    }

}