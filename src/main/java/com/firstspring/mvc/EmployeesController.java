package com.firstspring.mvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "employees")
@RestController
public class EmployeesController {

	// @RequestMapping(value = "/getEmployees", method = RequestMethod.POST)
	// @PostMapping(value = "/getEmployees")
	@RequestMapping(value = "/getEmployees", method = { RequestMethod.POST, RequestMethod.GET })
	public List<Employee> getEmployees() {
		
		List<Employee> result = new ArrayList<Employee>();
		
		result.add(new Employee("Jan", "Nowak", new BigDecimal("1250.50")));
		result.add(new Employee("Ola", "Kowalczyk", new BigDecimal("2750.50")));
		result.add(new Employee("Karol", "Dlubak", new BigDecimal("3450.50")));
		
		return result;
		
	}
	
	// @RequestMapping(value = "/findAny", method = RequestMethod.GET)
	@GetMapping(value = "/findAny")
	public Employee findAny() {
		
		return new Employee("Jan", "Nowak", new BigDecimal("1250.50"));
		
	}
	
}
