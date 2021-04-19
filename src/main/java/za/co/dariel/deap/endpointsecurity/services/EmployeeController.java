package za.co.dariel.deap.endpointsecurity.services;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import za.co.dariel.deap.endpointsecurity.entities.EmployeeEntity;
import za.co.dariel.deap.endpointsecurity.models.EmployeeDto;
import za.co.dariel.deap.endpointsecurity.security.JWTUtil;
import za.co.dariel.deap.endpointsecurity.security.encryption.AES;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
public class EmployeeController {

	private final EmployeeService employeeService;
	private ModelMapper modelMapper;
	private final AES aes;
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	
	
	@RolesAllowed({"employee-user", "employee-admin"})
	@PostMapping(value = "employee/registration")
	public String registerNewEmployee(@RequestBody EmployeeDto employeeDto) {

		// Convert base 64 to String
		byte[] decodedBytes = Base64.getDecoder().decode(employeeDto.getPassword());
		String decodedPassword = new String(decodedBytes);

		// Decrypt String
		String decryptedString = aes.decrypt(decodedPassword);


		// convert DTO to entity
		EmployeeEntity employeeRequest = modelMapper.map(employeeDto, EmployeeEntity.class);

		EmployeeEntity employee = employeeService.addEmployee(employeeRequest);

		// convert entity to DTO
		EmployeeDto employeeResponse = modelMapper.map(employee, EmployeeDto.class);

		logger.info(employeeResponse.toString());
		return "Employee successfully added";
	}
	
	
	@RolesAllowed({"employee-user", "employee-admin"})
	@GetMapping("")
	public String home() {
		String body =
		        "<HTML><body> <a href=\"http://localhost:8081/employee/get\">View all employees</a></body></HTML>";
		    return ("Welcome!!!" + "\r" + body);
	}
	
	
	@RolesAllowed({"employee-user", "employee-admin"})
	@GetMapping("/logout")
	  public String logout(HttpServletRequest request) throws ServletException {
	    request.logout();
	    return "You are logged out";
	  }

	
	
	@RolesAllowed("employee-admin")
	@GetMapping("employee/get")
	public List<EmployeeDto> showAllEmployees() {

		return employeeService.getEmployees().stream().map(post -> modelMapper.map(post, EmployeeDto.class))
				.collect(Collectors.toList());
		
	}
	
	
	
	@RolesAllowed("employee-admin")
	@GetMapping("/token")
    public String getToken(){
		
		String token = JWTUtil.getJWTToken();
		
		 java.util.Base64.Decoder decoder = java.util.Base64.getUrlDecoder();
         String[] parts = token.split("\\."); // split out the "parts" (header, payload and signature)

         String headerJson = new String(decoder.decode(parts[0]));
         String payloadJson = new String(decoder.decode(parts[1]));
         String signatureJson = new String(decoder.decode(parts[2]));    
         
         return headerJson + "--------" + payloadJson + "--------" + signatureJson;
	    
    }
	
	
	
	@RolesAllowed("employee-admin")
	@GetMapping("employee/get/{username}")
	public EmployeeDto showSingleEmployee(@PathVariable("username") String username) {
		EmployeeEntity employee = employeeService.getSingleEmployee(username);

		// convert entity to DTO
		EmployeeDto employeepostResponse = modelMapper.map(employee, EmployeeDto.class);
		return employeepostResponse;
	}

	
	@RolesAllowed("employee-admin")
	@PutMapping("employee/update/{username}")
	public String updateEmployee(@PathVariable("username") String username, @RequestBody EmployeeDto employeeDto) {

		// convert DTO to Entity
		EmployeeEntity employeeRequest = modelMapper.map(employeeDto, EmployeeEntity.class);

		EmployeeEntity employee = employeeService.updateStudent(username, employeeRequest);

		// entity to DTO
		EmployeeDto employeeResponse = modelMapper.map(employeeDto, EmployeeDto.class);
		System.out.println(employeeResponse);

		return "Updated employee details";
	}

	
	
	@RolesAllowed("employee-admin")
	@DeleteMapping("employee/delete/{email}")
	public String deleteEmployee(@PathVariable("email") String email) {

		EmployeeEntity employee = employeeService.getSingleEmployee(email);


		employeeService.deleteEmployee(employee.getEmail());
		return "Successfully deleted employee";
	}

}
