package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findStudentByEmail(String email); //Query-Method: Select * from student Where Email = @Email
    @Query("SELECT s FROM Student s WHERE s.firstName = ?1 AND s.age >= ?2") //override
    List<Student> findStudentsByFirstNameEqualsAndAgeEqualsAndIsGreaterThanEqual(String firstName, Integer age); //Select * from student where first_name = ? and age >= ? (Two value list)

    /*@Query(
            value = "SELECT * FROM student WHERE first_name = ?1 AND age >= ?2",
            nativeQuery = true) //nativeQuery
    List<Student> selectStudentWhereFirstNameAndAgeGreaterOrEqualNative(String firstName, Integer age);*/

    @Query(
            value = "SELECT * FROM student WHERE first_name = :firstName AND age >= :age",
            nativeQuery = true) //parameter
    List<Student> selectStudentWhereFirstNameAndAgeGreaterOrEqualNative(
            @Param("firstName") String firstName,
            @Param("age") Integer age);

    List<Student> findStudentsByFirstNameEqualsAndAgeEquals(String firstName, Integer age); // Select * from student Where first_name = ? and age = ?
    List<Student> findStudentsByFirstNameEqualsAndAgeEqualsAndIsGreaterThan(String firstName, Integer age); //Select * from student where first_name = ? and age > ? (Two value list)

    @Transactional
    @Modifying
    @Query("DELETE FROM Student u WHERE u.id = ?1")
    int deleteStudentById(Long id);

}
