package za.co.dariel.deap.endpointsecurity.services;

import java.util.List;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import za.co.dariel.deap.endpointsecurity.entities.EmployeeEntity;
import za.co.dariel.deap.endpointsecurity.models.EmployeeDto;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
	
	private final EmployeeService employeeService;
	private ModelMapper modelMapper;

	@PostMapping(value = "/registration")
	public String registerNewEmployee(@RequestBody EmployeeDto employeeDto) {
		
		// convert DTO to entity
		EmployeeEntity employeeRequest = modelMapper.map(employeeDto, EmployeeEntity.class);
		
		EmployeeEntity employee = employeeService.addEmployee(employeeRequest);

		// convert entity to DTO
		EmployeeDto employeeResponse = modelMapper.map(employee, EmployeeDto.class);

		System.out.println(employeeResponse);
		return "EmployeeEntity successfully added";
	}

	@GetMapping("/test")
	public String testApi(){
		return "API IS WORKING!!!";
	}

	@GetMapping("/get")
	public List<EmployeeEntity> showAllEmployees(){
		
		return employeeService.getEmployees();
	}
	
	@GetMapping("/get/{username}")
	public EmployeeEntity showSingleEmployee(@PathVariable("username") String username) {
		return employeeService.getSingleEmployee(username);
	}
	
	
	
	@PutMapping("/update/{username}")
	public void updateEmployee(@PathVariable("username") String username, 
			@RequestParam(required=false) String firstName, 
			@RequestParam(required=false) String lastName) {
		
		employeeService.updateStudent(username, firstName, lastName);
	}

}
