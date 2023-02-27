package com.mongo.demo.handler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.BsonBinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongo.demo.entity.EncryptedPerson;
import com.mongo.demo.entity.Person;
import com.mongo.demo.entity.PersonEntityHelper;
import com.mongo.demo.repo.EncryptedPersonRepository;

/*
	Author: Visweshwar Ganesh
	created on 1/21/20
*/
@Component
public class PersonHandler {
	

	private static final Logger logger = LoggerFactory.getLogger(PersonHandler.class);
	@Autowired
	private EncryptedPersonRepository encryptedPersonRepository;

	@Autowired
	private PersonEntityHelper personEntityHelper;

	private void clean() {
		encryptedPersonRepository.deleteAll();
	}

	public void runApplication() {
		clean();
		// Create a couple of non encrypted persons
		Person p1 = new Person("Alice", "Smith", 113431222, "+1-114-114-1250", "B+");
		Person p2 = new Person("Bob", "Smith", 113771224, "+1-114-114-1251", "O+");

		// Encrypt the Person and save to EncryptedPerson
		EncryptedPerson ep1 = personEntityHelper.getEncrypedPerson(p1);
		EncryptedPerson ep2 = personEntityHelper.getEncrypedPerson(p2);
		// Save persons..
		encryptedPersonRepository.saveAll(Arrays.asList(new EncryptedPerson[] { ep1, ep2 }));

		// fetch all persons
		logger.debug("Persons found with findAll():");
		logger.debug("-------------------------------");

		List<Person> decryptedPersons = encryptedPersonRepository.findAll().stream()
				.map(ep -> personEntityHelper.getPerson(ep)).collect(Collectors.toList());

		for (Person person : decryptedPersons) {
			logger.debug(person.toString());
		}

		// fetch an individual customer
		logger.debug("Person found with findByFirstName('Alice'):");
		logger.debug("--------------------------------");

		EncryptedPerson findByFirstNamePerson = encryptedPersonRepository.findByFirstName("Alice");
		logger.info("findByFirstNamePerson Equals Alice Success: {}",
				findByFirstNamePerson.getFirstName().equals("Alice"));

		// For Find by SSN we have to first get the binary version of SSN
		BsonBinary data = personEntityHelper.getEncryptedSsn(113431222);
		BsonBinary data1 = personEntityHelper.getEncryptedSsn(113431222);
		EncryptedPerson findBySsn = encryptedPersonRepository.findBySsn(data);
		List<EncryptedPerson> findBySsns = encryptedPersonRepository.findBySsnLike(data1);
		logger.info("findBySsn equals Alice Success: {}",
				personEntityHelper.getPerson(findBySsn).getFirstName().equals("Alice"));

	}
}
