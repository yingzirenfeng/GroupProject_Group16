/***************************************************************************f******************u************zz*******y**
 * File: ProductSerializer.java
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

import com.algonquincollege.cst8277.models.ProductPojo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
/**
 * product serializer class
 */
public class ProductSerializer extends StdSerializer<Set<ProductPojo>> implements Serializable {
    /**
     * declare serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * constuctor
     */
    public ProductSerializer() {
        this(null);
    }
    /**
     * constructor with parameter
     * @param t Class<Set<ProductPojo>>
     */
    public ProductSerializer(Class<Set<ProductPojo>> t) {
        super(t);
    }
    /**
     * override method for serializing product
     */
    @Override
    public void serialize(Set<ProductPojo> originalProducts, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
        
        Set<ProductPojo> hollowProducts = new HashSet<>();
        for (ProductPojo originalProduct : originalProducts) {
            // create a 'hollow' copy of the original Product entity
            ProductPojo hollowP = new ProductPojo();
            hollowP.setId(originalProduct.getId());
            hollowP.setDescription(originalProduct.getDescription());
            hollowP.setCreatedDate(originalProduct.getCreatedDate());
            hollowP.setUpdatedDate(originalProduct.getUpdatedDate());
            hollowP.setVersion(originalProduct.getVersion());
            hollowP.setSerialNo(originalProduct.getSerialNo());
            hollowP.setStores(null);
            hollowProducts.add(hollowP);
        }
        generator.writeObject(hollowProducts);
    }
}