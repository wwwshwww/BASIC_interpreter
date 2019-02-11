# BASIC Interpreter
Basicライクな言語のインタプリタだよ

## Getting Started
1. `javac src/*/*.java`
2. `cd src`
3. `javac Main.java`
4. take Basic file into src/
5. `java Main <Basic file name>`

## Details
### Result View
```
basic interpreter
=========parsed=========
<expression of parsed syntax>
=========output=========
<outputs of program>
========================
```

## Example of execution
### test1.bas
#### source
```
a = 5
DO UNTIL a < 1
	PRINT "Hello World"
	a = a - 1
LOOP
END
```
#### result
```
basic interpreter
============parsed============
a[5];UNTIL_LOOP[[PRINT["Hello World", ];a[-[a, 1]];]<[a, 1]];END;
============output============
Hello World
Hello World
Hello World
Hello World
Hello World
==============================
```

### test2.bas
#### source
```
FOR i = 1 TO 10
    IF i = 2 THEN
        d = 1
        DO
            PRINT "Hello World" + "aaaaaa!"
            PRINT d
            d = d * 2
        LOOP WHILE d < 1000
    ELSEIF i = 5 THEN
        PRINT "five"
    ELSEIF i = 8 THEN
        PRINT "eight"
    ELSE
        PRINT "Yeah!"
    ENDIF
NEXT i
PRINT i + 1
PRINT SQRT(d)
PRINT MAX(d, i)
PRINT MIN(d, 20)
END
```
#### result
```
basic interpreter
============parsed============
FOR[[i[1]->10][IF[=[i, 2][d[1];WHILE_LOOP[<[d, 1000][PRINT[+["Hello World", "aaaaaa!"], ];PRINT[d, ];d[*[d, 2]];]];]]ELSEIF[=[i, 5][PRINT["five", ];]]ELSEIF[=[i, 8][PRINT["eight", ];]]ELSE[PRINT["Yeah!", ];];]];PRINT[+[i, 1], ];PRINT[SQRT[d, ], ];PRINT[MAX[d, i, ], ];PRINT[MIN[d, 20, ], ];END;
============output============
Yeah!
Hello Worldaaaaaa!
1
Hello Worldaaaaaa!
2
Hello Worldaaaaaa!
4
Hello Worldaaaaaa!
8
Hello Worldaaaaaa!
16
Hello Worldaaaaaa!
32
Hello Worldaaaaaa!
64
Hello Worldaaaaaa!
128
Hello Worldaaaaaa!
256
Hello Worldaaaaaa!
512
Yeah!
Yeah!
five
Yeah!
Yeah!
eight
Yeah!
Yeah!
12
32.0
1024
20
==============================
```

### test3.bas
#### source
```
num = 0
result = 0
WHILE result < 1000
    result =  result + num * num
    PRINT num
    PRINT result
    PRINT "---------"
    num = num + 1
WEND
PRINT "root 1032-2*2*(4-2) : "
PRINT SQRT(1032 - 2 * 2 * (4 - 2))
END
```
#### result
```
basic interpreter
============parsed============
num[0];result[0];WHILE_LOOP[[result[+[result, *[num, num]]];PRINT[num, ];PRINT[result, ];PRINT["---------", ];num[+[num, 1]];]<[result, 1000]];PRINT["root 1032-2*2*(4-2) : ", ];PRINT[SQRT[-[1032, *[*[2, 2], -[4, 2]]], ], ];END;
============output============
0
0
---------
1
1
---------
2
5
---------
3
14
---------
4
30
---------
5
55
---------
6
91
---------
7
140
---------
8
204
---------
9
285
---------
10
385
---------
11
506
---------
12
650
---------
13
819
---------
14
1015
---------
root 1032-2*2*(4-2) :
32.0
==============================
```