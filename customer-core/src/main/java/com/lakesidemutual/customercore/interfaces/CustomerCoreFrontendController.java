package com.lakesidemutual.customercore.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lakesidemutual.customercore.interfaces.configuration.CustomerCoreRemoteSOAPProxy;
import com.lm.ccore.GetCustomerResponse;

@Controller
public class CustomerCoreFrontendController {
	
	@Autowired
	private CustomerCoreRemoteSOAPProxy customerWSClient; 
	
    @GetMapping("/customercorefe")
    public String getCustomerById(@RequestParam(name="id", required=true) String id, Model model) {

        model.addAttribute("id", id);
		GetCustomerResponse response = customerWSClient.getCustomerbyId(id);
		
		if(response==null || response.getCustomer()==null)
			System.err.println("Customer not found in sample data: " + id);
		else {
	        model.addAttribute("name", response.getCustomer().getName());	
		}
		        
        return "CustomerCoreFrontEndView"; // this name is the name of the Thymeleaf template (view) 
    }
}