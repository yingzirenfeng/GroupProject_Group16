/***************************************************************************f******************u************zz*******y**
 * File: PBKDF2HashGenerator.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277;

import java.util.HashMap;
import java.util.Map;

import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

import org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl;

/**
 * PBKDF2 Hash Generator
 *
 */
public class PBKDF2HashGenerator {

    // the nickname of this Hash algorithm is 'PBandJ' (Peanut-Butter-And-Jam, like the sandwich!)
    // I would like to use the constants from org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl
    // but they are not visible, so type in them all over again :-( Hope there are no typos!
    /**
     * declare PROPERTY_ALGORITHM
     */
    public static final String PROPERTY_ALGORITHM  = "Pbkdf2PasswordHash.Algorithm";
    /**
     * declare DEFAULT_PROPERTY_ALGORITHM
     */
    public static final String DEFAULT_PROPERTY_ALGORITHM  = "PBKDF2WithHmacSHA256";
    /**
     * declare PROPERTY_ITERATIONS
     */
    public static final String PROPERTY_ITERATIONS = "Pbkdf2PasswordHash.Iterations";
    /**
     * declare DEFAULT_PROPERTY_ITERATIONS
     */
    public static final String DEFAULT_PROPERTY_ITERATIONS = "2048";
    /**
     * declare PROPERTY_SALTSIZE
     */
    public static final String PROPERTY_SALTSIZE   = "Pbkdf2PasswordHash.SaltSizeBytes";
    /**
     * declare DEFAULT_SALT_SIZE
     */
    public static final String DEFAULT_SALT_SIZE   = "32";
    /**
     * declare PROPERTY_KEYSIZE
     */
    public static final String PROPERTY_KEYSIZE    = "Pbkdf2PasswordHash.KeySizeBytes";
    /**
     * declare DEFAULT_KEY_SIZE
     */
    public static final String DEFAULT_KEY_SIZE    = "32";

    /**
     * main method
     * @param args String
     */
    public static void main(String[] args) {

        Pbkdf2PasswordHash pbAndjPasswordHash = new Pbkdf2PasswordHashImpl();

        Map<String, String> pbAndjProperties = new HashMap<>();
        pbAndjProperties.put(PROPERTY_ALGORITHM, DEFAULT_PROPERTY_ALGORITHM);
        pbAndjProperties.put(PROPERTY_ITERATIONS, DEFAULT_PROPERTY_ITERATIONS);
        pbAndjProperties.put(PROPERTY_SALTSIZE, DEFAULT_SALT_SIZE);
        pbAndjProperties.put(PROPERTY_KEYSIZE, DEFAULT_KEY_SIZE);
        pbAndjPasswordHash.initialize(pbAndjProperties);
        String pwHash = pbAndjPasswordHash.generate(args[0].toCharArray());
        System.out.printf("Hash for %s is %s%n", args[0], pwHash);
    }
}
