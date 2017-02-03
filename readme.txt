HelloWorldApp:
-------------

Handles a GET request for /hello-world and returns the Greeting resource in JSON format
	{"id":2,"content":"Hello, Stranger!"}

Run:
D:\SpringBoot\HelloWorld>java -jar build/libs/helloWorld-0.1.0.jar

Test:
http://localhost:8000/hello-world

Result:
Displays a JSON format	{"id":2,"content":"Hello, Stranger!"}


How it works:
-------------
1. The @RequestMapping  annotation maps the url ("/hello-world") to this controller action.

Id increments after every http request