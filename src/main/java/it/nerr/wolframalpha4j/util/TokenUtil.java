package it.nerr.wolframalpha4j.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class TokenUtil {

    /**
     * Extracts the bot user's ID from the token used to authenticate requests.
     *
     * @param token The bot token used to authenticate requests.
     * @return The bot user's ID.
     */
    public static long getSelfId(String token) {
        try {
            return Long.parseLong(new String(Base64.getDecoder()
                    .decode(token.split("\\.")[0]), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid token, make sure you're using the token from the " +
                    "developer portal Bot section and not the application client secret or public key.", e);
        }
    }

}
