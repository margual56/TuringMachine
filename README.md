# TuringMachine
A Turing machine interpreter made in Java using Processing libraries.

## Description
In [this Wikipedia article](https://en.wikipedia.org/wiki/Turing_machine) it is explained what a Turing machine is.
What I have made is a "simulator" of that machine.

## Programming
Inside the "data" folder, there are 5 example programs of how to code a Turing Machine program. The extension of the file is completely arbitrary and it can be changed.

Firstly, you -define the initial state of the tape (example):
```Td
{q011101111};
```

After that (in this order), you define the final state:
```Td
#define F = {f};
```

And finally, you define all the states that you want. The syntax is the following: (state, oldValue, newValue, L/R/H (meaning "left/right/halt"), goToState);<br/><br/>
Example:
```Td
(q0, 1, 0, R, q1);
```
