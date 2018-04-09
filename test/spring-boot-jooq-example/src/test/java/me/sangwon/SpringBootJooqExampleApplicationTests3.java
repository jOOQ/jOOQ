
package me.sangwon;

import me.sangwon.dto.CustomerDTO;
import me.sangwon.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootJooqExampleApplicationTests3 {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Test
	public void removeOneTest1_1() {
			customerRepository.removeOne(3);
	}
	@Test
	public void removeOneTest1_2() {
		try{
			customerRepository.findOne(3).get();
		}
		catch(NoSuchElementException expected){
            expected.printStackTrace();
		}
	}
	@Test
	public void removeOneTest2() {
		try{
			customerRepository.removeOne(2);
		}
		catch(DataIntegrityViolationException expected){
            expected.printStackTrace();
		}
	}
	@Test
	public void removeGtTest1_1() {
			customerRepository.removeGt(11);
	}
	@Test
	public void removeGtTest1_2() {
		try{
			customerRepository.findOne(12).get();
		}
		catch(NoSuchElementException expected){
            expected.printStackTrace();
		}
	}
	@Test
	public void removeGtTest2_1() {
		try{
			customerRepository.removeGt(0);
		}
		catch(DataIntegrityViolationException expected){
            expected.printStackTrace();
		}
	}
	@Test
	public void removeGtTest2_2() {
		final CustomerDTO customerDTO = customerRepository.findOne(0).get();
		assertThat(customerDTO.getName()).isEqualTo("test0");
	}
	@Test
	public void modifyTest() {
		customerRepository.modify(0, "TEST", "TEST@TEST");
		final CustomerDTO customerDTO = customerRepository.findOne(0).get();
		assertThat(customerDTO.getEmail()).isEqualTo("TEST@TEST");
	}
	@Test
	public void modifyTest2() {
		customerRepository.modify(2, "KEVIN", "KEVIN@TEST.COM");
		final CustomerDTO customerDTO = customerRepository.findOne(2).get();
		assertThat(customerDTO.getEmail()).isEqualTo("KEVIN@TEST.COM");
	}
	
}
