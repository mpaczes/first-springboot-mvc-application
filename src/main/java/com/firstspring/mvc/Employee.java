package com.firstspring.mvc;

import java.math.BigDecimal;

public class Employee {

	private Long id;
	
	private String firstName;

	private String lastName;

	private BigDecimal salary;
	
	private static long lastEmployeeId = 0;
	
	public Employee() {
		// domyslny konstruktor jest potzrebny dla com.fasterxml.jackson
		// potzrebne sa tez settery !!
	}

	public Employee(String firstName, String lastName, BigDecimal salary) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.salary = salary;
	}
	
    Employee(Long id, String firstName, String lastName, BigDecimal salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }
    
	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}
	
    static long getNextEmployeeId() {
        return lastEmployeeId++;
    }

}
