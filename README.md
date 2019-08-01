# asMessaging
Is a compact app, that allows sending and receiving messages using a REST API

**Case of uses**
* Register a User
* Login a User
* Logout a User
* Send a message(text, video or image content) to a system User
* Recieve messages from a particular User

**Tecnnologies**
* Kotlin Language
* Spark Framework
* Google Guice
* Exposed
* Jackson
* SQLite
* Logback

The intention of using this stack of apps, is to create a light-weight app, suitable 
for microservices architecture. In this case this is a state app, but I´d preferred to have a stateless app with the logic
connected to other apps, like sign in app and messages persistence app. I could have used
NewRelic for logging and controlling, a circuit braker like Hystrix for charge control between apps, and
a centralized log.  

**How to use**
1) Cloning GITHub repository
    `git clone git@github.com:leandrointelisano/asMessaging.git`
    
     
     Opening the project and running Main.kt (main() function)
2) Recommended: java -jar com.asapp.asMessaging.challenge-1.0-SNAPSHOT-jar-with-dependencies.jar in a console (JRE 8 required) 

3) The app will listen on port 9290. So all requests must be done to:

localhost:9290/

* users -> POST with request body:

`{
 	"username": "user1",
 	"password":"pass1"
 }`
 
 
     Response:
     
     `{
        "id": 1
      }` 
  
  User id used along the system
  
* login -> POST with request body

`{
 	"username": "user1",
 	"password":"pass1"
 }`


 
     Response:
     
     `{
        "id": 1,
        "token": "C0n3E9b1uh"
      }`
  
  
  Where id is the logged user id and token is the token to be used as request header for authentication
  
* logout -> POST with request body

`{
 	"username": "user1",
 	"password":"pass1"
 }`
 
     Response:
     `{}`
  Note: Response is always empty due to security policy
  
 
  Required headers:
  * Content-Type: application/json
 
 
 Note: password field is not verified since logout operation is not a restricted operation
 

* messages -> GET with query params (recipient=int, start=int, limit=int?)

recipient = is the user who has received messages

start = start message index

limit = optional, limit the amount of messages to get

 Required headers:
 * Content-Type: application/json
 * token: sender user token (provided by login endpoint)
 
            Response:
            
            `[
            {
            "id": 1,
            "sender": 1,
            "recipient": 1,
            "content": {
            "type": "video",
            "height": 200,
            "width": 200,
            "url": "url"
            },
            "timestamp": "2019-07-23T20:38:52.517Z"
            },
            {
            "id": 2,
            "sender": 1,
            "recipient": 1,
            "content": {
            "type": "video",
            "height": 200,
            "width": 200,
            "url": "url"
            },
            "timestamp": "2019-07-23T20:39:10.462Z"
            },
            {
            "id": 3,
            "sender": 1,
            "recipient": 1,
            "content": {
            "type": "video",
            "height": 200,
            "width": 200,
            "url": "url"
            },
            "timestamp": "2019-07-23T20:39:12.057Z"
            }
            ]`

* messages -> POST with body:

Examples:


`{
   "sender": 1,
   "recipient": 1,
   "content": {
     "type": "text",
     "text": "string"
   }
 }`
 
 `{
    "sender": 1,
    "recipient": 1,
    "content": {
      "type": "image",
      "url": "string",
  		"height": 200,
  		"width": 200
    }
  }`
  
  `{
        "sender": 1,
        "recipient": 1,
        "content": {
          "type": "video",
          "url": "string",
      		"height": 200,
      		"width": 200
        }
      }`
      
      Response:
      `{
         "id": 12,
         "timestamp": "2019-07-24T01:58:55.603Z"
       }`
      
 Where sender and recipient are userIds (int)
 content must have type text, video or image
 
 Required headers:
 * Content-Type: application/json
 * token: sender user token (provided by login endpoint)
