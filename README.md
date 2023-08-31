# Kahala

The Kahala game, also known as Mancala, is a traditional two-player board game originating from Africa. It's played with a board consisting of several small pits or cups, with each player having their own side. The objective is to capture as many stones or seeds as possible from your opponent's side and deposit them into your larger pit. The game involves strategic moves to outwit your opponent and collect more seeds to win.

## Project Description

This project contains backend code that simulates the Kahala game.
Client applications can utilize these endpoints to present the game to their users.
![Kahala System](https://github.com/elifAkgun/kahala-system/blob/main/kahala-system.png)

# Kahala Game Features

Kahala implementation includes the following features:

- **Game Initialization:** The game board is initialized with a specified number of stones in each pit.

- **Player Turn Management:** The game keeps track of each player's turn to make a move.

- **Valid Move Validation:** The system ensures that the chosen move is valid according to the game rules.

- **Stone Distribution:** When a player selects a pit, stones are distributed to other pits following the game rules.

- **Capture Stones:** Capturing stones from the opponent's pits is implemented.

- **Game End Detection:** The game ends when one player's pits are all empty.

- **Move Validation:** Validating the selected pit for a move is part of the game.

- **Game State Management:** The game maintains the current state of the board and player turns.

- **Game Reset:** The game can be reset to its initial state.

- **Win Condition:** The game determines the winner based on the player with the most stones in their large pit.

- **User Interaction:** The game provides a user interface for players to interact with and make moves.

- **Multiplayer Support:** The game supports multiplayer gameplay.

- **Error Handling:** Error handling is implemented for invalid moves and game over scenarios.

- **Scoring Display:** The current score of each player is displayed.

- **Game Replay:** Players can review the game history and replay moves.

## Usage Guide

# Kahala Game API Calls

## 1. Get User

**Request:**
```http
GET /kahala/users/de92501e-37be-4da4-b0af-82646a390dc3
```

## 2. Create User

**Request:**
```http
POST /kahala/users
Content-Type: application/json

{
  "userName": "first"
}
```

## 3. Create Game

**Request:**
```http
POST /kahala/games
Content-Type: application/json

{
  "firstPlayerId": "f22e6c36-f36b-4db6-8cce-a6b37fd4c5a0",
  "secondPlayerId": "26fa9531-57a1-4c09-b8d6-6043f0085889"
}
```

## 4. Get Game

**Request:**
```http
GET /kahala/games/61754050-9800-4da3-aa6e-4bec5ac7475e
Content-Type: application/json

{
  "firstPlayerId": "123",
  "secondPlayerId": "1234"
}
```

## 5. Move

**Request:**
```http
PUT /kahala/games/move/61754050-9800-4da3-aa6e-4bec5ac7475e
Content-Type: application/json

{
  "playerId": "685c3dda-89db-4ee4-9197-ce2a397c8766",
  "pitNumber": "1"
}
```

## 6. Reset Game

**Request:**
```http
DELETE /kahala/games/1f94845d-e44c-4329-9eb1-6358a7fa80cd
```

## Requirements

---

### System Requirements

To ensure the smooth operation of the project, please make sure your system meets the following requirements:

### Redis Installation

The project requires a running instance of Redis, an open-source in-memory data store. Redis is used to cache and manage game data, enhancing performance and responsiveness. If Redis is not already installed on your server, you can follow these steps to set it up:

1. **Install Redis:** Depending on your operating system, you can install Redis using package managers like `apt` for Debian-based systems or `brew` for macOS. For example, on a Debian-based system, you can use the following command:
   ```
   sudo apt-get update
   sudo apt-get install redis-server
   ```

2. **Start Redis:** After installation, start the Redis server using the following command:
   ```
   sudo systemctl start redis
   ```

3. **Enable on Boot:** To ensure Redis starts automatically upon server boot, enable it as a service:
   ```
   sudo systemctl enable redis
   ```

4. **Verify Installation:** You can verify that Redis is up and running by connecting to the Redis server:
   ```
   redis-cli ping
   ```

Please make sure to have Redis configured and running on your server before deploying the project. The application relies on Redis for efficient data management and caching, contributing to a seamless gaming experience.

---

## Project Installation Guide

---

To run the "kahala-system" Spring project, follow the steps below:

### Prerequisites

- Java Development Kit (JDK) 17 or higher installed on your system.
- Git installed to clone the repository.
- Maven installed to build the project.

### Installation Steps

1. #### Clone the Repository:

   Open your terminal and navigate to the directory where you want to clone the project. Then run the following command to clone the repository:

   ```sh
   git clone https://github.com/elifAkgun/kahala-system.git
   ```
   
2. #### Install Project ApiGateway

   1. **Navigate Project Folder and Build the ApiGateway Project:**

      Change your directory to the newly cloned project folder:

      ```sh
      cd kahala-system/ApiGateway
      ```
      Build the project using the following maven command:
      ```sh
      mvn clean install
      ```
      This command will download the required dependencies and build the ApiGateway project.

   2. **Run the Application:**
      Run the application using the following command:
      ```sh
      java -jar target/gateway-<version>.jar
      ```
      Replace <version> with the actual version of the JAR file generated in the target directory.

3. #### Install DiscoveryServer
   1. **Navigate Project Folder and Build the DiscoveryServer Project:**

      Change your directory to the newly cloned project folder:

      ```sh
      cd kahala-system/DiscoveryServer
      ```
      Build the project using the following maven command:
      ```sh
      mvn clean install
      ```
      This command will download the required dependencies and build the DiscoveryServer project.

   2. **Run the Application:**
      Run the application using the following command:
      ```sh
      java -jar target/discoveryServer-<version>.jar
      ```
      Replace <version> with the actual version of the JAR file generated in the target directory.

4. #### Install Kahala
   1. **Navigate Project Folder and Build the Kahala Project:**

      Change your directory to the newly cloned project folder:

      ```sh
      cd kahala-system/kahala
      ```
      Build the project using the following maven command:
      ```sh
      mvn clean install
      ```
      This command will download the required dependencies and build the Kahala project.

   2. **Run the Application:**
      Run the application using the following command:
      ```sh
      java -jar target/kahala-<version>.jar
      ```
      Replace <version> with the actual version of the JAR file generated in the target directory.

5. #### Access the Application:

   Once the application is running, you can access it using a web browser or tools like Postman. The base URL will be `http://localhost:9090`.

   Example endpoints:
    - GET User: `http://localhost:9090/kahala/users/USER_ID`
    - POST Create User: `http://localhost:9090/kahala/users`
    - POST Create Game: `http://localhost:9090/kahala/games`
    - GET Game: `http://localhost:9090/kahala/games/GAME_ID`
    - PUT Move: `http://localhost:9090/kahala/games/move/GAME_ID`
    - DELETE Reset Game: `http://localhost:9090/kahala/games/GAME_ID`

Please ensure that you have the required JDK version and Git installed before proceeding with the installation. The provided commands assume you're using a Unix-like terminal. Adjust the commands accordingly if you're using a different environment. You can also use [Kahala.postman_collection](https://github.com/elifAkgun/kahala-system/blob/main/Kahala.postman_collection.json).

---

You can also play the game with this swagger-ui http://localhost:9090/kahala/swagger-ui/index.html

![swagger-ui](https://github.com/elifAkgun/kahala-system/blob/main/swagger-ui.png)

---


This guide provides step-by-step instructions to clone, build, and run the "kahala-system" Spring project on your local machine.

---

## License

The "kahala-system" project is licensed under the [Apache License 2.0](https://github.com/elifAkgun/kahala-system/blob/main/LICENSE).

---

