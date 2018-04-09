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
public class SpringBootJooqExampleApplicationTests5 {
	
	@Autowired
	private CustomerRepository customerRepository;
	/*
	 *  transactionPairTest
	 */
	
	// TransactionPair1
	// save and removeOne
	// After remove, find id but NoElement --> expected NoSuchElementException.class
	@Test(expected = NoSuchElementException.class)
	public void saveAndRemoveOne() {
		customerRepository.save(14,"saveman", "saveman@naver.com");
		CustomerDTO customerDTO = customerRepository.findOne(14).get();
		assertThat(customerDTO.getId()).isEqualTo(14);
		assertThat(customerDTO.getName()).isEqualTo("saveman");
		assertThat(customerDTO.getEmail()).isEqualTo("saveman@naver.com");
		customerRepository.removeOne(14);
		customerDTO = customerRepository.findOne(14).get();
	}
	// 
	// TransactionPair2
	// save and modify
	// After modify, john -> jerry / john6813@naver.com -> jerry5211@naver.com
	@Test
	public void saveAndModify(){
		customerRepository.save(15, "john", "john6813@naver.com");
		CustomerDTO customerDTO = customerRepository.findOne(15).get();
		assertThat(customerDTO.getId()).isEqualTo(15);
		assertThat(customerDTO.getName()).isEqualTo("john");
		assertThat(customerDTO.getEmail()).isEqualTo("john6813@naver.com");
		customerRepository.modify(15, "jerry", "jerry5211@naver.com");
		customerDTO = customerRepository.findOne(15).get();
		assertThat(customerDTO.getId()).isEqualTo(15);
		assertThat(customerDTO.getName()).isNotEqualTo("john");
		assertThat(customerDTO.getEmail()).isNotEqualTo("john6813@naver.com");
		assertThat(customerDTO.getName()).isEqualTo("jerry");
		assertThat(customerDTO.getEmail()).isEqualTo("jerry5211@naver.com");
	}
	
	// TransactionPair3
	// remove and modify
	// After remove, execute modify test6 --> newValue / test6@test --> newValue@test
	@Test
	public void removeOneAndModify(){
		customerRepository.removeOne(15); //remove
		CustomerDTO customerDTO = customerRepository.findOne(10).get();
		assertThat(customerDTO.getId()).isEqualTo(10);
		assertThat(customerDTO.getName()).isEqualTo("test6");
		assertThat(customerDTO.getEmail()).isEqualTo("test6@test");
		customerRepository.modify(10, "newValue", "newValue@test");
		customerDTO = customerRepository.findOne(10).get();
		assertThat(customerDTO.getId()).isEqualTo(10);
		assertThat(customerDTO.getName()).isEqualTo("newValue");
		assertThat(customerDTO.getEmail()).isEqualTo("newValue@test");
	}
	
	// TransactionPair4
	// save and removeGT
	// After save person20,21,22,23, execute removeGT(21)
	// result --> can find person20,21 but, can't find person22,23 deleted
	@Test(expected = NoSuchElementException.class)
	public void saveAndRemoveGT(){
		customerRepository.save(20,"person20","person20@example");
		customerRepository.save(21,"person21","person21@example");
		customerRepository.save(22,"person22","person22@example");
		customerRepository.save(23,"person23","person23@example");
		customerRepository.removeGt(21);
		CustomerDTO customerDTO = customerRepository.findOne(20).get();
		assertThat(customerDTO.getId()).isEqualTo(20);
		assertThat(customerDTO.getName()).isEqualTo("person20");
		assertThat(customerDTO.getEmail()).isEqualTo("person20@example");
		
		customerDTO = customerRepository.findOne(21).get();
		assertThat(customerDTO.getId()).isEqualTo(21);
		assertThat(customerDTO.getName()).isEqualTo("person21");
		assertThat(customerDTO.getEmail()).isEqualTo("person21@example");
		
		customerDTO = customerRepository.findOne(22).get();
	}
	
}
