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