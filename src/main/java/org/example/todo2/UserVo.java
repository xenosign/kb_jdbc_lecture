package org.example.todo2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class UserVo {
    private final String user_id;
    private final String name;
    private final String password;
    private final Timestamp created_at;
}







