/***************************************************************************f******************u************zz*******y**
 * File: ProductResource.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.utils.MyConstants.ADMIN_ROLE;      //change
import static com.algonquincollege.cst8277.utils.MyConstants.PRODUCT_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
//import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;

import java.util.List;


import javax.annotation.security.RolesAllowed;  //change
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.algonquincollege.cst8277.ejb.CustomerService;
//import com.algonquincollege.cst8277.models.CustomerPojo;
//import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.ProductPojo;
//import com.algonquincollege.cst8277.models.StorePojo;
/**
 * Product resource file which relate to yaml file
 */
@Path(PRODUCT_RESOURCE_NAME)                            //change
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    /**
     * declare CustomerService customerServiceBean
     */
    @EJB                               //change
    protected CustomerService customerServiceBean;
    /**
     * declare ServletContext servletContext
     */
    @Inject
    protected ServletContext servletContext;
    /**
     * response to yaml for get Product
     * @return response
     */
    @GET
    public Response getProducts() {
        servletContext.log("retrieving all products ...");
        List<ProductPojo> custs = customerServiceBean.getAllProducts();
        Response response = Response.ok(custs).build();
        return response;
    }
    /**
     * response to yaml for get Product by id
     * @param id int
     * @return response
     */
    @GET                     //change
//    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getProductById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific product " + id);
        ProductPojo theProduct = customerServiceBean.getProductById(id);
        Response response = Response.ok(theProduct).build();
        return response;
    }
    /**
     * response to yaml for delete Product by id
     * @param id int
     * @return response
     */
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deletetProduct(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        boolean result = customerServiceBean.deleteProduct(id);

        if(result) {
//            return Response.ok().status(Response.Status.NO_CONTENT).build();
            return Response.ok().build();
        }else {
            return Response.notModified().build();
        }
    }
    /**
     * response to yaml for add Product
     * @param newCustomer ProductPojo
     * @return response
     */
    @POST
    @Transactional
    @RolesAllowed({ADMIN_ROLE})
    public Response addProduct(ProductPojo newCustomer) {
      Response response = null;
      ProductPojo newCustomerWithIdTimestamps = customerServiceBean.persistProduct(newCustomer);
      //build a SecurityUser linked to the new customer
//      customerServiceBean.buildUserForNewCustomer(newCustomerWithIdTimestamps);
      response = Response.ok(newCustomerWithIdTimestamps).build();
      return response;
    }
    /**
     * response to yaml for update Product
     * @param id int
     * @param updatedCustomer ProductPojo
     * @return Response
     */
    @Transactional
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    @PUT
    public Response updateProduct(@PathParam(RESOURCE_PATH_ID_ELEMENT)int id, ProductPojo updatedCustomer) {
      Response response = null;
      customerServiceBean.updateProductById(id, updatedCustomer);
      response = Response.ok().build();
      return response;
    }
    
//    @POST
//    @Transactional
//    @RolesAllowed({ADMIN_ROLE})
//    @Path("/store")
//    public Response addProductStore(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, StorePojo[] newCustomer) {
//      Response response = null;
//      StorePojo[] newCustomerWithIdTimestamps = customerServiceBean.persistStoreList(id,newCustomer);
//      //build a SecurityUser linked to the new customer
////      customerServiceBean.buildUserForNewCustomer(newCustomerWithIdTimestamps);
//      response = Response.ok(newCustomerWithIdTimestamps).build();
//      return response;
//    }
}