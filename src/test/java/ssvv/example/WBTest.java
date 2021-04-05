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
                    new StreamResult(filenameTema));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void TC1() {
        Tema tema= new Tema("","desc",4,2);
        try{
            service.addTema(tema);
            assert(false);
        }catch (Exception e){
            assert(true);
        }
    }
    @Test
    public void TC2() {
        Tema tema= new Tema("1","",4,2);
        try{
            service.addTema(tema);
            assert(false);
        }catch (Exception e){
            assert(true);
        }
    }
    @Test
    public void TC3() {
        Tema tema= new Tema("1","desc",0,2);
        try{
            service.addTema(tema);
            assert(false);
        }catch (Exception e){
            assert(true);
        }
    }
    @Test
    public void TC4() {
        Tema tema= new Tema("1","desc",4,0);
        try{
            service.addTema(tema);
            assert(false);
        }catch (Exception e){
            assert(true);
        }
    }
    @Test
    public void TC5() {
        Tema tema= new Tema("1","descriere",8,6);
        assertNull(service.addTema(tema));
    }
    @Test
    public void TC6() {
        Tema tema= new Tema("2","descriere",8,6);
        assertNull(service.addTema(tema));
        assertEquals(service.addTema(tema),tema);
    }
}
