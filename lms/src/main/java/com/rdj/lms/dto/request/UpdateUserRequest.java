package com.rdj.lms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//dto/request/UpdateUserRequest.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

 private String name;    // optional
 private String email;   // optional
}
