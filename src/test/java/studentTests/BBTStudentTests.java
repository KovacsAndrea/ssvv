package studentTests;

import domain.Student;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import java.io.IOException;
import java.io.PrintWriter;


public class BBTStudentTests {
    private static final String filenameStudent = "src/test/java/studentTests/TestStudenti.xml";
    private static final String filenameTema = "src/test/java/studentTests/TestTeme.xml";
    private static final String filenameNota = "src/test/java/studentTests/TestNote.xml";

    private final Service service;

    public BBTStudentTests() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @BeforeAll
    static void SetUpOnce() throws IOException {
        PrintWriter pw = new PrintWriter(filenameStudent);

        pw.write(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<inbox>\n" +
                        "    <student idStudent=\"1001\">\n" +
                        "        <nume>Andrada</nume>\n" +
                        "        <grupa>935</grupa>\n" +
                        "        <email>andrada@stud.ubb</email>\n" +
                        "    </student>\n" +
                        "</inbox>");

        pw.close();
    }

    @BeforeEach
    void Setup() throws IOException {
        PrintWriter pw = new PrintWriter(filenameStudent);

        pw.write(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<inbox>\n" +
                        "    <student idStudent=\"1001\">\n" +
                        "        <nume>Andrada</nume>\n" +
                        "        <grupa>935</grupa>\n" +
                        "        <email>andrada@stud.ubb</email>\n" +
                        "    </student>\n" +
                        "</inbox>");

        pw.close();
    }

    int countStudents(){
        int count = 0;

        for (Student s: service.getAllStudenti()
        ) {
            count++;
        }

        return count;
    }


    @Test
    void TestAddCorrectStudent() {
        int initialStudentsC = countStudents();
        Student student = new Student("1002", "Florin", 937, "florin@ymail.com");
        service.addStudent(student);
        int studentsC = countStudents();
        assert initialStudentsC == studentsC - 1;
    }

    @Test
    void TestAddAlreadyExistingIDStudent() {
        int initialStudentsC = countStudents();
        Student student = new Student("1001", "Florin", 937, "florin@ymail.com");
        service.addStudent(student);
        int studentsC = countStudents();
        assert initialStudentsC == studentsC;
    }

    @Test
    void TestAddEmptyIdStudent() {
        int initialStudentsC = countStudents();
        Student student = new Student("", "Florin", 937, "florin@ymail.com");

        try {
            service.addStudent(student);
            Assertions.fail("Expected ValidationException was not thrown");
        } catch (Exception e) {
            Assertions.assertEquals("Id incorect!", e.getMessage());
        }

        int studentsC = countStudents();
        Assertions.assertEquals(initialStudentsC, studentsC);
    }

    @Test
    void TestAddSmallerGroupStudent() {
        int initialStudentsC = countStudents();
        Student student = new Student("1002", "Florin", 93, "florin@ymail.com");

        try {
            service.addStudent(student);
            Assertions.fail("Expected ValidationException was not thrown");
        } catch (Exception e) {
            Assertions.assertEquals("Grupa incorecta! Grupa trebuie sa aiba intre 3 si 4 cifre.", e.getMessage());
        }

        int studentsC = countStudents();
        Assertions.assertEquals(initialStudentsC, studentsC);
    }

    @Test
    void TestAddBiggerGroupStudent() {
        int initialStudentsC = countStudents();
        Student student = new Student("1002", "Florin", 93756, "florin@ymail.com");

        try {
            service.addStudent(student);
            Assertions.fail("Expected ValidationException was not thrown");
        } catch (Exception e) {
            Assertions.assertEquals("Grupa incorecta! Grupa trebuie sa aiba intre 3 si 4 cifre.", e.getMessage());
        }

        int studentsC = countStudents();
        Assertions.assertEquals(initialStudentsC, studentsC);
    }

    @Test
    void TestAddEmptyNameStudent() {
        int initialStudentsC = countStudents();
        Student student = new Student("1002", "", 937, "florin@ymail.com");

        try {
            service.addStudent(student);
            Assertions.fail("Expected ValidationException was not thrown");
        } catch (Exception e) {
            Assertions.assertEquals("Nume incorect!", e.getMessage());
        }

        int studentsC = countStudents();
        Assertions.assertEquals(initialStudentsC, studentsC);
    }

    @Test
    void TestAddEmptyEmailStudent() {
        int initialStudentsC = countStudents();
        Student student = new Student("1002", "Florin", 937, "");

        try {
            service.addStudent(student);
            Assertions.fail("Expected ValidationException was not thrown");
        } catch (Exception e) {
            Assertions.assertEquals("Email gol!", e.getMessage());
        }
        int studentsC = countStudents();
        Assertions.assertEquals(initialStudentsC, studentsC);
    }

    @Test
    void TestAddFakeEmailStudent() {
        int initialStudentsC = countStudents();
        Student student = new Student("1002", "Florin", 937, "fakeEmail");

        try {
            service.addStudent(student);
            Assertions.fail("Expected ValidationException was not thrown");
        } catch (Exception e) {
            Assertions.assertEquals("Email incorect! Email-ul trebuie sa fie de forma ceva@ceva.ceva", e.getMessage());
        }
        int studentsC = countStudents();
        Assertions.assertEquals(initialStudentsC, studentsC);
    }
}
