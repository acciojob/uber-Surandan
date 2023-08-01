package com.driver.services.impl;

import com.driver.model.Cab;
import com.driver.repository.CabRepository;
import com.driver.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Driver;
import com.driver.repository.DriverRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class DriverServiceImpl implements DriverService {

	@Autowired
	DriverRepository driverRepository3;

	@Autowired
	CabRepository cabRepository3;

	@Override
	public void register(String mobile, String password) {
		//Save a driver in the database having given details and a cab with ratePerKm as 10 and availability as True by default.
		Driver driver = new Driver();
		driver.setMobile(mobile);
		driver.setPassword(password);



		Cab cab = new Cab();
		cab.setAvailable(true);
		cab.setPerKmRate(10);
//		driver.setCab(cab);

		driver = driverRepository3.save(driver);
		cab.setDriver(driver);
        cabRepository3.save(cab);

		//	Cab cab = new Cab();
//		cab.setPerKmRate(10);
//		cab.setAvailable(true);
//		cab.setDriver(driver);
//		cabRepository3.save(cab);
//		driver.setTripBookingList(new ArrayList<>());
//     	driverRepository3.save(driver);

//		driver.setTripBookingList(new ArrayList<>());
//		Driver savedDriver = driverRepository3.save(driver);
//		Cab cab = new Cab();
//		cab.setPerKmRate(10);
//		cab.setAvailable(true);
//		cab.setDriver(driver);
//		cabRepository3.save(cab);
	}

	@Override
	public void removeDriver(int driverId){
		// Delete driver without using deleteById function
		Optional<Driver> optionalDriver = driverRepository3.findById(driverId);
		if(!optionalDriver.isPresent()) return;
		Driver driver = optionalDriver.get();
		driverRepository3.delete(driver);
	}

	@Override
	public void updateStatus(int driverId){
		//Set the status of respective car to unavailable
		Driver driver = driverRepository3.getOne(driverId);
		Cab cab = driver.getCab();
		cab.setAvailable(false);
		cabRepository3.save(cab);
	}
}
