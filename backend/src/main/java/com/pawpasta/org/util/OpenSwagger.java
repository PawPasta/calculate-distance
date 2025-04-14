package com.pawpasta.org.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URI;


@Component
public class OpenSwagger implements CommandLineRunner {

    @Value("${swagger.url}")
    String url ;

    public void run (String... args) throws Exception {

        Thread.sleep(1000);

        if(Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            System.out.println("Please open the browser manually: " + url);
        }

    }
}
