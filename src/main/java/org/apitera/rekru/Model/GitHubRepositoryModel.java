package org.apitera.rekru.Model;

import java.util.List;

public class GitHubRepositoryModel {
    public String name;
    public boolean fork;
    public Owner owner;
    public List<Branch> branches;
}
