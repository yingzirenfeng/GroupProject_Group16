/***************************************************************************f******************u************zz*******y**
 * File: PojoListener.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.models;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class PojoListener {

    /**
     * executed before the EntityManager persist is actually exectuted
     * automatically set the date of create and update by now
     * @param pojo PojoBase
     */
    @PrePersist
    public void setCreatedOnDate(PojoBase pojo) {
        LocalDateTime now = LocalDateTime.now();
        pojo.setCreatedDate(now);
        pojo.setUpdatedDate(now);
    }
    /**
     * executed before the EntityManager update is actually exectuted
     * automatically set the date of update by now
     * @param pojo PojoBase
     */
    @PreUpdate
    public void setUpdatedDate(PojoBase pojo) {
        pojo.setUpdatedDate(LocalDateTime.now());
    }
}