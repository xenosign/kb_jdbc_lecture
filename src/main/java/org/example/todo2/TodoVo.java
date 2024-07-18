package org.example.todo2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class TodoVo {
    private final int id;
    private final String user_id;
    private final String todo;
    private final boolean is_completed;
    private final Timestamp created_at;
}
