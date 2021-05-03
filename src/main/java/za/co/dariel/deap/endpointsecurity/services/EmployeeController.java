package za.co.dariel.deap.endpointsecurity.services;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import za.co.dariel.deap.endpointsecurity.entities.EmployeeEntity;
import za.co.dariel.deap.endpointsecurity.models.EmployeeDto;
import za.co.dariel.deap.endpointsecurity.security.encryption.AES;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@CrossOrigin(value = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {

	private final EmployeeService employeeService;
	private ModelMapper modelMapper;
	private final AES aes;

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	private KeycloakService keyClockService;


	@ApiOperation(value = "Register a new employee", notes = "endpoint to register a new employee in the DEAP-demo application")
	@RolesAllowed({"employee-user", "employee-admin"})
	@PostMapping(value = "registration")
	public String registerNewEmployee(@RequestBody EmployeeDto employeeDto) {

		// Convert base 64 to String
		byte[] decodedBytes = Base64.getDecoder().decode(employeeDto.getPassword());
		var decodedPassword = new String(decodedBytes);

		// Decrypt String
		var decryptedString = aes.decrypt(decodedPassword);


		// convert DTO to entity
		EmployeeEntity employeeRequest = modelMapper.map(employeeDto, EmployeeEntity.class);

		EmployeeEntity employee = employeeService.addEmployee(employeeRequest);

		// convert entity to DTO
		EmployeeDto employeeResponse = modelMapper.map(employee, EmployeeDto.class);


		logger.info(employeeResponse.toString());
		return "Employee successfully added";
	}

	@ApiOperation(value = "returns the username of the loged in keycloak user", notes = "returns the username of the loged in keycloak user in the DEAP-demo application", response = EmployeeEntity.class)
	@RolesAllowed({"employee-user", "employee-admin"})
	@GetMapping("name")
	public List<EmployeeEntity> returnUserName(HttpServletRequest request){
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		String username = principal.getAccount().getKeycloakSecurityContext().getToken().getName();

		List<EmployeeEntity> list = new ArrayList<>();
		list.add(new EmployeeEntity(username, username, username, username));

		return list;
	}

	@ApiOperation(value = "returns all the users in the keycloak DB", notes = "returns all the users in the keycloak DB in the DEAP-demo application", response = EmployeeEntity.class)
	@RolesAllowed("employee-admin")
	@GetMapping("get")
	public List<EmployeeEntity> showAllEmployees() {

		return keyClockService.getUserInKeyCloak();

//		return employeeService.getEmployees().stream().map(post -> modelMapper.map(post, EmployeeDto.class))
//		.collect(Collectors.toList());

	}




	@ApiOperation(value = "returns a single users in the keycloak DB", notes = "returns a single users in the keycloak DB according to its username in the DEAP-demo application", response = EmployeeEntity.class)
	@RolesAllowed("employee-admin")
	@GetMapping("get/{username}")
	public EmployeeDto showSingleEmployee(@PathVariable("username") String username) {
		EmployeeEntity employee = employeeService.getSingleEmployee(username);

		// convert entity to DTO
		EmployeeDto employeepostResponse = modelMapper.map(employee, EmployeeDto.class);
		return employeepostResponse;
	}

	@ApiOperation(value = "updates a single users in the keycloak DB", notes = "updates a single users in the keycloak DB according to its user ID in the DEAP-demo application")
	@RolesAllowed("employee-admin")
	@PutMapping("update/{userId}")
	public String updateEmployee(@PathVariable("userId") String userId, @RequestBody EmployeeDto employeeDto) {

//		// convert DTO to Entity
		EmployeeEntity employeeRequest = modelMapper.map(employeeDto, EmployeeEntity.class);
//
//		EmployeeEntity employee = employeeService.updateStudent(username, employeeRequest);
//
//		// entity to DTO
//		EmployeeDto employeeResponse = modelMapper.map(employeeDto, EmployeeDto.class);
//		System.out.println(employeeResponse);

		keyClockService.updateUserInKeyCloak(userId, employeeRequest);

		return "Updated employee details";
	}


	@ApiOperation(value = "deletes a single users in the keycloak DB", notes = "deletes a single users in the keycloak DB according to its keycloak user ID in the DEAP-demo application")
	@RolesAllowed("employee-admin")
	@DeleteMapping("delete/{userId}")
	public String deleteEmployee(@PathVariable("userId") String userId) {

		keyClockService.deleteUserInKeyCloak(userId);
		employeeService.deleteEmployee(userId);
		return "Successfully deleted employee";
	}

}
