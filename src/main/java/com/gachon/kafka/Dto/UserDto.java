package com.gachon.kafka.Dto;

import com.gachon.kafka.srcDB.model.User;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link User} entity
 */
@Data
public class UserDto implements Serializable {
    private final String name;
}