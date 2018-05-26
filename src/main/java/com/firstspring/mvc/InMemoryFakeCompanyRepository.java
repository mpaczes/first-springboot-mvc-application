package com.firstspring.mvc;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryFakeCompanyRepository implements CompanyRepository {
	
	// private List<Company> companies;
	
	private Map<String, Company> companies;
	
//	// @Autowired				// w SpringBoot 1.4 ta adnotacja jest opcjonalna
//	public InMemoryFakeCompanyRepository(CompanyGenerator companyGenerator) {
//		
//		companies = companyGenerator.generate();
//		
//	}
	
    public InMemoryFakeCompanyRepository(CompanyGenerator companyGenerator) {
        companies = companyGenerator
                .generate()
                .stream()
                .collect(
                        Collectors.toMap(
                                Company::getName,
                                Function.identity()));
    }

	@Override
	public List<Company> findAll() {

		// return new ArrayList<Company>(companies);
		
		return new ArrayList<>(companies.values());
		
	}

//	@Override
//	public Company findOne(String name) {
//		
//		return companies
//			.stream()
//			.filter(company -> Objects.equals(company.getName() , name))
//			.findFirst()
//			.orElseGet(() -> null);
//		
//	}
	
    @Override
    public Company findOne(String name) {
    	
        return companies.get(name);
        
    }
    
    @Override
    public Company save(Company company) {
    	
        return companies.put(company.getName(), company);
        
    }

}
