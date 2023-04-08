# Course project

## What will the application do?
This application will be a recreation of the traditional minesweeper with some added features. 
The application should be able to:
- Generate a bomb layout upon first click
- Save scores associated with usernames
- Be able to generate an average score for a player, based on saved scores
- Be able to create customizable layouts (customized grid size, number of bombs, etc)


In short, you should be able to play minesweeper on it and 
save and view your times.

## Who will use it?
I imagine that it would be used by people who like playing puzzle games or perhaps just enjoy playing minesweeper. 

## Why is this project of interest to you?
This was inspired by a game that I like to play called Demoncrawl.
Demoncrawl is a rogue-like minesweeper where you solve boards in succession. 
I enjoy playing games and my original idea probably wasn't feasible to accomplish. 

## User stories
As a user, I want to be able to:
- be able to flag a bomb.
- be able to see my times.
- be able to see times for certain map sizes
- be able to use the middle click function (known as chording).
- be able to end the game on fail. 
- be able to add cells to my grid to change the dimensions
- be able to add my times to scores
- be able to stop and save my minesweeper game 
- be able to resume my last minesweeper game


## Instructions for Grader
- You can generate the first required action related to adding Xs to a Y by winning a game (after selecting play on the first menu) and choosing to add the time
- You can generate the second required action related to adding Xs to a Y by going to leaderboard. Select an option from the dropdown menu to show scores for a specific map size
- You can locate the visual component of my application by selecting play. From there, you can select a cell. The labels will update with each click
- You can save the state of my application by selecting the save button after selecting play. 
- You can reload the state of my application by clicking load. This loads the saved board onto the screen.

## Phase 4: Task 2 
Fri Apr 07 16:19:05 PDT 2023

Changed width to 5

Fri Apr 07 16:19:05 PDT 2023

Changed height to 5

Fri Apr 07 16:19:05 PDT 2023

Changed number of bombs to 10

Fri Apr 07 16:19:52 PDT 2023

Added a time for l

## Phase 4: Task 3

If I were to refactor the project, I would likely change
what I was actually storing in between programs. 
I find that I'm likely saving more details than necessary. 
If I were to change what information was stored, then I could likely
change Game into a Singleton class. In that case, I would only save the
board and leaderboard information. Then I would know
that I was always changing the same instance and that I wasn't
copying over the wrong values when trying to get the appropriate 
parts from the stored Game. The downsides may include 
include greater difficulty in trying to make temporary changes.


I would also try to reduce the amount of associations there are in the UI classes. 
Within my Leaderboard class, I don't actually need to store a reference to game since
the class doesn't ever change anything to do with the game.

There are chances are that some of the functions in game could
be moved to grid. Then game could lose it's dependencies on cell
which could improve bug hunting if that occured. 