# SOURCES=Percolation.java PercolationStats.java
# SOURCES=Deque.java RandomizedQueue.java Permutation.java
SOURCES=BruteCollinearPoints.java FastCollinearPoints.java Point.java LineSegment.java Client.java

ZIP_SOURCES=BruteCollinearPoints.java FastCollinearPoints.java Point.java

# ZIPFILE=queues.zip
ZIPFILE=collinear.zip

compile:
	javac -cp algs4.jar -Xlint:unchecked ${SOURCES}
# javac -cp algs4.jar ${SOURCES}

zip:
	zip ${ZIPFILE} ${ZIP_SOURCES}

clean:
	rm -f *.class ${ZIPFILE}
