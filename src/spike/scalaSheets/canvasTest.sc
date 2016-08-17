for (i <- 1 to 10; j <- 1 to 10) {

  if (i == 1 || i == 10)
    print("-")

  if ((i != 1 && i != 10)  && (j > 1 && j < 10) )
    print(" ")

  if ((i != 1 && i != 10)  && (j == 1 || j == 10) )
    print("|")

  if (j == 10)
    println("")
}




val matrix = Array.ofDim[String](10,10)


for (i <- 0 to 9; j <- 0 to 9) {

  if (i == 0 || i == 9)
    matrix(i)(j) = "-"

  if ((i != 0 && i != 9)  && (j > 0 && j < 9) )
    matrix(i)(j) = " "

  if ((i != 0 && i != 9)  && (j == 0 || j == 9) )
    matrix(i)(j) = "|"

}


for (i <- 0 to 9; j <- 0 to 9) {

  print(matrix(i)(j))

  if (j == 9) println()

}



/*
matrix(0)(0) = "h"

for (i <- 0 to 1)
  println(matrix(i)(0))*/
