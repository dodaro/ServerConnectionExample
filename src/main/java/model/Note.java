package model;

import client.util.Ignore;

public record Note(@Ignore String id, String date, String title, String file_id) {

    @Override
    public String toString() {
        return date + ";" + title + ";" + file_id;
    }
}
