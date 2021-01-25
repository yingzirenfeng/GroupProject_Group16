/***************************************************************************f******************u************zz*******y**
 * File: ShippingAddressPojo.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.models;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
/**
*
* Description: model for the ShippingAddress object
*/
@Entity(name="shippingAddress")
@DiscriminatorValue(value="S")
public class ShippingAddressPojo extends AddressPojo implements Serializable  {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    /**
     * contructor
     */
    // JPA requires each @Entity class have a default constructor
    public ShippingAddressPojo() {
    }

}