//Number x2

{p01111};

#deq0ine F = {q0};

(p0, 1, 0, R, p1);
(p0, 0, 0, L, p5);

//Move to the end of the number
(p1, 1, 1, R, p1);
(p1, 0, 0, R, p2);

(p2, 1, 1, R, p2);
(p2, 0, 1, L, p3);

(p3, 1, 1, L, p3);
(p3, 0, 0, L, p4);

(p4, 1, 1, L, p4);
(p4, 0, 1, R, p0);

(p5, 1, 1, L, p5);
(p5, 0, 0, R, q0);

(q0, 0, 0, H, q0);
(q0, 1, 1, H, q0);