/***************************************************************************f******************u************zz*******y**
 * File: CustomerService.java
 * Course materials (20W) CST 8277
 * @author (original) Mike Norman
 * @author Qing Wang 040883693
 * @author jennifer yuan 040944503
 * @author Chunyuan Luo 040926918
 *
 */
package com.algonquincollege.cst8277.ejb;

import static com.algonquincollege.cst8277.models.SecurityRole.ROLE_BY_NAME_QUERY;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_KEY_SIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_PROPERTY_ALGORITHM;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_PROPERTY_ITERATIONS;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_SALT_SIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PASSWORD;
import static com.algonquincollege.cst8277.utils.MyConstants.DEFAULT_USER_PREFIX;
import static com.algonquincollege.cst8277.utils.MyConstants.PARAM1;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_ALGORITHM;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_ITERATIONS;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_KEYSIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.PROPERTY_SALTSIZE;
import static com.algonquincollege.cst8277.utils.MyConstants.USER_ROLE;

import static com.algonquincollege.cst8277.models.CustomerPojo.ALL_CUSTOMERS_QUERY_NAME;
import static com.algonquincollege.cst8277.models.CustomerPojo.FIND_CUSTOMERS_QUERY_ID; //changed
//import static com.algonquincollege.cst8277.models.SecurityUser.SECURITY_USER_BY_NAME_QUERY; //changed


import java.io.Serializable;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
//import javax.transaction.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;

//import com.algonquincollege.cst8277.models.AddressPojo;
import com.algonquincollege.cst8277.models.BillingAddressPojo;
import com.algonquincollege.cst8277.models.CustomerPojo;
import com.algonquincollege.cst8277.models.OrderLinePk;
import com.algonquincollege.cst8277.models.OrderLinePojo;
import com.algonquincollege.cst8277.models.OrderPojo;
import com.algonquincollege.cst8277.models.OrderPojo_;
import com.algonquincollege.cst8277.models.ProductPojo;
import com.algonquincollege.cst8277.models.ProductPojo_;
import com.algonquincollege.cst8277.models.SecurityRole;
import com.algonquincollege.cst8277.models.SecurityUser;
import com.algonquincollege.cst8277.models.ShippingAddressPojo;
import com.algonquincollege.cst8277.models.StorePojo;
import com.algonquincollege.cst8277.models.StorePojo_;

/**
 * Stateless Singleton Session Bean - CustomerService
 */
