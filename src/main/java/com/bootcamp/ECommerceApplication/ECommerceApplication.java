package com.bootcamp.ECommerceApplication;

import jdk.internal.misc.Unsafe;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Field;

@SpringBootApplication
public class ECommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}


	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
//
//	@Bean
//	public static void disableWarning() {
//		try {
//			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
//			theUnsafe.setAccessible(true);
//			Unsafe u = (Unsafe) theUnsafe.get(null);
//
//			Class cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
//			Field logger = cls.getDeclaredField("logger");
//			u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
//		} catch (Exception e) {
//			// ignore
//		}
//	}

//	public static void disableWarning() {
//		System.err.close();
//		System.setErr(System.out);
//	}

}
