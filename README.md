# GitHub API Client with Quarkus 3

This is a simple REST API built using **Quarkus 3** that interacts with the GitHub API. It retrieves the list of repositories for a specified user and, for each repository, fetches the list of branches along with the latest commit's SHA. The API responses are structured using custom response objects for better readability and usability.

## Technologies Used

- **Java 17+**
- **Quarkus 3**
- **MicroProfile Rest Client**
- **SmallRye Mutiny** (Reactive programming)
- **JAX-RS** (Jakarta RESTful Web Services)
- **JSON Responses**

## Endpoints

### `GET /github/repos/{user}`

Fetches the repositories of a specified GitHub user along with the list of branches for each repository. It filters out forked repositories and returns a list of non-forked repositories along with their branch names and the latest commit's SHA.

#### Parameters:
- `user`: GitHub username for which to fetch the repositories.

#### Responses:

- **200 OK**: Successfully fetched repositories and their branches.
- **404 Not Found**: If the user does not exist or has no repositories.
- **403 Forbidden**: If there is a rate limit or access restriction.
- **500 Internal Server Error**: If there is an unexpected error.

#### Example:

**Request: GET /github/repos/octocat**

**Response:**
```json
[
  {
    "repositoryName": "Hello-World",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "abc123"
      },
      {
        "name": "dev",
        "lastCommitSha": "def456"
      }
    ]
  },
  {
    "repositoryName": "Learning-Repo",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "xyz789"
      }
    ]
  }
]
```

## Project Setup

### Prerequisites
- **Java 17+**: You need Java 17 or above installed on your system.
- **Maven**: To build and run the project.
- **Quarkus 3**: Quarkus framework is used to build the application.

## Running the Application

- **Clone this repository**: `git clone https://github.com/Grzybci0o/.git`
- **Build and run the application with Maven**:
```bash
./mvnw compile quarkus:dev
```
This will start a development server on **http://localhost:8080**

## Testing the Endpoint

Once the application is running, you can test the API endpoint using tools like `Postman` or `curl`.

For example, to get the repositories for the user `Grzybci0o`:
```curl 
http://localhost:8080/github/repos/Grzybci0o
```

## Configuration

The base URL for the GitHub API is configured in the GitHubRepository interface as https://api.github.com.

### Rate Limiting

Keep in mind that GitHub API has rate limits. If you hit the rate limit, the API will return a **403 Forbidden** response.

## Error Handling

The API provides appropriate error messages for various scenarios:

- **404 Not Found**: When the user does not exist or has no repositories.
- **403 Forbidden**: When rate limits are exceeded or access is restricted.
- **500 Internal Server Error**: For unexpected server errors.

## Project Structure

- `src/main/java/org/apitera/rekru`: Contains the application code.
- `GitHubResource`: REST resource that exposes the /github/repos/{user} endpoint.
- `GitHubRepository`: MicroProfile Rest Client interface for interacting with GitHub's REST API.
- `Response`: Classes representing the response structures (e.g., GitHubRepositoryResponse, BranchResponse, ErrorResponse).
- `Model`: Data models used to represent GitHub repositories, branches, and commits.