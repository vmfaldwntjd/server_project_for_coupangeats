package com.example.demo.src.event.query;

public class EventQuery {
    public static String getEventTopListQuery = "select E.event_id, event_image_url\n" +
            "from (\n" +
            "         SELECT event_id,\n" +
            "                status\n" +
            "         FROM event\n" +
            "         WHERE status = 1\n" +
            "     ) E\n" +
            "         join (\n" +
            "    SELECT event_id,\n" +
            "           image_id,\n" +
            "           url as event_image_url\n" +
            "    FROM event_image\n" +
            "    WHERE image_id = 1\n" +
            ") EI on E.event_id = EI.event_id;";

    public static String getEventMiddleListQuery = "select E.event_id, event_image_url\n" +
            "from (\n" +
            "         SELECT event_id,\n" +
            "                status\n" +
            "         FROM event\n" +
            "         WHERE status = 1\n" +
            "     ) E\n" +
            "         join (\n" +
            "    SELECT event_id,\n" +
            "           image_id,\n" +
            "           url as event_image_url\n" +
            "    FROM event_image\n" +
            "    WHERE image_id = 2\n" +
            ") EI on E.event_id = EI.event_id;";
}
