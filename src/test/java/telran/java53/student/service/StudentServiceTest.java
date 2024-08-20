package telran.java53.student.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;


import telran.java53.configuration.ServiceConfiguration;
import telran.java53.student.dao.StudentRepository;
import telran.java53.student.dto.StudentAddDto;
import telran.java53.student.dto.StudentDto;
import telran.java53.student.model.Student;

@ContextConfiguration(classes = ServiceConfiguration.class)
@SpringBootTest
public class StudentServiceTest {
	final long studentId = 1000;
	Student student;
	
	@Autowired
	ModelMapper modelMapper;
	
	@MockBean
	StudentRepository studentRepository;
	
	StudentService studentService;
	
	@BeforeEach
	void setUp() {
		student = new Student(studentId, "John", "1234");
        studentService = new StudentServiceImpl(studentRepository, modelMapper);
	}
	
	@Test
	void testAddStudent() {
		when(studentRepository.save(Mockito.any(Student.class))).thenReturn(student);
		StudentAddDto studentAddDto = new StudentAddDto(studentId, "John", "1234");
		assertThat(studentService.addStudent(studentAddDto)).isTrue();
		assertTrue(studentService.addStudent(studentAddDto));
	}
	
	@Test
	void testFindStudent() {
		when(studentRepository.findById(studentId)).thenReturn(Optional.ofNullable(student));
		StudentDto studentDto = studentService.findStudent(studentId);
		assertThat(studentDto).isNotNull();
		assertNotNull(studentDto);
		assertEquals(studentId, studentDto.getId());
	}

}
