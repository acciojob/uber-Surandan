package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.List;
import java.util.Optional;



@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
         customerRepository2.deleteById(customerId);

	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query

		Customer customer = customerRepository2.findById(customerId).get();
        TripBooking tripBooking = new TripBooking();
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setCustomer(customer);

		List<Driver> driverList = driverRepository2.findAll();
		Driver tripDriver = null;
		for(Driver driver : driverList) {
			if(driver.getCab().isAvailable() && tripDriver == null) tripDriver = driver;
			else {
                if(driver.getCab().isAvailable()) {
                    if (tripDriver != null && driver.getDriverId() < tripDriver.getDriverId()) tripDriver = driver;
                }
            }
		}
		if (tripDriver == null) throw new Exception("No cab available!");
		tripBooking.setDriver(tripDriver);

		Cab cab = tripDriver.getCab();

		tripBooking.setStatus(TripStatus.valueOf("CONFIRMED"));
        tripBooking.setBill(cab.getPerKmRate() * distanceInKm);
		return tripBookingRepository2.save(tripBooking);
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		Optional<TripBooking> optionalTripBooking = tripBookingRepository2.findById(tripId);
		if(!optionalTripBooking.isPresent()) return;
		TripBooking tripBooking = optionalTripBooking.get();
		tripBooking.setStatus(TripStatus.valueOf("CANCELLED"));

		tripBooking.getDriver().getCab().setAvailable(true);
		tripBooking.setDriver(null);
		tripBooking.setCustomer(null);
		tripBooking.setBill(0);
		tripBooking.setDistanceInKm(0);
		tripBooking.setFromLocation(null);
		tripBooking.setToLocation(null);
        tripBookingRepository2.save(tripBooking);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		Optional<TripBooking> optionalTripBooking = tripBookingRepository2.findById(tripId);
		if(!optionalTripBooking.isPresent()) return;
		TripBooking tripBooking = optionalTripBooking.get();
		tripBooking.setStatus(TripStatus.valueOf("COMPLETED"));
		tripBooking.getDriver().getCab().setAvailable(true);

		tripBookingRepository2.save(tripBooking);
	}
}
