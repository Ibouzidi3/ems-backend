package net.javaguides.ems.service;


import net.javaguides.ems.dto.EmployeeDto;
import net.javaguides.ems.entity.Employee;
import net.javaguides.ems.mapper.EmployeeMapper;
import net.javaguides.ems.repository.EmployeeRepository;
import net.javaguides.ems.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeServiceUnitTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDto employeeDto;


    @BeforeEach
    public void setup(){

        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Cena")
                .email("john@gmail.com")
                .build();
        employeeDto = EmployeeMapper.mapToEmployeeDto(employee);
    }


    @Test
    @Order(1)
    public void saveEmployeeTest(){

        // precondition
        given(employeeRepository.save(any(Employee.class))).willReturn(employee);

        //action
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);

        // verify the output
        System.out.println(savedEmployee);
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @Order(2)
    public void getEmployeeById(){
        // precondition
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // action
        EmployeeDto existingEmployee = employeeService.getEmployeeById(employee.getId());

        // verify
        System.out.println(existingEmployee);
        assertThat(existingEmployee).isNotNull();

    }


    @Test
    @Order(3)
    public void getAllEmployee(){
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Sam")
                .lastName("Curran")
                .email("sam@gmail.com")
                .build();

        // precondition
        given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));

        // action
        List<EmployeeDto> employeeList = employeeService.getAllEmployees();

        // verify
        System.out.println(employeeList);
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isGreaterThan(1);
    }

    @Test
    @Order(4)
    public void updateEmployee(){

        // precondition
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        employee.setEmail("max@gmail.com");
        employee.setFirstName("Max");
        given(employeeRepository.save(employee)).willReturn(employee);

        // action
        EmployeeDto updatedEmployee = employeeService.updateEmployee(employee.getId(), EmployeeMapper.mapToEmployeeDto(employee));

        // verify
        System.out.println(updatedEmployee);
        assertThat(updatedEmployee.getEmail()).isEqualTo("max@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Max");
    }

    @Test
    @Order(5)
    public void deleteEmployee(){

        // precondition
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        willDoNothing().given(employeeRepository).deleteById(employee.getId());

        // action
        employeeService.deleteEmployee(employee.getId());

        // verify
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }


}