# DD2480-project-2
This repo contains source code and tests for a CI-server that can compile and run tests on a gradle project.

This is the work of Group 5 in the KTH course DD2480 (2018).

Group members:
- Anders Sjöbom
- Albin Remnestål
- Johan Bergdorf
- Marcus Wengelin
- Robert Wörlund
## How-to

### gradle
To create a gradle wrapper, compile the code and run the main method which starts a server on port 8080 on the local machine:
```
$ gradle wrapper # to create generate gradle/wrapper/*
$ gradle run
```
To run the test-cases associated with the program, run:
```
$ gradle test
```
## API
### HTTP API
Requests to the following URLs (in bold below) will respond with the described functionality:

**/webhook** - if a POST request with a [github push-payload](https://developer.github.com/v3/activity/events/types/#pushevent)
is sent to this URL the server will clone the branch specified in the payload and perform integration tasks (a gradle check) on the cloned repo.
The commit status on github will be updated once this has finished with the builds status and a URL with additional info about the build result.

**/build** - returns an HTML page with a list of all the builds that have been run on the server.

**/build/{commit_sha}** - returns an HTML page with information about the build whose most recent commit was the checksum given in the URL.

### Structure
* The class ContinousIntegrationServer is the core of the CI server. It contains the handler for HTTP requests to the server
* The functionality of the server is categorized in the different handler-classes
  * The BuildHandler tries to do the integration by calling other handlers and running a gradle check
  * The GitHandler concerns cloning and deleting git repositories
  * CIHistory contains business logic to create persistent reports of the builds
  * StatusHandler has business logic to update the commit status when a build is finished
  * ShellCommand has methods to execute commands in the host's shell
  * gradleParser is a class used to parse gradle output to determine the state of the build

For the detailed API documentation check out the javadoc in the root of the repo.
## Statement of Contributions

#### Anders
git cloning programmatically, travis setup, documentation, running shell commands programmatically, setting up development/production server

#### Marcus
build report generation, build list, documentation

#### Johan
small fixes, refactoring, git cloning programmatically, added tests

#### Albin
commit status updates via the GitHub API, web url interface, build report generation

#### Robert
gradle setup, running shell commands programmatically, execute gradle check programmatically, parse gradle response, timestamps in /build (not currently in master))

UPDATE
UPDATE2
UPDATE3