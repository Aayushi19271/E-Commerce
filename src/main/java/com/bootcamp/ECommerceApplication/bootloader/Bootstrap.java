package com.bootcamp.ECommerceApplication.bootloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class Bootstrap implements ApplicationRunner {
    @Autowired
    private BootstrapData bootstrapData;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        bootstrapData.addRoles();
        bootstrapData.addAdmin();
        bootstrapData.addData();
    }

}




























