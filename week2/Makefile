SOURCES=Deque.java RandomizedQueue.java Permutation.java

ZIP_SOURCES=BruteCollinearPoints.java FastCollinearPoints.java Point.java

ZIPFILE=collinear.zip

compile:
	javac -cp ../algs4.jar -Xlint:unchecked ${SOURCES}
# javac -cp algs4.jar ${SOURCES}

zip:
	zip ${ZIPFILE} ${SOURCES}

clean:
	rm -f *.class ${ZIPFILE}
