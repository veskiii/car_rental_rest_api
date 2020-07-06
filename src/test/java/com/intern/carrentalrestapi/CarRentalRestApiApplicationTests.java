package com.intern.carrentalrestapi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarRentalRestApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarRentalRestApiApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testGetAllCars(){
		HttpHeaders httpHeaders = new HttpHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, httpHeaders);

		ResponseEntity<String> responseEntity = testRestTemplate
				.exchange(getRootUrl() + "/api/cars", HttpMethod.GET, httpEntity, String.class);

		Assert.assertNotNull(responseEntity.getBody());
	}

	@Test
	public void testGetCarById() {
		Car car = testRestTemplate.getForObject(getRootUrl() + "/api/cars/1", Car.class);
		System.out.println(car.getModel());
		Assert.assertNotNull(car);
	}

	@Test
	public void testAddCar() {
		Car car = new Car();
		car.setBrand("Ford");
		car.setModel("Focus");
		car.setIsRented(Boolean.FALSE);

		ResponseEntity<Car> responseEntity = testRestTemplate.postForEntity(getRootUrl() + "/api/cars", car, Car.class);
		Assert.assertNotNull(responseEntity);
		Assert.assertNotNull(responseEntity.getBody());
	}

	@Test
	public void testRentCar() {
		int id = 1;
		Car car = testRestTemplate.getForObject(getRootUrl() + "/api/cars/" + id, Car.class);
		car.setIsRented(Boolean.TRUE);

		testRestTemplate.put(getRootUrl() + "/api/cars/" + id, car);

		Car rentedCar = testRestTemplate.getForObject(getRootUrl() + "/api/cars/" + id, Car.class);
		Assert.assertNotNull(rentedCar);
	}

	@Test
	public void testReturnCar() {
		int id = 1;
		Car car = testRestTemplate.getForObject(getRootUrl() + "/api/cars/" + id, Car.class);
		car.setIsRented(Boolean.FALSE);

		testRestTemplate.put(getRootUrl() + "/api/cars/" + id, car);

		Car rentedCar = testRestTemplate.getForObject(getRootUrl() + "/api/cars/" + id, Car.class);
		Assert.assertNotNull(rentedCar);
	}


	@Test
	public void testDeleteCar() {
		int id = 2;
		Car car = testRestTemplate.getForObject(getRootUrl() + "/api/cars/" + id, Car.class);
		Assert.assertNotNull(car);

		testRestTemplate.delete(getRootUrl() + "/api/cars/" + id);

		try {
			car = testRestTemplate.getForObject(getRootUrl() + "/api/cars/" + id, Car.class);
		} catch (final HttpClientErrorException e) {
			Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
		}
	}
}