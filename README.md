# sokoban-solver

A sokoban solver written in Java (java version "1.8.0_191").\
Inluded in the project is an [older version of Ysokoban](http://ygp.orgfree.com/pgms/ysokoban_1_620.zip) which is excellent for visually playing a solution as a macro.

---

# Usage instructions

## Solve example level.txt

From the command line at the project top directory, run from a command prompt:
    java -cp bin Solver level.txt

This will print the solver steps and the solution at the end, along with some statistics.\
An example run is shown in the file [level.solution.example](level.solution.example).

## Play a solution using Ysokoban

- Open the level file in Ysokoban.
- Click the Macro button.
- Paste the solution string into the text area.
- Click the Play Macro button.

## Solve multiple levels in a for loop

Some example code is available in [command_line.txt](command_line.txt).\
The output from this example code is recorded in the following files:\
[auto52_solutions_-hashMap.txt](auto52_solutions_-hashMap.txt)\
[handmade_solutions_-hashMap.txt](handmade_solutions_-hashMap.txt)

# Algorithm outline

## Terminology

- box position: the location of a given box
- sokoban position: the location of the sokoban player
- target position: a location where a box should be pushed to in order to win the game
- field: a location on which either the sokoban player or a box potentially can be located
- free field: a field where no box is located
(NOTE: the sokoban player can be located on a free field)
- wall: a location which is not a field

- game board: the collection of all fields
- free island: the maximum collection of free fields adjacent directly or indirectly to a given free field
(NOTE: the game board contains 1 or multiple free islands)
- sokoban island: the free island containing the sokoban position as the given free field for this free island
(NOTE: we can uniquely represent a sokoban island by its sokoban position)

- configuration: the set of all box positions, along with the sokoban position
(NOTE: a configuration represents a solving step during the game)
- root configuration: the configuration when beginning a sokoban level
- top configuration: a configuration in which all box positions are on the target positions and the sokoban position is in 1 of potentially multiple free island
(NOTE: a given sokoban level can have 1 or multiple end configurations)

- root: the start configuration
- top: the set of end configurations

## Strategy

From a given configuration we can create multiple (potentially 1 or none) new configurations by following the rules of the sokoban game.\
Each potential movement of a box by the sokoban player will generate such a new configuration.\
We can represent this process in a tree with a configuration on each tree node.

This way we can explore the search space of the game both from the root and from the top.<
From the top we generate a new configuration by pulling a box with the sokoban user, since reversing that proces results in a valid step in the game.

Searching both from the root and from the top, we have multiple search trees during the solving process.\
There is 1 tree from the root and 1 tree or multople trees from the top.\
At each step we keep track of the number of leaf configurations.\
In each solving step from the root we obtain all possible new leaf configurations resulting from moving only 1 box in 1 direction.\
In case of searching from the top, we obtain leaf configurations by pulling 1 box in 1 direction.

At each solving step we choose to do this for either root or top.\
The choice is determined by which of both root and top has currently the least leaf nodes.\
This is a heuristic optimization, since probably less computation is required to search one level deeper in the search tree at the end with the least leaf nodes.

We check for doubles, so each configuration during the solving process is only once kept in memory.\
This means each search tree is actually a directed graph which can be cyclic, but by removing doubles it remains a tree.

Whenever a leaf node from the root and the top match we have a path and thus a solution for the sokoban level.\
We then can construct the solution steps by following the tree path from root to the unque top configuration connected to that path.

Since the search is breadth first, if we find a solution, this will be an optimal solution, meaning the number op pushes is minimal.\
Note however that the number of moves of the sokoban player may not be minimal.

## Preprocessing

We calculate all fields from which a box can still be moved from at least 1 side with the sokoban player.\
We also calculate all top configurations, by calculating all free islands of any one of the top configurations.

## Optimizations

We discard configurations in which boxes and walls form a square and for which the boxes involved in this square are not all on target positions.

A configuration is only represented by the box positions and the sokoban position.<
We consider 2 configurations identical if all box positions from both configurations match and if both sokoban positions are in the same free island.\
This greatly reduces the search space and was the motivation for trying to implement this method.

Another crucial optimization is choosing a good heuristic for determlining if 2 configurations are considered identical.\
To do this efficiently, the collection of leaf configurations is implemented in Java as a hashmap.\
The hash of a configuration is the string obtained by concatenating 2 values with a dot in between:\
on one hand the sum of horizontal coordinates of boxes and on the other hand the sum of vertical coordinates of boxes.\
After some experimentation, this turned out to be a hash function by which we have a good ratio between,\
at one end, the number of distinct hash values of all configurations in a search tree,\
while on the other hand, the number of configurations with the same hash value.\
Checking if 2 configurations are equal is at the core of the algorithm, so this hash function greatly determines the performance.
