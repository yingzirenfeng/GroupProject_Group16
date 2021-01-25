/***************************************************************************f******************u************zz*******y**
 * File: ProductPojo.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
*
* Description: model for the Product object
*/

@Entity(name = "Product")
@Table(name = "PRODUCT")
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name="PRODUCT_ID"))
public class ProductPojo extends PojoBase implements Serializable {
    /**
     * declare serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * declare description
     */
    protected String description;
    /**
     * declare serialNo
     */
    protected String serialNo;
    /**
     * declare stores
     */
    protected Set<StorePojo> stores = new HashSet<>();

    // JPA requires each @Entity class have a default constructor
    /**
     * contructor
     */
    public ProductPojo() {
    }
    
    /**
     * @return the value for firstName
     */
    
    @Column(name="DESCRIPTION")
    public String getDescription() {
        return description;
    }
    /**
     * @param description new value for description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * get serial No
     * @return serialNo
     */
    @Column(name="SERIALNUMBER")
    public String getSerialNo() {
        return serialNo;
    }
    /**
     * set serial No
     * @param serialNo String
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    /**
     * get stores
     * @return stores
     */
    @JsonInclude(Include.NON_NULL)
    @ManyToMany
    @JoinTable(name="STORES_PRODUCTS",
      joinColumns=@JoinColumn(name="PRODUCT_ID", referencedColumnName="PRODUCT_ID"),
      inverseJoinColumns=@JoinColumn(name="STORE_ID", referencedColumnName="STORE_ID"))
    public Set<StorePojo> getStores() {
        return stores;
    }
    /**
     * set stores
     * @param stores set
     */
    public void setStores(Set<StorePojo> stores) {
        this.stores = stores;
    }
    
    public void addStore(StorePojo s) {
        if (s != null) {
            getStores().add(s);
            s.getProducts().add(this);
        }
    }

}