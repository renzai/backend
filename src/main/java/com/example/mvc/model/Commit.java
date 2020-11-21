package com.example.mvc.model;

import lombok.Data;

@Data
public class Commit {
    private CommitAuthor author;
    private Committer committer;
}
