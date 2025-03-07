package org.apitera.rekru.Response;

import java.util.List;

public class GitHubRepositoryResponse {
    public String repositoryName;
    public String ownerLogin;
    public List<BranchResponse> branches;

    public GitHubRepositoryResponse(String repositoryName, String ownerName, List<BranchResponse> branches) {
        this.repositoryName = repositoryName;
        this.ownerLogin = ownerName;
        this.branches = branches;
    }
}
