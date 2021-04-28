package za.co.dariel.deap.endpointsecurity.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import za.co.dariel.deap.endpointsecurity.entities.EmployeeEntity;

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeEntity, String>{

    EmployeeEntity findByKeycloakId(String userId);

}
