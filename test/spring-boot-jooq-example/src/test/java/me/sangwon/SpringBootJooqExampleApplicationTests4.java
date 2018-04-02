package me.sangwon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.jooq.Record3;
import org.jooq.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import me.sangwon.domain.tables.Customer;
import me.sangwon.dto.CustomerDTO;
import me.sangwon.repository.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootJooqExampleApplicationTests4 {

	@Autowired
	private CustomerRepository customerRepository;


	@Test
	public void selectCountTest() { //test for select count(*) from customer;
		
		int count = customerRepository.countcolumn(); //call method (customer table is already made)
		
		assertEquals(count,14); //it is same in customerRepository record's number 

	}
	@Test
	public void findAllTest_1() { //test for select query
		final CustomerDTO customerDTO = customerRepository
			.findAll()
			.stream()
			.findFirst()
			.get(); 
		assertThat(customerDTO.getId()).isEqualTo(0); 
		
	}
	@Test
	public void findAllTest_2() {
		final CustomerDTO customerDTO = customerRepository
			.findAll()
			.stream()
			.findFirst()
			.get(); 
		
		assertThat(customerDTO.getName()).isEqualTo("test0"); // already inserted data
	}
	
	@Test
	public void findAllTest_3() {
		final CustomerDTO customerDTO = customerRepository
			.findAll()
			.stream()
			.findFirst()
			.get(); 
		
		assertThat(customerDTO.getEmail()).isEqualTo("test0@test"); // already inserted data
	}
	
	@Test
	public void findAllTest_4() {
		final CustomerDTO customerDTO = customerRepository
			.findAll()
			.stream()
			.findFirst()
			.get(); 
		
		assertThat(customerDTO.getProducts()).hasSize(4);
	}
	@Test
	public void saveTest_1() { //test for select query
		customerRepository.save("leewonjun", "lwjun0513@gmail.com"); //Inserting data - test for insert 
		
		final CustomerDTO customerDTO = customerRepository.findByname("leewonjun")
			.stream()
			.findFirst()
			.get();//
		
		assertEquals(customerDTO.getName(),"leewonjun"); //it is that data is correct

	}
	
	@Test
	public void saveTest_2() { //test for select query
		customerRepository.save("leewonjun", "lwjun0513@gmail.com"); //Inserting data - test for insert 
		
		final CustomerDTO customerDTO = customerRepository.findByname("leewonjun")
			.stream()
			.findFirst()
			.get();//
	
		assertEquals(customerDTO.getEmail(),"lwjun0513@gmail.com"); //it is that data is correct
	}

	@Test
	public void findOneTest_1() { //test for select by index query
		
		customerRepository.save("leewonjun", "lwjun0513@gmail.com"); //Inserting data - test for Select by index
		final CustomerDTO customerDTO = customerRepository.findOne(14).get(); //index number is 14. because 14 data already inserted. 
		
		assertThat(customerDTO.getId()).isEqualTo(14);//so id number is 14
		
	}
	@Test
	public void findOneTest_2() { 
		
		customerRepository.save("leewonjun", "lwjun0513@gmail.com"); //Inserting data - test for Select by index
		final CustomerDTO customerDTO = customerRepository.findOne(14).get(); //index number is 14. because 14 data already inserted. 
		
		assertThat(customerDTO.getName()).isEqualTo("leewonjun"); //it is that data is correct
		
	}
	@Test
	public void findOneTest_3() { 
		
		customerRepository.save("leewonjun", "lwjun0513@gmail.com"); //Inserting data - test for Select by index
		final CustomerDTO customerDTO = customerRepository.findOne(14).get(); //index number is 14. because of data already inserted. 
		
		assertThat(customerDTO.getEmail()).isEqualTo("lwjun0513@gmail.com"); //it is that data is correct
		
	}
	@Test
	public void findOneTest_4() {
		
		customerRepository.save("leewonjun", "lwjun0513@gmail.com"); //Inserting data - test for Select by index
		final CustomerDTO customerDTO = customerRepository.findOne(14).get(); //index number is 14. because 14 data already inserted. 
		
		assertThat(customerDTO.getProducts()).hasSize(0); //has size is zero because of not product inserted
	}
	


	@Test
	public void DeleteRecordTest_1() { //test for delete query
		final CustomerDTO customerDTO =  customerRepository.DeleteRecord(0)
				.stream()
				.findFirst()
				.get();
		
		assertNotEquals(customerDTO.getName(),"test0");//original data "test@test" is deleted
		
	
	}
	@Test
	public void DeleteRecordTest_2() { //test for delete query
		final CustomerDTO customerDTO =  customerRepository.DeleteRecord(0)
				.stream()
				.findFirst()
				.get(); 
	
		assertNotEquals(customerDTO.getEmail(),"test0@test");//original data "test@test" is deleted
		
	}
	
	
}
