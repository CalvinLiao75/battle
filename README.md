# Tetris
Hey there! This is a project that I coded as my AP Computer Science final in 2018. It's a simple Tetris game in Java (Tetris-Client directory), with 1 player and 2 player options. In order to enable 2 player gameplay, you need the Tetris Node.js server that I created along with it (Tetris-Server directory).

This is by _no_ means supposed to be at any production level, just meant to be a simple game to host on a local network with friends. It has no security and the IP is hard-coded into the source code.

## Gameplay
### Controls
* **Left Arrow** — Move left
* **Right Arrow** — Move right
* **Up Arrow** — Rotate
* **Down Arrow** — Move down
* **Space Bar** — Move all the way down and get next piece
* **Escape** — Pause game

### Multiplayer functionality
Two players are playing Tetris at the same time. Each time break a row, your opponent gets a "stale row" or a row of gray blocks at the bottom of their screen, effectively giving them less space to play. If you break a row and you already have stale rows at the bottom of your screen, each row you break will first remove those stale rows before they are given to the opponent. The first person to hit the top loses.

## Installation
### Set Up Server
In order to set up the Node.js server, find your favorite environment and run this command in the `Tetris-Server` directory:
```bash
npm install
```

Now you're ready to run the server:
```bash
node index.js
```

It should tell you the port that it's running on in the console, which is port `4444`. Make note of the host's IP or domain, because you'll need to add this to the Tetris Client before you compile its source code. If you want to access it out of the scope of your local network, make sure you port-forward your network.

### Set Up Client
In the `Tetris-Client` directory, find line 19 in the file `src/InternetUtilities.java`. Change the text from `YOUR-IP-HERE.COM` to your IP or domain name.

Now you're ready to compile and run your Tetris Client.

## Images
### Title screen
![Title screen](https://i.imgur.com/7m7Zz7T.png)
### One player screen
![One player screen](https://i.imgur.com/Zp9XdO2.png)
### Instructions
![Instructions](https://i.imgur.com/CZ9L6qe.png)
### Two player screen
![Two player screen](https://i.imgur.com/SmOGDxJ.png)