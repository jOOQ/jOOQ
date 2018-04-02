package me.sangwon.repository;

import me.sangwon.domain.tables.Customer;
import me.sangwon.domain.tables.Product;
import me.sangwon.dto.CustomerDTO;
import me.sangwon.dto.ProductDTO;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.Table;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by wonjun on 2018-04-12
 */
@Repository
@Transactional(readOnly = true)
public class CustomerRepository_lee {

  private DSLContext dslContext;


  public CustomerRepository_lee(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Transactional


  public int countcolumn() { //select count(*) from CUSTOMER
	  return this.dslContext
			  .fetchCount(this.dslContext
					  .select()
					  .from(Customer.CUSTOMER));
  }

  public void Update(int id, String name, String email) { //update query
	  this.dslContext.update(Customer.CUSTOMER)
	  .set(Customer.CUSTOMER.NAME, Customer.CUSTOMER.EMAIL)
      .where(Customer.CUSTOMER.ID.eq(id))
      .execute();
  }
  
 
  public void InsertInto(String name, String email) { //insert record query
    this.dslContext.insertInto(Customer.CUSTOMER)
      .columns(Customer.CUSTOMER.NAME, Customer.CUSTOMER.EMAIL)
      .values(name, email)
      .execute();
  }

  public Collection<CustomerDTO> Select() { // select query
	    final Map<Record, Result<Record>> recordResultMap = this.dslContext.select().from(Customer.CUSTOMER)
	      .leftJoin(Product.PRODUCT)
	      .on(Customer.CUSTOMER.ID.eq(Product.PRODUCT.CUSTOMER_ID))
	      .fetch()
	      .intoGroups(Customer.CUSTOMER.fields());
	    return getCollect(recordResultMap).collect(toList());
	  }
  
  public Optional<CustomerDTO> SelectIndex(Integer seq) { //select by index query
    final Map<Record, Result<Record>> recordResultMap = this.dslContext.select().from(Customer.CUSTOMER)
      .leftJoin(Product.PRODUCT)
      .on(Customer.CUSTOMER.ID.eq(Product.PRODUCT.CUSTOMER_ID))
      .where(Customer.CUSTOMER.ID.eq(seq))
      .fetch()
      .intoGroups(Customer.CUSTOMER.fields());
    return getCollect(recordResultMap).findFirst();
  }

  public Collection<CustomerDTO> SelectByname(String name) { //select by name query
    final Map<Record, Result<Record>> recordResultMap = this.dslContext.select().from(Customer.CUSTOMER)
      .leftJoin(Product.PRODUCT)
      .on(Customer.CUSTOMER.ID.eq(Product.PRODUCT.CUSTOMER_ID))
      .where(Customer.CUSTOMER.NAME.eq(name))
      .fetch()
      .intoGroups(Customer.CUSTOMER.fields());
    return getCollect(recordResultMap).collect(toList());
  }
  
  public Collection<CustomerDTO> DeleteRecord(int id) { //delect query
		 this.dslContext.delete(Customer.CUSTOMER)
	      .where(Customer.CUSTOMER.ID.eq(id))
	      .execute();
		 
		  final Map<Record, Result<Record>> recordResultMap =  this.dslContext.select().from(Customer.CUSTOMER)
			  .leftJoin(Product.PRODUCT)
		      .on(Customer.CUSTOMER.ID.eq(Product.PRODUCT.CUSTOMER_ID))
		      .fetch()
		      .intoGroups(Customer.CUSTOMER.fields());
		  return getCollect(recordResultMap).collect(toList());
	  }

  private Stream<CustomerDTO> getCollect(Map<Record, Result<Record>> recordResultMap) {
    return recordResultMap
      .values()
      .stream()
      .map(records -> {
        final Record3<Integer, String, String> record3 = records.into(Customer.CUSTOMER.ID, Customer.CUSTOMER.NAME, Customer.CUSTOMER.EMAIL).get(0);
        final Integer customerId = record3.value1();
        final String name = record3.value2();
        final String email = record3.value3();
        List<ProductDTO> products = records.sortAsc(Customer.CUSTOMER.ID).into(ProductDTO.class)
          .stream()
          .filter(productDTO -> productDTO.getId() != null)
          .collect(toList());
        return new CustomerDTO(customerId, name, email, products);
      });
  }


}
