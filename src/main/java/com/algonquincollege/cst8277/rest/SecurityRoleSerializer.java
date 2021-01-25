/***************************************************************************f******************u************zz*******y**
 * File: SecurityRoleSerializer.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.rest;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.algonquincollege.cst8277.models.SecurityRole;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
/**
 * security role for serializer
 */
public class SecurityRoleSerializer extends StdSerializer<Set<SecurityRole>> implements Serializable {
    /**
     * declare long serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * contructor
     */
    public SecurityRoleSerializer() {
        this(null);
    }
    /**
     * contructor with parameter
     * @param t Class<Set<SecurityRole>>
     */
    public SecurityRoleSerializer(Class<Set<SecurityRole>> t) {
        super(t);
    }
    /**
     * override method for rerializing role
     */
    @Override
    public void serialize(Set<SecurityRole> originalRoles, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
        
        Set<SecurityRole> hollowRoles = new HashSet<>();
        for (SecurityRole originalRole : originalRoles) {
            // create a 'hollow' copy of the original Security Roles entity
            SecurityRole hollowP = new SecurityRole();
            hollowP.setId(originalRole.getId());
            hollowP.setRoleName(originalRole.getRoleName());
            hollowP.setUsers(null);
            hollowRoles.add(hollowP);
        }
        generator.writeObject(hollowRoles);
    }
}