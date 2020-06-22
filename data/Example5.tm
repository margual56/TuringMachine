//SUMATORY FROM 1 TO X

{q01111};

#define F = {f};

//Eliminate the first one
(q0, 1, 0, R, q1);

//Slip a one
(q1, 1, 1, R, q2);

//STOP
(q1, 0, 0, R, r0);

//Leave a mark
(q2, 1, 0, R, q3);
(q2, 0, 0, R, q1);

//Go to the end of x
(q3, 1, 1, R, q3);
(q3, 0, 0, R, q4);

//Go to the end and add 1
(q4, 0, 1, L, q5);
(q4, 1, 1, R, q4);

//Go to the separator
(q5, 1, 1, L, q5);
(q5, 0, 0, L, q6);

//Search for the mark, remove it and start over
(q6, 1, 1, L, q6);
(q6, 0, 1, R, q2);

//Move all the ones together

//Move to the end of the numbers
(r0, 0, 0, R, r1);
(r0, 1, 1, R, r0);
(r1, 0, 0, R, r2);
(r1, 1, 1, R, r0);

//Go to the beggining of the last number
(r2, 0, 0, L, r2);
(r2, 1, 1, L, r3);
(r3, 1, 1, L, r3);

//Replace the spacing with a 1 and go right to delete the first one
(r3, 0, 1, R, r4);
(r4, 1, 0, R, r0); //And repeat

//Check if finished
(r4, 0, 0, H, f); 

(f, 0, 0, H, f);
(f, 1, 1, H, f);