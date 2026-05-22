package com.rdj.lms.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rdj.lms.dto.request.CreateLessonRequest;
import com.rdj.lms.dto.response.LessonResponse;
import com.rdj.lms.entity.Lesson;

@Component
public class LessonMapper {

    // Entity → Response DTO
    public LessonResponse toResponse(Lesson lesson) {
        LessonResponse response = new LessonResponse();
        response.setId(lesson.getId());
        response.setTitle(lesson.getTitle());
        response.setContent(lesson.getContent());
        response.setVideoUrl(lesson.getVideoUrl());
        response.setLessonOrder(lesson.getLessonOrder());

        // get course id from nested Course object
        response.setCourseId(lesson.getCourse().getId());

        return response;
    }

    // Request DTO → Entity
    public Lesson toEntity(CreateLessonRequest request) {
        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setLessonOrder(request.getLessonOrder());
        return lesson;
        // course set separately in service
    }

    // List Entity → List Response DTO
    public List<LessonResponse> toResponseList(List<Lesson> lessons) {
        List<LessonResponse> list = new ArrayList<>();
        for (Lesson lesson : lessons) {
            list.add(toResponse(lesson));
        }
        return list;
    }
}