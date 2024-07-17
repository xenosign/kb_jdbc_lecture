package org.example.todo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class UserVo {
    private final String userId;
    private final String name;
    private final String password;
    private final Timestamp createdAt;
}
