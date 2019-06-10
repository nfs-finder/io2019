# NFS Finder
Android app which allows racing or jogging fans find each other and quickly set up a track for a race.

## Setup
To run app:
 - clone the repository and open it in Android Studio
 - build it using Gradle
 - run it on Android Virtual Device using API 26
 
## Functions
**1.  Finding race partners:**
When user wants to race with nearby users the app finds them and provides the route of the race.
	
**2. Localization sharing on travels:**
During group travels, when user wants to share information about the group members' localization the app
provides him with it.
	
**3. Finding friends location.** 
In social model user can see the location of his friends.
	
**4. Finding jogging partners.**
When user wants to jog with nearby users the app finds them and provides the route for run.

## Use cases
 **1.  Race with unknown partner.**
 
	The primary actor is a normal user. He wants to find a person nearby and race. 
	This scenario will not succeed if there is no person nearby.
	
	1. The user logs in.
	
	2. The user clicks button 'Find race'.
	
	3. App tries to match him with person nearby. The waiting screen appears. 
	   Waiting ends when nearby user is found.
	
	4. The user sees notification about possibility of race. 
	
	* if he accepts it, the app sets the race destination and shows it to user in Google Maps screen.
	
	* if he doesn't, the screen from 2. is shown.
	
	5. The user moves towards the destination of the race.
	
	6. When the user is close enough to the destination the app shows screen 
	   notifying about end of the race and it's result.
	
	7. The user may retake the race now or try to find another partner.

 **2.  Find friends.**
 
	The primary actor is a normal user with network of his friends in the app. 
	The user may want to check his friends location or even race with them.
	 
	1. The user logs in.
	
	2. The user click button 'Find friends'.
	
	3. The system finds the location of the user's friends.
	
	4. The Google Maps screen is opened with the map of his active friends.
	
	Optional: race with friend.
	
	5. The user clicks the friend location pin.
	
	6. The app asks user if he wants to propose this user a race.
	
	7. If the user agrees the race is set for the both of them.

 **3.  Add car.**
	
	The primary actor is a normal user who wants to add new car to his account.
	 
	1. The user logs in.
	
	2. The user click button to modify his account.
	
	3. The app shows the screen with possible modifications.
	
	4. The user clicks add car button.
	
	5. The user types in the car model.
	
	6. The app saves car model data in the database of user cars.
