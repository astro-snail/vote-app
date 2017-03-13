# Technical Challenge – Explanation Notes

## Assumptions
The assumptions I was making when working on this challenge:
-	In an online vote unrestricted number of users can submit their votes;
-	There are several candidates to vote for;
-	Each user has as most 3 votes to cast, and any subsequent votes won’t have effect on the result;
-	The votes should be counted close to real-time so the response time should be very quick;
-	Since there are restrictions as to number of votes per user there should be some means of identifying a user. I chose user’s phone number, having in mind TV votes via SMS (e.g. Eurovision Song Contest). As an alternative, it could have been possible to identify a user by their IP address or to use cookies to save the number of votes the user has already cast. The user can also be required to sign in before they can vote. In this case their login would serve as an identifier. The phone number identification was chosen because of the ease of testing and because of existing examples in real life.

## Where to keep votes
The votes can reside in memory or be stored in the database. 
Storing data in memory have the advantage of speed, but disadvantage of memory usage. Database storage is fault-tolerant and once committed, the votes won’t be lost. Persisting votes also allows for post-vote analysis should it be required. Having this in mind, I chose to persist votes in database.
However, after completing the task and when doing stress testing with appx. 6 million records I realised, that I should have looked for other alternatives – the performance was unacceptable.
For the purposes of this task I chose Apache Derby database mainly because it is included in JDK and can be easily distributed with the application.

## Database design
There are 3 tables in the databese:
- CANDIDATES
- USERS
- VOTES

Candidate details are stores in CANDIDATES table in the form of CANDIDATA_ID (freely assigned number, primary key) and CANDIDATE_NAME.

Since it was required to restrict the number of votes per user, a table USERS is used that stores the details of all users who took part in the vote so far. Each user is represented as USER_ID (the primary key, which is the phone number converted to BIGINT) and VOTE_COUNT (number of times this user has voted).

When a user sends their vote, first a query is run against this table to check how many votes the user has already used. If the count has reached the maximum allowed number of votes (3), than the vote is not accepted.

If the vote is accepted, it is stored in the main table VOTES that is “insert-only” to minimise locking issues. Also once the vote is accepted, the user’s VOTE_COUNT is incremented by 1.

It could have been also possible not to have a separate USERS table but simply perform COUNT on VOTES table for the given USER_ID to find out how many time the user has already voted. I considered this option (1 table lookup and update less) but decided against it. The lookup in USERS table is performed using the primary key (USER_ID) and is bound to be faster than COUNT on VOTES. Also, there are should be no locking issues with USERS table since each record is update by 1 user only (the user whose USER_ID is that) and at most 3 time.

## Vote count
The database design and implementation of the number of used votes check early are supposed to facilitate the vote count, because it is not required to consider which votes should be accepted and which not.

Actual vote count is executed as a query run against table VOTES. The time required is more than 1 second. The performance degrades with time as the table grows, because it stores raw (not aggregated) data.

A solution to this could have been an aggregation query, that picks up only recent votes (for example, it can use timestamps to identify unprocessed votes) and aggregates them as the total number of votes cast for each candidate. Then it accumulates this and previously aggregated results. In this case obtaining current number of votes would amount to reading data from a small aggregated table and therefore must be very quick. This however is not implemented.

## Tools
I chose to use plain Java Servlets and JSP to solve the challenge because it would allow me to focus on the task rather than framework details.
The choice of the database was mainly due to portability and my familiarity with relational databases. It could have been possible to consider NoSQL databases, but that was not done due to time constraints.

## Setup
The database is created automatically when the servlet starts (if was not created earlier). Then tables are created and some fictitious candidates are inserted. There is a script that contains all the statements executed at start up.
The database URL is relative to the web application root directory as <root>/Databases/VoteDB. Database connection details are defined in the CONTEXT.XML file.

## Build
To run the application please deploy the VOTE-APP.WAR file to the web server. 
