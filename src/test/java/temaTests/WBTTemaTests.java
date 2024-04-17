package temaTests;

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
import studentTests.BBTStudentTests;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.io.IOException;
import java.io.PrintWriter;

public class WBTTemaTests extends TestCase {
    private static final String filenameStudent = "src/test/java/studentTests/TestStudenti.xml";
    private static final String filenameTema = "src/test/java/studentTests/TestTeme.xml";
    private static final String filenameNota = "src/test/java/studentTests/TestNote.xml";

    private final Service service;

    public WBTTemaTests(String testName) {
        super(testName);
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    public static Test suite() { return new TestSuite(WBTTemaTests.class); }
    public void setUp() throws IOException {
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

        PrintWriter pw2 = new PrintWriter(filenameTema);

        pw2.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<inbox>\n" +
                "    <nrTema nrTema=\"1\">\n" +
                "        <descriere>wt</descriere>\n" +
                "        <deadline>7</deadline>\n" +
                "        <primire>4</primire>\n" +
                "    </nrTema>\n" +
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

    public void testNCAInvalidId() {
        try{this.setUp();} catch (Exception e) { Assertions.fail(e.getMessage());}
        int initialTemaCount = countTeme();
        String id = "", description = "wt";
        int primire = 2, deadline = 4;
        Tema tema = new Tema(id, description, primire, deadline);
        try{
            service.addTema(tema);
            Assertions.fail("Expected ValidationException was not thrown");
        }catch (ValidationException e){
            Assertions.assertEquals("Numar tema invalid!", e.getMessage());
        }
        int temaCount = countTeme();
        Assertions.assertEquals(initialTemaCount, temaCount);
    }


    public void testNCAInvalidDescription() {
        try{this.setUp();} catch (Exception e) { Assertions.fail(e.getMessage());}
        int initialTemaCount = countTeme();
        String id = "1", description = "";
        int primire = 3, deadline = 5;
        Tema tema = new Tema(id, description, primire, deadline);
        try{
            service.addTema(tema);
            Assertions.fail("Expected ValidationException was not thrown");
        }catch (ValidationException e){
            Assertions.assertEquals("Descriere invalida!", e.getMessage());
        }
        int temaCount = countTeme();
        Assertions.assertEquals(initialTemaCount, temaCount);
    }


    public void testNCAInvalidDeadline() {
        try{this.setUp();} catch (Exception e) { Assertions.fail(e.getMessage());}
        int initialTemaCount = countTeme();
        String id = "1", description = "wt";
        int primire = 3, deadline = -2;
        Tema tema = new Tema(id, description, deadline, primire);
        try{
            service.addTema(tema);
            Assertions.fail("Expected ValidationException was not thrown");
        }catch (ValidationException e){
            Assertions.assertEquals("Deadlineul trebuie sa fie intre 1-14.", e.getMessage());
        }
        int temaCount = countTeme();
        Assertions.assertEquals(initialTemaCount, temaCount);
    }



    public void testNCAInvalidPrimire() {
        try{this.setUp();} catch (Exception e) { Assertions.fail(e.getMessage());}
        int initialTemaCount = countTeme();
        String id = "1", description = "wt";
        int primire = -2, deadline = 7;
        Tema tema = new Tema(id, description, deadline, primire);
        try{
            service.addTema(tema);
            Assertions.fail("Expected ValidationException was not thrown");
        }catch (ValidationException e){
            Assertions.assertEquals("Saptamana primirii trebuie sa fie intre 1-14.", e.getMessage());
        }
        int temaCount = countTeme();
        Assertions.assertEquals(initialTemaCount, temaCount);
    }


    public void testPCA() {
        try{this.setUp();} catch (Exception e) { Assertions.fail(e.getMessage());}
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
        Assertions.assertEquals(initialTemaCount, 1);
        Assertions.assertEquals(temaCount, 2);
        Assertions.assertEquals(initialTemaCount, temaCount-1);
    }


    public void testNCADuplicateId(){
        try{this.setUp();} catch (Exception e) { Assertions.fail(e.getMessage());}
        int initialTemaCount = countTeme();
        String id = "1", description = "wt";
        int primire = 4, deadline = 7;
        Tema tema = new Tema(id, description, deadline, primire);
        try{
            service.addTema(tema);
        }catch (ValidationException e){
            Assertions.fail(e.getMessage());
        }
        int temaCount = countTeme();
        Assertions.assertEquals(initialTemaCount, temaCount);
    }
}
