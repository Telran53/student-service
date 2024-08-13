package telran.java53.student.service;

import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java53.student.dao.StudentRepository;
import telran.java53.student.dto.ScoreDto;
import telran.java53.student.dto.StudentAddDto;
import telran.java53.student.dto.StudentDto;
import telran.java53.student.dto.StudentUpdateDto;
import telran.java53.student.dto.exceptions.StudentNotFoundException;
import telran.java53.student.model.Student;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	final StudentRepository studentRepository;
	final ModelMapper modelMapper;

	@Override
	public Boolean addStudent(StudentAddDto studentAddDto) {
		if (studentRepository.findById(studentAddDto.getId()).isPresent()) {
			return false;
		}
		Student student = modelMapper.map(studentAddDto, Student.class);
		studentRepository.save(student);
		return true;
	}

	@Override
	public StudentDto findStudent(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
		return modelMapper.map(student, StudentDto.class);
	}

	@Override
	public StudentDto removeStudent(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
		studentRepository.deleteById(id);
		return new StudentDto(id, student.getName(), student.getScores());
	}

	@Override
	public StudentAddDto updateStudent(Long id, StudentUpdateDto studentUpdateDto) {
		Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
		if (studentUpdateDto.getName() != null) {
			student.setName(studentUpdateDto.getName());
		}
		if (studentUpdateDto.getPassword() != null) {
			student.setPassword(studentUpdateDto.getPassword());
		}
		studentRepository.save(student);
		return new StudentAddDto(student.getId(), student.getName(), student.getPassword());
	}

	@Override
	public Boolean addScore(Long id, ScoreDto scoreDto) {
		Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
		boolean res = student.addScore(scoreDto.getExamName(), scoreDto.getScore());
		studentRepository.save(student);
		return res;
	}

	@Override
	public List<StudentDto> findStudentsByName(String name) {
		return studentRepository.findByNameIgnoreCase(name)
				.map(s -> new StudentDto(s.getId(), s.getName(), s.getScores()))
				.toList();
	}

	@Override
	public Long getStudentsQuantitityByNames(Set<String> names) {
		return studentRepository.countByNameInIgnoreCase(names);
	}

	@Override
	public List<StudentDto> getStudentsByExamMinScore(String exam, Integer minScore) {
		return studentRepository.findByExamAndScoreGreaterThan(exam, minScore)
				.map(s -> new StudentDto(s.getId(), s.getName(), s.getScores()))
				.toList();
	}

}
