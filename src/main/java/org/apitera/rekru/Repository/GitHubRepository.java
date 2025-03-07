package org.apitera.rekru.Repository;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.apitera.rekru.Model.Branch;
import org.apitera.rekru.Model.GitHubRepositoryModel;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(baseUri = "https://api.github.com")
public interface GitHubRepository {

    @GET
    @Path("/users/{user}/repos")
    Uni<List<GitHubRepositoryModel>> getRepositories(@PathParam("user") String user);

    @GET
    @Path("/repos/{user}/{repositoryName}/branches")
    Uni<List<Branch>> getBranches(@PathParam("user") String user, @PathParam("repositoryName") String repositoryName);

}
