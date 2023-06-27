SOURCES=Solver.java Board.java

ZIP_SOURCES=Board.java Solver.java

ZIPFILE=8puzzle.zip

compile:
	javac -cp ../algs4.jar -Xlint:unchecked ${SOURCES}

zip:
	zip ${ZIPFILE} ${ZIP_SOURCES}

clean:
	rm -f *.class ${ZIPFILE} TAGS
