package telran.java53.student.service;

import java.util.List;
import java.util.Set;

import telran.java53.student.dto.ScoreDto;
import telran.java53.student.dto.StudentAddDto;
import telran.java53.student.dto.StudentDto;
import telran.java53.student.dto.StudentUpdateDto;

public interface StudentService {
	Boolean addStudent(StudentAddDto studentAddDto);

	StudentDto findStudent(Long id);

	StudentDto removeStudent(Long id);

	StudentAddDto updateStudent(Long id, StudentUpdateDto studentUpdateDto);

	Boolean addScore(Long id, ScoreDto scoreDto);

	List<StudentDto> findStudentsByName(String name);

	Long getStudentsQuantitityByNames(Set<String> names);
	
	List<StudentDto> getStudentsByExamMinScore(String exam, Integer minScore);

}
