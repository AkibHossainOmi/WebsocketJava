package com.tb.common.uniqueIdGenerator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class ShortIdGenerator {
    public static String getNext() {
        UUID uuid = UUID.randomUUID();
        byte[] uuidBytes = uuid.toString().getBytes(StandardCharsets.UTF_8);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(uuidBytes).substring(0, 12); // Truncate to desired length
    }
}
