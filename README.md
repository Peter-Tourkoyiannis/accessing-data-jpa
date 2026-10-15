**List Of HTTP Requests In MVC** - Host = http://localhost:8080



**Display All Address Books**

&#x09;GET /addressbooklist HTTP/1.1



**Create Address Book**

&#x09;POST /addressbooklist HTTP/1.1



**Add BuddyInfo to Address Book #**

&#x09;POST /addressbooklist/#/buddies HTTP/1.1

&#x09;	Note: # should be replaced with the specific Address Book ID.

&#x09;Format: \&name="NAME-GOES-HERE"\&phoneNumber="XXXXXXXXXX"

