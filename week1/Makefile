SOURCES=Percolation.java PercolationStats.java

ZIP_SOURCES=Percolation.java PercolationStats.java

ZIPFILE=percolation.zip

compile:
	javac -cp ../algs4.jar -Xlint:unchecked ${SOURCES}

zip:
	zip ${ZIPFILE} ${ZIP_SOURCES}

clean:
	rm -f *.class ${ZIPFILE} TAGS