@Singleton
public class CustomerService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String CUSTOMER_PU = "20f-groupProject-PU";

    @PersistenceContext(name = CUSTOMER_PU)
    protected EntityManager em;

    @Inject
    protected Pbkdf2PasswordHash pbAndjPasswordHash;
    
    //TODO
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // Customer
    // ---------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    /**
     * get all customers
     * @return List<CustomerPojo>
     */
    public List<CustomerPojo> getAllCustomers() {
        //changed
        return em.createNamedQuery(ALL_CUSTOMERS_QUERY_NAME, CustomerPojo.class).getResultList();
    }
    /**
     * get customer by id
     * @param custPK int
     * @return CustomerPojo
     */
    public CustomerPojo getCustomerById(int custPK) {    //changed
        CustomerPojo cust = null;
        try {
            cust = em.createNamedQuery(FIND_CUSTOMERS_QUERY_ID, CustomerPojo.class)
            .setParameter(PARAM1,custPK)
            .getSingleResult();
            return cust;
        }
        catch (Exception e) {
            return null;
        }
       
    }
    
    /**
     * insert a customer
     * @param newCustomer CustomerPojo
     * @return CustomerPojo
     */
    
    @Transactional
    public CustomerPojo persistCustomer(CustomerPojo newCustomer) {
        em.persist(newCustomer);
        em.flush();
        return newCustomer;
    }
    
    /**
     * update customer
     * @param id int
     * @param updatedcustomer CustomerPojo
     */
    @Transactional
    public void updateCustomerById(int id, CustomerPojo updatedcustomer) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
        if(cust!=null) {
            cust.setFirstName(updatedcustomer.getFirstName());
            cust.setLastName(updatedcustomer.getLastName());
            cust.setEmail(updatedcustomer.getEmail());
            cust.setPhoneNumber(updatedcustomer.getPhoneNumber());
            em.merge(cust);
        }
    }
    
    /**
     * delete a customer by id
     * @param id int
     */
    @Transactional
    public void deleteCustomerById(int id) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
        SecurityUser user = em.createQuery("select c from SecurityUser c where c.customer.id=:param1", SecurityUser.class).setParameter(PARAM1, id).getSingleResult();
        if(user !=null) {
            user.setCustomer(null);
        }
        if (cust != null) {
            em.merge(user);
            em.remove(cust);
        }
    }
    
  //--------------------------------------------------------------------------------------------------------------
    /**
     * build customer with id time stamps
     * @param newCustomerWithIdTimestamps CustomerPojo
     */
    @Transactional
    public void buildUserForNewCustomer(CustomerPojo newCustomerWithIdTimestamps) {
        SecurityUser userForNewCustomer = new SecurityUser();
        userForNewCustomer.setUsername(DEFAULT_USER_PREFIX + "" + newCustomerWithIdTimestamps.getId());
        Map<String, String> pbAndjProperties = new HashMap<>();
        pbAndjProperties.put(PROPERTY_ALGORITHM, DEFAULT_PROPERTY_ALGORITHM);
        pbAndjProperties.put(PROPERTY_ITERATIONS, DEFAULT_PROPERTY_ITERATIONS);
        pbAndjProperties.put(PROPERTY_SALTSIZE, DEFAULT_SALT_SIZE);
        pbAndjProperties.put(PROPERTY_KEYSIZE, DEFAULT_KEY_SIZE);
        pbAndjPasswordHash.initialize(pbAndjProperties);
        String pwHash = pbAndjPasswordHash.generate(DEFAULT_USER_PASSWORD.toCharArray());
        userForNewCustomer.setPwHash(pwHash);
        userForNewCustomer.setCustomer(newCustomerWithIdTimestamps);
        SecurityRole userRole = em.createNamedQuery(ROLE_BY_NAME_QUERY,
            SecurityRole.class).setParameter(PARAM1, USER_ROLE).getSingleResult();
        userForNewCustomer.getRoles().add(userRole);
        userRole.getUsers().add(userForNewCustomer);
        em.persist(userForNewCustomer);
    }

