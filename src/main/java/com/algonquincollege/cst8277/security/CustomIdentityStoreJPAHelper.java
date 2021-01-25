/***************************************************************************f******************u************zz*******y**
 * File: CustomIdentityStoreJPAHelper.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.security;

import static com.algonquincollege.cst8277.models.SecurityUser.SECURITY_USER_BY_NAME_QUERY;
import static com.algonquincollege.cst8277.utils.MyConstants.PARAM1;
//import static com.algonquincollege.cst8277.utils.MyConstants.PU_NAME;
import static java.util.Collections.emptySet;

import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
//import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.algonquincollege.cst8277.models.SecurityRole;
import com.algonquincollege.cst8277.models.SecurityUser;

/*
 * Stateless Session bean should also be a Singleton
 */
/**
 * this is JPA Helper to custom Indentuty store
 *
 */
@Singleton
public class CustomIdentityStoreJPAHelper {
    /**
     * declare String CUSTOMER_PU
     */
    public static final String CUSTOMER_PU = "20f-groupProject-PU";

    @PersistenceContext(name = CUSTOMER_PU)
    /**
     * declare EntityManager em
     */
    protected EntityManager em;
    
    /**
     * The method is to find the user by name
     * @param username String
     * @return SecurityUser
     */
    public SecurityUser findUserByName(String username) {
//        SecurityUser user = null;
//        try {
//            //TODO
//            TypedQuery<SecurityUser> query = em.createNamedQuery(SECURITY_USER_BY_NAME_QUERY, SecurityUser.class);
//            query.setParameter(PARAM1, username);
//            user = query.getSingleResult();
//        }
//        catch (Exception e) {
//
//        }
//        return user;
        SecurityUser user = null;
        try {
            //TODO actually use db
            TypedQuery<SecurityUser> query = em.createNamedQuery(SECURITY_USER_BY_NAME_QUERY, SecurityUser.class);
            query.setParameter(PARAM1, username);
            user = query.getSingleResult();
        }
        catch (Exception e) {
//            e.printStackTrace();
        }
        return user;
//        try {
//            return em.createNamedQuery(SECURITY_USER_BY_NAME_QUERY, SecurityUser.class).setParameter(PARAM1, username).getSingleResult();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return user;
    }

    /**
     * the method is find the role name for user
     * @param username String
     * @return rolename
     */
    public Set<String> findRoleNamesForUser(String username) {
        Set<String> roleNames = emptySet();
        SecurityUser securityUser = findUserByName(username);
        if (securityUser != null) {
            roleNames = securityUser.getRoles().stream().map(s -> s.getRoleName()).collect(Collectors.toSet());
        }
        return roleNames;
    }
    /**
     * save security user
     * @param user SecurityUser
     */
    @Transactional
    public void saveSecurityUser(SecurityUser user) {
        //TODO
        SecurityUser addUser = new SecurityUser();
        addUser.setPwHash(user.getPwHash());
        addUser.setRoles(user.getRoles());
        addUser.setUsername(user.getUsername());
        addUser.setCustomer(user.getCustomer());
        em.persist(user);
    }
    /**
     * save security role
     * @param role SecurityRole
     */
    @Transactional
    public void saveSecurityRole(SecurityRole role) {
        //TODO
        SecurityRole addRole = new SecurityRole();
        addRole.setRoleName(role.getRoleName());
        addRole.setUsers(role.getUsers());
        em.persist(role);
    }
}