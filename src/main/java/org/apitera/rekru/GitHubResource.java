package org.apitera.rekru;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apitera.rekru.Repository.GitHubRepository;
import org.apitera.rekru.Response.BranchResponse;
import org.apitera.rekru.Response.ErrorResponse;
import org.apitera.rekru.Response.GitHubRepositoryResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/github")
public class GitHubResource {

    @RestClient
    GitHubRepository gitHubService;

    @GET
    @Path("/repos/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getGitHubRepos(@PathParam("user") String user) {
        return gitHubService.getRepositories(user)
                .onFailure().transform(this::handleGitHubError)
                .onItem().transformToUni(repos -> {
                    if (repos.isEmpty()) {
                        return Uni.createFrom().item(
                                Response.status(404)
                                        .entity(new ErrorResponse(404, "User not found"))
                                        .build()
                        );
                    }

                    List<Uni<GitHubRepositoryResponse>> responseList = repos.stream()
                            .filter(repo -> !repo.fork)
                            .map(repo ->
                                    gitHubService.getBranches(user, repo.name)
                                            .onFailure().recoverWithItem(ex -> {
                                                getHttpStatusFromException(ex);
                                                throw new RuntimeException("Error fetching branches for repo: "
                                                        + repo.name
                                                        + " - "
                                                        + ex.getMessage(),
                                                        ex);
                                            })
                                            .onItem().transform(branches -> new GitHubRepositoryResponse(
                                                    repo.name,
                                                    user,
                                                    branches.stream()
                                                            .map(branch -> new BranchResponse(branch.name,
                                                                    branch.commit.sha))
                                                            .collect(Collectors.toList())
                                            ))
                            )
                            .collect(Collectors.toList());

                    return Uni.join().all(responseList).andCollectFailures()
                            .onItem().transform(responses -> {
                                List<GitHubRepositoryResponse> filteredResponse = new ArrayList<>(responses);
                                return Response.ok(filteredResponse).build();
                            });
                })
                .onFailure().recoverWithItem(t -> {
                    int status = getHttpStatusFromException(t);
                    return Response.status(status)
                            .entity(new ErrorResponse(status, t.getMessage()))
                            .build();
                });
    }

    private Throwable handleGitHubError(Throwable t) {
        return new RuntimeException("Error fetching repositories - "
                + t.getCause().getMessage(),
                t);
    }

    private int getHttpStatusFromException(Throwable t) {
        String message = t.getMessage();
        if (message.contains("404")) return 404;
        if (message.contains("403")) return 403;
        return 500;
    }
}
