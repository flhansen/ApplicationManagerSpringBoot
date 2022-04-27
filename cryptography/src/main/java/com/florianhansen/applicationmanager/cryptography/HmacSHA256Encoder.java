package com.florianhansen.applicationmanager.cryptography;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class HmacSHA256Encoder implements PasswordEncoder {
	
	/**
	 * Generates a new random salt.
	 * @param length The length of the salt in bytes
	 * @return
	 */
	private byte[] generateRandomSalt(int length) {
		byte[] salt = new byte[length];
		
		try {
			SecureRandom.getInstanceStrong().nextBytes(salt);
		} catch (NoSuchAlgorithmException e) {
			// This machine does not support such kind of randomness, so use a
			// non-blocking method instead.
			SecureRandom random = new SecureRandom();
			random.nextBytes(salt);
		}
		
		return salt;
	}

	/**
	 * Calculates a salted hash.
	 * @param salt The salt
	 * @param password The password
	 * @return The combination of the salt and the hashed password.
	 */
	private byte[] calculateHash(byte[] salt, String password) {
		// Generate 256-bit hash
		final int hashLength = 256;
		final int iterations = 100000;

		KeySpec spec = new PBEKeySpec(
				password.toCharArray(),
				salt,
				iterations,
				hashLength);

		try {
			// Calculate the hash of the raw password
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			SecretKey secretKey = keyFactory.generateSecret(spec);
			byte[] passwordHash = secretKey.getEncoded();
			
			// Concatenate salt and hashed password
			byte[] combinedHash = new byte[salt.length + passwordHash.length];

			for (int i = 0; i < salt.length; i++)
				combinedHash[i] = salt[i];

			for (int i = 0; i < passwordHash.length; i++)
				combinedHash[salt.length + i] = passwordHash[i];
			
			// Return the final result, which is [salt ++ passwordHash]
			return combinedHash;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		// Something went wrong
		return null;
	}
	
	@Override
	public String encode(CharSequence rawPassword) {
		// Generate a new 128-bit salt
		byte[] salt = generateRandomSalt(128/8);
		String password = String.valueOf(rawPassword);

		byte[] hash = calculateHash(salt, password);
		return Base64.getEncoder().encodeToString(hash);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		// First decode the base64 string
		byte[] combinedHash = Base64.getDecoder().decode(encodedPassword);
		
		// Extract the salt from the decoded hash and transform the raw input
		byte[] salt = Arrays.copyOf(combinedHash, 16);
		String password = String.valueOf(rawPassword);

		// Recalculate the hash using the old salt and the password and compare
		// the result with the decoded input hash.
		byte[] hash = calculateHash(salt, password);
		return Arrays.equals(combinedHash, hash);
	}

}
