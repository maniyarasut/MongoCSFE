package com.mongo.demo.entity;

import org.bson.BsonBinary;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongo.demo.keymanagement.KMSHandler;
import com.mongodb.client.model.vault.DataKeyOptions;
import com.mongodb.client.model.vault.EncryptOptions;
import com.mongodb.client.vault.ClientEncryption;

@Component
public class PersonEntityHelper {
	@Autowired
	protected KMSHandler kmsHandler;

	private BsonBinary dataKeyId;

	PersonEntityHelper() {
		if (dataKeyId != null && kmsHandler != null)
			this.dataKeyId = kmsHandler.getClientEncryption().createDataKey("local", new DataKeyOptions());
	}

	public static final String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
	public static final String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";

	public EncryptedPerson getEncrypedPerson(Person p) {
		ClientEncryption clientEncryption = kmsHandler.getClientEncryption();
		EncryptedPerson ep = new EncryptedPerson(p.getFirstName(), p.getLastName());
		System.out.println(kmsHandler.getClientEncryption().toString());
		System.out.println(new BsonInt32(p.getSsn()));
		BsonBinary encryptedFieldValue = clientEncryption.encrypt(new BsonString("123456789"),
				new EncryptOptions("AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic").keyId(getData()));

		ep.setSsn(kmsHandler.getClientEncryption().encrypt(new BsonInt32(p.getSsn()),
				getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE).keyId(getData())));
		ep.setPhone(kmsHandler.getClientEncryption().encrypt(new BsonString(p.getPhone()),
				getEncryptOptions(RANDOM_ENCRYPTION_TYPE).keyId(getData())));
		ep.setBloodType(kmsHandler.getClientEncryption().encrypt(new BsonString(p.getBloodType()),
				getEncryptOptions(RANDOM_ENCRYPTION_TYPE).keyId(getData())));
		return ep;
	}

	public Person getPerson(EncryptedPerson ep) {

		Person p = new Person(ep.getFirstName(), ep.getLastName());
		p.setSsn(kmsHandler.getClientEncryption().decrypt(new BsonBinary(ep.getSsn().getType(), ep.getSsn().getData()))
				.asNumber().intValue());
		p.setPhone(kmsHandler.getClientEncryption()
				.decrypt(new BsonBinary(ep.getPhone().getType(), ep.getPhone().getData())).asString().getValue());
		p.setBloodType(kmsHandler.getClientEncryption()
				.decrypt(new BsonBinary(ep.getBloodType().getType(), ep.getBloodType().getData())).asString()
				.getValue());
		return p;

	}

	public BsonBinary getEncryptedSsn(int ssn) {
		return kmsHandler.getClientEncryption().encrypt(new BsonInt32(ssn),
				getEncryptOptions(DETERMINISTIC_ENCRYPTION_TYPE).keyId(getData()));
	}

	private EncryptOptions getEncryptOptions(String algorithm) {

		EncryptOptions encryptOptions = new EncryptOptions(algorithm);
		encryptOptions.keyId(new BsonBinary(kmsHandler.getEncryptionKeyUUID()));
		return encryptOptions;

	}

	private BsonBinary getData() {
		ClientEncryption clientEncryption = kmsHandler.getClientEncryption();
		BsonBinary data = this.dataKeyId == null?clientEncryption.createDataKey("local", new DataKeyOptions()): this.dataKeyId;
		this.dataKeyId=data;
		return data;

	}
}
