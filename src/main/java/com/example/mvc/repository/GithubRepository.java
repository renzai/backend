package com.example.mvc.repository;

import com.example.mvc.model.CommitSearchResponse;
import com.example.mvc.model.Repository;
import com.example.mvc.model.RepositorySearchResponse;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Repository
@AllArgsConstructor
public class GithubRepository {
    private final RestTemplate restTemplate;
    private final HttpEntity httpEntity;

    private static final UriComponents repoSearchUri = UriComponentsBuilder.fromHttpUrl("https://api.github.com/search/repositories?q={repoQuery}&per_page=10").build();
    private static final UriComponents commitUri = UriComponentsBuilder.fromHttpUrl("https://api.github.com/repos/{repository}/commits?&per_page=100").build();

    @Cacheable("repositories")
    public List<Repository> getRepositories(String repoQuery) {
        return get(repoSearchUri.expand(Map.of("repoQuery", repoQuery)).toUriString(), RepositorySearchResponse.class).getItems();
    }

    @Cacheable("repositoryCommitters")
    public Map<String, Long> getRepositoryCommitters(String repository) {
        return get(commitUri.expand(Map.of("repository", repository)).toUriString(), new ParameterizedTypeReference<List<CommitSearchResponse>>() {})
                .stream()
                .collect(Collectors.groupingBy(resp -> resp.getCommit().getAuthor().getName(), Collectors.counting()));
    }

    @Cacheable("repositoryCommitDates")
    public Map<LocalDate, Long> getRepositoryCommitDates(String repository) {
        return get(commitUri.expand(Map.of("repository", repository)).toUriString(), new ParameterizedTypeReference<List<CommitSearchResponse>>() {})
                .stream()
                .collect(Collectors.groupingBy(resp -> resp.getCommit().getCommitter().getDate(), Collectors.counting()));
    }

    <T> T get(String url, Class<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType).getBody();
    }

    <T> T get(String url, ParameterizedTypeReference<T> parameterizedTypeReference) {
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, parameterizedTypeReference).getBody();
    }
}
