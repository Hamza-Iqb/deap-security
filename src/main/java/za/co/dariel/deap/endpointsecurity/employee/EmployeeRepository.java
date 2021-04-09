package za.co.dariel.deap.endpointsecurity.employee;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, String>{

    @Transactional
    @Modifying      // to mark delete or update query
    @Query("DELETE FROM Employee e WHERE e.email = :email")       // it will delete all the record with specific name
    void deleteByEmail(@Param("email") String email);
}
