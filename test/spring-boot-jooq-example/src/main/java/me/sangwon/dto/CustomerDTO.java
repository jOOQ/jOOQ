package me.sangwon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
  private Integer id;
  private String name;
  private String email;
  private Collection<ProductDTO> products = new HashSet<>();
public CustomerDTO(Integer customerId, String name2, String email2, List<ProductDTO> products2) {
	// TODO Auto-generated constructor stub
	id=customerId;
	name=name2;
	email=email2;
	products=products2;
}
public Integer getId() {
	// TODO Auto-generated method stub
	return id;
}
public String getName() {
	// TODO Auto-generated method stub
	return name;
}
public String getEmail() {
	// TODO Auto-generated method stub
	return email;
}
public Collection<ProductDTO> getProducts() {
	// TODO Auto-generated method stub
	return products;
}
}
