/***************************************************************************f******************u************zz*******y**
 * File: MyObjectMapperProvider.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
/**
 *My Object Mapper Provider
 */
@Provider
public class MyObjectMapperProvider implements ContextResolver<ObjectMapper> {
    final ObjectMapper defaultObjectMapper;
    public MyObjectMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
    }
    /**
     * object mapper get context
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
            return defaultObjectMapper;
        }
    /**
     * object mapper create default mapper
     * @return ObjectMapper
     */
    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}