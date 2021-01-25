/***************************************************************************f******************u************zz*******y**
 * File: OrderPojo.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
*
* Description: model for the Order object
*/
@Entity(name = "Order")
@Table(name = "ORDER_TBL")
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name="ORDER_ID"))
public class OrderPojo extends PojoBase implements Serializable {
    /**
     * decalre seiralVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * decalre description
     */
    protected String description;
    /**
     * decalre orderlines
     */
    protected List<OrderLinePojo> orderlines;
    /**
     * decalre owningCustomer
     */
    protected CustomerPojo owningCustomer;
    /**
     * contructor
     */
    
    // JPA requires each @Entity class have a default constructor
	public OrderPojo() {
	}
	/**
     * get method for description
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * set method for description
     * @param description String
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * get method for orderlines
     * @return orderlines
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "owningOrder", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OneToOne(mappedBy = "owningOrder") //professor solution
	public List<OrderLinePojo> getOrderlines() {
		return this.orderlines;
	}
    /**
     * set method for orderlines
     * @param orderlines
     */
	public void setOrderlines(List<OrderLinePojo> orderlines) {
		this.orderlines = orderlines;
	}
	/**
     * add orderline
     * @param orderline Orderline Pojo
     * @return orderline
     */
	public OrderLinePojo addOrderline(OrderLinePojo orderline) {
		getOrderlines().add(orderline);
		orderline.setOwningOrder(this);
		return orderline;
	}
	/**
     * remove orderline
     * @param orderline OrderLinePojo
     * @return orderline
     */
	public OrderLinePojo removeOrderline(OrderLinePojo orderline) {
		getOrderlines().remove(orderline);
        orderline.setOwningOrder(null);
		return orderline;
	}
	/**
     * get owning customer
     * @return owningCustomer
     */
	@JsonBackReference
	@ManyToOne
    @JoinColumn(name = "OWNING_CUST_ID")
	public CustomerPojo getOwningCustomer() {
		return this.owningCustomer;
	}
	/**
     * set owning customer
     * @param owner CustomerPojo
     */
	public void setOwningCustomer(CustomerPojo owner) {
		this.owningCustomer = owner;
	}

}