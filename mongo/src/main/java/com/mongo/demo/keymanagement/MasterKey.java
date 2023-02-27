package com.mongo.demo.keymanagement;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;

public class MasterKey {

    private static final int SIZE_MASTER_KEY = 96;
    private static final String MASTER_KEY_FILENAME = "C:\\Users\\maniy\\Documents\\master-key.txt";

    public static void main(String[] args) {
        new MasterKey().tutorial();
    }

    private void tutorial() {
        final byte[] masterKey = generateNewOrRetrieveMasterKeyFromFile(MASTER_KEY_FILENAME);
        System.out.println("Master Key: " + Arrays.toString(masterKey));
    }

    private byte[] generateNewOrRetrieveMasterKeyFromFile(String filename) {
        byte[] masterKey = new byte[SIZE_MASTER_KEY];
        try {
            retrieveMasterKeyFromFile(filename, masterKey);
            System.out.println("An existing Master Key was found in file \"" + filename + "\".");
        } catch (IOException e) {
            masterKey = generateMasterKey();
            saveMasterKeyToFile(filename, masterKey);
            System.out.println("A new Master Key has been generated and saved to file \"" + filename + "\".");
        }
        return masterKey;
    }

    private void retrieveMasterKeyFromFile(String filename, byte[] masterKey) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            fis.read(masterKey, 0, SIZE_MASTER_KEY);
        }
    }

    private byte[] generateMasterKey() {
        byte[] masterKey = new byte[SIZE_MASTER_KEY];
        new SecureRandom().nextBytes(masterKey);
        return masterKey;
    }

    private void saveMasterKeyToFile(String filename, byte[] masterKey) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(masterKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}