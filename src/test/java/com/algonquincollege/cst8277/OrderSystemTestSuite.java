/***************************************************************************f******************u************zz*******y**
 * File: OrderSystemTestSuite.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277;

import static com.algonquincollege.cst8277.utils.MyConstants.APPLICATION_API_VERSION;
import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_ADMIN_USER;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_ADMIN_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PREFIX;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PREFIX1;

import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_ELEMENT;
import static com.algonquincollege.cst8277.utils.MyConstants.RESOURCE_PATH_ID_PATH;
//import static com.algonquincollege.cst8277.utils.MyConstants.CUSTOMER_ADDRESS_RESOURCE_PATH;
import static com.algonquincollege.cst8277.utils.MyConstants.ORDER_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.SLASH;
import static com.algonquincollege.cst8277.utils.MyConstants.ORDERLINE_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.SHIPPINGADDRESS_SUBRESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.BILLINGADDRESS_SUBRESOURCE_NAME ;
import static com.algonquincollege.cst8277.utils.MyConstants.PRODUCT_RESOURCE_NAME;
import static com.algonquincollege.cst8277.utils.MyConstants.STORE_RESOURCE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.invoke.MethodHandles;
import java.net.URI;
//import java.util.ArrayList;
//import java.util.HashSet;
import java.util.List;
//import java.util.Set;
//
//import javax.ejb.Singleton;
import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Persistence;
//import javax.persistence.PersistenceContext;
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
//import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.BillingAddressPojo;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.OrderLinePk;
import com.algonquincollege.cst8277.models.OrderLinePojo;
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.ProductPojo;
//import com.algonquincollege.cst8277.models.ProductPojo_;
import com.algonquincollege.cst8277.models.ShippingAddressPojo;
import com.algonquincollege.cst8277.models.StorePojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;



@TestMethodOrder(MethodOrderer.Alphanumeric.class)
/**
 * Order System Test Suite
 */
public class OrderSystemTestSuite {
    /**
     * declare Class<?> _thisClaz
     */
    private static final Class<?> _thisClaz = MethodHandles.lookup().lookupClass();
    /**
     * declare Logger logger
     */
    private static final Logger logger = LoggerFactory.getLogger(_thisClaz);
    /**
     * declare APPLICATION_CONTEXT_ROOT
     */
    static final String APPLICATION_CONTEXT_ROOT = "rest-orderSystem";
    /**
     * declare HTTP_SCHEMA
     */
    static final String HTTP_SCHEMA = "http";
    /**
     * declare HOST
     */
    static final String HOST = "localhost";
    /**
     * declare EntityManager
     */
    protected EntityManager em;
    
    //TODO - if you changed your Payara's default port (to say for example 9090)
    //       your may need to alter this constant
    /**
     * declare PORT
     */
    static final int PORT = 9090;

    // test fixture(s)
    /**
     * declare uri
     */
    static URI uri;
    /**
     * declare adminAuth
     */
    static HttpAuthenticationFeature adminAuth;
    /**
     * declare userAuth
     */
    static HttpAuthenticationFeature userAuth;
    /**
     * declare userAuth1
     */
    static HttpAuthenticationFeature userAuth1;
    
