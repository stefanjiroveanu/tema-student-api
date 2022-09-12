package com.nagarro.studentapi.utils;

import com.nagarro.studentapi.integration.model.Student;
import com.nagarro.studentapi.integration.model.domain.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ValidationUtils {
    @Data
    @AllArgsConstructor
    public static class AverageGrade {
        private String discipline;
        private double average;
        private int noOfGrades;
    }

    public static List<Double> getAverageGrades(Student student) {
        List<AverageGrade> averageGrades = new ArrayList<>();
        for (Grade grade : student.getGrades()) {
            Optional<AverageGrade> first = averageGrades.stream()
                    .filter(averageGrade -> grade.getDiscipline().equals(averageGrade.getDiscipline()))
                    .findFirst();
            if (first.isPresent()) {
                AverageGrade averageGrade = first.get();
                averageGrade.setAverage(averageGrade.getAverage() + grade.getGrade());
                averageGrade.setNoOfGrades(averageGrade.getNoOfGrades() + 1);
            } else {
                averageGrades.add(new AverageGrade(grade.getDiscipline(), grade.getGrade(), 1));
            }
        }
        averageGrades.forEach(grade -> grade.setAverage(grade.getAverage() / grade.getNoOfGrades()));
        return averageGrades.stream().map(AverageGrade::getAverage).collect(Collectors.toList());
    }

    public static boolean hasAtLeastTwoGrades(Student student) {
        List<AverageGrade> averageGrades = new ArrayList<>();
        for (Grade grade : student.getGrades()) {
            Optional<AverageGrade> first = averageGrades.stream()
                    .filter(averageGrade -> grade.getDiscipline().equals(averageGrade.getDiscipline()))
                    .findFirst();
            if (first.isPresent()) {
                AverageGrade averageGrade = first.get();
                averageGrade.setNoOfGrades(averageGrade.getNoOfGrades() + 1);
            } else {
                averageGrades.add(new AverageGrade(grade.getDiscipline(), grade.getGrade(), 1));
            }
        }
        for (AverageGrade averageGrade : averageGrades) {
            if (averageGrade.getNoOfGrades() < 2) {
                return false;
            }
        }
        return true;
    }
}
