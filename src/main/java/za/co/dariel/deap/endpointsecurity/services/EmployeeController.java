package za.co.dariel.deap.endpointsecurity.services;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import za.co.dariel.deap.endpointsecurity.entities.EmployeeEntity;
import za.co.dariel.deap.endpointsecurity.models.EmployeeDto;
import za.co.dariel.deap.endpointsecurity.security.encryption.AES;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

	private final EmployeeService employeeService;
	private ModelMapper modelMapper;
	private final AES aes;

	@PostMapping(value = "/registration")
	public String registerNewEmployee(@RequestBody EmployeeDto employeeDto) {

		// Convert base 64 to String
		byte[] decodedBytes = Base64.getDecoder().decode(employeeDto.getPassword());
		String decodedPassword = new String(decodedBytes);

		// Decrypt String
		String decryptedString = aes.decrypt(decodedPassword);
		System.out.println(decodedPassword);

		// test
		String originalString = "howtodoinjava.com";
		String encryptedString = aes.encrypt(originalString);
		String decryptedStringg = aes.decrypt(encryptedString);
		System.out.println(originalString);
		System.out.println(encryptedString);
		System.out.println(decryptedStringg);

		// convert DTO to entity
		EmployeeEntity employeeRequest = modelMapper.map(employeeDto, EmployeeEntity.class);

		EmployeeEntity employee = employeeService.addEmployee(employeeRequest);

		// convert entity to DTO
		EmployeeDto employeeResponse = modelMapper.map(employee, EmployeeDto.class);

		System.out.println(employeeResponse);
		return "Employee successfully added";
	}

	@GetMapping("/test")
	public String testApi() {
		return "API IS WORKING!!!";
	}

	@GetMapping("/get")
	public List<EmployeeDto> showAllEmployees() {

		return employeeService.getEmployees().stream().map(post -> modelMapper.map(post, EmployeeDto.class))
				.collect(Collectors.toList());
	}

	@GetMapping("/get/{username}")
	public EmployeeDto showSingleEmployee(@PathVariable("username") String username) {
		EmployeeEntity employee = employeeService.getSingleEmployee(username);

		// convert entity to DTO
		EmployeeDto employeepostResponse = modelMapper.map(employee, EmployeeDto.class);
		return employeepostResponse;
	}

	@PutMapping("/update/{username}")
	public String updateEmployee(@PathVariable("username") String username, @RequestBody EmployeeDto employeeDto) {

		// convert DTO to Entity
		EmployeeEntity employeeRequest = modelMapper.map(employeeDto, EmployeeEntity.class);

		EmployeeEntity employee = employeeService.updateStudent(username, employeeRequest);

		// entity to DTO
		EmployeeDto employeeResponse = modelMapper.map(employeeDto, EmployeeDto.class);
		System.out.println(employeeResponse);

		return "Updated employee details";
	}

	@DeleteMapping("/delete/{email}")
	public String deleteEmployee(@PathVariable("email") String email) {

		EmployeeEntity employee = employeeService.getSingleEmployee(email);


		employeeService.deleteEmployee(employee.getEmail());
		return "Successfully deleted employee";
	}

}
