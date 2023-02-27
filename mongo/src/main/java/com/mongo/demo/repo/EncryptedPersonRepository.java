package com.mongo.demo.repo;

import java.util.List;

import org.bson.BsonBinary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongo.demo.entity.EncryptedPerson;

@Repository
public interface EncryptedPersonRepository extends MongoRepository<EncryptedPerson, String> {
	public EncryptedPerson findByFirstName(String firstName);

	public EncryptedPerson findBySsn(BsonBinary ssn);

	public List<EncryptedPerson> findBySsnLike(BsonBinary ssn);
}