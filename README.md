# assignment-2

# Task
## Develop a Java program for the HighSum game Administration module.
### Administration Module
This module allows the administor to
1. Create a player
2. Delete a player
3. View all players
4. Issue more chips to a player
5. Reset player’s password
6. Change administrator’s password
7. Logout


This module will have access to two files (admin.txt and players.bin)
The first text file (admin.txt) contains the administrator hashed password (SHA-256).
For example
0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e
Note: Refer Reference and Utility.java function getHash() function



The second text file (players.bin) contains the following players object information in a binary
serialized file.

• Login Name
• Password in SHA-256
• Chips

Login Name Password in SHA-256 Chips
BlackRanger 6cf615d5bcaac778352a8f1f3360d23f02f34ec182e259897fd6ce485d7870d4 1000
BlueKnight 5906ac361a137e2d286465cd6588ebb5ac3f5ae955001100bc41577c3d751764 1500
IcePeak b97873a40f73abedd8d685a7cd5e5f85e4a9cfb83eac26886640a0813850122b 100
GoldDigger 8b2c86ea9cf2ea4eb517fd1e06b74f399e7fec0fef92e3b482a6cf2e2b092023 2200
Game play module
Modify Assignment 1 HighSum Game module such that the Player data is read from the binary file
players.bin. And different players are able to login to the GameModule.

---
![U5lv1tVURB](https://user-images.githubusercontent.com/131016233/236362398-cebe6ced-2500-450a-af8c-f1ef37420d3d.png)

