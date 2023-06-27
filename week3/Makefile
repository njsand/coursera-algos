SOURCES=BruteCollinearPoints.java FastCollinearPoints.java Point.java LineSegment.java Client.java

ZIP_SOURCES=BruteCollinearPoints.java FastCollinearPoints.java Point.java

ZIPFILE=collinear.zip

compile:
	javac -cp ../algs4.jar -Xlint:unchecked ${SOURCES}

zip:
	zip ${ZIPFILE} ${ZIP_SOURCES}

clean:
	rm -f *.class ${ZIPFILE}
