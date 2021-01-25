/***************************************************************************f******************u************zz*******y**
 * File: HttpErrorAsJSONServlet.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.utils;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.MOVED_PERMANENTLY;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.fromStatusCode;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import com.algonquincollege.cst8277.rest.HttpErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Http error  as json servlet
 */
@WebServlet({"/http-error-as-json-handler"})
public class HttpErrorAsJSONServlet extends HttpServlet implements Serializable {
    /**
     * declare long serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * declare ObjectMapper objectMapper
     */
    static ObjectMapper objectMapper;
    /**
     * static method to get ObjectMapper
     * @return ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
    /**
     * static method to set ObjectMapper
     * @param objectMapper ObjectMapper
     */
    public static void setObjectMapper(ObjectMapper objectMapper) {
        HttpErrorAsJSONServlet.objectMapper = objectMapper;
    }
    /**
     * http service with request and response
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        int statusCode = response.getStatus();
        if (statusCode >= OK.getStatusCode() && statusCode < (MOVED_PERMANENTLY.getStatusCode()-1)) {
            super.service(request, response);
        }
        else {
            response.setContentType(APPLICATION_JSON);
            Response.Status status = fromStatusCode(statusCode);
            HttpErrorResponse httpErrorResponse = new HttpErrorResponse(statusCode, status.getReasonPhrase());
            String httpErrorResponseStr = getObjectMapper().writeValueAsString(httpErrorResponse);
            try (PrintWriter writer = response.getWriter()) {
                writer.write(httpErrorResponseStr);
                writer.flush();
            }
        }
    }
}