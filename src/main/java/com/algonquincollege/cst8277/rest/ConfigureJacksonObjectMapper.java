/***************************************************************************f******************u************zz*******y**
 * File: ConfigureJacksonObjectMapper.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.algonquincollege.cst8277.utils.HttpErrorAsJSONServlet;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
/**
 * configure jackson object mapper
 *
 */
@Provider
public class ConfigureJacksonObjectMapper implements ContextResolver<ObjectMapper> {
    /**
     * declare ObjectMapper objectMapper
     */
    private final ObjectMapper objectMapper;
    /**
     * constructor
     */
    public ConfigureJacksonObjectMapper() {
        this.objectMapper = createObjectMapper();
    }
    /**
     * get context from object mapper
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
    /**
     * the method automatically generate timestamps when create object
     * @return ObjectMapper
     */
    //configure JDK 8's new DateTime objects to use proper ISO-8601 timeformat
    protected ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            // lenient parsing of JSON - if a field has a typo, don't fall to pieces
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            ;
        HttpErrorAsJSONServlet.setObjectMapper(mapper);
        return mapper;
    }
}