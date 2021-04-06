package ssvv.example;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class BBIntegration {
    String filenameStudent = "fisiere/test/Studenti.xml";
    String filenameTema = "fisiere/test/Teme.xml";
    String filenameNota = "fisiere/test/Note.xml";

    NotaXMLRepo notaXMLRepo = new NotaXMLRepo(filenameNota);
    TemaXMLRepo temaXMLRepo = new TemaXMLRepo(filenameTema);
    StudentXMLRepo studentXMLRepo = new StudentXMLRepo(filenameStudent);
    StudentValidator studentValidator = new StudentValidator();
    NotaValidator notaValidator = new NotaValidator(studentXMLRepo, temaXMLRepo);
    TemaValidator temaValidator = new TemaValidator();

    Service service = new Service(studentXMLRepo, studentValidator, temaXMLRepo, temaValidator, notaXMLRepo, notaValidator);
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Before
    public void setup() {
        Student student = new Student("abcd1234", "Bob", 999, "email");
        service.addStudent(student);

        Tema tema= new Tema("1","descriere",7,5);
        service.addTema(tema);
    }

    @After
    public void cleanup() {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();
            Element root  = document.createElement("inbox");
            document.appendChild(root);

            //write Document to file
            Transformer transformer = TransformerFactory.
                    newInstance().newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(filenameTema));
            transformer.transform(new DOMSource(document),
                    new StreamResult(filenameNota));
            transformer.transform(new DOMSource(document),
                    new StreamResult(filenameStudent));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // Add student test
    @Test
    public void addStudentTest() {
        Student nameNull = new Student("abcd1235", null, 931, "email");
        try {
            service.addStudent(nameNull);
            assert false;
        }
        catch (NullPointerException | ValidationException e) {
            Student nameEmpty = new Student("abcd1235", "", 931, "email");
            try {
                service.addStudent(nameEmpty);
                assert false;
            }
            catch (NullPointerException | ValidationException f) {
                assert true;
            }
        }
    }

    // Add Tema test
    @Test
    public void addAssignmentTest() {
        Tema tema= new Tema("","desc",4,2);
        try{
            service.addTema(tema);
            assert(false);
        }catch (Exception e){
            assert(true);
        }
    }

    @Test
    public void addGradeTest() {
        Nota nota = new Nota("nota1", "abcd1234", "1", 8.00, LocalDate.of(2021, 4, 6));
        assert service.addNota(nota, "Nice") == 8.00;
    }

    @Test
    public void testAll() {
        Student student = new Student("bbi_student", "Bob", 999, "email");
        assertNull(service.addStudent(student));

        Tema tema= new Tema("bbi_tema","descriere",5,3);
        assertNull(service.addTema(tema));

        Nota nota = new Nota("nota1", "bbi_student", "bbi_tema", 8.00, LocalDate.of(2021, 4, 6));
        try {
            service.addNota(nota, "Nice");
            assert false;
        } catch (ValidationException e) {
            assert true;
        }
    }
}
