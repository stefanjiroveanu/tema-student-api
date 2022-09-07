package com.nagarro.studentapi.util;

import com.nagarro.studentapi.controller.model.Address;
import com.nagarro.studentapi.controller.model.Grade;
import com.nagarro.studentapi.controller.model.Student;
import com.nagarro.studentapi.exception.AppException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class XmlParser {

    private static final String TEXT = "/text()";

    public Student parse(byte[] studentAsByteArray) throws Exception {
        Document document = computeDocument(studentAsByteArray);
        return getStudent(document);
    }

    public Student parse(String path) {
        try {
            Document document = computeDocument(path);
            return getStudent(document);
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    private static Document computeDocument(Object xmlDoc) throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document document;
        if (xmlDoc instanceof byte[]) {
            document = documentBuilder.parse(new ByteArrayInputStream((byte[]) xmlDoc));
        } else if (xmlDoc instanceof String) {
            document = documentBuilder.parse(xmlDoc.toString());
        } else {
            throw new AppException("The document type is not compatible");
        }
        document.getDocumentElement().normalize();
        return document;
    }

    private Student getStudent(Document document) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        Student student = new Student();
        student.setFirstname(xpath.compile(createExpression("firstname", TEXT)).evaluate(document));
        student.setLastname(xpath.compile(createExpression("lastname", TEXT)).evaluate(document));
        student.setCnp(xpath.compile(createExpression("cnp", TEXT)).evaluate(document));
        student.setBirthDate(LocalDate.parse(xpath.compile(createExpression("birthDate", TEXT)).evaluate(document)));
        List<Grade> gradeList = getGrades(document, xpath);
        student.setGrades(gradeList);
        Address address = getAddress(document, xpath);
        student.setAddress(address);
        return student;
    }

    private Address getAddress(Document document, XPath xpath) throws XPathExpressionException {
        Address address = new Address();
        address.setCity(xpath.compile(createExpression("address/city", TEXT)).evaluate(document));
        address.setCountry(xpath.compile(createExpression("address/country", TEXT)).evaluate(document));
        address.setStreet(xpath.compile(createExpression("address/street", TEXT)).evaluate(document));
        address.setNumber(Integer.parseInt(xpath.compile(createExpression("address/number", TEXT)).evaluate(document)));
        return address;
    }

    private List<Grade> getGrades(Document document, XPath xpath) throws XPathExpressionException {
        List<Grade> gradeList = new ArrayList<>();
        NodeList xpathList = (NodeList) xpath.evaluate("student/grades/grade", document, XPathConstants.NODESET);
        for (int i = 0; i < xpathList.getLength(); i++) {
            Grade grade = new Grade();
            Node currentGrade = xpathList.item(i);
            NamedNodeMap attributes = currentGrade.getAttributes();
            for (int j = 0; j < attributes.getLength(); j++) {
                Node currentAttribute = attributes.item(j);
                String nodeName = currentAttribute.getNodeName();
                if ("discipline".equals(nodeName)) {
                    grade.setDiscipline(currentAttribute.getNodeValue());
                } else if ("date".equals(nodeName)) {
                    grade.setDate(LocalDate.parse(currentAttribute.getNodeValue()));
                }
            }
            grade.setGrade(Integer.parseInt(currentGrade.getTextContent()));
            gradeList.add(grade);
        }
        return gradeList;
    }

    public String createExpression(String tag, String function) {
        return "/student/" + tag + function;
    }

}
