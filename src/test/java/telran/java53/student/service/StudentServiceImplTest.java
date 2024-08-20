package telran.java53.student.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;

import telran.java53.student.dao.StudentRepository;
import telran.java53.student.dto.ScoreDto;
import telran.java53.student.dto.StudentAddDto;
import telran.java53.student.dto.StudentDto;
import telran.java53.student.dto.StudentUpdateDto;
import telran.java53.student.dto.exceptions.StudentNotFoundException;
import telran.java53.student.model.Student;

import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    private ModelMapper modelMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        // Use ModelMapper
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
			.setMatchingStrategy(MatchingStrategies.STRICT)
			.setFieldMatchingEnabled(true)
			.setFieldAccessLevel(AccessLevel.PRIVATE);
        studentService = new StudentServiceImpl(studentRepository, modelMapper);
    }

    @Test
    void testAddStudentWhenStudentExists() {
        // Arrange
        StudentAddDto dto = new StudentAddDto(1L, "John", "password");
        when(studentRepository.findById(dto.getId())).thenReturn(Optional.of(new Student()));

        // Act
        Boolean result = studentService.addStudent(dto);

        // Assert
        assertFalse(result);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testAddStudentWhenStudentNotExists() {
        // Arrange
        StudentAddDto dto = new StudentAddDto(1L, "John", "password");
        when(studentRepository.findById(dto.getId())).thenReturn(Optional.empty());

        // Act
        Boolean result = studentService.addStudent(dto);

        // Assert
        assertTrue(result);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testFindStudentWhenStudentExists() {
        // Arrange
        Student student = new Student(1L, "John", "password");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Act
        StudentDto result = studentService.findStudent(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    void testFindStudentWhenStudentNotExists() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StudentNotFoundException.class, () -> studentService.findStudent(1L));
    }

    @Test
    void testRemoveStudent() {
        // Arrange
        Student student = new Student(1L, "John", "password");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Act
        StudentDto result = studentService.removeStudent(1L);

        // Assert
        assertNotNull(result);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateStudent() {
        // Arrange
        Student student = new Student(1L, "John", "password");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentUpdateDto updateDto = new StudentUpdateDto("NewName", null);

        // Act
        StudentAddDto result = studentService.updateStudent(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("NewName", result.getName());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testAddScore() {
        // Arrange
        Student student = new Student(1L, "John", "password");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        ScoreDto scoreDto = new ScoreDto("Math", 90);

        // Act
        Boolean result = studentService.addScore(1L, scoreDto);

        // Assert
        assertTrue(result);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testFindStudentsByName() {
        // Arrange
        Student student = new Student(1L, "John", "password");
        when(studentRepository.findByNameIgnoreCase("John")).thenReturn(Collections.singletonList(student).stream());

        // Act
        List<StudentDto> result = studentService.findStudentsByName("John");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    void testGetStudentsQuantitityByNames() {
        // Arrange
        Set<String> names = Set.of("John", "Jane");
        when(studentRepository.countByNameInIgnoreCase(names)).thenReturn(2L);

        // Act
        Long result = studentService.getStudentsQuantitityByNames(names);

        // Assert
        assertEquals(2L, result);
    }

    @Test
    void testGetStudentsByExamMinScore() {
        // Arrange
        Student student = new Student(1L, "John", "password");
        when(studentRepository.findByExamAndScoreGreaterThan("Math", 80)).thenReturn(Collections.singletonList(student).stream());

        // Act
        List<StudentDto> result = studentService.getStudentsByExamMinScore("Math", 80);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }
}

