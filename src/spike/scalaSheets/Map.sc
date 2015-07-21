val map = Map(1 -> "hi", 2 -> "hi", 3 -> "ho").withFilter(e => e._2 == "he")

val list: List[Int] = map.flatMap(e => List(e._1)).toList

Some(3).get

//None.getOrElse(null)

val list2: Option[Int] = None

list2.getOrElse("hello")

for (i <- list2) yield(i)

1 eq map

List(1,2,3,4).contains(map)