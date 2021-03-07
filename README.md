# Degree Project

Implementation of a software agent for the backgammon variant called Swedish Tables. The development of the agent is based on TD-Gammon, by G. Tesauro, a neural network that trains itself
to be an evaluation function for the game of backgammon by playing against itself and learning from the outcome.

### Training the agent

To train the agent you have to set the number of games, decay parameter(λ) and learning parameter(α). Simply run:

```java
java Training 10000 0.7 0.1
```

### Run the agent

To test the trained agent, simply run:

```java
java TestStrength
```
