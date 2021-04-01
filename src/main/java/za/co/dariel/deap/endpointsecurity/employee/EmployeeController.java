package za.co.dariel.deap.endpointsecurity.employee;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

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
