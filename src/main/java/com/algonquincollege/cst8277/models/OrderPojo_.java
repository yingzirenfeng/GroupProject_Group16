package com.algonquincollege.cst8277.models;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-12-05T22:30:10.506-0500")
@StaticMetamodel(OrderPojo.class)
public class OrderPojo_ extends PojoBase_ {
	public static volatile SingularAttribute<OrderPojo, String> description;
	public static volatile ListAttribute<OrderPojo, OrderLinePojo> orderlines;
	public static volatile SingularAttribute<OrderPojo, CustomerPojo> owningCustomer;
}
