/***************************************************************************f******************u************zz*******y**
 * File: StorePojo.java
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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.algonquincollege.cst8277.rest.ProductSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
*
* Description: model for the Store object
*/
@Entity(name = "Stores")
@Table(name = "STORES")
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name="STORE_ID"))
public class StorePojo extends PojoBase implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    /**
     * declare store name
     */
    protected String storeName;
    /**
     * declare set products
     */
    protected Set<ProductPojo> products = new HashSet<>();
    /**
     * contructor
     */
    // JPA requires each @Entity class have a default constructor
    public StorePojo() {
    }
    /**
     * get store name
     * @return store name
     */
    public String getStoreName() {
        return storeName;
    }
    /**
     * set store name
     * @param storeName String
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    /**
     * get set products
     * @return products
     */
    @JsonSerialize(using = ProductSerializer.class)
      //Discovered what I think is a bug: you should be able to list them in any order,
      //but it turns out, EclipseLink's JPA implementation needs the @JoinColumn StorePojo's PK
      //first, the 'inverse' to ProductPojo's PK second
    @ManyToMany(mappedBy = "stores", cascade = CascadeType.ALL)
    public Set<ProductPojo> getProducts() {
        return products;
    }
    /**
     * set products
     * @param products set
     */
    public void setProducts(Set<ProductPojo> products) {
        this.products = products;
    }
    public void addProduct(ProductPojo p) {
        if (p != null) {
            getProducts().add(p);
            p.addStore(this);
        }
    }
}
