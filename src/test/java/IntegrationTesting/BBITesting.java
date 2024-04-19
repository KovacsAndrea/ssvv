package IntegrationTesting;

import domain.Nota;
import domain.Student;
import domain.Tema;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import temaTests.WBTTemaTests;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class BBITesting extends TestCase {
    private static final String filenameStudent = "src/test/java/studentTests/TestStudenti.xml";
    private static final String filenameTema = "src/test/java/studentTests/TestTeme.xml";
    private static final String filenameNota = "src/test/java/studentTests/TestNote.xml";

    private final Service service;

    public BBITesting(String testName){
        super(testName);
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    public static Test suite() { return new TestSuite(BBITesting.class); }

    public void setUpStudent() throws IOException {
        PrintWriter pw = new PrintWriter(filenameStudent);

        pw.write(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<inbox>\n" +
                        "</inbox>");

        pw.close();
    }

    public void setUpTema() throws IOException {
        PrintWriter pw2 = new PrintWriter(filenameTema);

        pw2.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<inbox>\n" +
                "</inbox>");
        pw2.close();
    }

    public void setUpNote() throws IOException {
        PrintWriter pw2 = new PrintWriter(filenameTema);

        pw2.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<inbox>\n" +
                "</inbox>");
        pw2.close();
    }
    int countTeme(){
        int count = 0;

        for (Tema a: service.getAllTeme()
        ) {
            count++;
        }

        return count;
    }

    int countStudents(){
        int count = 0;

        for (Student s: service.getAllStudenti()
        ) {
            count++;
        }

        return count;
    }

    int countNote(){
        int count = 0;

        for (Nota n: service.getAllNote()
        ) {
            count++;
        }

        return count;
    }

    public void testAddCorrectStudent() {
        try{this.setUpStudent();} catch (Exception e) { Assertions.fail(e.getMessage());}
        int initialStudentsC = countStudents();
        Student student = new Student("1002", "Florin", 937, "florin@ymail.com");
        service.addStudent(student);
        int studentsC = countStudents();
        Assertions.assertEquals(initialStudentsC, studentsC -1);
    }

    public void testAddCorrectTema() {
        try{this.setUpTema();} catch (Exception e) { Assertions.fail(e.getMessage());}
        int initialTemaCount = countTeme();
        String id = "2", description = "wt";
        int primire = 4, deadline = 7;
        Tema tema = new Tema(id, description, deadline, primire);
        try{
            service.addTema(tema);
        }catch (ValidationException e){
            Assertions.fail(e.getMessage());
        }
        int temaCount = countTeme();
        Assertions.assertEquals(initialTemaCount, temaCount-1);
    }

    public void testAddCorrectNota() {
        try{this.setUpNote();} catch (Exception e) { Assertions.fail(e.getMessage());}
        int initialNoteC = countNote();
        Nota nota = new Nota("1", "1001", "2", 10, LocalDate.now());

        try{
            service.addNota(nota, "good job");
        }catch (ValidationException e){
            Assertions.fail(e.getMessage());
        }
        int notaCount = countNote();
        Assertions.assertEquals(initialNoteC, notaCount);
    }

}