    /**
     * oneTime SetUp
     * @throws Exception exception
     */
    @BeforeAll
    public static void oneTimeSetUp() throws Exception {
        logger.debug("oneTimeSetUp");
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        uri = UriBuilder
            .fromUri(APPLICATION_CONTEXT_ROOT + APPLICATION_API_VERSION)
            .scheme(HTTP_SCHEMA)
            .host(HOST)
            .port(PORT)
            .build();
        adminAuth = HttpAuthenticationFeature.basic(DEFAULT_ADMIN_USER, DEFAULT_ADMIN_USER_PASSWORD);
        userAuth = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX, DEFAULT_USER_PASSWORD);
        userAuth1 = HttpAuthenticationFeature.basic(DEFAULT_USER_PREFIX1, DEFAULT_USER_PASSWORD);
    }
    /**
     * declare WebTarget
     */
    protected WebTarget webTarget;
    /**
     * setup
     */
    @BeforeEach
    public void setUp() {
        Client client = ClientBuilder.newClient(
            new ClientConfig().register(MyObjectMapperProvider.class).register(new LoggingFeature()));
        webTarget = client.target(uri);
    }
    /**
     * test Read All Customers with adminrole
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test01_ReadAllCustomers_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(userAuth)
            //.register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<CustomerPojo> custs = response.readEntity(new GenericType<List<CustomerPojo>>(){});
        assertThat(custs, is(not(empty())));
        //TODO - depending on what is in your Db when you run this, you may need to change the next line
        //assertThat(custs, hasSize(2));
    }
    
    // TODO - create39 more test-cases that send GET/PUT/POST/DELETE messages
    // to REST'ful endpoints for the OrderSystem entities using the JAX-RS
    // ClientBuilder APIs
    /**
     * test02 Creat Customer adminrole
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test02_CreatCustomer_adminrole() throws JsonMappingException, JsonProcessingException {
        CustomerPojo cust = new CustomerPojo();
        cust.setEmail("FirstTest0001@test.com");
        cust.setFirstName("FirstTest0001");
        cust.setLastName("LastTest0001");
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .post(Entity.json(cust));
        assertThat(response.getStatus(), is(200));
        CustomerPojo cust_res = response.readEntity(CustomerPojo.class);
        assertThat(cust_res.getEmail(),is("FirstTest0001@test.com"));
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, cust_res.getId())//only 1 order in order database
            .request()
            .delete();
        assertThat(response3.getStatus(), is(200));
    }
    
    /**
     * test03 Read Customer By Id adminrole
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test03_ReadCustomerById_adminrole() throws JsonMappingException, JsonProcessingException {
        CustomerPojo cust = new CustomerPojo();
        cust.setFirstName("FirstTest0002");
        cust.setLastName("LastTest0002");
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .post(Entity.json(cust));
        assertThat(response.getStatus(), is(200));
        CustomerPojo cust_res = response.readEntity(CustomerPojo.class);
        assertThat(cust_res.getFirstName(),is("FirstTest0002"));
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, cust_res.getId())//only 1 order in order database
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        CustomerPojo cust_res2 = response2.readEntity(CustomerPojo.class);
        assertThat(cust_res2.getFirstName(),is("FirstTest0002"));
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, cust_res.getId())//only 1 order in order database
            .request()
            .delete();
        assertThat(response3.getStatus(), is(200));
    }
    /**
     * test04 Update Customer admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test04_UpdateCustomer_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        CustomerPojo cust_res2 = response2.readEntity(CustomerPojo.class);
        cust_res2.setEmail("Updated0002@test.com");
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .request()
            .put(Entity.json(cust_res2));
        assertThat(response.getStatus(), is(200));
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .request()
            .get();
        assertThat(response3.getStatus(), is(200));
        CustomerPojo cust_res3 = response3.readEntity(CustomerPojo.class);
        assertThat(cust_res3.getEmail(),is("Updated0002@test.com"));
    }
    /**
     * test05 Delete Customer By Id admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test05_DeleteCustomerById_adminrole() throws JsonMappingException, JsonProcessingException {
        CustomerPojo cust = new CustomerPojo();
        cust.setFirstName("ToDelete0004");
        cust.setLastName("LastTest0004");
        
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME)
            .request()
            .post(Entity.json(cust));
        assertThat(response.getStatus(), is(200));
        CustomerPojo cust_res = response.readEntity(CustomerPojo.class);
        assertThat(cust_res.getFirstName(),is("ToDelete0004"));
        
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, cust_res.getId())//only 1 order in order database
            .request()
            .delete();
        assertThat(response2.getStatus(), is(200));
    }
    /**
     * test06 Add Billing Address To Customer adminrole
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    //TEST NOTE: Billing Address Start
    @Test
    public void test06_AddBillingAddressToCustomer_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        CustomerPojo cust_res2 = response2.readEntity(CustomerPojo.class);
        BillingAddressPojo bAddr = new BillingAddressPojo();
        bAddr.setStreet("1001 Bank Rd");
        bAddr.setCity("Ottawa");
        bAddr.setCountry("Canada");
        bAddr.setAlsoShipping(true);
        cust_res2.setBillingAddress(bAddr);
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .path(SLASH + BILLINGADDRESS_SUBRESOURCE_NAME)
            .request()
            .post(Entity.json(bAddr));
        assertThat(response.getStatus(), is(200));
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .request()
            .get();
        assertThat(response3.getStatus(), is(200));
        CustomerPojo cust_res3 = response3.readEntity(CustomerPojo.class);
        assertThat(cust_res3.getBillingAddress().getStreet(),is("1001 Bank Rd"));
    }
    /**
     * test07 Get Billing Address From Customer admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test07_GetBillingAddressFromCustomer_adminrole() throws JsonMappingException, JsonProcessingException {
        
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        CustomerPojo cust_res3 = response.readEntity(CustomerPojo.class);
        assertThat(cust_res3.getBillingAddress().getStreet(),is("1001 Bank Rd"));
    }
    /**
     * test08 Update BillingAddress In Customer adminrole
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test08_UpdateBillingAddressInCustomer_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        CustomerPojo cust_res2 = response2.readEntity(CustomerPojo.class);
        AddressPojo bAddr = cust_res2.getBillingAddress();
        bAddr.setStreet("1001 Bank Rd");
        bAddr.setCity("Ottawa_Updated");
        bAddr.setCountry("Canada");
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .path(SLASH + BILLINGADDRESS_SUBRESOURCE_NAME)
            .request()
            .put(Entity.json(bAddr));
        assertThat(response.getStatus(), is(200));
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .request()
            .get();
        assertThat(response3.getStatus(), is(200));
        CustomerPojo cust_res3 = response3.readEntity(CustomerPojo.class);
        assertThat(cust_res3.getBillingAddress().getCity(),is("Ottawa_Updated"));
    }
    /**
     * test09 Delete Billing Address In Customer admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    
    @Test
    public void test09_DeleteBillingAddressInCustomer_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .path(SLASH + BILLINGADDRESS_SUBRESOURCE_NAME)
            .request()
            .delete();
        assertThat(response2.getStatus(), is(200));
                
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .request()
            .get();
        assertThat(response3.getStatus(), is(200));
        CustomerPojo cust_res3 = response3.readEntity(CustomerPojo.class);
        assertNull(cust_res3.getBillingAddress());
    }
  //TEST NOTE: Billing Address End
    /**
     * test10 Customer Get Order admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonMappingException
     */
    
    @Test
    public void test10_CustomerGetOrder_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH )
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .path(ORDER_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        List<OrderPojo> custs = response2.readEntity(new GenericType<List<OrderPojo>>(){});
        assertThat(custs, is(not(empty())));
    }
    /**
     * test11 Add Order To Customer admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test11_AddOrderToCustomer_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customerid
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        CustomerPojo cust_res2 = response2.readEntity(CustomerPojo.class);
        int before_size = cust_res2.getOrders().size();
        OrderPojo order = new OrderPojo();
        order.setDescription("NewOrder0001");
        cust_res2.addOrders(order);
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH )
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .path(ORDER_RESOURCE_NAME)
            .request()
            .post(Entity.json(order));
        assertThat(response.getStatus(), is(200));
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH )
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .path(ORDER_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response3.getStatus(), is(200));
        List<OrderPojo> custs = response3.readEntity(new GenericType<List<OrderPojo>>(){});
        int after_size = custs.size();
        assertEquals(after_size,before_size+1);
    }
    /**
     * test12 Read Order By ID admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test12_ReadOrderByID_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        OrderPojo cust_res3 = response2.readEntity(OrderPojo.class);
        assertThat(cust_res3.getDescription(),is("new order"));
    }
    /**
     * test13 Update Order By ID admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    
    @Test
    public void test13_UpdateOrderByID_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 4)//only 1 order in order database
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        OrderPojo cust_res3 = response2.readEntity(OrderPojo.class);
        cust_res3.setDescription("UpdatedOrder0001");
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 4)//only 1 order in order database
            .request()
            .put(Entity.json(cust_res3));
        assertThat(response2.getStatus(), is(200));
        OrderPojo cust_res4 = response3.readEntity(OrderPojo.class);
        assertThat(cust_res4.getDescription(),is("UpdatedOrder0001"));
    }
    
    /**
     * test14 Delete Order To Customer admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test14_DeleteOrderToCustomer_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        CustomerPojo cust_res2 = response2.readEntity(CustomerPojo.class);
        int before_size = cust_res2.getOrders().size();
        OrderPojo order = cust_res2.getOrders().get(before_size-1);
        int deleteId = order.getId();
                
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH )
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, deleteId)//only 1 order in order database
            .request()
            .delete();
        assertThat(response.getStatus(), is(200));
    }

    /**
     * test15 Add Shipping Address To Customer admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    //TEST NOTE: Shipping Address Start
    @Test
    public void test15_AddShippingAddressToCustomer_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        CustomerPojo cust_res2 = response2.readEntity(CustomerPojo.class);
        ShippingAddressPojo bAddr = new ShippingAddressPojo();
        bAddr.setStreet("2001 Bank Rd");
        bAddr.setCity("Ottawa");
        bAddr.setCountry("Canada");
        bAddr.setPostal("K1K 1N1");
        bAddr.setState("ON");
        cust_res2.setShippingAddress(bAddr);
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .path(SLASH + SHIPPINGADDRESS_SUBRESOURCE_NAME)
            .request()
            .post(Entity.json(bAddr));
        assertThat(response.getStatus(), is(200));
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .request()
            .get();
        assertThat(response3.getStatus(), is(200));
        CustomerPojo cust_res3 = response3.readEntity(CustomerPojo.class);
        assertThat(cust_res3.getShippingAddress().getStreet(),is("2001 Bank Rd"));
    }
    /**
     * test16 Get Shipping Address From Customer admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test16_GetShippingAddressFromCustomer_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        CustomerPojo cust_res3 = response.readEntity(CustomerPojo.class);
        assertThat(cust_res3.getShippingAddress().getStreet(),is("2001 Bank Rd"));
    }
    /**
     * test17 Update Shipping Address In Customer admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test17_UpdateShippingAddressInCustomer_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        CustomerPojo cust_res2 = response2.readEntity(CustomerPojo.class);
        AddressPojo bAddr = cust_res2.getShippingAddress();
        bAddr.setStreet("2001 Bank Rd");
        bAddr.setCity("Ottawa_Shipping_Updated");
        bAddr.setCountry("Canada");
        
        Response response = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .path(SLASH + SHIPPINGADDRESS_SUBRESOURCE_NAME)
            //.resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)//billing address id
            .request()
            .put(Entity.json(bAddr));
        assertThat(response.getStatus(), is(200));
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .request()
            .get();
        assertThat(response3.getStatus(), is(200));
        CustomerPojo cust_res3 = response3.readEntity(CustomerPojo.class);
        assertThat(cust_res3.getShippingAddress().getCity(),is("Ottawa_Shipping_Updated"));
    }
    
    /**
     * test18 Delete Shipping Address In Customer admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test18_DeleteShippingAddressInCustomer_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .path(SLASH + SHIPPINGADDRESS_SUBRESOURCE_NAME)
            .request()
            .delete();
        assertThat(response2.getStatus(), is(200));
                
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .request()
            .get();
        assertThat(response3.getStatus(), is(200));
        CustomerPojo cust_res3 = response3.readEntity(CustomerPojo.class);

        assertNull(cust_res3.getShippingAddress());

    }
  //TEST NOTE: Shipping Address End
    
    /**
     * test19 get All OrderLine admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test19_getAllOrderLine_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//only 1 order in order database
            .path(SLASH + ORDERLINE_RESOURCE_NAME)
            .request()
            .get();
      
        assertThat(response2.getStatus(), is(200));
        List<OrderLinePojo> custs = response2.readEntity(new GenericType<List<OrderLinePojo>>(){});
        assertThat(custs, is(not(empty())));
    }
    
    /**
     * test20 Add OrderLine admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test20_AddOrderLine_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//order id
            .request()
            .get();
      
        assertThat(response2.getStatus(), is(200));
        OrderPojo cust_res2 = response2.readEntity(OrderPojo.class);
        int before_size = cust_res2.getOrderlines().size();
        Response response_prd = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//order id
            .request()
            .get();
      
        assertThat(response_prd.getStatus(), is(200));
        ProductPojo prd1 = response_prd.readEntity(ProductPojo.class);
        OrderLinePojo orderline1 = new OrderLinePojo();
        orderline1.setAmount(9777.77);
        orderline1.setOwningOrder(cust_res2);
        orderline1.setProduct(prd1);
        OrderLinePk olpk = new OrderLinePk();
        olpk.setOrderLineNo(706);
        olpk.setOwningOrderId(1);
        olpk.setOwningOrderId(cust_res2.getId());
        orderline1.setPk(olpk);
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//order id
            .path(ORDERLINE_RESOURCE_NAME)
            .request()
            .post(Entity.json(orderline1));
        assertThat(response3.getStatus(), is(200));
        Response response4 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//order id
            .path(SLASH + ORDERLINE_RESOURCE_NAME)  //orderline path
            .request()
            .get();
        assertThat(response4.getStatus(), is(200));
        List<OrderLinePojo> custs = response4.readEntity(new GenericType<List<OrderLinePojo>>(){});
        int after_size = custs.size();
        assertEquals(after_size,before_size+1);

    }
     /**
      * test21 get OrderLine By ID admin role
      * @throws JsonMappingException JsonMappingException
      * @throws JsonProcessingException JsonProcessingException
      */
    @Test
    public void test21_getOrderLineByID_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//order id
            .path(SLASH + ORDERLINE_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 706)//order id
            .request()
            .get();
      
        assertThat(response2.getStatus(), is(200));
        OrderLinePojo orderline1 = response2.readEntity(OrderLinePojo.class);
        assertEquals(orderline1.getPk().getOrderLineNo(),706);
        assertEquals(orderline1.getPk().getOwningOrderId(),1);
    }
    /**
     * test22 Update OrderLine By ID admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test22_UpdateOrderLineByID_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//order id
            .path(SLASH + ORDERLINE_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 706)//order id
            .request()
            .get();
      
        assertThat(response2.getStatus(), is(200));
        OrderLinePojo orderline1 = response2.readEntity(OrderLinePojo.class);
        orderline1.setAmount(111.11);
        Response response3 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//order id
            .path(SLASH + ORDERLINE_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 706)//order id
            .request()
            .put(Entity.json(orderline1));
        assertThat(response3.getStatus(), is(200));
        Response response4 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//order id
            .path(SLASH + ORDERLINE_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 706)//order id
            .request()
            .get();
        assertThat(response4.getStatus(), is(200));
        OrderLinePojo orderline2 = response4.readEntity(OrderLinePojo.class);
        assertEquals(orderline2.getAmount(), 111.11, 0.01);
    }
    
    /**
     * test23 Delete OrderLine By ID admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test23_DeleteOrderLineByID_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response1 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//order id
            .path(SLASH + ORDERLINE_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 703)//order id
            .request()
            .get();
        assertThat(response1.getStatus(), is(200));
//        OrderLinePojo orderline2 = response1.readEntity(OrderLinePojo.class);
        Response response2 = webTarget
            .register(adminAuth)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//customer id
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 1)//order id
            .path(SLASH + ORDERLINE_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 706)//order id
            .request()
            .delete();
        assertThat(response2.getStatus(), is(200));
    }
    
    /**
     * test24 Read All Products with admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */

    @Test
    public void test24_ReadAllProducts_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<ProductPojo> l_obj = response.readEntity(new GenericType<List<ProductPojo>>(){});
        assertThat(l_obj, is(not(empty())));
    }
    
    /**
     * test25 Creat Product admin  role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test25_CreatProduct_adminrole() throws JsonMappingException, JsonProcessingException {
        ProductPojo obj = new ProductPojo();
        obj.setDescription("NewProduct0001");
                       
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME)
            .request()
            .post(Entity.json(obj));
        assertThat(response.getStatus(), is(200));
        ProductPojo obj_res = response.readEntity(ProductPojo.class);
        assertThat(obj_res.getDescription(),is("NewProduct0001"));
        
        Response response3 = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, obj_res.getId())//only 1 order in order database
            .request()
            .delete();
        assertThat(response3.getStatus(), is(200));

    }
    /**
     * test26 Read Product By Id admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test26_ReadProductById_adminrole() throws JsonMappingException, JsonProcessingException {
        ProductPojo obj = new ProductPojo();
        obj.setDescription("NewProduct0002_ForRead");
                
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME)
            .request()
            .post(Entity.json(obj));
        assertThat(response.getStatus(), is(200));
        ProductPojo obj_res = response.readEntity(ProductPojo.class);
        assertThat(obj_res.getDescription(),is("NewProduct0002_ForRead"));
        
        Response response2 = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, obj_res.getId())//only 1 order in order database
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        ProductPojo obj_res2 = response2.readEntity(ProductPojo.class);
        assertThat(obj_res2.getDescription(),is("NewProduct0002_ForRead"));
        
        Response response3 = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, obj_res.getId())//only 1 order in order database
            .request()
            .delete();
        assertThat(response3.getStatus(), is(200));
    }
    /**
     * test27 Update Product admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test27_UpdateProduct_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)//objomer id
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        ProductPojo obj_res2 = response2.readEntity(ProductPojo.class);
        obj_res2.setDescription("Updated_Product0002");
        
        Response response = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)//only 1 order in order database
            .request()
            .put(Entity.json(obj_res2));
   
        assertThat(response.getStatus(), is(200));
        
        Response response3 = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 2)//only 1 order in order database
            .request()
            .get();
        assertThat(response3.getStatus(), is(200));
        ProductPojo obj_res3 = response3.readEntity(ProductPojo.class);

        assertThat(obj_res3.getDescription(),is("Updated_Product0002"));

    }
    /**
     * test28 Delete Product By Id admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test28_DeleteProductById_adminrole() throws JsonMappingException, JsonProcessingException {
        ProductPojo obj = new ProductPojo();
        obj.setDescription("ToDelete0004");
               
        Response response = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME)
            .request()
            .post(Entity.json(obj));
        assertThat(response.getStatus(), is(200));
        ProductPojo obj_res = response.readEntity(ProductPojo.class);
        assertThat(obj_res.getDescription(),is("ToDelete0004"));
        
        Response response2 = webTarget
            .register(adminAuth)
            .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, obj_res.getId())//only 1 order in order database
            .request()
            .delete();
        assertThat(response2.getStatus(), is(200));

    }
    
    /**
     * test29 Read All Stores with admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test29_ReadAllStores_with_adminrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<StorePojo> l_obj = response.readEntity(new GenericType<List<StorePojo>>(){});
        assertThat(l_obj, is(not(empty())));
    }

    /**
     * test30 Creat Store admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test30_CreatStore_adminrole() throws JsonMappingException, JsonProcessingException {
        StorePojo obj = new StorePojo();
        obj.setStoreName("NewStore0001");
                       
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME)
            .request()
            .post(Entity.json(obj));
        assertThat(response.getStatus(), is(200));
        StorePojo obj_res = response.readEntity(StorePojo.class);
        assertThat(obj_res.getStoreName(),is("NewStore0001"));
        
        Response response3 = webTarget
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, obj_res.getId())//only 1 order in order database
            .request()
            .delete();
        assertThat(response3.getStatus(), is(200));

    }
    /**
     * test31 Read Store By Id admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test31_ReadStoreById_adminrole() throws JsonMappingException, JsonProcessingException {
        StorePojo obj = new StorePojo();
        obj.setStoreName("NewStore0002_ForRead");
                
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME)
            .request()
            .post(Entity.json(obj));
        assertThat(response.getStatus(), is(200));
        StorePojo obj_res = response.readEntity(StorePojo.class);
        assertThat(obj_res.getStoreName(),is("NewStore0002_ForRead"));
        
        Response response2 = webTarget
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, obj_res.getId())//only 1 order in order database
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        StorePojo obj_res2 = response2.readEntity(StorePojo.class);
        assertThat(obj_res2.getStoreName(),is("NewStore0002_ForRead"));
        
        Response response3 = webTarget
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, obj_res.getId())//only 1 order in order database
            .request()
            .delete();
        assertThat(response3.getStatus(), is(200));
    }

    /**
     * test32 Update Store By Id admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test32_UpdateStoreById_adminrole() throws JsonMappingException, JsonProcessingException {
    
        Response response2 = webTarget
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 33)//objomer id
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        StorePojo obj_res2 = response2.readEntity(StorePojo.class);
        obj_res2.setStoreName("Updated_Store0002");
        Response response = webTarget
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 33)//only 1 order in order database
            .request()
            .put(Entity.json(obj_res2));
        assertThat(response.getStatus(), is(200));
        Response response3 = webTarget
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 33)//only 1 order in order database
            .request()
            .get();
        assertThat(response3.getStatus(), is(200));
        StorePojo obj_res3 = response3.readEntity(StorePojo.class);
        assertThat(obj_res3.getStoreName(),is("Updated_Store0002"));
    }
    
    /**
     * test33 Delete Store By Id admin role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test33_DeleteStoreById_adminrole() throws JsonMappingException, JsonProcessingException {
        StorePojo obj = new StorePojo();
        obj.setStoreName("StoreToDelete0004");
               
        Response response = webTarget
            //.register(userAuth)
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME)
            .request()
            .post(Entity.json(obj));
        assertThat(response.getStatus(), is(200));
        StorePojo obj_res = response.readEntity(StorePojo.class);
        assertThat(obj_res.getStoreName(),is("StoreToDelete0004"));
        
        Response response2 = webTarget
            .register(adminAuth)
            .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, obj_res.getId())//only 1 order in order database
            .request()
            .delete();
        assertThat(response2.getStatus(), is(200));

    }
    /**
     * test34 Get Customer By Id user role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test34_GetCustomerById_userrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(userAuth1)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 51)//only 1 order in order database
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        CustomerPojo cust_res2 = response2.readEntity(CustomerPojo.class);
        assertThat(cust_res2.getFirstName(),is("John"));
    }
   /**
    * test35 Get All Products with all Role
    * @throws JsonMappingException JsonMappingException
    * @throws JsonProcessingException JsonProcessingException
    */
    @Test
    public void test35_GetAllProducts_with_allRole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .path(PRODUCT_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<ProductPojo> l_obj = response.readEntity(new GenericType<List<ProductPojo>>(){});
        assertThat(l_obj, is(not(empty())));
    }
    
    /**
     * test36 Get All Stores with all role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test36_GetAllStores_with_allrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .path(STORE_RESOURCE_NAME)
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        List<StorePojo> l_obj = response.readEntity(new GenericType<List<StorePojo>>(){});
        assertThat(l_obj, is(not(empty())));
    }
    /**
     * test37 Get Order By ID user role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test37_GetOrderByID_userrole() throws JsonMappingException, JsonProcessingException {
        Response response2 = webTarget
            .register(userAuth1)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 51)//only 1 order in order database
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 41)//only 1 order in order database
            .request()
            .get();
        assertThat(response2.getStatus(), is(200));
        OrderPojo cust_res3 = response2.readEntity(OrderPojo.class);
        assertThat(cust_res3.getDescription(),is("new order"));
    }
    
    /**
     * test38 Get Product By Id all role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test38_GetProductById_allrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .path(PRODUCT_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 3)//only 1 order in order database
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        ProductPojo cust_res2 = response.readEntity(ProductPojo.class);
        assertThat(cust_res2.getDescription(),is("testProduct1"));
    }
    /**
     * test39 Get Store By Id all role
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test39_GetStoreById_allrole() throws JsonMappingException, JsonProcessingException {
        Response response = webTarget
            .path(STORE_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 33)//only 1 order in order database
            .request()
            .get();
        assertThat(response.getStatus(), is(200));
        StorePojo cust_res2 = response.readEntity(StorePojo.class);
        assertThat(cust_res2.getStoreName(),is("Updated_Store0002"));
    }
    /**
     * test40 Update Order By ID user role Will Show 403
     * @throws JsonMappingException JsonMappingException
     * @throws JsonProcessingException JsonProcessingException
     */
    @Test
    public void test40_UpdateOrderByID_userroleWillShow403() throws JsonMappingException, JsonProcessingException {
        OrderPojo order = new OrderPojo();
        order.setDescription("beatiful");
        Response response2 = webTarget
            .register(userAuth1)
            .path(CUSTOMER_RESOURCE_NAME + RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 51)//only 1 order in order database
            .path(SLASH + ORDER_RESOURCE_NAME+ RESOURCE_PATH_ID_PATH)
            .resolveTemplate(RESOURCE_PATH_ID_ELEMENT, 41)//only 1 order in order database
            .request()
            .put(Entity.json(order));
        assertThat(response2.getStatus(), is(403));
    }
    
}