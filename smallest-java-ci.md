The smallest Java Continuous Integration server for Github
===========================================================

Here is a tiny CI server skeleton implemented in Java for educational purposes. It is meant to be called as webhook by Github. The HTTP part of it is based on Jetty.

We assume here that you have a standard Linux machine (eg with Ubuntu), with Java installed.

We first checkout this repository:
```
git clone https://github.com/monperrus/smallest-java-ci
cd smallest-java-ci
```

We then download the required dependencies:
```
JETTY_VERSION=7.0.2.v20100331
wget -U none http://repo1.maven.org/maven2/org/eclipse/jetty/aggregate/jetty-all/$JETTY_VERSION/jetty-all-$JETTY_VERSION.jar
wget -U none http://repo1.maven.org/maven2/javax/servlet/servlet-api/2.5/servlet-api-2.5.jar
#For linux users: 
curl -LO --tlsv1 https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-linux-amd64.zip
unzip ngrok-stable-linux-amd64.zip 
#For Mac user:
curl -LO --tlsv1 https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-darwin-386.zip
unzip ngrok-stable-darwin-386.zip
```

We compile the skeleton the continuous integration server:
```
javac -cp servlet-api-2.5.jar:jetty-all-$JETTY_VERSION.jar ContinuousIntegrationServer.java
```

We run the server on the machine, and we may make it visible on the Internet thanks to [Ngrok](https://ngrok.com/):
```
# open a first terminal window
JETTY_VERSION=7.0.2.v20100331
java -cp .:servlet-api-2.5.jar:jetty-all-$JETTY_VERSION.jar ContinuousIntegrationServer

# open a second terminal window
# this gives you the public URL of your CI server to set in Github
# copy-paste the forwarding URL "Forwarding                    http://8929b010.ngrok.io -> localhost:8080"
# note that this url is short-lived, and is reset everytime you run ngrok
./ngrok http 8080

```

We configure our Github repository:

* go to `Settings >> Webhooks`, click on `Add webhook`.
* paste the forwarding URL (eg `http://8929b010.ngrok.io`) in field `Payload URL`) and send click on `Add webhook`. In the simplest setting, nothing more is required.

We test that everything works:

* go to <http://localhost:8080> tp check that the CI server is running locally
* go to your Ngrok forwarding URL (eg <http://8929b010.ngrok.io>) to check that the CI server is visible from the internet, hence visible from Github
* make a commit in your repository
* observe the result, in two ways:
  * locally: in the console of your first terminal window, observe the requested URL printed on the console
  * on github: go to `Settings >> Webhooks` in your repo, click on your newly created webhook, scroll down to "Recent Deliveries", click on the last delivery and the on the `Response tab`, you'll see the output of your server `CI job done`
  * on ngrok: raise the terminal window with Ngrok, and you'll also the see URLs requested by Github

We shutdown everything:

* `Ctrl-C` in the ngrok terminal window
* `Ctrl-C` in the ngrok java window
* delete the webhook in the webhook configuration page.

Notes:
* by default, Github delivers a `push` JSON payloard, documented here: <https://developer.github.com/v3/activity/events/types/#pushevent>, this information can be used to get interesting information about the commit that has just been pushed.
