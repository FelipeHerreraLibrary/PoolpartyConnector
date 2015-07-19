val map = Map(1 -> "hi", 2 -> "hi", 3 -> "ho").withFilter(e => e._2 == "he")

val list: List[Int] = map.flatMap(e => List(e._1)).toList

Some(3).get

None.getOrElse(null)