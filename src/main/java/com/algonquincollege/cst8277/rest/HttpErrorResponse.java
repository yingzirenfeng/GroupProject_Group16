/***************************************************************************f******************u************zz*******y**
 * File: HttpErrorResponse.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 *http error response class
 */
public class HttpErrorResponse implements Serializable {
    /**
     * declare serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * declare int statusCode
     */
    private final int statusCode;
    /**
     * declare String reasonPhrase
     */
    private final String reasonPhrase;
    /**
     * http error response
     * @param code int
     * @param reasonPhrase string
     */
    public HttpErrorResponse(int code, String reasonPhrase) {
        this.statusCode = code;
        this.reasonPhrase = reasonPhrase;
    }
    /**
     * get stadus code by json
     * @return int
     */
    @JsonProperty("status-code")
    public int getStatusCode() {
        return statusCode;
    }
    /**
     * get reason phrase by json
     * @return String
     */
    @JsonProperty("reason-phrase")
    public String getReasonPhrase() {
        return reasonPhrase;
    }

}