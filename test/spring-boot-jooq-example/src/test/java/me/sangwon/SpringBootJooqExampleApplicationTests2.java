package me.sangwon;

import me.sangwon.dto.CustomerDTO;
import me.sangwon.dto.ProductDTO;
import me.sangwon.repository.CustomerRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootJooqExampleApplicationTests2 {

	@Autowired
	private CustomerRepository customerRepository;
	
	// find by name that is in the table
	@Test
	public void findBynameTest1() {
		final CustomerDTO customerDTO = customerRepository.findByname("sangwon").stream().findFirst().get();
		assertThat(customerDTO.getName()).isEqualTo("sangwon");
	}
	
	@Test
	public void findBynameTest2() {
		final CustomerDTO customerDTO = customerRepository.findByname("kevin").stream().findFirst().get();
		assertThat(customerDTO.getName()).isEqualTo("kevin");
	}
	

	// save = insert into(id, name, email)
	@Test
	public void saveTest1() {
		customerRepository.save(3, "test", "test@test.com");
		final CustomerDTO customerDTO = customerRepository.findByname("test").stream().findFirst().get();
		assertThat(customerDTO.getId()).isEqualTo(3);
	}
	
	@Test
	public void saveTest2() {
		final CustomerDTO customerDTO = customerRepository.findByname("test").stream().findFirst().get();
		assertThat(customerDTO.getName()).isEqualTo("test");
	}
	
	@Test
	public void saveTest3() {
		final CustomerDTO customerDTO = customerRepository.findByname("test").stream().findFirst().get();
		assertThat(customerDTO.getEmail()).isEqualTo("test@test.com");
	}
	
	// if you don't enter the ID, assign the ID in order. 
	@Test
	public void saveTest4() {
		customerRepository.save("test5", "test5@test.com");
		final CustomerDTO customerDTO = customerRepository.findByname("test5").stream().findFirst().get();
		assertThat(customerDTO.getId()).isEqualTo(5);
	}
	
	// If you enter the same ID again, you can't insert it.
	@Test(expected = DuplicateKeyException.class)
	public void saveTest5() {
		customerRepository.save(3, "test6", "test6@test.com");
		final CustomerDTO customerDTO = customerRepository.findByname("test6").stream().findFirst().get();
		assertThat(customerDTO.getId()).isEqualTo(3);
	}
	
	
	@Test
	public void findOne1() {
		customerRepository.save(4, "test123", "test123@test.com");
		final CustomerDTO customerDTO = customerRepository.findOne(4).get(); 
		assertThat(customerDTO.getEmail()).isEqualTo("test123@test.com");
	}
}
