package com.tb.common.uniqueIdGenerator;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class ShortIdGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int RANDOM_LENGTH = 4; // Extra random characters for more randomness

    public static String getNext() {
        // Step 1: Generate a random UUID and encode it with Base64
        UUID uuid = UUID.randomUUID();
        byte[] uuidBytes = uuid.toString().getBytes(StandardCharsets.UTF_8);
        String base64Uuid = Base64.getUrlEncoder().withoutPadding().encodeToString(uuidBytes).substring(0, 8); // Taking a portion

        // Step 2: Generate a short random alphanumeric string
        StringBuilder extraRandom = new StringBuilder(RANDOM_LENGTH);
        for (int i = 0; i < RANDOM_LENGTH; i++) {
            extraRandom.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        // Step 3: Combine the UUID part and the extra random part
        return base64Uuid + extraRandom.toString();
    }
}