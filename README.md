# TuringMachine
A Turing machine interpreter made in Java using Processing libraries.

## Description
In [this Wikipedia article](https://en.wikipedia.org/wiki/Turing_machine) it is explained what a Turing machine is.
What I have made is a "simulator" of that machine.

## Programming in Turing Machine code
Inside the [exmaples folder](https://github.com/margual56/TuringMachine/tree/master/Examples), there are 5 example programs of how to code a Turing Machine program. The extension of the files __has__ to be `.tm`.

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

Comments can be written using `//`

# Use the interpreter
Go to the [releases page](https://github.com/margual56/TuringMachine/releases) and download the latest version. It is programmed in Java, so it is cross-platform.<br/>
When you execute it, it will prompt you to select the ".tm" file (the turing machine code) you want to execute. Select it and it will start running.
* Press the __space bar__ to pause or unpause the execution.
* When it finishes the execution, press __any key__ to restart the execution.
* Currently, there is no way to speed up/down the execution whithout modifying the code, but it is possible.

# License
[GNU GPLv3](https://choosealicense.com/licenses/gpl-3.0/)
