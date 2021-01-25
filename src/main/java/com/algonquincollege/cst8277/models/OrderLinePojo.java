/***************************************************************************f******************u************zz*******y**
 * File: OrderLinePojo.java
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
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
*
* Description: model for the OrderLine object
*/
@Entity(name = "Orderline")
@Table(name = "ORDERLINE")
@Access(AccessType.PROPERTY)
public class OrderLinePojo implements Serializable {
    private static final long serialVersionUID = 1L;

    protected OrderLinePk primaryKey;
    protected OrderPojo owningOrder;
    protected Double amount;
    protected ProductPojo product;

    // JPA requires each @Entity class have a default constructor
    public OrderLinePojo() {
    }
    
    @EmbeddedId
    public OrderLinePk getPk() {
        return primaryKey;
    }
    public void setPk(OrderLinePk primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    @JsonBackReference
    @MapsId("owningOrderId")
    @ManyToOne
//    @JoinColumn(name="OWNING_ORDER_ID")
    @JoinColumn(name="OWNING_ORDER_ID", referencedColumnName="ORDER_ID")
    public OrderPojo getOwningOrder() {
        return owningOrder;
    }
    public void setOwningOrder(OrderPojo owningOrder) {
        this.owningOrder = owningOrder;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name="PRODUCT_ID")
    public ProductPojo getProduct() {
        return product;
    }
    public void setProduct(ProductPojo product) {
        this.product = product;
    }

    
    //TODO missing price
}