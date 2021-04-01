package za.co.dariel.deap.endpointsecurity.employee;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200" , maxAge = 3600)// allow cors for front end
@RestController
@AllArgsConstructor
@RequestMapping("employee")
public class EmployeeController {
	
	private final EmployeeService employeeService;

	@PostMapping("/registration")
	public String registerNewEmployee(@RequestBody Employee employee) {
		employeeService.addEmployee(employee);
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
