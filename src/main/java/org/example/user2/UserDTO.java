package org.example.user2;

public class UserDTO {
    private String name;
    private int age;

    public UserDTO(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {   return name;   }

    public void setName(String name) {   this.name = name;   }

    public int getAge() {   return age;   }

    public void setAge(int age) {   this.age = age;   }
}
