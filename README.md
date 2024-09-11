# Final Exam Sirma Academy: 

## Task: 

Pair of Football Players Who Played Together in the Same Matches for the Longest Time

## My Understanding of the Task

`First task` - I need to identify the pair of football players who spent the longest cumulative time on the field together in matches, where both players participated.

`Second task` - I need to find the matches and the time the players played together in each of them. 

### Motivation

1. **Tournament Format**: Since the tournament format involves only one match between the teams, identifying players from different teams, which played together is less meaningful. The focus should be on intra-team dynamics.

2. **Team Dynamics**: By identifying the longest-playing pairs within the same team, you can gain insights into team cohesion and how well players work together over the course of the matches.

## Algorithm

### Goal: Find player pairs from the same team who played together the longest.

**Process**:

1. Fetch the Players and Records from the DB.
2. Find pairs from the same team.
3. For each player find the records where both footballers played.
4. For each match it finds the ovelapping minutes where both players met.
5. Each pair is stored with:
    * Total time they played together
    * The matches with total time for each match, where both played
6. Final step is filtering the pairs with the longest total time played together.
