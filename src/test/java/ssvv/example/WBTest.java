package ssvv.example;

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

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class WBTest
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
        /**
         *
         * @param nrTema - numarul temei
         * @param descriere - descrierea unei teme
         * @param deadline - deadlineul unei teme
         * @param primire - saptamana de primirea unei teme
         * Class Constructor
         */
        Tema tema= new Tema("1","descriere",8,6);
        assertNull(service.addStudent(student));
    }
}
