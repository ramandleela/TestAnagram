Test Anagram:
-------------
This is a microservice running in Tomcat JVM. It provides REST endpoints.
It has a Service Test included. It has no persistance store.
The dictonary of words are packaged with the service.

Usage:
------
http://localhost:8010/word/glare



API specification
------------------
Request : GET /word/:word
Response :
Returns 200 OK if the word exists in the dictionary.

GET /word/glare

HTTP 200 OK

{
    "word": "glare",
    "anagrams": [
      "Alger",
      "lager",
      "large",
      "regal"
    ]
}

Notes:
------

It anagrams that are single words. For example, if word is hear, a her is not an anagram.
Anagrams returned comes only from the provided list of words.
The list of words in anagrams does not contain the requested word itself.
The list of words in anagrams are sorted alphabetically.
The list of words in anagrams are returned in their original case in the dictionary.

Error and Exceptions:
---------------------
Returns 404 Not Found if the word does not exist in the dictionary, with the response body

Example:
GET /word/asdfasdf1234

HTTP 404 Not Found

{ "message" : "Couldn't find word asdfasdf1234" }

Get/<space> Returns simply '404 Not Found'