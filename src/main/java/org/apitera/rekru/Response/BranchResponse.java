package org.apitera.rekru.Response;

public class BranchResponse {
    public String name;
    public String lastCommitSha;

    public BranchResponse(String name, String lastCommitSha) {
        this.name = name;
        this.lastCommitSha = lastCommitSha;
    }
}
