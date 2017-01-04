# Matrix Calculator

# Supported Calculations
- Addition/Subtraction
- Matrix Multiplication
- Scalar Multiplication
- Transpose
- Determinant 
- Inverse Matrix
- Cofactor Expansion
- Reduced Row Echelon Form
- Dot Product
- Cross Product

# Features
- Name and Save Matrices
- Save Result of Calculation
- Display All Saved Matrices
- Rename and Remove Matrices
- Load Commands From a File
- Step by Step Calculations for Row Reduced Echelon Form

# Help.txt

```
  Matrix Calculator 

  Examples: 

   mat: 
   |       1       2       3       4       |       Representation of saved 4 x 4 matrix mat. 
   |       5       6       7       8       |       Command used: 
   |       9       10      11      12      |       mat 4 x 4 { 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 } 
   |       13      14      15      16      | 

  To add matrix M1 to M2:                         M1 + M2
  Same as above but save to M3:                   M1 + M2 -> M3

  *Anything in parentheses indicates optional command arguments*

  Commands:
  M R x C { A1 A2 ... An }    Saves a matrix M with dimensions R x C with n = R x C entries to input.
  rename M1 M2                Renames matrix M1 to M2.
  remove M                    Removes matrix M.
  display                     Prints saved matrices.
  reset                       Resets saved matrices.
  M ?                         Displays matrix M.
  M1 + M2 ( -> M3 )           Adds matrices M1 and M2. Save to matrix M3 if desired.
  M1 - M2 ( -> M3 )           Subtracts matrices M1 and M2.
  M1 * M2 ( -> M3 )           Multiply matrix M1 by a matrix or by a constant C.
  C * M1 ( -> M2 )            ...
  M1 * C ( -> M2 )            ...
  M1 ^ T ( -> M2 )            Transpose matrix M1.
  inverse M1 ( -> M2 )        Compute inverse of matrix M1.
  det M1                      Compute determinant of matrix M1.
  rref M1 ( -> M2 )           Compute reduced row echelon form of M1 with all steps displayed.
  cofactor M1 ( -> M2 )       Compute cofactor expansion matrix of M1.
  V1 dot V2                   Compute dot product of vectors V1 and V2.
  V1 x V2 ( -> V3 )           Compute cross product of vectors V1 and V2.
  M N identity                Saves N x N identity matrix M.
  M R x C zero                Saves R x C matrix of zeroes M.
  load F                      Loads commands from file F.
  quit                        Exits calculator.
  help                        Prints this file.
  
```

# Sample Output:

**Multiplication**

```
>>> a 4 x 4 { 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 }

>>> a ?
|       1       2       3       4       |
|       5       6       7       8       |
|       9       10      11      12      |
|       13      14      15      16      |

>>> a * a
|       90      100     110     120     |
|       202     228     254     280     |
|       314     356     398     440     |
|       426     484     542     600     |

```
**Transpose**

```
>>> a ^ T
|       1       5       9       13      |
|       2       6       10      14      |
|       3       7       11      15      |
|       4       8       12      16      |
```

**Reduced Row Echelon Form**

```
>>> rref  a
Initial
|       1       2       3       4       |
|       5       6       7       8       |
|       9       10      11      12      |
|       13      14      15      16      |

R2 -> R2 - R1 * 5
|       1       2       3       4       |
|       0       -4      -8      -12     |
|       9       10      11      12      |
|       13      14      15      16      |

Reduced
|       1       2       3       4       |
|       0       1       2       3       |
|       9       10      11      12      |
|       13      14      15      16      |

R3 -> R3 - R1 * 9
|       1       2       3       4       |
|       0       1       2       3       |
|       0       -8      -16     -24     |
|       13      14      15      16      |

Reduced
|       1       2       3       4       |
|       0       1       2       3       |
|       0       0       0       0       |
|       13      14      15      16      |


R4 -> R4 - R1 * 13
|       1       2       3       4       |
|       0       1       2       3       |
|       0       0       0       0       |
|       0       -12     -24     -36     |

Reduced
|       1       2       3       4       |
|       0       1       2       3       |
|       0       0       0       0       |
|       0       0       0       0       |

R1 -> R1 - R2 * 2
|       1       0       -1      -2      |
|       0       1       2       3       |
|       0       0       0       0       |
|       0       0       0       0       |

```
**Cofactor Expansion**

```
>>> g 6 x 6 { 1 3 2 4 7 8 9 10 1 2 8 4 3 0 8 0 1 7 0 2 3 4 7 8 1 2 9 0 12 3 4 3 7 6 3 9 }

>>> g ?
|       1       3       2       4       7       8       |
|       9       10      1       2       8       4       |
|       3       0       8       0       1       7       |
|       0       2       3       4       7       8       |
|       1       2       9       0       12      3       |
|       4       3       7       6       3       9       |

>>> cofactor g
|       -25920  31968   10008   -6748   -10984  1240    |
|       3420    -4680   -1260   1140    1500    -240    |
|       270     -432    -162    882     306     -540    |
|       26910   -32760  -9810   7210    11050   -1900   |
|       -2430   3096    666     -766    -1258   460     |
|       -1800   2088    288     -1348   -544    520     |

```

**Determinant**

```
>>> det g
-3960
```
**Inverse Matrix**

```
>>> k 3 x 3 { 0 1 4 2 5 8 0 8 3 }

>>> k ?
|       0       1       4       |
|       2       5       8       |
|       0       8       3       |

>>> inverse k -> inv_k
|       -0.84   0.5     -0.21   |
|       -0.1    0       0.14    |
|       0.28    0       -0.03   |

>>> inv_k * k
|       1       0       0       |
|       0       1       0       |
|       0       0       1       |

```

**Load Commands From File**

```
>>> load input.inp

>>> display
a:
|       1       2       3       4       |
|       5       6       7       8       |
|       9       10      11      12      |
|       13      14      15      16      |

b:
|       1       8       2       |
|       4       7       1       |
|       0       9       2       |
|       13      4       7       |
|       8       13      4       |

c:
|       9       8       7       |
|       1       2       3       |
|       4       5       7       |

d:
|       1       2       |
|       3       4       |

e:
|       1       |
|       2       |
|       9       |

f:
|       1       9       2       11      |

g:
|       1       3       2       4       7       8       |
|       9       10      1       2       8       4       |
|       3       0       8       0       1       7       |
|       0       2       3       4       7       8       |
|       1       2       9       0       12      3       |
|       4       3       7       6       3       9       |

h:
|       1       0       2       0       |
|       0       0       0       1       |
|       1       9       0       1       |
|       2       0       0       7       |

i:
|       1       3       2       4       7       8       |
|       9       10      1       2       8       4       |
|       3       0       8       0       1       7       |
|       0       2       3       4       7       8       |
|       1       2       9       0       12      3       |
|       4       3       7       6       3       9       |
|       8       9       11      4       8       22      |

j:
|       1       0       2       0       |
|       0       0       0       1       |
|       1       9       0       1       |
|       2       0       0       7       |
|       1       0       6       0       |

k:
|       0       1       4       |
|       2       5       8       |
|       0       8       3       |

l:
|       1       3       7       1       0       0       |
|       4       11      13      0       1       0       |
|       8       9       4       0       0       1       |

m:
|       1       4       -1      0       |
|       2       3       5       -2      |
|       0       3       1       6       |
|       3       0       2       1       |

n:
|       2       3       4       |
|       -1      5       1       |
|       5       0       3       |

o:
|       2       5       |
|       4       3       |

```


