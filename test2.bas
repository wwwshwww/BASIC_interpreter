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