package com.example.mvc.controller;

import com.example.mvc.repository.GithubRepository;
import com.example.mvc.model.Repository;
import com.example.mvc.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/api")
public class GithubController {

    private GithubRepository githubRepository;

    @GetMapping("/repositories")
    public List<Repository> searchRepository(@RequestParam("query") Optional<String> queryParam) throws Exception {
        String repoQuery = queryParam.filter(StringUtils::isNotBlank)
                .orElseThrow(() -> new Exception("invalid"));

        return githubRepository.getRepositories(repoQuery);
    }

    @GetMapping(value = "/committers")
    public Map<String, Long> getCommitters(@RequestParam("repo") Optional<String> repositoryParam) throws Exception {
        String repository = repositoryParam.filter(StringUtils::isNotBlank)
                .orElseThrow(() -> new Exception("invalid"));

        return githubRepository.getRepositoryCommitters(repository);
    }

    @GetMapping("/commitDistribution")
    public Map<LocalDate, Long> getCommitDistribution(@RequestParam("repo") Optional<String> repositoryParam) throws Exception {
        String repository = repositoryParam.filter(StringUtils::isNotBlank)
                .orElseThrow(() -> new Exception("invalid"));

        return githubRepository.getRepositoryCommitDates(repository);
    }
}