//    /**
//     * set adrress for shipping address or billing address
//     * @param custId  int
//     * @param newAddress AddressPojo
//     * @return CustomerPojo
//     */
//    @Transactional
//    public CustomerPojo setAddressFor(int custId, AddressPojo newAddress) {
//        CustomerPojo updatedCustomer = em.find(CustomerPojo.class, custId);
//        if (newAddress instanceof ShippingAddressPojo) {
//            updatedCustomer.setShippingAddress(newAddress);
//        }
//        else {
//            updatedCustomer.setBillingAddress(newAddress);
//        }
//        em.merge(updatedCustomer);
//        return updatedCustomer;
//    }

    
    
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // PRODUCT
    // ---------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    
    /**
     * get all products
     * @return List<ProductPojo>
     */
    public List<ProductPojo> getAllProducts() {
        //example of using JPA Criteria query instead of JPQL
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProductPojo> q = cb.createQuery(ProductPojo.class);
            Root<ProductPojo> c = q.from(ProductPojo.class);
            q.select(c);
            TypedQuery<ProductPojo> q2 = em.createQuery(q);
            List<ProductPojo> products = q2.getResultList();
            return products;
        }
        catch (Exception e) {
            return null;
        }
    }
    /**
     * get product by id
     * @param prodId int
     * @return ProductPojo
     */
    public ProductPojo getProductById(int prodId) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProductPojo> q1 = cb.createQuery(ProductPojo.class);
            Root<ProductPojo> root = q1.from(ProductPojo.class);
            q1.where(cb.equal((root.get(ProductPojo_.id)),prodId));
            
            TypedQuery<ProductPojo> tq = em.createQuery(q1);
            ProductPojo product = tq.getSingleResult();
            return product;
        }
        catch (Exception e) {
            return null;
        }
    }
    /**
     * delete product by id
     * @param custId int
     * @return boolean
     */
    @Transactional
    public boolean deleteProduct(int custId) {
        ProductPojo cust = em.find(ProductPojo.class, custId);
        boolean delete = false;
        try {
            if(cust != null) {
                em.remove(cust);
                delete= true;
            }
            return delete;

        }catch(Exception e){

            delete= false;
            return delete;
        }
    }
    
    /**
     * update product by id
     * @param id int
     * @param updatedcustomer productpojo
     */
    @Transactional
    public void updateProductById(int id, ProductPojo product) {
        ProductPojo cust = em.find(ProductPojo.class, id);
        if(cust!=null) {
            cust.setDescription(product.getDescription());
            cust.setSerialNo(product.getSerialNo());
            em.merge(cust);
        }
    }
    /**
     * insert product
     * @param newCustomer  ProductPojo
     * @return ProductPojo
     */
    @Transactional
    public ProductPojo persistProduct(ProductPojo product) {
//        ProductPojo p = em.find(ProductPojo.class, product.getId());
//        S
        
        
//        ProductPojo newProduct =new ProductPojo();
//        newProduct.setSerialNo(product.getSerialNo());
//        newProduct.setDescription(product.getDescription());
//        newProduct.setStores(product.getStores());
        
        em.persist(product);
        em.flush();
        return product;
    }
    
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // STORE
    // ---------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    
    /**
     * get all stores
     * @return List<StorePojo>
     */
    public List<StorePojo> getAllStores() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<StorePojo> q = cb.createQuery(StorePojo.class);
            Root<StorePojo> c = q.from(StorePojo.class);
            q.select(c);
            TypedQuery<StorePojo> q2 = em.createQuery(q);
            List<StorePojo> stores = q2.getResultList();
            return stores;
        }
        catch (Exception e) {
            return null;
        }
    }
    /**
     * get store by id
     * @param id int
     * @return StorePojo
     */
    public StorePojo getStoreById(int id) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<StorePojo> q1 = cb.createQuery(StorePojo.class);
            Root<StorePojo> root = q1.from(StorePojo.class);
            q1.where(cb.equal((root.get(StorePojo_.id)),id));
            
            TypedQuery<StorePojo> tq = em.createQuery(q1);
            StorePojo product = tq.getSingleResult();
            return product;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    /**
     * delete store
     * @param custId int
     * @return boolean
     */
    @Transactional
    public boolean deleteStore(int custId) {
        StorePojo cust = em.find(StorePojo.class, custId);
        boolean delete = false;
        try {
            if(cust != null) {
                em.remove(cust);
                delete= true;
            }
            return delete;

        }catch(Exception e){

            delete= false;
            return delete;
        }
    }
    
    /**
     * update store by id
     * @param id int
     * @param updatedcustomer StorePojo
     */
    @Transactional
    public void updateStoreById(int id, StorePojo store) {
        StorePojo cust = em.find(StorePojo.class, id);
        if (cust != null) {
            cust.setStoreName(store.getStoreName());
            em.merge(cust);
        }
    }
    /**
     * inster a store
     * @param store StorePojo
     * @return StorePojo
     */
    @Transactional
    public StorePojo persistStore(StorePojo store) {
        em.persist(store);
        em.flush();
        return store;
    }
    
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // ORDER
    // ---------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    /*
    
    public OrderPojo getAllOrders ... getOrderbyId ... build Orders with OrderLines ...
     
    */
    /**
     * get all orders
     * @return List<OrderPojo>
     */
    public List<OrderPojo> getAllOrders() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<OrderPojo> q = cb.createQuery(OrderPojo.class);
            Root<OrderPojo> c = q.from(OrderPojo.class);
            q.select(c);
            TypedQuery<OrderPojo> q2 = em.createQuery(q);
            List<OrderPojo> orders = q2.getResultList();
            return orders;
        }
        catch (Exception e) {
            return null;
        }
    }
    /**
     * get order by id
     * @param orderId int
     * @return OrderPojo
     */
    public OrderPojo getOrderById(int orderId) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<OrderPojo> q1 = cb.createQuery(OrderPojo.class);
            Root<OrderPojo> root = q1.from(OrderPojo.class);
            q1.where(cb.equal((root.get(OrderPojo_.id)), orderId));
            
            TypedQuery<OrderPojo> tq = em.createQuery(q1);
            OrderPojo order = tq.getSingleResult();
            return order;
        }
        catch (Exception e) {
            return null;
        }
    }
    /**
     * insert order
     * @param newCustomer
     * @return OrderPojo
     */
    @Transactional
    public OrderPojo persistOrder(OrderPojo order) {
        em.persist(order);
        em.flush();
        return order;
    }
    /**
     * delete order by id
     * @param custId int
     * @return boolean
     */
    @Transactional
    public boolean deleteOrderById(int custId) {
        OrderPojo cust = em.find(OrderPojo.class, custId);
        boolean delete = false;
        try {
            if(cust != null) {
                em.remove(cust);
                delete= true;
            }
            return delete;

        }catch(Exception e){

            delete= false;
            return delete;
        }
    }
    /**
     * update order by id
     * @param id int
     * @param updatedcustomer
     */
    @Transactional
    public void updateOrderById(int id, OrderPojo order) {
        OrderPojo cust = em.find(OrderPojo.class, id);
        if(cust!=null) {
            cust.setDescription(order.getDescription());
            em.merge(cust);
        }
    }
    
    
    
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // CUSTOMER ORDER
    // ---------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    
    /**
     * insert customer order
     * @param id int
     * @param newOrder OrderPojo
     * @return CustomerPojo
     */
    @Transactional
    public CustomerPojo persistCustomerOrder(int id, OrderPojo newOrder) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
        if(cust!=null) {
            cust.addOrders(newOrder);
            em.persist(newOrder);
            em.flush();
            em.merge(cust);
        }
        return cust;
    }
    /**
     * get customer all orders
     * @param id int
     * @return List<OrderPojo>
     */
    @Transactional
    public List<OrderPojo> getCustomerAllOrders(int id) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
        return cust.getOrders();
    }
    /**
     * get customer order by id
     * @param id int
     * @param orderid int
     * @return OrderPojo
     */
    @Transactional
    public OrderPojo getCustomerOrderById (int id, int orderid) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
        OrderPojo order = em.find(OrderPojo.class, orderid);
        CustomerPojo owner=order.getOwningCustomer();
        if (owner!=null &owner.equals(cust))
            return order;
        return null;
    }
    
    /**
     * update Customer Order By Id
     * @param id int
     * @param orderid int
     * @param neworder
     * @return OrderPojo
     */
    @Transactional
    public OrderPojo updateCustomerOrderById(int id, int orderid, OrderPojo neworder) {
        OrderPojo order = getCustomerOrderById (id,orderid);
        if (order!=null) {
            order.setDescription(neworder.getDescription());
            em.merge(order);
        }
        return order;
    }
    /**
     * delete Customer Order By Id
     * @param id int
     * @param orderid int
     * @return OrderPojo
     */
    @Transactional
    public OrderPojo deleteCustomerOrderById(int id, int orderid) {
        OrderPojo order = getCustomerOrderById (id,orderid);
        if(order!=null) {
            em.remove(order);
            return order;
        }
        return null;
    }
    
    
    
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // BILLING ADDRESS
    // ---------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    /**
     * get billing addredd by customer id
     * @param id int
     * @return BillingAddressPojo
     */
    public BillingAddressPojo getBillingAddressByCustomerId(int id) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
        if(cust !=null) {
            return (BillingAddressPojo)cust.getBillingAddress();
        }
        return null;
        
    }
    
    /**
     * persist BillingAddress for Customer Id
     * @param id int
     * @param addr BillingAddressPojo
     * @return CustomerPojo
     */
    @Transactional
    public CustomerPojo persistBillingAddressforCustomerId(int id, BillingAddressPojo addr) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
        if(cust !=null) {
            em.persist(addr);
            em.flush();
            
            cust.setBillingAddress(addr);
            em.merge(cust);
            return cust;
        }
        return null;
       
       
    }

    /**
     * delete BillingAddress By Customer Id
     * @param id int
     */
    @Transactional
    public void deleteBillingAddressByCustomerId(int id) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
       
        if(cust !=null) {
            BillingAddressPojo addr = (BillingAddressPojo)cust.getBillingAddress();
            try {
                if (addr != null) {
                    BillingAddressPojo bAddr = em.find(BillingAddressPojo.class, addr.getId());
                    if(bAddr !=null) {
                        cust.setBillingAddress(null);
                        em.merge(cust);
                   
                        em.remove(bAddr);
                    }
                }
            }
            catch (Exception e) {
               
            }
        }
     
    }

    /**
     * update Billing Address By Customer Id
     * @param id int
     * @param updatedAddr BillingAddressPojo
     */
    @Transactional
    public void updateBillingAddressByCustomerId(int id, BillingAddressPojo updatedAddr) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
        if(cust!=null) {
            BillingAddressPojo addr = (BillingAddressPojo)cust.getBillingAddress();
            if (addr != null) {
                addr.setAlsoShipping(updatedAddr.isAlsoShipping());
                addr.setCity(updatedAddr.getCity());
                addr.setCountry(updatedAddr.getCountry());
                addr.setPostal(updatedAddr.getPostal());
                addr.setState(updatedAddr.getState());
                addr.setStreet(updatedAddr.getStreet());
                em.merge(addr);
            }
        }
       
    }
    
 // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // SHIPPING ADDRESS
    // ---------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    
    /**
     * get Shipping Address By Customer Id
     * @param id int
     * @return ShippingAddressPojo
     */
    public ShippingAddressPojo getShippingAddressByCustomerId(int id) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
        if(cust !=null) {
            return (ShippingAddressPojo)cust.getShippingAddress();
        }
        return null;
        
    }

    /**
     * persist Shipping Address for Customer Id
     * @param id int
     * @param addr ShippingAddressPojo
     * @return CustomerPojo
     */
    @Transactional
    public CustomerPojo persistShippingAddressforCustomerId(int id, ShippingAddressPojo addr) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
        if(cust !=null) {
            em.persist(addr);
            em.flush();
            
            cust.setShippingAddress(addr);
            em.merge(cust);
            return cust;
        }
        return null;
       
       
        }
    /**
     * delete Shipping Address By Customer Id
     * @param id int
     */
    @Transactional
    public void deleteShippingAddressByCustomerId(int id) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
       
        if(cust !=null) {
            ShippingAddressPojo addr = (ShippingAddressPojo)cust.getShippingAddress();
            try {
                if (addr != null) {
                    ShippingAddressPojo bAddr = em.find(ShippingAddressPojo.class, addr.getId());
                    if(bAddr !=null) {
                        cust.setShippingAddress(null);
                        em.merge(cust);
                   
                        em.remove(bAddr);
                    }
                }
            }
            catch (Exception e) {
               
            }
        }
     
    }
    /**
     * update Shipping Address By Customer Id
     * @param id int
     * @param updatedAddr ShippingAddressPojo
     */
    @Transactional
    public void updateShippingAddressByCustomerId(int id, ShippingAddressPojo updatedAddr) {
        CustomerPojo cust = em.find(CustomerPojo.class, id);
        if(cust!=null) {
            ShippingAddressPojo addr = (ShippingAddressPojo)cust.getShippingAddress();
            if (addr != null) {
                addr.setCity(updatedAddr.getCity());
                addr.setCountry(updatedAddr.getCountry());
                addr.setPostal(updatedAddr.getPostal());
                addr.setState(updatedAddr.getState());
                addr.setStreet(updatedAddr.getStreet());
                em.merge(addr);
            }
        }
       
    }
    
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------
    // orderline
    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------------
    
    /**
     * get Customer OrderLines
     * @param id int
     * @param orderid int
     * @return List<OrderLinePojo>
     */
    public List<OrderLinePojo> getCustomerOrderLines(int id, int orderid) {
        OrderPojo order = getCustomerOrderById(id, orderid);
        if(order !=null) {
            List<OrderLinePojo> line = order.getOrderlines();
            return line;
        }
        return null;
       
    }
    /**
     * get Customer OrderLine By Id
     * @param id int
     * @param orderid int
     * @param orderlineid int
     * @return OrderLinePojo
     */
    public OrderLinePojo getCustomerOrderLineById(int id, int orderid, int orderlineid) {
        List<OrderLinePojo> lines = getCustomerOrderLines(id, orderid);
        
        if(lines != null) {
           for (int i = 0; i<lines.size(); i++) {
               if (lines.get(i).getPk().getOrderLineNo() == orderlineid) {
                   return lines.get(i);
               }
           }
        }
        return null;
    }
    
 /**
  * persist OrderLine
  * @param id int
  * @param orderid int
  * @param line OrderLinePojo
  * @return OrderPojo
  */
 @Transactional
 public OrderPojo persistOrderLine(int id, int orderid, OrderLinePojo line) {
     TypedQuery<OrderPojo> findorder = em.createQuery("select c from Order c where c.id=:q", OrderPojo.class).setParameter("q", orderid);
     OrderPojo order = findorder.getSingleResult();

     TypedQuery<ProductPojo> findProduct = em.createQuery("select c from Product c where c.id=:q", ProductPojo.class).setParameter("q", line.getProduct().getId());
         ProductPojo product = findProduct.getSingleResult();

         OrderLinePojo p = new OrderLinePojo();
         p.setAmount(line.getAmount());
         p.setOwningOrder(order);
 
         p.setProduct(product);
         OrderLinePk pk = new OrderLinePk();
         pk.setOrderLineNo(line.getPk().getOrderLineNo());
         p.setPk(pk);
         
         order.addOrderline(p);
         em.persist(p);
         em.flush();

         return order;
     }
 
     /**
      * update OrderLine By Id
      * @param id int
      * @param orderid int
      * @param orderlineid int
      * @param line OrderLinePojo
      * @return OrderLinePojo
      */
     @Transactional
    public OrderLinePojo updateOrderLineById(int id, int orderid, int orderlineid, OrderLinePojo line) {

         TypedQuery<OrderLinePojo> findOrderLines = em.createQuery("select c from Orderline c where c.pk.orderLineNo=:q1 and c.pk.owningOrderId=:q2",
             OrderLinePojo.class).setParameter("q1", orderlineid).setParameter("q2", orderid);
         OrderLinePojo ps = findOrderLines.getSingleResult();
         ps.setAmount(line.getAmount());
         ps.setProduct(line.getProduct());
         em.persist(ps);
         
         return ps;

    }
     
     /**
      * delete OrderLine By Id
      * @param id int
      * @param orderid int
      * @param orderlineid int
      */
    @Transactional
    public void deleteOrderLineById(int id, int orderid, int orderlineid) {
        TypedQuery<OrderLinePojo> findOrderLines = em.createQuery("select c from Orderline c where c.pk.orderLineNo=:q1 and c.pk.owningOrderId=:q2",
            OrderLinePojo.class).setParameter("q1", orderlineid).setParameter("q2", orderid);
            OrderLinePojo p2 = findOrderLines.getSingleResult();
            em.remove(p2);
            em.flush();
    }
    
//    @Transactional
//    public StorePojo[] persistStoreList(int id, StorePojo[] list) {
//        ProductPojo p = em.find(ProductPojo.class, id);
//        for (int i=0;i<list.length;i++) {
//            p.getStores().add(list[i]);
//        }
//        em.persist(p);
//        return list;
//    }

}