package org.javaup.graphrag.dto;

import java.util.List;

/**
 * @description: 数据传输对象
 * @author: slienceecheo
 **/
public record InstructorCoursesDto(String instructor, List<String> otherCourses) {
}
