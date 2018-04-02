package me.sangwon;

import me.sangwon.dto.CustomerDTO;
import me.sangwon.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootJooqExampleApplicationTests {

	@Autowired
	private CustomerRepository customerRepository;
	
	// Find until a condition is met.
	@Test
	public void findAllTest1() {
		final CustomerDTO customerDTO = customerRepository.findAll().stream().findFirst().get();
		assertThat(customerDTO.getId()).isEqualTo(0);
	}
	@Test
	public void findAllTest2() {
		final CustomerDTO customerDTO = customerRepository.findAll().stream().findFirst().get();
		assertThat(customerDTO.getName()).isEqualTo("test0");
	}
	@Test
	public void findAllTest3() {
		final CustomerDTO customerDTO = customerRepository.findAll().stream().findFirst().get();
		assertThat(customerDTO.getEmail()).isEqualTo("test0@test.com");
	}
	@Test
	public void findAllTest4() {
		final CustomerDTO customerDTO = customerRepository.findAll().stream().findFirst().get();
		assertThat(customerDTO.getProducts()).hasSize(0);
	}
	
	// Test save method is working(save mean 'insert into')
	@Test
	public void saveTest1() {
		customerRepository.save("test", "test@test.com");
		final CustomerDTO customerDTO = customerRepository.findByname("test").stream().findFirst().get();
		assertThat(customerDTO.getName()).isEqualTo("test");
	}
	@Test
	public void saveTest2() {
		final CustomerDTO customerDTO = customerRepository.findByname("test").stream().findFirst().get();
		assertThat(customerDTO.getEmail()).isEqualTo("test@test.com");
	}


	// Finds a consumer with id equal to 1
	@Test
	public void findOneTest1() {
		final CustomerDTO customerDTO = customerRepository.findOne(1).get(); 
		assertThat(customerDTO.getId()).isEqualTo(1);
	}
	@Test
	public void findOneTest2() {
		final CustomerDTO customerDTO = customerRepository.findOne(1).get(); 
		assertThat(customerDTO.getName()).isEqualTo("sangwon");
	}
	@Test
	public void findOneTest3() {
		final CustomerDTO customerDTO = customerRepository.findOne(1).get(); 
		assertThat(customerDTO.getEmail()).isEqualTo("sangwon@test.com");
	}
	@Test
	public void findOneTest4() {
		final CustomerDTO customerDTO = customerRepository.findOne(1).get(); 
		assertThat(customerDTO.getProducts()).hasSize(4);
	}
}
