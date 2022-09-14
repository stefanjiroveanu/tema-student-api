package com.nagarro.studentapi.integration.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nagarro.studentapi.integration.model.Student;
import com.nagarro.studentapi.integration.model.domain.Address;
import com.nagarro.studentapi.integration.model.domain.Grade;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class StudentDeserializer extends JsonDeserializer<Student> {
    private final ObjectMapper objectMapper;

    public StudentDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Student deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Student student = new Student();
        student.setUuid(node.get("uuid").textValue());
        student.setCnp(node.get("data").get("cnp").textValue());
        student.setFirstname(node.get("data").get("firstname").textValue());
        student.setLastname(node.get("data").get("lastname").textValue());
        ObjectReader reader = objectMapper.readerFor(new TypeReference<List<Grade>>() {
        });
        List<Grade> grades = reader.readValue(node.get("data").get("grades"));
        student.setGrades(grades);
        reader = objectMapper.readerFor(new TypeReference<Address>() {
        });
        Address address = reader.readValue(node.get("data").get("address"));
        student.setAddress(address);
        ArrayNode birthDateNode = (ArrayNode) node.get("data").get("birthDate");
        student.setBirthDate(
                LocalDate.of(birthDateNode.get(0).asInt(), birthDateNode.get(1).asInt(), birthDateNode.get(2).asInt())
        );
        return student;
    }
}
