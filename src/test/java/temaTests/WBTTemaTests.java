package temaTests;

import domain.Student;
import domain.Tema;
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
import validation.ValidationException;

import java.io.IOException;
import java.io.PrintWriter;

public class WBTTemaTests {
    private static final String filenameStudent = "src/test/java/studentTests/TestStudenti.xml";
    private static final String filenameTema = "src/test/java/studentTests/TestTeme.xml";
    private static final String filenameNota = "src/test/java/studentTests/TestNote.xml";

    private final Service service;

    public WBTTemaTests() {
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
    int countTeme(){
        int count = 0;

        for (Tema a: service.getAllTeme()
        ) {
            count++;
        }

        return count;
    }
    @Test
    void NCAInvalidId() {
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
        assert initialTemaCount == temaCount;
    }

    @Test
    void NCAInvalidDescription() {
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
        assert initialTemaCount == temaCount;
    }

    @Test
    void NCAInvalidDeadline() {
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
        assert initialTemaCount == temaCount;
    }


    @Test
    void NCAInvalidPrimire() {
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
        assert initialTemaCount == temaCount;
    }

    @Test
    void PCA() {
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
        assert initialTemaCount == temaCount-1;
    }

    @Test
    void NCADuplicateId(){
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
        assert initialTemaCount == temaCount;
    }
}
