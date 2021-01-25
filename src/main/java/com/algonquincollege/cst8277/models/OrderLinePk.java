/***************************************************************************f******************u************zz*******y**
 * File: OrderLinePk.java
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
* JPA helper class: Composite Primary Key class for OrderLine - two columns
* ORDERLINE_NO identifies which orderLine within an Order (i.e. line 1, line 2, line 3)
* OWNING_ORDER_ID identifies which Order this orderLine belongs to
* 
*/
import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
public class OrderLinePk implements Serializable {
    /** explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    /**
     * declare owningorderId
     */
    protected int owningOrderId;
    /**
     * declare orderlineNo
     */
    protected int orderLineNo;
    /**
     * get owning orderId
     * @return owningOrderId
     */
    @Column(name="OWNING_ORDER_ID")
    public int getOwningOrderId() {
        return owningOrderId;
    }
    /**
     * set owning orderid
     * @param owningOrderId int
     */
    public void setOwningOrderId(int owningOrderId) {
        this.owningOrderId = owningOrderId;
    }
    /**
     * get orderline No
     * @return orderLineNo
     */
    @Column(name="ORDERLINE_NO")
    public int getOrderLineNo() {
        return orderLineNo;
    }
    /**
     * set orderlineNo
     * @param orderLineNo int
     */
    public void setOrderLineNo(int orderLineNo) {
        this.orderLineNo = orderLineNo;
    }
    /**
     * override method for hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(orderLineNo, owningOrderId);
    }
    /**
     * override equals
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OrderLinePk)) {
            return false;
        }
        OrderLinePk other = (OrderLinePk) obj;
        return orderLineNo == other.orderLineNo && owningOrderId == other.owningOrderId;
    }

}