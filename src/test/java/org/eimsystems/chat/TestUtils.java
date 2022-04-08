package org.eimsystems.chat;

import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TestUtils {
    static String strAllowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final long startdate = TimeUnit.DAYS.toMillis(1);

    static String getRandString() {
        Random random = new Random(20);
        StringBuilder builder = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {
            builder.append(strAllowedCharacters.charAt(random.nextInt(strAllowedCharacters.length())));
        }
        return builder.toString();
    }

    static User getRandUser() {
        return new User(getRandString(), getRandString(), getRandString() + "@" + getRandString(), "" + new Random().nextInt(100),
                new Locale("en"), getRandDate(), getRandString(), new Random().nextInt());
    }



    static Date getRandDate() {
        return new Date(ThreadLocalRandom
                .current()
                .nextLong(startdate, new Date().getTime()));

    }

}
