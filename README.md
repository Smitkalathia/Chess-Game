# Chess Game Web Application with Web Chat Server

## Project Information
This project includes a Chess Game Web Application which provides an interactive chess-playing experience online. It is designed to be played in a web browser, utilizing web technologies like HTML, CSS, and JavaScript to create an engaging user interface. The game features a WebSocket connection for real-time gameplay, allowing players to connect with others. With this project, users can also establish and join various chat rooms on the web in real-time to keep in touch while playing the game. Because Java and WebSocket technology were used in its development, users may expect a responsive and live chat experience.
### Video Demonstration
Live chatroom:
https://youtu.be/PPv0bU_9Zuo
Chess Game:
https://youtu.be/6iKrftZGUD4
### Group Members
- Smit Kalathia - Front End (JS, CSS And HTML), README file
- Bav Sandhu - Front End (JS, CSS And HTML)
- Bohdan Synytskyi - Game Engine, Back End, JS
- Nimreet Gill - Back End

## Improvements
We have made the following improvements to enhance user experience and interface:
-includes a fully interactive chessboard where users can click on chess pieces to see possible applicable moves pulsing in green.
-the game supports real-time interactions, as it updates moves instantaneously on the opponent's screen with the use of WebSockets.
-enhanced user sessions and chat room management on the server side, improving the application's scalability and efficiency.
-Improved the chat application's navigation to make it simpler for users to navigate between chat groups and quickly access important features.
-We improved the user experience by making it clearer and easier to navigate and communicate with others.
-The colours of the chat room and "Create New Room" button have been updated to better match the application's overall look and improve user experience.
-Visually appealing layout, with the about button being moved to opposite end, and having same size as button leading to chatroom page
-About page followed same styling as the main chatroom page to keep consistency
## How to Run

To run the application locally, follow these steps:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/OntarioTech-CS-program/w24-csci2020u-final-project-synytskyi-gill-kalathia-sandhu.git
## Open the Project in IntelliJ IDEA:
1. Open IntelliJ IDEA.
2. Choose `File > Open` from the menu.
3. Browse to the directory where the repository was cloned.
4. Select the project folder and click `OK`.

## Edit the Configuration:
1. Click on `Run > Edit Configurations` within IntelliJ IDEA.
2. Add a new configuration for the GlassFish Server.
3. Set the application context to the appropriate path as needed: http://localhost:8080/WSChatServer-1.0-SNAPSHOT/

## Build the Project:
- Use `Build > Build Project` in IntelliJ IDEA to compile the project.
## Run the Application:
1. Click on the run icon to start the GlassFish Server.
2. Click on the appropriate button for what you want to do (play chess or chat or info about the project)
## Other Resources
Demo code from lecture and class activity material were used  with the help of web sockets.
Used https://www.florin-pop.com/blog/2019/03/css-pulse-effect/ for help with creating the pulse animation for chess piece movement
## Contribution Report
- Smit Kalathia: 25%
- Bav Sandhu: 25%
- Bohdan Synytskyi: 25%
- Nimreet Gill: 25%

Equal contributions have been made by all team members in the development of this chat application.