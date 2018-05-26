package com.firstspring.mvc;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @RequestMapping(value = "companies")
@RequestMapping(value = "companies", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
@RestController
public class CompanyController {

	private final CompanyRepository companyRepository;
	
	public CompanyController(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	// wstrzykiwanie wartosci typu : ustawienia regionalne, strefa czasowa, cookie, naglowki zadania :
	@GetMapping
	public List<Company> findAll(
			Locale locale,				// ustawienia regionalne
			ZoneId zoneId,				// strefa czasowa
			@CookieValue("myCookie") String myCookie,			// cookie
			@RequestHeader Map<String, String> headers 			// wszystkie naglowki
	) {
		
		return companyRepository.findAll();
		
	}
	
//	@GetMapping(value = "/{companyName}")
//	public Company findOne(@PathVariable("companyName") String name) {
//		
//		return companyRepository.findOne(name);
//		
//	}
	
	@GetMapping(value = "/{companyName}", produces = MediaType.APPLICATION_XML_VALUE)
	public HttpEntity<Company> findOne(@PathVariable("companyName") String name) {
		
		Company company = companyRepository.findOne(name);
		
		if (company != null) {
			
			return ResponseEntity.ok(company);							// I spsoob wolania ResponseEntity
			
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);				// II spsoob wolania ResponseEntity
		
	}
				
	// @GetMapping(value = "/{companyName}/employees/{firstName}")
	@GetMapping(value = "/{companyName}/employees/{firstName:[a-zA-Z]+}")
	List<Employee> findCompanyEmployeesWithFirstName(
			@PathVariable("companyName") String companyName, @PathVariable("firstName") String employeeName) {
		
//		return findOne(companyName)
		return companyRepository.findOne(companyName)
				.getEmployees()
				.stream()
				.filter(employee -> Objects.equals(employee.getFirstName(), employeeName))
				.collect(Collectors.toList());
		
	}
	
	// @GetMapping("/{companyName}/employees/{employeeId}")
	@GetMapping("/{companyName}/employees/{employeeId:\\d+}")			// z wyrazeniami regularnymi
	Employee findCompanyemployeeWithId(@PathVariable String companyName, @PathVariable long employeeId) {
		
//		return findOne(companyName)
		return companyRepository.findOne(companyName)
				.getEmployees()
				.stream()
				.filter(employee -> employee.getId() == employeeId)
				.findAny()
				.orElse(null);				// jesli nie znajdziemy to zwracamy null
				
		
	}
	
	@GetMapping(value = "/{companyName}/employees/{lastName}/{firstName}")
	List<Employee> findCompanyEmployeesWithLastNameAndFirstName(@PathVariable Map<String, String> pathVariable) {				// !! mapa z parametrami ze sciezki (fajny patent)
		
//		return findOne(pathVariable.get("companyName"))
		return companyRepository.findOne(pathVariable.get("companyName"))
				.getEmployees()
				.stream()
				.filter(employee -> Objects.equals(employee.getLastName(), pathVariable.get("lastName")))
				.filter(employee -> Objects.equals(employee.getFirstName(), pathVariable.get("firstName")))
				.collect(Collectors.toList());
		
	}
	
	@PostMapping("{companyName}/employees")
	public Employee addEmployee(
			@PathVariable String companyName, 
			@RequestParam(value = "firstName", required = true) String firstName, 
			@RequestParam String lastName, 
			@RequestParam(required = false) BigDecimal salary) {
		
		Company original = companyRepository.findOne(companyName);
		
		List<Employee> employees = new ArrayList<>(original.getEmployees());
		
		Employee employee = new Employee(Employee.getNextEmployeeId(), firstName, lastName, salary);
		
		employees.add(employee);
		
		Company newCompany = new Company(original.getName(), employees);
		
		companyRepository.save(newCompany);
		
		return employee;
		
	}
	
	@PostMapping(
			value = "{companyName}/employees/create",
			produces = MediaType.APPLICATION_XML_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	List<Employee> addEmployees(@PathVariable String companyName, @RequestBody AddEmployeesRequest request) {
		
		Company original = companyRepository.findOne(companyName);
		
		List<Employee> employees = new ArrayList<>(original.getEmployees());
		
		List<Employee> newEmployees = createEmployees(request.getEmployees());
		employees.addAll(newEmployees);
		
		Company newCompany = new Company(original.getName(), employees);
		
		companyRepository.save(newCompany);
		
		return newEmployees;
		
	}
	
	/*
	 * jako @RequestBody w Postmanie mozna wyslac cos takiego :
	 * 
{
	
	"employees" : [
		
		{
			"firstName" : "Jerzy",
			"lastName" : "Biedziak",
			"salary" : 2355.75
		},
		
		{
			"firstName" : "Janina",
			"lastName" : "Kloszukowa",
			"salary" : 7355.45
		}
		
		]
}
	 */

	private List<Employee> createEmployees(List<Employee> employees) {

		return employees
				.stream()
				.map(employee -> 
					new Employee(
							employee.getNextEmployeeId(), 
							employee.getFirstName(), 
							employee.getLastName(), 
							employee.getSalary()))
				.collect(Collectors.toList());

	}
	
}
