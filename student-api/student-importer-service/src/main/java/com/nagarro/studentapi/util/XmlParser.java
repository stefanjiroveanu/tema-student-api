package com.nagarro.studentapi.util;

import com.nagarro.studentapi.controller.model.Address;
import com.nagarro.studentapi.controller.model.Grade;
import com.nagarro.studentapi.controller.model.ImportedStudent;
import com.nagarro.studentapi.exception.AppException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
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


    public ImportedStudent parse(String path) throws Exception {
        ImportedStudent importedStudent = new ImportedStudent();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(path);
        document.getDocumentElement().normalize();
        XPath xpath = XPathFactory.newInstance().newXPath();
        if(Integer.parseInt(xpath.compile(NODES_NUMBER).evaluate(document)) != NUMBER_OF_STUDENT_ATTRIBUTES){
            throw new AppException("Invalid Xml due to number of elements");
        }
        importedStudent.setFirstname(xpath.compile(createExpression("firstname", TEXT)).evaluate(document));
        importedStudent.setLastname(xpath.compile(createExpression("lastname", TEXT)).evaluate(document));
        importedStudent.setCnp(xpath.compile(createExpression("cnp", TEXT)).evaluate(document));
        importedStudent.setBirthDate(LocalDate.parse(xpath.compile(createExpression("birthDate", TEXT)).evaluate(document)));
        List<Grade> gradeList = getGrades(document, xpath);
        importedStudent.setGrades(gradeList);
        Address address = new Address();
        address.setCity(xpath.compile(createExpression("address/city", TEXT)).evaluate(document));
        address.setCountry(xpath.compile(createExpression("address/country", TEXT)).evaluate(document));
        address.setStreet(xpath.compile(createExpression("address/street", TEXT)).evaluate(document));
        address.setNumber(Integer.parseInt(xpath.compile(createExpression("address/number", TEXT)).evaluate(document)));
        importedStudent.setAddress(address);
        return importedStudent;
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
