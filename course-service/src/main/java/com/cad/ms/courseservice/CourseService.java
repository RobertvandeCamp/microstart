package com.cad.ms.courseservice;

import com.cad.ms.course.Course;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

public class CourseService extends BusModBase {

    private static final Logger LOG = LoggerFactory.getLogger(CourseService.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public void start() {
        super.start();
        eb.registerHandler("courseService", new CourseHandler());
    }

    private class CourseHandler implements Handler<Message> {
        @Override
        public void handle(Message event) {
            String provider = getMandatoryString("provider", event);
            LOG.debug("Received course request for provider [{}]", provider);
            JsonObject body = new JsonObject().putArray("courses", getCourses());
            event.reply(body);
        }
    }

    private JsonArray getCourses() {
        JsonArray courses = new JsonArray();
        Course java = new Course("1", "Java");
        Course scala = new Course("2", "Scala");
        Course angular = new Course("3", "Angular");
        try {
            courses.addObject(new JsonObject(OBJECT_MAPPER.writeValueAsString(java)));
            courses.addObject(new JsonObject(OBJECT_MAPPER.writeValueAsString(angular)));
            courses.addObject(new JsonObject(OBJECT_MAPPER.writeValueAsString(scala)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return courses;
    }
}
