package me.sangwon;

import me.sangwon.dto.CustomerDTO;
import me.sangwon.repository.CustomerRepository;
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
		customerRepository.save(15, "testSave", "testSave@test.com");
		final CustomerDTO customerDTO = customerRepository.findByname("testSave").stream().findFirst().get();
		assertThat(customerDTO.getId()).isEqualTo(15);
	}
	
	@Test
	public void saveTest2() {
		final CustomerDTO customerDTO = customerRepository.findByname("testSave").stream().findFirst().get();
		assertThat(customerDTO.getName()).isEqualTo("testSave");
	}
	
	@Test
	public void saveTest3() {
		final CustomerDTO customerDTO = customerRepository.findByname("testSave").stream().findFirst().get();
		assertThat(customerDTO.getEmail()).isEqualTo("testSave@test.com");
	}
	
	// if you don't enter the ID, assign the ID in order. 
	@Test
	public void saveTest4() {
		customerRepository.save("test16", "test16@test.com");
		final CustomerDTO customerDTO = customerRepository.findByname("test16").stream().findFirst().get();
		assertThat(customerDTO.getId()).isEqualTo(16);
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
		customerRepository.save(14, "test123", "test123@test.com");
		final CustomerDTO customerDTO = customerRepository.findOne(14).get(); 
		assertThat(customerDTO.getEmail()).isEqualTo("test123@test.com");
	}
}
