package com.intern.carrentalrestapi;

import com.intern.carrentalrestapi.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CarController {

    @Autowired
    private CarRepository carRepository;


    @GetMapping(path="/cars")
    public List<Car> getAllCars(){
        return carRepository.findAll();
    }


    @GetMapping(path="/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable(value = "id") Long carId)
        throws ResourceNotFoundException {


        Car car = carRepository.findById(carId)
                    .orElseThrow(() -> new ResourceNotFoundException("Car with id " + carId + " not found." ));

        return ResponseEntity.ok().body(car);
    }
/*
    @PostMapping(path="/cars")
    public @ResponseBody String addCar(@RequestParam String brand, @RequestParam String model){
        Car car = new Car();
        car.setBrand(brand);
        car.setModel(model);
        car.setIsRented(Boolean.FALSE);

        carRepository.save(car);
        return "Car added.";
    }
*/
    @PostMapping(path="/cars")
    public Car addCar(@RequestBody Car car){

        return carRepository.save(car);

    }

    @PutMapping(path="/cars/rent/{id}")
    public ResponseEntity<Car> rentCar(@PathVariable(value = "id") Long carId) throws ResourceNotFoundException{
        Car car = carRepository.findById(carId)
                    .orElseThrow(() -> new ResourceNotFoundException("Car with id " + carId + " not found." ));

        if(car.getIsRented()) throw new ResourceNotFoundException("Car with id " + carId + " is currently rented.");
        else{
            car.setIsRented(Boolean.TRUE);
            final Car rentedCar = carRepository.save(car);
            return ResponseEntity.ok(rentedCar);
        }
    }

    @PutMapping(path="/cars/return/{id}")
    public ResponseEntity<Car> returnCar(@PathVariable(value = "id") Long carId) throws ResourceNotFoundException{
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car with id " + carId + " not found." ));

        if(!car.getIsRented()) throw new ResourceNotFoundException("Car with id " + carId + " isn't currently rented.");
        else{
            car.setIsRented(Boolean.FALSE);
            final Car rentedCar = carRepository.save(car);
            return ResponseEntity.ok(rentedCar);
        }
    }

    @DeleteMapping(path="/cars/{id}")
    public Map<String, Boolean> deleteCar(@PathVariable(value = "id") Long carId) throws ResourceNotFoundException{
        Car car = carRepository.findById(carId)
                    .orElseThrow(() -> new ResourceNotFoundException("Car with id " + carId + " not found."));

        carRepository.delete(car);
        Map<String, Boolean> response = new HashMap<>();

        response.put("deleted", Boolean.TRUE);
        return response;
    }

}
