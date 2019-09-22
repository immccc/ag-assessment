package com.immccc.assesement.delay;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class DelayUtils {

    private static final Random RANDOM = new Random();
    private static final int MAX_DELAY_IN_MS = 5000;

    @SneakyThrows(InterruptedException.class)
    public static void delayRandomly() {
        int delay = RANDOM.nextInt(MAX_DELAY_IN_MS);
        TimeUnit.MILLISECONDS.sleep(delay);
    }

}
