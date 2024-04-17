package studentTests;
import domain.Nota;
import domain.Student;
import domain.Tema;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.jupiter.api.BeforeEach;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import ssvv.example.AppTest;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

public class StudentTests extends TestCase {
    private final Service service;

    public StudentTests(String testName){
        super( testName );
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        String filenameStudent = "src/test/java/studentTests/TestStudenti.xml";
        String filenameTema = "src/test/java/studentTests/TestTeme.xml";
        String filenameNota = "src/test/java/studentTests/TestNote.xml";


        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    public static Test suite()
    {
        return new TestSuite( StudentTests.class );
    }

    public void testStudent()
    {
        assertTrue( true );
    }
}
