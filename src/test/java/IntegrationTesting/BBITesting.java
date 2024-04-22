package IntegrationTesting;

import domain.Nota;
import domain.Student;
import domain.Tema;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.jupiter.api.Assertions;
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
import java.time.LocalDate;

public class BBITesting extends TestCase {
    private static final String filenameStudent = "src/test/java/IntegrationTesting/testStudentiBBI.txt";
    private static final String filenameTema = "src/test/java/IntegrationTesting/testTemeBBI.txt";
    private static final String filenameNota = "src/test/java/IntegrationTesting/testNoteBBI.txt";


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
                        "    <student idStudent=\"1001\">\n" +
                        "        <nume>Andrada</nume>\n" +
                        "        <grupa>935</grupa>\n" +
                        "        <email>andrada@stud.ubb</email>\n" +
                        "    </student>\n" +
                        "</inbox>");

        pw.close();
    }

    public void setUpTema() throws IOException {
        PrintWriter pw2 = new PrintWriter(filenameTema);

        pw2.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<inbox>\n" +
                "    <nrTema nrTema=\"1\">\n" +
                "        <descriere>wt</descriere>\n" +
                "        <deadline>2</deadline>\n" +
                "        <primire>1</primire>\n" +
                "    </nrTema>\n" +
                "</inbox>");
        pw2.close();
    }

    public void setUpNote() throws IOException {
        PrintWriter pw2 = new PrintWriter(filenameNota);

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
        Assertions.assertEquals(initialStudentsC, 1);
        Assertions.assertEquals(studentsC, 2);
        Assertions.assertEquals(initialStudentsC, studentsC -1);
        try{this.setUpStudent();} catch (Exception e) { Assertions.fail(e.getMessage());}
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
        try{this.setUpTema();} catch (Exception e) { Assertions.fail(e.getMessage());}
    }

    public void testAddTemaIntegrationTestingSA(){
        try{this.setUpTema();} catch (Exception e) { Assertions.fail(e.getMessage());}
        try{this.setUpStudent();} catch (Exception e) {Assertions.fail(e.getMessage());}
        int temaC = countTeme();
        Assertions.assertEquals(temaC, 1);
        int studentC = countStudents();
        Assertions.assertEquals(studentC, 1);

        Student student = new Student("1002", "Florin", 937, "florin@ymail.com");
        service.addStudent(student);
        int finalStudentC = countStudents();
        Assertions.assertEquals(finalStudentC, 2);

        Tema tema = new Tema("2", "wt", 7, 4);
        try{service.addTema(tema);}catch (ValidationException e){ Assertions.fail(e.getMessage()); }
        int finalTemaC = countTeme();
        Assertions.assertEquals(finalTemaC, 2);

        try{this.setUpTema();} catch (Exception e) { Assertions.fail(e.getMessage());}
        try{this.setUpStudent();} catch (Exception e) {Assertions.fail(e.getMessage());}
    }

    public void testAddCorrectNota() {
        try{this.setUpTema();} catch (Exception e) { Assertions.fail(e.getMessage());}
        try{this.setUpStudent();} catch (Exception e) {Assertions.fail(e.getMessage());}
        try{this.setUpNote();} catch (Exception e) {Assertions.fail(e.getMessage());}
        int temaC = countTeme();
        Assertions.assertEquals(temaC, 1);
        int studentC = countStudents();
        Assertions.assertEquals(studentC, 1);
        int notaC = countNote();
        Assertions.assertEquals(notaC, 0);
        LocalDate date = LocalDate.of(2018, 10, 1);
        LocalDate oneWeekLater = date.plusWeeks(2);
        Nota nota = new Nota("1", "1001", "1", 10, oneWeekLater);

        try{

            service.addNota(nota, "good job");
        }catch (ValidationException e){
            Assertions.fail(e.getMessage());
        }
        int finalNotaC = countNote();
        Assertions.assertEquals(notaC, 0);
        Assertions.assertEquals(finalNotaC, 1);
        Assertions.assertEquals(notaC, finalNotaC-1);

        try{this.setUpTema();} catch (Exception e) { Assertions.fail(e.getMessage());}
        try{this.setUpStudent();} catch (Exception e) {Assertions.fail(e.getMessage());}
        try{this.setUpNote();} catch (Exception e) {Assertions.fail(e.getMessage());}
    }

    public void testAddGradeIntegrationTestingSAG() {
        try{this.setUpTema();} catch (Exception e) { Assertions.fail(e.getMessage());}
        try{this.setUpStudent();} catch (Exception e) {Assertions.fail(e.getMessage());}
        try{this.setUpNote();} catch (Exception e) {Assertions.fail(e.getMessage());}
        int temaC = countTeme();
        Assertions.assertEquals(temaC, 1);
        int studentC = countStudents();
        Assertions.assertEquals(studentC, 1);
        int notaC = countNote();
        Assertions.assertEquals(notaC, 0);

        Student student = new Student("1002", "Florin", 937, "florin@ymail.com");
        service.addStudent(student);
        int finalStudentC = countStudents();
        Assertions.assertEquals(finalStudentC, 2);

        Tema tema = new Tema("2", "wt", 7, 4);
        try{service.addTema(tema);}catch (ValidationException e){ Assertions.fail(e.getMessage()); }
        int finalTemaC = countTeme();
        Assertions.assertEquals(finalTemaC, 2);


        LocalDate date = LocalDate.of(2018, 10, 1);
        LocalDate notaDate = date.plusWeeks(7);
        Nota nota = new Nota("1", "1002", "2", 10, notaDate);

        try{
            service.addNota(nota, "good job");
        }catch (ValidationException e){
            Assertions.fail(e.getMessage());
        }
        int finalNotaC = countNote();
        Assertions.assertEquals(notaC, 0);
        Assertions.assertEquals(finalNotaC, 1);
        Assertions.assertEquals(notaC, finalNotaC-1);

        try{this.setUpTema();} catch (Exception e) { Assertions.fail(e.getMessage());}
        try{this.setUpStudent();} catch (Exception e) {Assertions.fail(e.getMessage());}
        try{this.setUpNote();} catch (Exception e) {Assertions.fail(e.getMessage());}
    }

}
