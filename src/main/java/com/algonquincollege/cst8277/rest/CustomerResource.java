/***************************************************************************f******************u************zz*******y**
 * File: CustomerResource.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.rest;

import static com.algonquincollege.cst8277.utils.MyConstants.ADMIN_ROLE;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;
//import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_ADDRESS_RESOURCE_PATH;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.soteria.WrappingCallerPrincipal;

import com.algonquincollege.cst8277.ejb.CustomerService;
//import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.BillingAddressPojo;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.OrderPojo;
//import com.algonquincollege.cst8277.models.ProductPojo;
import com.algonquincollege.cst8277.models.OrderLinePojo;
import com.algonquincollege.cst8277.models.SecurityUser;
import com.algonquincollege.cst8277.models.ShippingAddressPojo;

@Path(CUSTOMER_RESOURCE_NAME)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {
    @EJB
    protected CustomerService customerServiceBean;

    @Inject
    protected ServletContext servletContext;

    @Inject
    protected SecurityContext sc;
    //--------------------------------------------------------------------------------
    /*
 *    ---------------------customer-----------
 */
  //--------------------------------------------------------------------------------
    //TODO - endpoints for setting up customers
    /**
     * get all customer from database and response to yaml
     * @return reponse
     */
    @GET
    @RolesAllowed({ADMIN_ROLE})
    public Response getCustomers() {
        servletContext.log("retrieving all customers ...");
        List<CustomerPojo> custs = customerServiceBean.getAllCustomers();
        Response response = Response.ok(custs).build();
        return response;
    }
    /**
     * get customer by id from database and response to yaml
     * @param id int
     * @return repsonse
     */
    @GET
    @RolesAllowed({ADMIN_ROLE,USER_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response getCustomerById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("try to retrieve specific customer " + id);
        Response response = null;
        CustomerPojo cust = null;

        if (sc.isCallerInRole(ADMIN_ROLE)) {
            cust = customerServiceBean.getCustomerById(id);
            response = Response.status( cust == null ? NOT_FOUND : OK).entity(cust).build();
        }
        else if (sc.isCallerInRole(USER_ROLE)) {
            WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
            SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
            cust = sUser.getCustomer();
            if (cust != null && cust.getId() == id) {
                response = Response.status(OK).entity(cust).build();
            }
            else {
                throw new ForbiddenException();
            }
        }
        else {
            response = Response.status(BAD_REQUEST).build();
        }
        return response;
    }
    /**
     * add customer to database and response to yaml
     * @param newCustomer CustomerPojo
     * @return Response
     */
    @POST
    @Transactional
    @RolesAllowed({ADMIN_ROLE})
    public Response addCustomer(CustomerPojo newCustomer) {
      Response response = null;
      CustomerPojo newCustomerWithIdTimestamps = customerServiceBean.persistCustomer(newCustomer);
      //build a SecurityUser linked to the new customer
      customerServiceBean.buildUserForNewCustomer(newCustomerWithIdTimestamps);
      response = Response.ok(newCustomerWithIdTimestamps).build();
      return response;
    }
    
    /**
     * delete customer from database and response to yaml
     * @param id int
     * @return response
     */
    
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    public Response deleteCustomer(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        Response response = null;
        customerServiceBean.deleteCustomerById(id);
        response = Response.ok().build();
        return response;
    }
    
    @Transactional
    @RolesAllowed({ADMIN_ROLE})
    @Path(RESOURCE_PATH_ID_PATH)
    @PUT
    public Response updateCustomer(@PathParam(RESOURCE_PATH_ID_ELEMENT)int id, CustomerPojo updatedCustomer) {
      Response response = null;
      customerServiceBean.updateCustomerById(id, updatedCustomer);
      response = Response.ok().build();
      return response;
    }

   
    
//    @POST
//    @RolesAllowed({ADMIN_ROLE})
//    @Transactional
//    @Path("/{id}/billingAddress/{id}")
//    public Response addAddressForCustomer(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, AddressPojo newAddress) {
//      Response response = null;
//      System.out.println(newAddress.getStreet());
//      CustomerPojo updatedCustomer = customerServiceBean.setAddressFor(id, newAddress);
//      response = Response.ok(updatedCustomer).build();
//      return response;
//    }
    
    
  //--------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------
    //----------------billing address
    //--------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------
    
      @GET
      @RolesAllowed({ADMIN_ROLE})
      @Path("/{id}/billingAddress")
      public Response getBillingAddressByCustomerId(@PathParam("id") int id) {
          servletContext.log("try to retrieve addr for customer " + id);
          Response response = null;
          BillingAddressPojo addr = null;

          if (sc.isCallerInRole(ADMIN_ROLE)) {
              addr = customerServiceBean.getBillingAddressByCustomerId(id);
              response = Response.status( addr == null ? NOT_FOUND : OK).entity(addr).build();
          }
          else if (sc.isCallerInRole(USER_ROLE)) {
              WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
              SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
              CustomerPojo cust = sUser.getCustomer();
              if (cust != null && cust.getId() == id) {
                  addr = customerServiceBean.getBillingAddressByCustomerId(id);
                  response = Response.status(OK).entity(addr).build();
              }
              else {
                  throw new ForbiddenException();
              }
          }
          else {
              response = Response.status(BAD_REQUEST).build();
          }
          return response;
         
      }
      
      @DELETE
      @RolesAllowed({ADMIN_ROLE})
      @Path("/{id}/billingAddress")
      public Response deleteBillingAddress(@PathParam("id") int id) {
          Response response = null;
          customerServiceBean.deleteBillingAddressByCustomerId(id);
          response = Response.ok().build();
          return response;
      }
      
      @PUT
      @Transactional
      @RolesAllowed({ADMIN_ROLE})
      @Path("/{id}/billingAddress")
      public Response updateBillingAddress(@PathParam("id")int id, BillingAddressPojo updatedAddr) {
        Response response = null;
        customerServiceBean.updateBillingAddressByCustomerId(id, updatedAddr);
        response = Response.ok().build();
        return response;
      }
          
      @POST
      @RolesAllowed({ADMIN_ROLE})
      @Transactional
      @Path("/{id}/billingAddress")
      public Response addBillingAddressForCustomer(@PathParam("id") int id, BillingAddressPojo newAddress) {
        Response response = null;
        CustomerPojo updatedCustomer = customerServiceBean.persistBillingAddressforCustomerId(id, newAddress);
        response = Response.ok(updatedCustomer).build();
        return response;
      }
      
      
      //--------------------------------------------------------------------------------
      //--------------------------------------------------------------------------------
      //----------------shipping address
      //--------------------------------------------------------------------------------
      //--------------------------------------------------------------------------------
      
        @GET
        @RolesAllowed({ADMIN_ROLE})
        @Path("/{id}/shippingAddress")
        public Response getShippingAddressByCustomerId(@PathParam("id") int id) {
            servletContext.log("try to retrieve addr for customer " + id);
            Response response = null;
            ShippingAddressPojo addr = null;

            if (sc.isCallerInRole(ADMIN_ROLE)) {
                addr = customerServiceBean.getShippingAddressByCustomerId(id);
                response = Response.status( addr == null ? NOT_FOUND : OK).entity(addr).build();
            }
            else if (sc.isCallerInRole(USER_ROLE)) {
                WrappingCallerPrincipal wCallerPrincipal = (WrappingCallerPrincipal)sc.getCallerPrincipal();
                SecurityUser sUser = (SecurityUser)wCallerPrincipal.getWrapped();
                CustomerPojo cust = sUser.getCustomer();
                if (cust != null && cust.getId() == id) {
                    addr = customerServiceBean.getShippingAddressByCustomerId(id);
                    response = Response.status(OK).entity(addr).build();
                }
                else {
                    throw new ForbiddenException();
                }
            }
            else {
                response = Response.status(BAD_REQUEST).build();
            }
            return response;
           
        }
        
        @DELETE
        @RolesAllowed({ADMIN_ROLE})
        @Path("/{id}/shippingAddress")
        public Response deleteShippingAddress(@PathParam("id") int id) {
            Response response = null;
            customerServiceBean.deleteShippingAddressByCustomerId(id);
            response = Response.ok().build();
            return response;
        }
        
        @PUT
        @Transactional
        @RolesAllowed({ADMIN_ROLE})
        @Path("/{id}/shippingAddress")
        public Response updateShippingAddress(@PathParam("id")int id, ShippingAddressPojo updatedAddr) {
          Response response = null;
          customerServiceBean.updateShippingAddressByCustomerId(id, updatedAddr);
          response = Response.ok().build();
          return response;
        }
            
        @POST
        @RolesAllowed({ADMIN_ROLE})
        @Transactional
        @Path("/{id}/shippingAddress")
        public Response addShippingAddressForCustomer(@PathParam("id") int id, ShippingAddressPojo newAddress) {
          Response response = null;
          CustomerPojo updatedCustomer = customerServiceBean.persistShippingAddressforCustomerId(id, newAddress);
          response = Response.ok(updatedCustomer).build();
          return response;
        }
        
  //--------------------------------------------------------------------------------
   
    //TODO - endpoints for setting up Orders

    /**
     * get orders from database and response to yaml
     * @param id int
     * @return response
     */
    
    @GET
    @RolesAllowed({ADMIN_ROLE,USER_ROLE})
    @Path("/{id}/order")
    public Response getOrders(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id) {
        servletContext.log("retrieving all customers ...");
        List<OrderPojo> custs = customerServiceBean.getCustomerAllOrders(id);
        Response response = Response.ok(custs).build();
        return response;
    }
    
    /**
     * add order to database and response to yaml
     * @param id int
     * @param newCustomer OrderPojo
     * @return response
     */
    
    @POST
    @Transactional
    @RolesAllowed({ADMIN_ROLE,USER_ROLE})
    @Path("/{id}/order")
    public Response addOrder(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, OrderPojo newCustomer) {
      Response response = null;
      CustomerPojo newCustomerWithIdTimestamps = customerServiceBean.persistCustomerOrder(id, newCustomer);
      response = Response.ok(newCustomerWithIdTimestamps).build();
      return response;
    }
    
    /**
     * get order from database and response to yaml
     * @param id int
     * @param orderid int
     * @return response
     */
    @GET                     //change
    @RolesAllowed({ADMIN_ROLE,USER_ROLE})
    @Path("/{id}/order/{orderid}")
    public Response getOrderById(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id,@PathParam("orderid") int orderid) {
      servletContext.log("try to retrieve specific product " + id);
      OrderPojo custs  = customerServiceBean.getCustomerOrderById(id,orderid);
      Response response = Response.ok(custs).build();
      return response;
    }

    /**
     * delete order from database and response to yaml
     * @param id int
     * @param orderid int
     * @return Response
     */
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{id}/order/{orderid}")
    public Response deletetOrder(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, @PathParam("orderid")int orderid) {
        Response response = null;
        OrderPojo orderdelete = customerServiceBean.deleteCustomerOrderById(id,orderid);
        response = Response.ok(orderdelete).build();
        return response;
    }
    
    /**
     * update order from database and response to yaml
     * @param id int
     * @param orderid int
     * @param order OrderPojo
     * @return Response
     */

    @Transactional
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{id}/order/{orderid}")
    @PUT
    public Response updateOrder(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id,@PathParam("orderid") int orderid, OrderPojo order) {
      Response response = null;
      OrderPojo orderchange=customerServiceBean.updateCustomerOrderById(id, orderid,order);
      response = Response.ok(orderchange).build();
      return response;
    }
    
  
//  //--------------------------------------------------------------------------------
    //TODO - endpoints for setting up OrderLines
  //--------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------
    //----------------order Line
    //--------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------
    

    /**
     * get OrderLine By Id
     * @param id  int
     * @param orderid int
     * @return Response
     */
    @GET
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{id}/order/{orderid}/orderline")
    public Response getOrderLineById(@PathParam("id") int id,@PathParam("orderid") int orderid) {
      servletContext.log("try to retrieve specific product " + id);
      List<OrderLinePojo> custs  = customerServiceBean.getCustomerOrderLines(id,orderid);
      Response response = Response.ok(custs).build();
      return response;
    }
    
    /**
     * get OrderLine By Id
     * @param id int
     * @param orderid int
     * @param orderlineid int
     * @return Response
     */
    @GET
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{id}/order/{orderid}/orderline/{orderlineid}")
    public Response getOrderLineById(@PathParam("id") int id,@PathParam("orderid") int orderid , @PathParam("orderlineid") int orderlineid) {
      servletContext.log("try to retrieve specific product " + id);
     OrderLinePojo orderline  = customerServiceBean.getCustomerOrderLineById(id,orderid,orderlineid);
      Response response = Response.ok(orderline).build();
      return response;
    }
    /**
     * add OrderLine
     * @param id int
     * @param orderid int
     * @param line OrderLinePojo
     * @return Response
     */
    @POST
    @Transactional
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{id}/order/{orderid}/orderline")
    public Response addOrderLine(@PathParam("id") int id, @PathParam("orderid") int orderid, OrderLinePojo line) {
      Response response = null;
      OrderPojo order = customerServiceBean.persistOrderLine(id, orderid, line);
      response = Response.ok(order).build();
      return response;
    }
    
    /**
     * deletet OrderLine
     * @param id
     * @param orderid int
     * @param orderlineid int
     * @return Response
     */
    @DELETE
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{id}/order/{orderid}/orderline/{orderlineid}")
    public Response deletetOrderLine(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id, @PathParam("orderid")int orderid, @PathParam("orderlineid") int orderlineid) {
        Response response = null;
       customerServiceBean.deleteOrderLineById(id,orderid, orderlineid);
        response = Response.ok().build();
        return response;
    }
    /**
     * update OrderLine
     * @param id int
     * @param orderid int
     * @param orderlineid int
     * @param line OrderLinePojo
     * @return Response
     */
    @PUT
    @Transactional
    @RolesAllowed({ADMIN_ROLE})
    @Path("/{id}/order/{orderid}/orderline/{orderlineid}")
    public Response updateOrderLine(@PathParam(RESOURCE_PATH_ID_ELEMENT) int id,@PathParam("orderid") int orderid, @PathParam("orderlineid") int orderlineid, OrderLinePojo line) {
      Response response = null;
      OrderLinePojo orderchange=customerServiceBean.updateOrderLineById(id, orderid,orderlineid, line);
      response = Response.ok(orderchange).build();
      return response;
    }
}