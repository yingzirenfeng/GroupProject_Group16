package com.algonquincollege.cst8277;
/***************************************************************************f******************u************zz*******y**
 * File: Assignment3Driver.java
 * Course materials (20W) CST 8277
 * @author Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
//package com.algonquincollege.cst8277;


import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/**
 * test the table if create properly
 *
 */
public class DriverTestForJPAErrors {

//    public static final String ASSIGNMENT3_PU_NAME = "assignment3-orderSystem-PU";
    public static final String PU_NAME = "test-for-jpa-errors-PU";

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
        emf.close();
    }

}
