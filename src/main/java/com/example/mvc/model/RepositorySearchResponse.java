package com.example.mvc.model;

import lombok.Data;

import java.util.List;

@Data
public class RepositorySearchResponse {
    private List<Repository> items;
}
