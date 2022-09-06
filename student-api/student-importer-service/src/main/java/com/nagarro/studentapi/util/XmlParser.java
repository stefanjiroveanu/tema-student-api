package com.nagarro.studentapi.util;

import com.nagarro.studentapi.controller.model.Address;
import com.nagarro.studentapi.controller.model.Grade;
import com.nagarro.studentapi.controller.model.Student;
import com.nagarro.studentapi.exception.AppException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class XmlParser {

    private static final String COUNT_GRADE = "count(//grade)";
    private static final String TEXT = "/text()";
    private static final String DISCIPLINE = "/@discipline";
    private static final String DATE = "/@date";
    private static final String NODES_NUMBER = "count(/student/*)";
    private static final int NUMBER_OF_STUDENT_ATTRIBUTES = 6;


    public Student parsePath(byte[] studentAsByteArray) throws Exception {
        Document document = computeDocument(studentAsByteArray);
        return getStudent(document);
    }

    public Student parsePath(String path) {
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
        if (Integer.parseInt(xpath.compile(NODES_NUMBER).evaluate(document)) != NUMBER_OF_STUDENT_ATTRIBUTES) {
            throw new AppException("Invalid Xml due to number of elements");
        }
        student.setFirstname(xpath.compile(createExpression("firstname", TEXT)).evaluate(document));
        student.setLastname(xpath.compile(createExpression("lastname", TEXT)).evaluate(document));
        student.setCnp(xpath.compile(createExpression("cnp", TEXT)).evaluate(document));
        student.setBirthDate(LocalDate.parse(xpath.compile(createExpression("birthDate", TEXT)).evaluate(document)));
        List<Grade> gradeList = getGrades(document, xpath);
        student.setGrades(gradeList);
        Address address = new Address();
        address.setCity(xpath.compile(createExpression("address/city", TEXT)).evaluate(document));
        address.setCountry(xpath.compile(createExpression("address/country", TEXT)).evaluate(document));
        address.setStreet(xpath.compile(createExpression("address/street", TEXT)).evaluate(document));
        address.setNumber(Integer.parseInt(xpath.compile(createExpression("address/number", TEXT)).evaluate(document)));
        student.setAddress(address);
        return student;
    }

    private List<Grade> getGrades(Document document, XPath xpath) throws XPathExpressionException {
        List<Grade> gradeList = new ArrayList<>();
        int numberOfGrades = Integer.parseInt(xpath.compile(COUNT_GRADE).evaluate(document));
        for (int i = 1; i <= numberOfGrades; i++) {
            Grade grade = new Grade();
            grade.setDate(LocalDate.parse(xpath.compile(createExpression(String.format("grades/grade[%d]", i), DATE)).evaluate(document)));
            grade.setDiscipline(xpath.compile(createExpression(String.format("grades/grade[%d]", i), DISCIPLINE)).evaluate(document));
            grade.setGrade(Integer.parseInt(xpath.compile(createExpression(String.format("grades/grade[%d]", i), TEXT)).evaluate(document)));
            gradeList.add(grade);
        }
        return gradeList;
    }

    public String createExpression(String tag, String function) {
        return "/student/" + tag + function;
    }

}
