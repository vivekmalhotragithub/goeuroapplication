package com.goeuro.vivek.application.busroute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BusRouteAppApplication implements CommandLineRunner {

	@Autowired
	private BusRouteMapper busRouteMapper;

	public static void main(String[] args) {
		SpringApplication.run(BusRouteAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// check if a file is provided as commandline argument
		if(args.length != 1){
			throw new IllegalArgumentException("Please provide a valid route file!");
		}
		// load the bus route from file
		busRouteMapper.readBusRouteFile(args[0]);
		
	}
	
	
}
