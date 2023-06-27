SOURCES=PointSET.java KdTree.java

ZIP_SOURCES=PointSET.java KdTree.java

ZIPFILE=kdtree.zip

default: compile tags

compile:
	javac -cp ../algs4.jar -Xlint:unchecked ${SOURCES}

zip:
	zip ${ZIPFILE} ${ZIP_SOURCES}

clean:
	rm -f *.class ${ZIPFILE} TAGS

tags:
	etags --members *.java
