package za.co.dariel.deap.endpointsecurity.employee;


import java.util.Base64;
import java.util.List;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import za.co.dariel.deap.endpointsecurity.security.encryption.AES;


@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeController{

	private final EmployeeService employeeService;
	private final AES aes;

	@PostMapping(value = "/registration")
	public String registerNewEmployee(@RequestBody Employee employee) {

		//encryption secret key
		final String keyString = "NcRfUjXn2r5u7x!A%D*G-KaPdSgVkYp3";

		//Convert base 64 to String
		byte[] decodedBytes = Base64.getDecoder().decode(employee.getPassword());
		String decodedPassword = new String(decodedBytes);

		//Decrypt String
		String decryptedString = aes.decrypt(decodedPassword, keyString);
		System.out.println(decodedPassword);

		final String secretKey = keyString;
		String originalString = "howtodoinjava.com";
		String encryptedString = aes.encrypt(originalString, secretKey) ;
		String decryptedStringg = aes.decrypt(encryptedString, secretKey) ;
		System.out.println(originalString);
		System.out.println(encryptedString);
		System.out.println(decryptedStringg);

		//set employee password
		employee.setPassword(decryptedString);

		//store employee
		//employeeService.addEmployee(employee);
		return "Employee successfully added";
	}

	@GetMapping("/test")
	public String testApi(){
		return "API IS WORKING!!!";
	}

	@GetMapping("/get")
	public List<Employee> showAllEmployees(){
		
		return employeeService.getEmployees();
	}
	
	@GetMapping("/get/{username}")
	public Employee showSingleEmployee(@PathVariable("username") String username) {
		return employeeService.getSingleEmployee(username);
	}
	
	
	
	@PutMapping("/update/{username}")
	public void updateEmployee(@PathVariable("username") String username, 
			@RequestParam(required=false) String firstName, 
			@RequestParam(required=false) String lastName) {
		
		employeeService.updateStudent(username, firstName, lastName);
	}

}
