package me.sangwon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "customer")
public class ProductDTO {
  private Integer id;
  private String productName;
  private CustomerDTO customer;
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}

}
