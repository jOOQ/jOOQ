package me.sangwon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

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
	private CustomerRepository_lee customerRepository;


	@Test
	public void selectCountTest() { //test for select count(*) from customer;
		
		int count = customerRepository.countcolumn(); //call method (customer table is already made)
		
		assertEquals(count,13); //it is same in customerRepository record's number 

	}
	@Test
	public void SelectTest_1() { //test for select query
		final CustomerDTO customerDTO = customerRepository
			.Select()
			.stream()
			.findFirst()
			.get(); 
		assertThat(customerDTO.getId()).isEqualTo(0); 
		
	}
	@Test
	public void SelectTest_2() {
		final CustomerDTO customerDTO = customerRepository
			.Select()
			.stream()
			.findFirst()
			.get(); 
		
		assertThat(customerDTO.getName()).isEqualTo("test"); // already inserted data
	}
	
	@Test
	public void SelectTest_3() {
		final CustomerDTO customerDTO = customerRepository
			.Select()
			.stream()
			.findFirst()
			.get(); 
		
		assertThat(customerDTO.getEmail()).isEqualTo("test@test"); // already inserted data
	}
	
	@Test
	public void SelectTest_4() {
		final CustomerDTO customerDTO = customerRepository
			.Select()
			.stream()
			.findFirst()
			.get(); 
		
		assertThat(customerDTO.getProducts()).hasSize(4);
	}
	@Test
	public void InsertTest_1() { //test for select query
		customerRepository.InsertInto("leewonjun", "lwjun0513@gmail.com"); //Inserting data - test for insert 
		
		final CustomerDTO customerDTO = customerRepository.SelectByname("leewonjun")
			.stream()
			.findFirst()
			.get();//
		
		assertEquals(customerDTO.getName(),"leewonjun"); //it is that data is correct

	}
	
	@Test
	public void InsertTest_2() { //test for select query
		customerRepository.InsertInto("leewonjun", "lwjun0513@gmail.com"); //Inserting data - test for insert 
		
		final CustomerDTO customerDTO = customerRepository.SelectByname("leewonjun")
			.stream()
			.findFirst()
			.get();//
	
		assertEquals(customerDTO.getEmail(),"lwjun0513@gmail.com"); //it is that data is correct
	}

	@Test
	public void SelectIndexTest_1() { //test for select by index query
		
		customerRepository.InsertInto("leewonjun", "lwjun0513@gmail.com"); //Inserting data - test for Select by index
		final CustomerDTO customerDTO = customerRepository.SelectIndex(14).get(); //index number is 14. because of 14 data already inserted. 
		
		assertThat(customerDTO.getId()).isEqualTo(14);//so id number is 3
		
	}
	@Test
	public void SelectIndexTest_2() { 
		
		customerRepository.InsertInto("leewonjun", "lwjun0513@gmail.com"); //Inserting data - test for Select by index
		final CustomerDTO customerDTO = customerRepository.SelectIndex(14).get(); //index number is 14. because of 14 data already inserted. 
		
		assertThat(customerDTO.getName()).isEqualTo("leewonjun"); //it is that data is correct
		
	}
	@Test
	public void SelectIndexTest_3() { 
		
		customerRepository.InsertInto("leewonjun", "lwjun0513@gmail.com"); //Inserting data - test for Select by index
		final CustomerDTO customerDTO = customerRepository.SelectIndex(14).get(); //index number is 14. because of 14 data already inserted. 
		
		assertThat(customerDTO.getEmail()).isEqualTo("lwjun0513@gmail.com"); //it is that data is correct
		
	}
	@Test
	public void SelectIndexTest_4() {
		
		customerRepository.InsertInto("leewonjun", "lwjun0513@gmail.com"); //Inserting data - test for Select by index
		final CustomerDTO customerDTO = customerRepository.SelectIndex(14).get(); //index number is 14. because of 14 data already inserted. 
		
		assertThat(customerDTO.getProducts()).hasSize(0); //has size is zero because of not product inserted
	}
	
	
	@Test
	public void DeleteRecordTest_1() { //test for delete query
		final CustomerDTO customerDTO =  customerRepository.DeleteRecord(0)
				.stream()
				.findFirst()
				.get(); 
		
		assertThat(customerDTO.getId()).isEqualTo(0); 
			
	}
	@Test
	public void DeleteRecordTest_2() { //test for delete query
		final CustomerDTO customerDTO =  customerRepository.DeleteRecord(0)
				.stream()
				.findFirst()
				.get();
		
		assertThat(customerDTO.getName()).isnotEqualTo("test"); // original data "test" is deleted and second data is become first data
		
	
	}
	@Test
	public void DeleteRecordTest_3() { //test for delete query
		final CustomerDTO customerDTO =  customerRepository.DeleteRecord(0)
				.stream()
				.findFirst()
				.get(); 
	
		assertThat(customerDTO.getEmail()).isnotEqualTo("test@test"); //original data "test@test" is deleted and second data is become first data
	}
	
	
}
