package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository, StudentIdCardRepository studentIdCardRepository) {
        return args -> {
            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@amigoscode.edu", firstName, lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55));

            student.addBook(new Book("Clean Code", LocalDateTime.now().minusDays(4)));

            student.addBook(new Book("Think and Grow Rich", LocalDateTime.now()));

            student.addBook(new Book("Spring Data JPA", LocalDateTime.now().minusDays(1)));

            StudentIdCard studentIdCard = new StudentIdCard("123456789", student);

            student.setStudentIdCard(studentIdCard);

            studentRepository.save(student);

            //studentIdCardRepository.save(studentIdCard); one to one

            studentRepository.findById(1L).ifPresent(s -> {
                System.out.println("fetch book lazy....");
                List<Book> books = student.getBooks();
                books.forEach(book -> {
                    System.out.println(s.getFirstName() + "borrowes" + book.getBookName());
                });
            });
            //studentIdCardRepository.findById(1L).ifPresent(System.out::println);

            studentRepository.deleteById(1L);

        };

        /*return args -> {
            Student maria = new Student(
                    "Maria",
                    "Jones",
                    "maria.jones@emigoscode.edu",
                    21
            );

            Student maria2 = new Student(
                    "Maria",
                    "Jones",
                    "maria2.jones@emigoscode.edu",
                    25
            );

            Student ahmed = new Student(
                    "Ahmed",
                    "Ali",
                    "ahmet.ali@emigoscode.edu",
                    25
            );

            System.out.println("Adding maria and ahmed");
            studentRepository.saveAll(List.of(maria, ahmed, maria2));

            //Query-Method: Select * from Where Email = @Email
            studentRepository
                    .findStudentByEmail("ahmet.ali@amigoscode.edu")
                    .ifPresentOrElse(System.out::println, () -> System.out.println("Student with email ahmed.ali@amigoscode.edu not found"));

            //Query-Method: Select * from where first_name = ? and age = ? (one value list)
            studentRepository.findStudentsByFirstNameEqualsAndAgeEquals(
                    "Maria",
                    21
            ).forEach(System.out::println);

            //Query-Method: Select * from where first_name = ? and age > ? (Two value list)
            studentRepository.findStudentsByFirstNameEqualsAndAgeEqualsAndIsGreaterThan(
                    "Maria",
                    18
            ).forEach(System.out::println);

            //Query-Method: Select * from where first_name = ? and age >= ? (Two value list)
            studentRepository.findStudentsByFirstNameEqualsAndAgeEqualsAndIsGreaterThanEqual(
                    "Maria",
                    21
            ).forEach(System.out::println);

            studentRepository.selectStudentWhereFirstNameAndAgeGreaterOrEqualNative(
                    "Maria",
                    21
            ).forEach(System.out::println);

            System.out.println("Number of students:");
            System.out.println(studentRepository.count());

            studentRepository
                    .findById(2L)
                    .ifPresentOrElse(
                        System.out::println,
                        () ->  System.out.println("Student with ID 2 not found"));

            studentRepository
                    .findById(3L)
                    .ifPresentOrElse(
                            System.out::println,
                            () ->  System.out.println("Student with ID 3 not found"));

            System.out.println("Select all students");
            List<Student> students = studentRepository.findAll();
            students.forEach(System.out::println);

            System.out.println("Delete maria");
            studentRepository.deleteById(1L);

            System.out.println("Number of students: ");
            System.out.println(studentRepository.count());

            System.out.println("Delete");
            System.out.println(studentRepository.deleteStudentById(3L));



        };*/
    }

    private void paging(StudentRepository studentRepository) {
        PageRequest pageRequest = PageRequest.of(
                0,
                5, //5 size
                Sort.by("firstName").ascending());
        Page<Student> page = studentRepository.findAll(pageRequest);
        System.out.println(page); // out = 4
    }

    private void sorting(StudentRepository studentRepository) {
        Sort sort = Sort.by("firstName").ascending()
                .and(Sort.by("age").descending()); //order by firstName asc
        studentRepository.findAll(sort).forEach(student -> System.out.println(student.getFirstName() + " " + student.getAge()));
    }

    private void generateRandomStudents(StudentRepository studentRepository) {
        Faker faker = new Faker();
        for (int i = 0; i <= 20; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@amigoscode.edu", firstName, lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55));

            studentRepository.save(student);
        }
    }

}
