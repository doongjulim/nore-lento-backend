package io.github.dongjulim.security.util.dto;

import io.github.dongjulim.domain.user.enums.Role;

import java.util.List;

public record TokenParseResponse (String username, List<Role> roles){
}
