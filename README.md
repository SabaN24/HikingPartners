# HikingPartners
Web application which allows people to find hiking partners based on common interests - OOP (CS108) Final Project.
Users: nodo24, levani132, fkavache, sanddro, SabaN24.

# Full description:
Hiking Partners is a web application, which will be used by people who want to go hiking, but can not find proper partners around themselves (among their friends, relatives, co-workers, etc.). Application is developed using Java as Server-Side language and HTML, JavaScript, CSS as Client-Side language. Java codes are developed and executed using IntelliJ Idea IDE. Database is administrated using MySQL workbench. This repository is used as Version Control tool. All changes applied to code can be seen in this repository.

Java codes are written using Google Java Style. (See example here: https://google.github.io/styleguide/javaguide.html).

# Main features
## Sign Up/Log in
### User, who enters this webpage will sign up through Facebook account and all public information from their Facebook account will be fetched and stored in database, so that it can be used later. In case user already has an account, they will log in with their unique username and password.
## Feed
### Once signed in, user will see feed of suggested and/or frequently/recently chosen hikes. All hikes will be filtered based on user's interests and recent ratings of location. 
## Joining a hike
### Whenever an user finds suitable hike for them, they can send request for permission to the creator of given hike. Based on creator's decision, user will or will not be added to the hike.
## Creating a hike
### In case user has idea about own hike, which has not been initiated yet, they can create new hike, which will be added to the feed like all other available hikes. Joining hike will work just as mentioned above. When creating own hike, user must enter name of hike, dates, maximum number of people, cover photo and description, so that others can make their choice based on this public information.
## Hike page
### All the hikes available will have their own webpage, which will have public as well as private information. All information is stored in database. Hike information stores hike id, name, start date, end date, description, comments and maximum number of people who can join. Once user switches to a hike webpage hike name, description, cover photo, maximum number of people and public comments will be seen. Hike page will also have gallery, private comments, members' list. If user has not joined given hike, they will not be able to see private information like gallery, required items and private comments. Post can be added to hike (private as well as public), with comments. Comments and posts will have like function. 

## Project sprints:
### Sprint 1: https://github.com/SabaN24/HikingPartners/wiki/Sprint1
### Sprint 2: https://github.com/SabaN24/HikingPartners/wiki/Sprint2 
### Sprint 3: https://github.com/SabaN24/HikingPartners/wiki/Sprint3
