package ssvv.example;

import domain.Student;
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
import java.io.File;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
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
                    new StreamResult(filenameStudent));

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // TC 1
    @Test
    public void testAddStudentValid() {
        Student student = new Student("abcd1234", "Bob", 999, "email");
        assertNull(service.addStudent(student));
    }

    // TC 2
    @Test
    public void testAddStudentInvalidId() {
        Student idNull = new Student(null, "Bob", 999, "email");
        try {
            service.addStudent(idNull);
            assert false;
        }
        catch (NullPointerException | ValidationException e) {
            Student idEmpty = new Student("", "Bob", 999, "email");
            try {
                service.addStudent(idEmpty);
                assert false;
            }
            catch (NullPointerException | ValidationException f) {
                assert true;
            }
        }
    }

    // TC 3
    @Test
    public void testAddStudentInvalidName() {
        Student nameNull = new Student("abcd1235", null, 999, "email");
        try {
            service.addStudent(nameNull);
            assert false;
        }
        catch (NullPointerException | ValidationException e) {
            Student nameEmpty = new Student("abcd1235", "", 999, "email");
            try {
                service.addStudent(nameEmpty);
                assert false;
            }
            catch (NullPointerException | ValidationException f) {
                assert true;
            }
        }
    }

    // TC 4
    @Test
    public void testAddStudentInvalidEmail() {
        Student emailNull = new Student("abcd1235", "Bob", 999, null);
        try {
            service.addStudent(emailNull);
            assert false;
        }
        catch (NullPointerException | ValidationException e) {
            Student emailEmpty = new Student("abcd1235", "Bob", 999, "");
            try {
                service.addStudent(emailEmpty);
                assert false;
            }
            catch (NullPointerException | ValidationException f) {
                assert true;
            }
        }
    }

    // TC 5
    @Test
    public void testAddStudentInvalidGroup() {
        Student student = new Student("abcd1235", "Bob", -999, "email");

        try {
            service.addStudent(student);
            assert false;
        }
        catch (NullPointerException | ValidationException e) {
            assert true;
        }
    }

    // TC 6
    @Test
    public void testAddStudentDuplicateId() {
        Student student = new Student("abcdduplicate", "Bob", 999, "email");
        assertNull(service.addStudent(student));

        assertNotNull(service.addStudent(student));
    }
}
