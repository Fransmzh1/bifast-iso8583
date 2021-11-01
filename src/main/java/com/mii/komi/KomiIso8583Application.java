package com.mii.komi;

import org.jpos.q2.Q2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 *
 * @author Erwin Sugianto Santoso - MII
 */
@SpringBootApplication
public class KomiIso8583Application implements CommandLineRunner {

    @Autowired
    Environment env;

    public static void main(String[] args) {
        SpringApplication.run(KomiIso8583Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Q2 q2 = new Q2(env.getProperty("jpos.config.location"));
        Thread thread = new Thread(q2);
        thread.start();
    }

}
