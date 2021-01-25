/***************************************************************************f******************u************zz*******y**
 * File: BillingAddressPojo.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.models;
/**
*
* Description: model for the BillingAddress object
*/
import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
@Entity(name="billingAddress")
@DiscriminatorValue(value="B")
public class BillingAddressPojo extends AddressPojo implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    /**
     * declare isAlsoShipping
     */
    protected boolean isAlsoShipping;
    /**
     * contructor
     */
    // JPA requires each @Entity class have a default constructor
    public BillingAddressPojo() {
    }
    /**
     * get method for isAlsoShipping
     * @return isAlsoShipping
     */
    @Column(name="ALSOSHIPPING")
    public boolean isAlsoShipping() {
        return isAlsoShipping;
    }
    /**
     * set method for isAlsoShipping
     * @param isAlsoShipping boolean
     */
    public void setAlsoShipping(boolean isAlsoShipping) {
        this.isAlsoShipping = isAlsoShipping;
    }
    
}