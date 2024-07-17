package org.example.todo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class TodoVo {
    private final int id;
    private final String userId;
    private final String todo;
    private final boolean isCompleted;
    private final Timestamp createdAt;
}
