/***************************************************************************f******************u************zz*******y**
 * File: CustomerPojo.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;
//import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static com.algonquincollege.cst8277.models.CustomerPojo.ALL_CUSTOMERS_QUERY_NAME;
import static com.algonquincollege.cst8277.models.CustomerPojo.FIND_CUSTOMERS_QUERY_ID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
*
* Description: model for the Customer object
*/
@Entity(name = "Customer")
@Table(name = "CUSTOMER")
@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name="ID"))
//changed
@NamedQueries({
    @NamedQuery(name=ALL_CUSTOMERS_QUERY_NAME,query="select c from Customer c"),
    @NamedQuery(name=FIND_CUSTOMERS_QUERY_ID, query= "select c from Customer c where c.id = :param1")
})
public class CustomerPojo extends PojoBase implements Serializable {
    /**
     * declare serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * declare ALL_CUSTOMERS_QUERY_NAME
     */
    public static final String ALL_CUSTOMERS_QUERY_NAME = "allCustomers";
    /**
     * declare FIND_CUSTOMERS_QUERY_ID
     */
    public static final String FIND_CUSTOMERS_QUERY_ID = "findCustomer";

    /**
     * declare firstName
     */
    protected String firstName;
    /**
     * declare lastName
     */
    protected String lastName;
    /**
     * declare email
     */
    protected String email;
    /**
     * declare phoneNumber
     */
    protected String phoneNumber;
    /**
     * declare shipping address
     */
    protected AddressPojo shippingAddress;
    /**
     * declare billing address
     */
    protected AddressPojo billingAddress;
    /**
     * declare orders
     */
    protected List<OrderPojo> orders;
//  TODO  protected List<OrderPojo> orders = new ArrayList<>();
    
//    protected List<OrderPojo> orders = new ArrayList<>();
    // JPA requires each @Entity class have a default constructor
	public CustomerPojo() {
	}
	
    /**
     * @return the value for firstName
     */
    @Column(name = "FNAME")
    public String getFirstName() {
        return firstName;
    }
    /**
     * @param firstName new value for firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the value for lastName
     */
    @Column(name = "LNAME")
    public String getLastName() {
        return lastName;
    }
    /**
     * @param lastName new value for lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * get method for email
     * @return email
     */
    @Column(name ="EMAIL")
    public String getEmail() {
        return email;
    }
    /**
     * set method for email
     * @param email String
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * get phoneNumebr
     * @return phoneNumber
     */
    @Column(name="PHONENUMBER")
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * set phonenumber
     * @param phoneNumber String
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    //dont use CascadeType.All (skipping CascadeType.REMOVE): what if two customers
    //live at the same address and 1 leaves the house but the other does not?
    /**
     * get shipping address
     * @return ShippingAddressPojo
     */
    @OneToOne
    @JoinColumn(name="SHIPPING_ADDR")
    public AddressPojo getShippingAddress() {
        return shippingAddress;
    }
    /**
     * set shippingAddress
     * @param shippingAddress ShippingAddressPojo
     */
    public void setShippingAddress(AddressPojo shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    /**
     * get billing address
     * @return billingAddress
     */
    @OneToOne
    @JoinColumn(name="BILLING_ADDR")
    public AddressPojo getBillingAddress() {
        return billingAddress;
    }
    /**
     * set billing address
     * @param billingAddress BillingAddressPojo
     */
    public void setBillingAddress(AddressPojo billingAddress) {
        this.billingAddress = billingAddress;
    }
    /**
     * get orders
     * @return orders
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "owningCustomer", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<OrderPojo> getOrders() {
        return orders;
    }
    /**
     * set orders
     * @param orders list
     */
    public void setOrders(List<OrderPojo> orders) {
        this.orders = orders;
    }
    /**
     * add order method
     * @param order OrderPojo
     */
    public void addOrders(OrderPojo order) {
        getOrders().add(order);
        order.setOwningCustomer(this);
    }
    /**
     * override method for toString
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
            .append("Customer [id=")
            .append(id)
            .append(", ");
        if (firstName != null) {
            builder
                .append("firstName=")
                .append(firstName)
                .append(", ");
        }
        if (lastName != null) {
            builder
                .append("lastName=")
                .append(lastName)
                .append(", ");
        }
        if (phoneNumber != null) {
            builder
                .append("phoneNumber=")
                .append(phoneNumber)
                .append(", ");
        }
        if (email != null) {
            builder
                .append("email=")
                .append(email)
                .append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

}